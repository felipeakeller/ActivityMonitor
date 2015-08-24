package com.unisinos.activitymonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unisinos.activitymonitor.service.BackgroundService;

/**
 * Created by Felipe on 19/08/2015.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("BOOT CRIA SERVICE");
        context.startService(new Intent(context, BackgroundService.class));
    }

}
