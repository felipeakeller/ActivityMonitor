package com.unisinos.activitymonitor.util;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;

/**
 * Created by Felipe on 23/08/2015.
 */
public class AppProcessInfoTranslator {

    public static String translate(int importance) {

        switch(importance) {
            case UsageEvents.Event.MOVE_TO_BACKGROUND : return "BACKGROUND";
            case UsageEvents.Event.MOVE_TO_FOREGROUND : return "FOREGROUND";
            case UsageEvents.Event.NONE : return "NONE";
        }
        return "";

    }

}
