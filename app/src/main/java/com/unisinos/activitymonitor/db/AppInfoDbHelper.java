package com.unisinos.activitymonitor.db;

import com.unisinos.activitymonitor.domain.AppInfo;
import com.unisinos.activitymonitor.domain.Device;
import com.unisinos.activitymonitor.domain.ScreenAction;

/**
 * Created by Felipe on 23/08/2015.
 */
public class AppInfoDbHelper {

    public static String onCreate() {
        StringBuilder sqlCreateDevice = new StringBuilder();
        sqlCreateDevice.append(" CREATE TABLE ").append(AppInfo.TABLE_NAME).append(" ( ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_UID).append(" INTEGER, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_PROCESS_NAME).append(" TEXT, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_STATE).append(" TEXT, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_TX_BYTES).append(" INTEGER, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_RX_BYTES).append(" INTEGER, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_DATE).append(" INTEGER, ");
        sqlCreateDevice.append(AppInfo.COLUMN_NAME_SCREEN_ACTION_ID).append(" INTEGER, ");
        sqlCreateDevice.append(" FOREIGN KEY ( " + AppInfo.COLUMN_NAME_SCREEN_ACTION_ID + " ) ");
        sqlCreateDevice.append(" REFERENCES " + ScreenAction.TABLE_NAME + "(" + ScreenAction.COLUMN_NAME_ID +") )");
        return sqlCreateDevice.toString();
    }

    public static String dropTable() {
        return "DROP TABLE IF EXISTS " + AppInfo.TABLE_NAME;
    }
}
