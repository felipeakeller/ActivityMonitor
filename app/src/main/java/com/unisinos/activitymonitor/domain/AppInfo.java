package com.unisinos.activitymonitor.domain;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Felipe on 19/08/2015.
 */
public class AppInfo {

    public static final String TABLE_NAME = "app_infos";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_PROCESS_NAME = "process_name";
    public static final String COLUMN_NAME_STATE = "state";
    public static final String COLUMN_NAME_TX_BYTES = "tx_bytes";
    public static final String COLUMN_NAME_RX_BYTES = "rx_bytes";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_SCREEN_ACTION_ID = "screen_action_id";

    private long id;
    private int uid;
    private String processName;
    private String state;
    private long txBytes;
    private long rxBytes;
    private Date date;
    private long screenActionId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public long getTxBytes() {
        return txBytes;
    }
    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }

    public long getRxBytes() {
        return rxBytes;
    }
    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getScreenActionId() {
        return screenActionId;
    }
    public void setScreenActionId(long screenActionId) {
        this.screenActionId = screenActionId;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_UID, getUid());
        contentValues.put(COLUMN_NAME_PROCESS_NAME, getProcessName());
        contentValues.put(COLUMN_NAME_STATE, getState());
        contentValues.put(COLUMN_NAME_TX_BYTES, getTxBytes());
        contentValues.put(COLUMN_NAME_RX_BYTES, getRxBytes());
        contentValues.put(COLUMN_NAME_DATE, getDate().getTime());
        contentValues.put(COLUMN_NAME_SCREEN_ACTION_ID, getScreenActionId());
        return contentValues;
    }

    public static AppInfo fromCursor(Cursor cursor) {
        AppInfo appInfo = new AppInfo();
        appInfo.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID)));
        appInfo.setUid(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_UID)));
        appInfo.setProcessName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PROCESS_NAME)));
        appInfo.setState(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STATE)));
        appInfo.setTxBytes(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TX_BYTES)));
        appInfo.setRxBytes(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_RX_BYTES)));
        appInfo.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DATE))));
        appInfo.setScreenActionId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SCREEN_ACTION_ID)));
        return appInfo;
    }
}
