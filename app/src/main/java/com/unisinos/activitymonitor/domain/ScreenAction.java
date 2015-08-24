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
public class ScreenAction {

    public static final String TABLE_NAME = "screen_actions";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_DT_START = "dt_start";
    public static final String COLUMN_NAME_DT_STOP = "dt_stop";

    private Long id;
    private Date dtStart;
    private Date dtStop;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtStart() {
        return dtStart;
    }
    public void setDtStart(Date dtStart) {
        this.dtStart = dtStart;
    }

    public Date getDtStop() {
        return dtStop;
    }
    public void setDtStop(Date dtStop) {
        this.dtStop = dtStop;
    }

    public String getDtStartStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return getDtStart() == null ? "" : sdf.format(getDtStart());
    }

    public String getDtStopStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return getDtStop() == null ? "" : sdf.format(getDtStop());
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        contentValues.put(COLUMN_NAME_DT_START, sdf.format(getDtStart()));
        contentValues.put(COLUMN_NAME_DT_STOP, getDtStop() == null ? null : sdf.format(getDtStop()));

        return contentValues;
    }

    public static ScreenAction fromCursor(Cursor cursor) {
        ScreenAction screenAction = new ScreenAction();
        screenAction.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID)));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            String dtStartStr = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DT_START));
            screenAction.setDtStart(dtStartStr == null ? null : sdf.parse(dtStartStr));
        } catch (ParseException e) {
            screenAction.setDtStart(null);
        }
        try {
            String dtStopStr = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DT_STOP));
            screenAction.setDtStop(dtStopStr == null ? null : sdf.parse(dtStopStr));
        } catch (ParseException e) {
            screenAction.setDtStop(null);
        }

        return screenAction;
    }


    @Override
    public String toString() {
        return getId() + " DT_START: " + getDtStartStr() + " DT_STOP: " + getDtStopStr();
    }
}