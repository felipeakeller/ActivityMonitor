package com.unisinos.activitymonitor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unisinos.activitymonitor.domain.Device;
import com.unisinos.activitymonitor.domain.ScreenAction;

/**
 * Created by Felipe on 17/08/2015.
 */
public class DeviceDbHelper {

    public static String onCreate() {
        StringBuilder sqlCreateDevice = new StringBuilder();
        sqlCreateDevice.append(" CREATE TABLE ").append(Device.TABLE_NAME).append(" ( ");
        sqlCreateDevice.append(Device.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlCreateDevice.append(Device.COLUMN_NAME_DEVICE_ID).append(" TEXT, ");
        sqlCreateDevice.append(Device.COLUMN_NAME_VERSION).append(" TEXT, ");
        sqlCreateDevice.append(Device.COLUMN_NAME_MODEL).append(" TEXT, ");
        sqlCreateDevice.append(Device.COLUMN_NAME_DATE).append(" TEXT ) ");
        return sqlCreateDevice.toString();
    }

    public static String dropTable() {
        return "DROP TABLE IF EXISTS " + Device.TABLE_NAME;
    }

}
