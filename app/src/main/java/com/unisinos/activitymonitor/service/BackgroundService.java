package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

public class BackgroundService extends Service {

    public static boolean screenOn = true;
    public Handler handler = null;
    public static Runnable runnable = null;

    private ActionScreenReceiver actionScreenReceiver = new ActionScreenReceiver();

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        registerReceiver(actionScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(actionScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        handler = new Handler();
        final RunningAppProcessManager manager = new RunningAppProcessManager();

        runnable = new Runnable() {
            public void run() {
                if(screenOn) {
                    manager.execute((ActivityManager) BackgroundService.this.getSystemService(ACTIVITY_SERVICE));
                    handler.postDelayed(runnable, 1500);
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
