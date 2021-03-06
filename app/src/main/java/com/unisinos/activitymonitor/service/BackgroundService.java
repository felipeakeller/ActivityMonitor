package com.unisinos.activitymonitor.service;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.unisinos.activitymonitor.domain.Device;
import com.unisinos.activitymonitor.receiver.ActionScreenReceiver;
import com.unisinos.activitymonitor.servicedb.DeviceService;
import com.unisinos.activitymonitor.servicedb.ScreenActionService;

public class BackgroundService extends Service {

    public static boolean screenOn = true;
    public Handler handler = null;
    public static Runnable runnable = null;

    private ActionScreenReceiver actionScreenReceiver = new ActionScreenReceiver();
    private ScreenActionService actionScreenService;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        handler = new Handler();

        registerReceiver(actionScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(actionScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        actionScreenService = ScreenActionService.getInstance(getApplicationContext());
        actionScreenService.registerNewScreenAction();

        actionScreenReceiver.withScreenService(actionScreenService);

        DeviceService deviceService = new DeviceService(getApplicationContext());
        Device device = deviceService.registerDeviceInfo();

        final RunningAppProcessManager manager = new RunningAppProcessManager(getApplicationContext());
        manager.withScreenActionService(actionScreenService);
        manager.refreshInstalledApplications();

        runnable = new Runnable() {
            public void run() {
                if(screenOn) {
                    UsageStatsManager usage = (UsageStatsManager)BackgroundService.this.getSystemService(USAGE_STATS_SERVICE);
                    long time = System.currentTimeMillis();
                    UsageEvents usageEvents = usage.queryEvents(time - 2000, time);

                    manager.execute(usageEvents);

                    handler.postDelayed(runnable, 1000);
                } else {
                    handler.postDelayed(runnable, 2000);
                }
            }
        };

        handler.postDelayed(runnable, 2000);
    }

    public void onDestroy() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

}
