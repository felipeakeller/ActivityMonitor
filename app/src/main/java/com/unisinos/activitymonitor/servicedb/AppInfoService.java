package com.unisinos.activitymonitor.servicedb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unisinos.activitymonitor.db.DatabaseHelper;
import com.unisinos.activitymonitor.domain.AppInfo;
import com.unisinos.activitymonitor.domain.Device;
import com.unisinos.activitymonitor.domain.ScreenAction;

/**
 * Created by Felipe on 23/08/2015.
 */
public class AppInfoService {

    private DatabaseHelper databaseHelper;
    private Context context;

    public AppInfoService(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    public Cursor getAppInfoCursor() {
        SQLiteDatabase readDb = databaseHelper.getReadableDatabase();
        return readDb.rawQuery(" select * from " + AppInfo.TABLE_NAME, new String[]{});
    }

    public void save(AppInfo appInfo) {
        SQLiteDatabase writeDb = databaseHelper.getWritableDatabase();
        long id = writeDb.insert(AppInfo.TABLE_NAME, null, appInfo.toContentValues());
        appInfo.setId(id);
    }
}
