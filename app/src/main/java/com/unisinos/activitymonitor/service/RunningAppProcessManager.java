package com.unisinos.activitymonitor.service;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.util.Log;

import com.unisinos.activitymonitor.domain.AppInfo;
import com.unisinos.activitymonitor.servicedb.AppInfoService;
import com.unisinos.activitymonitor.servicedb.ScreenActionService;
import com.unisinos.activitymonitor.util.AppProcessInfoTranslator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunningAppProcessManager {

    private Context context;
    private ScreenActionService screenActionService;
    private AppInfoService appInfoService;

    Map<String, Integer> installedApplicationsMap = new HashMap<>();
    final Map<String, AppInfoDTO> map = new HashMap<>();
    final Map<Integer, Long> transmitidos = new HashMap<>();
    final Map<Integer, Long> recebidos = new HashMap<>();

    public RunningAppProcessManager(Context applicationContext) {
        this.context = applicationContext;
        this.appInfoService = new AppInfoService(applicationContext);
    }

    public void execute(UsageEvents usageEvents) {

        List<UsageEvents.Event> currentEvents = new ArrayList<>();
        while(usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);
            currentEvents.add(event);
        }

        for (UsageEvents.Event event : currentEvents) {
            if("com.android.systemui".equals(event.getPackageName())) {
                continue;
            }
            if (map.containsKey(event.getPackageName())) {
                int eventType = map.get(event.getPackageName()).eventType;
                if (eventType != event.getEventType()) {
                    AppInfoDTO appInfoDTO = new AppInfoDTO(event);
                    registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(event.getEventType()));
                    map.put(event.getPackageName(), appInfoDTO);
                }
            } else {
                AppInfoDTO appInfoDTO = new AppInfoDTO(event);
                registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(event.getEventType()));
                map.put(event.getPackageName(), appInfoDTO);
            }
        }
//        verifyRemovedProcess(currentEvents);
    }

    private void verifyRemovedProcess(List<UsageEvents.Event> events) {
        List<String> removedProcess = new ArrayList<>();
        for (String packageName : map.keySet()) {
            boolean processRunning = false;
            for (UsageEvents.Event event : events) {
                if (event.getPackageName().equals(packageName)) {
                    processRunning = true;
                    break;
                }
            }
            if (!processRunning) {
                removedProcess.add(packageName);
                registerAppInfo(map.get(packageName), "REMOVED");
            }
        }
        for (String packageName : removedProcess) {
            map.remove(packageName);
        }
    }

    private void registerAppInfo(AppInfoDTO appInfoDTO, String state) {
        AppInfo appInfo = new AppInfo();
        appInfo.setUid(installedApplicationsMap.get(appInfoDTO.packageName));
        appInfo.setProcessName(appInfoDTO.packageName);
        appInfo.setState(state);
        appInfo.setTxBytes(getTxBytes(appInfo.getUid()));
        appInfo.setRxBytes(getRxBytes(appInfo.getUid()));
        appInfo.setDate(Calendar.getInstance().getTime());
        appInfo.setScreenActionId(screenActionService.getScreenAction().getId());
        appInfoService.save(appInfo);
    }

    private long getRxBytes(int uid) {
        long currentRxBytes = TrafficStats.getUidRxBytes(uid);
        long oldRxBytes = recebidos.get(uid) == null ? 0l : recebidos.get(uid);
        recebidos.put(uid, currentRxBytes);
        return (currentRxBytes - oldRxBytes);
    }

    private long getTxBytes(int uid) {
        long currentTxBytes = TrafficStats.getUidTxBytes(uid);
        long oldTxBytes = transmitidos.get(uid) == null ? 0l : transmitidos.get(uid);
        transmitidos.put(uid, currentTxBytes);
        return (currentTxBytes - oldTxBytes);
    }

    public void withScreenActionService(ScreenActionService screenActionService) {
        this.screenActionService = screenActionService;
    }

    public void installedApplications(List<ApplicationInfo> installedApplications) {
        if(installedApplicationsMap.size() != installedApplications.size()) {
            for (ApplicationInfo applicationInfo : installedApplications) {
                installedApplicationsMap.put(applicationInfo.packageName, applicationInfo.uid);
            }
        }
    }

    class AppInfoDTO {
        String packageName;
        int eventType;

        AppInfoDTO(UsageEvents.Event event) {
            this.packageName = event.getPackageName();
            this.eventType = event.getEventType();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AppInfoDTO that = (AppInfoDTO) o;
            return packageName.equals(that.packageName);
        }

        @Override
        public int hashCode() {
            return packageName.hashCode();
        }
    }

}
