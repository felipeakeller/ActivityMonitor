package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.app.Service;
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
                    manager.execute((ActivityManager) BackgroundService.this.getSystemService(ACTIVITY_SERVICE));
                    handler.postDelayed(runnable, 1000);
                } else {
                    manager.updateAppToBackground();
                    handler.postDelayed(runnable, 1000);
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
