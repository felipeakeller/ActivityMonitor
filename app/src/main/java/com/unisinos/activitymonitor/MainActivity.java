package com.unisinos.activitymonitor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unisinos.activitymonitor.db.DatabaseHelper;
import com.unisinos.activitymonitor.domain.ScreenAction;
import com.unisinos.activitymonitor.service.BackgroundService;
import com.unisinos.activitymonitor.service.ServiceRunningManagerThread;
import com.unisinos.activitymonitor.servicedb.ScreenActionService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        ServiceRunningManagerThread serviceRunningManager = new ServiceRunningManagerThread(this, BackgroundService.class);
        serviceRunningManager.withTextView((TextView) findViewById(R.id.statusTextView));
        serviceRunningManager.withImageView((ImageView) findViewById(R.id.imageStatusView));
        serviceRunningManager.start();

        DatabaseHelper.getInstance(getApplicationContext());

        confireButtonsOnScreen();
    }

    private void confireButtonsOnScreen() {
        Button btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, BackgroundService.class));
            }
        });

        Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, BackgroundService.class));
            }
        });

        Button btnInfo = (Button) findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenActionService screenActionService = ScreenActionService.getInstance(getApplicationContext());
                List<ScreenAction> allScreenActions = screenActionService.listAll();

                try {
//                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                    File file = new File(dir, "screen.txt");
//                    FileOutputStream outputStream = new FileOutputStream(file);
//                    for (ScreenAction screenAction : allScreenActions) {
//                        outputStream.write(screenAction.toString().concat("\n").getBytes());
//                    }
//                    outputStream.flush();
//                    outputStream.close();

//                    File dir = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    String currentDBPath = "//data//com.unisinos.activitymonitor//databases//" + DatabaseHelper.DATA_BASE;
                    String backupDBPath  = "/BackupFolder/" + DatabaseHelper.DATA_BASE;

                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                    File file = new File(dir, DatabaseHelper.DATA_BASE);

                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(dir, DatabaseHelper.DATA_BASE);
//                    backupDB.createNewFile();

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }

}
