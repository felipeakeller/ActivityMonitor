package com.unisinos.activitymonitor.util;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;

/**
 * Created by Felipe on 23/08/2015.
 */
public class AppProcessInfoTranslator {

    public static String translate(int importance) {

        switch(importance) {
            case 1 : return "FOREGROUND";
            case 2 : return "BACKGROUND";
            case UsageEvents.Event.NONE : return "NONE";
        }
        return "";

    }

    public static int getEventType(int importance) {
        if(importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            return 1;
        }
        return 2;
    }

    public static String background() {
        return "BACKGROUND";
    }

    public static int backgroundState() {
        return 2;
    }
}
