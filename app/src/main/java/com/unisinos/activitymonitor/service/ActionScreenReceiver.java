package com.unisinos.activitymonitor.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Felipe on 17/08/2015.
 */
public class ActionScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

            BackgroundService.screenOn = true;

        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){

            BackgroundService.screenOn = false;

        }

    }

}
