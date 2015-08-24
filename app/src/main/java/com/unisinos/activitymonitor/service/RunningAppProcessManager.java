package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;

import com.unisinos.activitymonitor.domain.AppInfo;
import com.unisinos.activitymonitor.servicedb.AppInfoService;
import com.unisinos.activitymonitor.servicedb.ScreenActionService;
import com.unisinos.activitymonitor.util.AppProcessInfoTranslator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RunningAppProcessManager {

    private ScreenActionService screenActionService;
    private AppInfoService appInfoService;
    private Context context;

    final HashMap<AppInfoDTO, Integer> map = new HashMap<>();
    final HashMap<Integer, Long> transmitidos = new HashMap<>();
    final HashMap<Integer, Long> recebidos = new HashMap<>();

    public RunningAppProcessManager(Context applicationContext) {
        this.context = applicationContext;
    }

    public void execute(ActivityManager manager) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {

            if (map.containsKey(info.uid)) {
                Integer importance = map.get(info.uid);
                if (info.uid != 10008 && !importance.equals(info.importance)) {
                    AppInfoDTO appInfoDTO = new AppInfoDTO(info);
                    registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(info.importance));
                    map.put(appInfoDTO, info.importance);
                }
            } else {
                AppInfoDTO appInfoDTO = new AppInfoDTO(info);
                registerAppInfo(appInfoDTO, AppProcessInfoTranslator.translate(info.importance));
                map.put(appInfoDTO, info.importance);
            }

        }

        verifyRemovedProcess(runningAppProcesses);
    }

    private void verifyRemovedProcess(List<ActivityManager.RunningAppProcessInfo> runningAppProcesses) {
        List<AppInfoDTO> removedProcess = new ArrayList<>();
        for (AppInfoDTO appInfoDTO : map.keySet()) {
            boolean processRunning = false;
            for (ActivityManager.RunningAppProcessInfo appInfo : runningAppProcesses) {
                if (appInfo.uid == appInfoDTO.uid) {
                    processRunning = true;
                    break;
                }
            }
            if (!processRunning) {
                removedProcess.add(appInfoDTO);
                registerAppInfo(appInfoDTO, "REMOVED");
            }
        }
        for (AppInfoDTO appInfoDTO : removedProcess) {
            map.remove(appInfoDTO);
        }
    }

    private void registerAppInfo(AppInfoDTO appInfoDTO, String state) {
        AppInfo appInfo = new AppInfo();
        appInfo.setUid(appInfoDTO.uid);
        appInfo.setProcessName(appInfoDTO.processName);
        appInfo.setState(state);
        appInfo.setTxBytes(getTxBytes(appInfoDTO.uid));
        appInfo.setRxBytes(getRxBytes(appInfoDTO.uid));
        appInfo.setDate(Calendar.getInstance().getTime());
        appInfo.setScreenActionId(screenActionService.getScreenAction().getId());
        appInfoService.save(appInfo);
    }

    private long getRxBytes(int uid) {
        long currentRxBytes = TrafficStats.getUidRxBytes(uid);
        long oldRxBytes = transmitidos.get(uid) == null ? 0l : transmitidos.get(uid);
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

    class AppInfoDTO {
        String processName;
        int uid;

        AppInfoDTO(ActivityManager.RunningAppProcessInfo info) {
            this.uid = info.uid;
            this.processName = info.processName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AppInfoDTO that = (AppInfoDTO) o;
            return uid == that.uid;
        }

        @Override
        public int hashCode() {
            return uid;
        }
    }

}
