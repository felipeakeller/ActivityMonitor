package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.net.TrafficStats;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunningAppProcessManager {

    final HashMap<String, Integer> map = new HashMap<>();
    final HashMap<Integer, Long> transmitidos = new HashMap<>();
    final HashMap<Integer, Long> recebidos = new HashMap<>();

    public void execute(ActivityManager manager) {

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {

            if (map.containsKey(info.processName)) {
                Integer importance = map.get(info.processName);
                if (info.uid != 10008 && !importance.equals(info.importance)) {
                    showLogApps(info.uid, info.processName, translateImportance(info.importance));
                    map.put(info.processName, info.importance);
                }
            } else {
                showLogApps(info.uid, info.processName, translateImportance(info.importance));
                map.put(info.processName, info.importance);
            }

        }

        List<String> removedProcess = new ArrayList<>();
        for (String processName : map.keySet()) {
            boolean processRunning = false;
            for (ActivityManager.RunningAppProcessInfo appInfo : runningAppProcesses) {
                if (appInfo.processName.equals(processName)) {
                    processRunning = true;
                    break;
                }
            }
            if (!processRunning) {
                removedProcess.add(processName);
                showLogApps(0, processName, "REMOVED");
            }
        }
        for (String processName : removedProcess) {
            map.remove(processName);
        }

    }

    private void showLogApps(int uid, String processName, String importance) {
        Log.i("APPS", String.valueOf(uid) + " | " + processName + " | " + importance + " | ");

        long txAtual = TrafficStats.getUidTxBytes(uid);
        if(transmitidos.containsKey(uid)) {
            long tx = transmitidos.get(uid) == null ? 0l : transmitidos.get(uid);
            Log.i("APPS_NET", processName + " - TX: " + (txAtual - tx));
        }
        transmitidos.put(uid, txAtual);

        long rxAtual = TrafficStats.getUidRxBytes(uid);
        if(recebidos.containsKey(uid)) {
            long rx = recebidos.get(uid) == null ? 0l : recebidos.get(uid);
            Log.i("APPS_NET", processName + " - RX: "+ (rxAtual - rx) );
        }
        recebidos.put(uid, rxAtual);
    }

    private String translateImportance(int importance) {
        if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) return "FOREGROUND";
        else if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) return "PERCEPTIBLE";
        else if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) return "VISIBLE";
        else if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) return "SERVICE";
        else if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) return "BACKGROUND";
        else if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY) return "EMPTY";
        return "";
    }

}
