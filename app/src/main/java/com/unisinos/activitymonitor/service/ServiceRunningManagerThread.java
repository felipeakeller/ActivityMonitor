package com.unisinos.activitymonitor.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;
import com.unisinos.activitymonitor.R;
import java.util.concurrent.TimeUnit;

/**
 * Created by Felipe on 12/08/2015.
 */
public class ServiceRunningManagerThread {

    private boolean running;
    private boolean oldState;

    private TextView textView;
    private ImageView imageView;

    private final Class<? extends Service> serviceClass;
    private final Activity activity;

    private ActivityManager.RunningServiceInfo serviceInfo;

    public ServiceRunningManagerThread(Activity activity, Class<? extends Service> serviceClass) {
        this.serviceClass = serviceClass;
        this.activity = activity;
    }

    public void start() {

        running = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running) {

                    final boolean isServiceRunning = isServiceRunning(serviceClass);
                    if(isServiceRunning != oldState) {
                        oldState = isServiceRunning;
                        verifyServiceStatus(isServiceRunning);
                    }
                    if(isServiceRunning) {
                        refreshTime();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    public void refreshTime() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long activitySince = serviceInfo.activeSince;
                long actualTime = SystemClock.elapsedRealtime();
                long time = actualTime - activitySince;

                textView.setText(R.string.online);
                textView.append(" - ");

                long days = TimeUnit.MILLISECONDS.toDays(time);
                time -= TimeUnit.DAYS.toMillis(days);
                long hours = TimeUnit.MILLISECONDS.toHours(time);
                time -= TimeUnit.HOURS.toMillis(hours);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
                time -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(time);

                String timeElapsed = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
                textView.append(timeElapsed);
            }
        });
    }

    private void verifyServiceStatus(final boolean isServiceRunning) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isServiceRunning) {
                    imageView.setImageResource(R.mipmap.online);
                    textView.setText(R.string.online);
                } else {
                    imageView.setImageResource(R.mipmap.offline);
                    textView.setText(R.string.offline);
                }
            }
        });
    }

    public void stop() {
        running = false;
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                this.serviceInfo = serviceInfo;
                return true;
            }
        }
        return false;
    }

    public void withTextView(TextView textView) {
        this.textView = textView;
    }
    public void withImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}
