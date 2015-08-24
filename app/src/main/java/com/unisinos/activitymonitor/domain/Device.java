package com.unisinos.activitymonitor.domain;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Felipe on 18/08/2015.
 */
public class Device {

    public static final String TABLE_NAME = "devices";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_DEVICE_ID = "deviceId";
    public static final String COLUMN_NAME_VERSION = "version";
    public static final String COLUMN_NAME_MODEL = "model";
    public static final String COLUMN_NAME_DATE = "date";

    private int id;
    private String deviceId;
    private String version;
    private String model;
    //TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
    private Date date;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_DEVICE_ID, getDeviceId());
        contentValues.put(COLUMN_NAME_VERSION, getVersion());
        contentValues.put(COLUMN_NAME_MODEL, getModel());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        contentValues.put(COLUMN_NAME_DATE, sdf.format(getDate()));

        return contentValues;
    }

    public static Device fromCursor(Cursor cursor) {
        Device device = new Device();
        device.setId(cursor.getInt(cursor.getColumnIndex(Device.COLUMN_NAME_ID)));
        device.setDeviceId(cursor.getString(cursor.getColumnIndex(Device.COLUMN_NAME_DEVICE_ID)));
        device.setModel(cursor.getString(cursor.getColumnIndex(Device.COLUMN_NAME_MODEL)));
        device.setVersion(cursor.getString(cursor.getColumnIndex(Device.COLUMN_NAME_VERSION)));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            device.setDate(sdf.parse(cursor.getString(cursor.getColumnIndex(Device.COLUMN_NAME_DATE))));
        } catch (ParseException e) {
            device.setDate(null);
        }
        return device;
    }

}
