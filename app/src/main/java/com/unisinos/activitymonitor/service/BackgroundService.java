package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Felipe on 11/08/2015.
 */
public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        handler = new Handler();
        final RunningAppProcessManager manager = new RunningAppProcessManager();
        runnable = new Runnable() {
            public void run() {
                manager.execute((ActivityManager)BackgroundService.this.getSystemService(ACTIVITY_SERVICE));
                handler.postDelayed(runnable, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

}
