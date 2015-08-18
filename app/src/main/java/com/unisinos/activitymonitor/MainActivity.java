package com.unisinos.activitymonitor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unisinos.activitymonitor.service.ActionScreenReceiver;
import com.unisinos.activitymonitor.service.BackgroundService;
import com.unisinos.activitymonitor.service.ServiceRunningManagerThread;

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

//        Button btnInfo = (Button) findViewById(R.id.btn_info);
//        btnInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
//                for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
//                    if(info.processName.contains("service.BackgroundService")) {
//                        Toast.makeText(MainActivity.this, "Serviço está ativo", Toast.LENGTH_LONG);
//                        return;
//                    }
//                }
//                Toast.makeText(MainActivity.this, "Serviço offline", Toast.LENGTH_LONG);
//            }
//        });
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
