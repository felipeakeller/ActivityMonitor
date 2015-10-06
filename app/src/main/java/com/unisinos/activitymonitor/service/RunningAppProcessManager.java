package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

import com.unisinos.activitymonitor.domain.AppInfo;
import com.unisinos.activitymonitor.servicedb.AppInfoService;
import com.unisinos.activitymonitor.servicedb.ScreenActionService;
import com.unisinos.activitymonitor.util.AppProcessInfoTranslator;

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

    public void execute(ActivityManager manager) {

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {

            if (map.containsKey(info.processName)) {
                if("com.android.systemui".equals(info.processName)) {
                    continue;
                }
                int eventType = map.get(info.processName).eventType;
                AppInfoDTO appInfoDTO = new AppInfoDTO(info);
                if (eventType != appInfoDTO.eventType) {
                    registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(appInfoDTO.eventType));
                    map.put(info.processName, appInfoDTO);
                }
            } else if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == info.importance){
                AppInfoDTO appInfoDTO = new AppInfoDTO(info);
                registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(appInfoDTO.eventType));
                map.put(info.processName, appInfoDTO);
            }

        }

        verifyRemovedProcess(runningAppProcesses);
    }

    private void verifyRemovedProcess(List<ActivityManager.RunningAppProcessInfo> runningAppProcesses) {
        for (String processName : map.keySet()) {
            boolean processRunning = false;
            for (ActivityManager.RunningAppProcessInfo appInfo : runningAppProcesses) {
                if (appInfo.processName.equals(processName)) {
                    processRunning = true;
                    break;
                }
            }
            if (!processRunning) {
                AppInfoDTO appInfoDTO = map.get(processName);
                appInfoDTO.eventType = AppProcessInfoTranslator.backgroundState();
                registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(appInfoDTO.eventType));
                map.put(appInfoDTO.packageName, appInfoDTO);
            }
        }
    }

    private void registerAppInfo(AppInfoDTO appInfoDTO, String state) {
        AppInfo appInfo = new AppInfo();

        if(!installedApplicationsMap.containsKey(appInfoDTO.packageName)) {
            refreshInstalledApplications();
        }
        if(!installedApplicationsMap.containsKey(appInfoDTO.packageName)) {
            return;
        }
        Log.i("INFO", appInfoDTO.packageName);
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
        if(recebidos.get(uid) == null) {
            recebidos.put(uid, currentRxBytes);
            return 0l;
        } else {
            long oldRxBytes = recebidos.get(uid);
            recebidos.put(uid, currentRxBytes);
            return (currentRxBytes - oldRxBytes);
        }
    }

    private long getTxBytes(int uid) {
        long currentTxBytes = TrafficStats.getUidTxBytes(uid);
        if(transmitidos.get(uid) == null) {
            transmitidos.put(uid, currentTxBytes);
            return 0l;
        } else {
            long oldTxBytes = transmitidos.get(uid);
            transmitidos.put(uid, currentTxBytes);
            return (currentTxBytes - oldTxBytes);
        }
    }

    public void withScreenActionService(ScreenActionService screenActionService) {
        this.screenActionService = screenActionService;
    }

    public void refreshInstalledApplications() {
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : installedApplications) {
            installedApplicationsMap.put(applicationInfo.packageName, applicationInfo.uid);
        }
    }

    public void updateAppToBackground() {
        for(AppInfoDTO appInfoDTO : map.values()) {
            if(appInfoDTO.eventType != AppProcessInfoTranslator.backgroundState()) {
                appInfoDTO.eventType = AppProcessInfoTranslator.backgroundState();
                registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(appInfoDTO.eventType));
                map.put(appInfoDTO.packageName, appInfoDTO);
            }
        }
    }

    class AppInfoDTO {
        String packageName;
        int eventType;

        AppInfoDTO(ActivityManager.RunningAppProcessInfo info) {
            this.packageName = info.processName;
            this.eventType = AppProcessInfoTranslator.getEventType(info.importance);
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
