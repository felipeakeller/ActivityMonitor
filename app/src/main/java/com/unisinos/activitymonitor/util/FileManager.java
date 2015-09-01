package com.unisinos.activitymonitor.util;

import android.os.Environment;

import com.unisinos.activitymonitor.db.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Felipe on 31/08/2015.
 */
public class FileManager {

    private String databasePath;

    public static FileManager create() {
        return new FileManager();
    }

    public FileManager dataBasePath(String databasePath) {
        this.databasePath = databasePath;
        return this;
    }

    public File saveDirectory(File externalStorageDirectory) {
        File data = Environment.getDataDirectory();
        File currentDB = new File(data, databasePath);
        File backupDB = new File(externalStorageDirectory, DatabaseHelper.DATA_BASE);

        if (backupDB.exists()) {
            backupDB.delete();
        }

        try {
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return backupDB;
    }
}
