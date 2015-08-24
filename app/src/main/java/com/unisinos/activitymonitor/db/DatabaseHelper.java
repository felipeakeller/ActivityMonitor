package com.unisinos.activitymonitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Felipe on 19/08/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    public static final String DATA_BASE = "ActivityMonitor.db";
    public static final int VERSION = 1;

    private DatabaseHelper(Context context) {
        super(context, DATA_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DeviceDbHelper.onCreate());
        db.execSQL(ScreenActionDbHelper.onCreate());
        db.execSQL(AppInfoDbHelper.onCreate());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DeviceDbHelper.dropTable());
        db.execSQL(ScreenActionDbHelper.dropTable());
        db.execSQL(AppInfoDbHelper.dropTable());
        onCreate(db);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

}
