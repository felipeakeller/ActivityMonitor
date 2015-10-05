package com.unisinos.activitymonitor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unisinos.activitymonitor.db.DatabaseHelper;
import com.unisinos.activitymonitor.service.BackgroundService;
import com.unisinos.activitymonitor.ui.ServiceRunningManagerThread;
import com.unisinos.activitymonitor.util.FileManager;
import com.unisinos.activitymonitor.util.MailSender;

import java.io.File;

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

        Button btnExportar = (Button) findViewById(R.id.btn_export);
        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fileSaved = FileManager.create()
                        .dataBasePath("//data//com.unisinos.activitymonitor//databases//" + DatabaseHelper.DATA_BASE)
                        .saveDirectory(Environment.getExternalStorageDirectory());

                MailSender.create(MainActivity.this)
                        .to("felipeakeller@gmail.com")
                        .send(Uri.fromFile(fileSaved));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
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