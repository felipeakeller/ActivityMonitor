package com.unisinos.activitymonitor.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

	private static final String TAG = "TESTE";

	private CaptureDataService captureDateService;
	
	private Thread thread;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Serviço criado", Toast.LENGTH_LONG).show();
		
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		captureDateService = new CaptureDataService(manager);
		thread = new Thread(captureDateService);
		
		Log.d(TAG, "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Serviço iniciado", Toast.LENGTH_LONG).show();
		thread.start();
		Log.d(TAG, "onStart");
        return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		System.out.println("PAROU");
		Toast.makeText(this, "Serviço parado", Toast.LENGTH_LONG).show();
		captureDateService.stop();
		Log.d(TAG, "onDestroy");
	}
	
}