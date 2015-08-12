package com.unisinos.activitymonitor.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.unisinos.activitymonitor.R;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Button btnStart = (Button) findViewById(R.id.btn_start);
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startService(new Intent(MainActivity.this, MyService.class));
			}
		});
		
		Button btnStop = (Button) findViewById(R.id.btn_stop);
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(new Intent(MainActivity.this, MyService.class));
			}
		});
		
		Button btnInfo = (Button) findViewById(R.id.btn_info);
		btnInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				PackageManager pm = getPackageManager();
				List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
				StringBuilder builder = new StringBuilder();
				for (ApplicationInfo packageInfo : packages) {
					builder.append(packageInfo.uid).append(" | ");
					builder.append(packageInfo.className).append(" | ");
					builder.append(packageInfo.descriptionRes).append(" | ");
					builder.append(packageInfo.packageName).append("\n");
				}
				Log.i("APPINFO", builder.toString());
				
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_main, container, false);
		}
	}

}
