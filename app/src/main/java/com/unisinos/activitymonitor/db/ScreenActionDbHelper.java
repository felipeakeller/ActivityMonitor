package com.unisinos.activitymonitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.unisinos.activitymonitor.domain.Device;
import com.unisinos.activitymonitor.domain.ScreenAction;

/**
 * Created by Felipe on 18/08/2015.
 */
public class ScreenActionDbHelper {

    public static String onCreate() {
        StringBuilder sqlCreateDevice = new StringBuilder();
        sqlCreateDevice.append(" CREATE TABLE ").append(ScreenAction.TABLE_NAME).append(" ( ");
        sqlCreateDevice.append(ScreenAction.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlCreateDevice.append(ScreenAction.COLUMN_NAME_DT_START).append(" TEXT, ");
        sqlCreateDevice.append(ScreenAction.COLUMN_NAME_DT_STOP).append(" TEXT ) ");
        return sqlCreateDevice.toString();
    }

    public static String dropTable() {
        return "DROP TABLE IF EXISTS " + ScreenAction.TABLE_NAME;
    }

}
