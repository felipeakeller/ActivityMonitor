package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaptureDataService implements Runnable {

	private ActivityManager manager;
	private boolean controlRunnable;

	public CaptureDataService(ActivityManager manager) {		
		this.manager = manager;
	}

	@Override
	public void run() {
		
		this.controlRunnable = true;
		
		final HashMap<String, Integer> map = new HashMap<>();
		
		while (controlRunnable) {
			
			List<RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
			
			for (RunningAppProcessInfo info : runningAppProcesses) {
				
//				if(TrafficStats.getUidRxBytes(info.uid) > 0) {
//					Log.i("APPS", "TRAFFIC " + info.processName + TrafficStats.getUidRxBytes(info.uid));
//				}
				
				if(map.containsKey(info.processName)) {
					Integer importance = map.get(info.processName);
					if(info.uid != 10008 && !importance.equals(info.importance)) {
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
				for (RunningAppProcessInfo appInfo : runningAppProcesses) {
					if(appInfo.processName.equals(processName)) {
						processRunning = true;
						break;
					}					
				}
				if(!processRunning) {
					removedProcess.add(processName);
					showLogApps(0, processName, "REMOVED");
				}
			}
			for (String processName : removedProcess) {
				map.remove(processName);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void showLogApps(int uid, String processName, String importance) {
		Log.i("APPS", String.valueOf(uid) + " | " + processName + " | " + importance + " | ");
	}

	private String translateImportance(int importance) {
		if(importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) return "FOREGROUND";
		else if(importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) return "PERCEPTIBLE";
		else if(importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) return "VISIBLE";
		else if(importance == RunningAppProcessInfo.IMPORTANCE_SERVICE) return "SERVICE";
		else if(importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) return "BACKGROUND";
		else if(importance == RunningAppProcessInfo.IMPORTANCE_EMPTY) return "EMPTY";
		return "";
	}

	public void stop() {
		controlRunnable = false;
	}

}