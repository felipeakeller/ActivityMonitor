package com.unisinos.activitymonitor.servicedb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.unisinos.activitymonitor.db.DatabaseHelper;
import com.unisinos.activitymonitor.domain.Device;

import java.util.Calendar;

/**
 * Created by Felipe on 18/08/2015.
 */
public class DeviceService {

    private DatabaseHelper databaseHelper;
    private Context context;

    public DeviceService(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    public Device registerDeviceInfo() {

        Cursor cursor = getDeviceInfoCursor();
        boolean existsDeviceInfo = cursor.moveToFirst();

        if( !existsDeviceInfo ) {

            SQLiteDatabase writeDb = databaseHelper.getWritableDatabase();

            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Device device = new Device();
            device.setDeviceId(telephonyManager.getDeviceId());
            device.setVersion(Build.VERSION.RELEASE);
            device.setModel(Build.MODEL);
            device.setDate(Calendar.getInstance().getTime());
            long id = writeDb.insert(Device.TABLE_NAME, null, device.toContentValues());
            device.setId((int)id);
            return device;

        } else {
            Device device = Device.fromCursor(cursor);
            cursor.close();
            return device;
        }

    }

    public Cursor getDeviceInfoCursor() {
        SQLiteDatabase readDb = databaseHelper.getReadableDatabase();
        return readDb.rawQuery(" select * from " + Device.TABLE_NAME, new String[]{});
    }
}
