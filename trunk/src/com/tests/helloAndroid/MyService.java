package com.tests.helloAndroid;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	
	//Singleton 
	//private static MyService instance;
	
	private static final String TAG = "MyService";
	
	private boolean isRunning = false;
	private Timer timer;
	Integer counter = 0;
	
	private final MyBinder myBinder = new MyBinder(); 
	
	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		timer = new Timer();
		
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		counter = 0;
		//if (timer!=null) timer.cancel();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				counter++;
			}
			
		}, 0, 1000);
		
//		if (intent.getBooleanExtra("getValue", false)){
//			Toast.makeText(this, "Get Value", Toast.LENGTH_LONG).show();
//			Intent helloIntent = new Intent(this, HelloAndroid.class);
//			helloIntent.putExtra("text", "Ir a cuccot");
//			startActivity(helloIntent);
//			
//		}
		Toast.makeText(this, intent.getStringExtra("start"), Toast.LENGTH_LONG).show();
	}
	
	//Inner Class
	public class MyBinder extends Binder{
		MyService getService(){
			return (MyService.this);
		}
	}

}


