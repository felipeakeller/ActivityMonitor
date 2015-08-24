package com.unisinos.activitymonitor.servicedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unisinos.activitymonitor.db.DatabaseHelper;
import com.unisinos.activitymonitor.domain.ScreenAction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Felipe on 18/08/2015.
 */
public class ScreenActionService {

    private static ScreenActionService screenActionService;
    private boolean screenOn = true;
    private ScreenAction screenAction;
    private DatabaseHelper databaseHelper;

    private ScreenActionService(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void updateScreenStatus(boolean screenActualStatus) {

        boolean statusChanged = screenOn != screenActualStatus;

        if(statusChanged) {

            screenOn = screenActualStatus;

            if(screenActualStatus) {
                registerNewScreenAction();
            } else {
                screenAction.setDtStop(Calendar.getInstance().getTime());
                updateDtStop(screenAction);
            }

        }

    }

    private ScreenAction createNewScreenAction() {
        screenAction = new ScreenAction();
        screenAction.setDtStart(Calendar.getInstance().getTime());
        return screenAction;
    }

    public void registerNewScreenAction() {
        screenAction = getLastScreenActionDtStopNull();

        if(screenAction == null) {
            screenAction = createNewScreenAction();
            save(screenAction);
        }
    }

    private void save(ScreenAction screenAction) {
        SQLiteDatabase writeDb = databaseHelper.getWritableDatabase();
        long id = writeDb.insert(ScreenAction.TABLE_NAME, null, screenAction.toContentValues());
        screenAction.setId(id);
    }

    private void updateDtStop(ScreenAction screenAction) {
        SQLiteDatabase readDb = databaseHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ScreenAction.COLUMN_NAME_DT_STOP, screenAction.getDtStopStr());

        String selection = ScreenAction.COLUMN_NAME_ID + " = ? ";

        readDb.update(ScreenAction.TABLE_NAME, contentValues, selection, new String[]{String.valueOf(screenAction.getId())});
    }

    public ScreenAction getScreenAction() {
        return screenAction;
    }

    public List<ScreenAction> listAll() {
        SQLiteDatabase readDb = databaseHelper.getReadableDatabase();
        Cursor cursor = readDb.rawQuery(" select * from " + ScreenAction.TABLE_NAME, new String[]{});
        List<ScreenAction> screensActions = new ArrayList<>();
        while(cursor.moveToNext()) {
            screensActions.add(ScreenAction.fromCursor(cursor));
        }
        return screensActions;
    }

    private ScreenAction getLastScreenActionDtStopNull() {
        SQLiteDatabase readDb = databaseHelper.getReadableDatabase();
        Cursor cursor = readDb.rawQuery("select * from " + ScreenAction.TABLE_NAME + " where " + ScreenAction.COLUMN_NAME_DT_STOP + " IS NULL ", new String[]{});
        boolean existsScreenAction = cursor.moveToFirst();
        if(existsScreenAction) {
            return ScreenAction.fromCursor(cursor);
        }
        return null;
    }

    public static ScreenActionService getInstance(Context context) {
        if(screenActionService == null) {
            screenActionService = new ScreenActionService(context);
        }
        return screenActionService;
    }

}
