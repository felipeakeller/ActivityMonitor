package com.unisinos.activitymonitor.util;

import android.app.ActivityManager;

/**
 * Created by Felipe on 23/08/2015.
 */
public class AppProcessInfoTranslator {

    public static String translate(int importance) {

        switch(importance) {
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND : return "FOREGROUND";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE : return "PERCEPTIBLE";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE : return "VISIBLE";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE : return "SERVICE";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND : return "BACKGROUND";
            case ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY : return "EMPTY";
        }
        return "";

    }

}
