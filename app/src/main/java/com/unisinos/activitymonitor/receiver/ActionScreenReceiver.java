package com.unisinos.activitymonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unisinos.activitymonitor.servicedb.ScreenActionService;

/**
 * Created by Felipe on 17/08/2015.
 */
public class ActionScreenReceiver extends BroadcastReceiver {

    private ScreenActionService actionScreenService;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

            actionScreenService.updateScreenStatus(true);

        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){

            actionScreenService.updateScreenStatus(false);

        }

    }

    public void withScreenService(ScreenActionService actionScreenService) {
        this.actionScreenService = actionScreenService;
    }
}
