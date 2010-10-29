package com.tests.helloAndroid;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.LocalServerSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HelloAndroid extends Activity implements OnClickListener {
	private static final String TAG = "ServicesDemo";
	Button buttonStart, buttonStop, buttonGet, buttonSQL, buttonHttp;
	TextView textView;
	
	EditText editTextUserName, editTextPasswd;
	
	private static byte[] sBuffer = new byte[512];

	
	SQLiteDatabase db;
	
	HttpClient httpClient;
	
	//public Intent intent =  new Intent(this, MyService.class);
	private MyService myService;
	
	private Intent myServiceIntent; 
	
	private ServiceConnection conn = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			myService = null;
			
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			myService = ((MyService.MyBinder)service).getService();
		}
	};
	

@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.main);
  
  myServiceIntent = new Intent(this,MyService.class);

  buttonStart = (Button) findViewById(R.id.buttonStart);
  buttonStop = (Button) findViewById(R.id.buttonStop);
  buttonGet = (Button) findViewById(R.id.buttonGet);
  buttonSQL = (Button) findViewById(R.id.buttonSQL);
  buttonHttp = (Button) findViewById(R.id.buttonHTTP);
  
  editTextUserName = (EditText) findViewById(R.id.editTextUser);
  editTextPasswd = (EditText) findViewById(R.id.editTextPasswd);
  
  textView =  (TextView) findViewById(R.id.textView);

  buttonStart.setOnClickListener(this);
  buttonStop.setOnClickListener(this);
  buttonGet.setOnClickListener(this);
  buttonSQL.setOnClickListener(this);
  buttonHttp.setOnClickListener(this);
  

}

public void onClick(View src) {

	//Intent intent =  new Intent(this, MyService.class);
	switch (src.getId()) {
	case R.id.buttonStart:
		myServiceIntent.putExtra("start", "Start the Service");
		startService(myServiceIntent);
		bindService(new Intent(this,MyService.class), conn, 0);
		break;

	case R.id.buttonStop:
		try {
			unbindService(conn);
			myServiceIntent.putExtra("stop", "Stop the Service");
			stopService(myServiceIntent);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		break;
		
	case R.id.buttonGet:
		try {
			textView.setText(myService.getCounter().toString());
		} catch (Exception e) {
			if (myService==null){
				Toast.makeText(this, "The Service is not running!", Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		break;
	case R.id.buttonSQL:
		try {
			db = new MyDataBase(this).getReadableDatabase();
			
			//Cursor cursor = db.rawQuery("SELECT value FROM myTable WHERE title = 'Gravity Death Star'",null);
			Cursor cursor = db.rawQuery("SELECT * FROM myTable",null);
			
			cursor.moveToFirst();
//			
			do{
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					textView.setText(" " + textView.getText()+ " " + cursor.getString(i));
				}
				cursor.moveToNext();
				textView.setText(textView.getText()+"\n");
			}while (!cursor.isAfterLast()); 
			
			
//			while (cursor.isLast()){
//				cursor.moveToNext();
//				textView.setText(" " + textView.getText()+ " " + cursor.getString(cursor.getPosition()));
//				
//			}
			
			//String tmp = cursor.getString(1);
			//textView.setText(cursor.getColumnName(0) + " " + cursor.getColumnName(1) + " " + cursor.getColumnName(2) );
//			textView.setText(tmp);
			db.close();
			
		} catch (Exception e) {
			db.close();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		break;
		
	case R.id.buttonHTTP:
		try {
			httpClient = new DefaultHttpClient();
			//HttpGet request =  new HttpGet("http://www.google.com");
//			HttpGet request =  new HttpGet("http://localhost:8080/Test1/TestServlet?firstName=android");
			String url = new String("http://152.66.144.35:8080/PatientMonitoring2/AndroidLoginServlet?userName="
					+editTextUserName.getText()+"&passwd="+editTextPasswd.getText() );
			//HttpGet request =  new HttpGet("http://152.66.144.35:8080/PatientMonitoring2/AndroidLoginServlet?userName=hunci&passwd=punci");
			HttpGet request =  new HttpGet(url);
//			BasicHttpParams requestParams = new BasicHttpParams();
//			requestParams.setParameter("firstName", "android");
			
			
			//HttpGet request =  new HttpGet("http://192.168.1.3:8080/Test1/TestServlet?firstName=android");
			
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			HttpParams params = response.getParams();
			//textView.setText((String)params.getParameter("test"));
			//params.getParameter("");
			InputStream inputStream = entity.getContent();
//			int tmp;
//            while ((tmp = inputStream.read())!=-1){
//            	textView.setText(textView.getText()+ String.valueOf(tmp));
//            }
			ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
			textView.setText(content.toString());
			
			
		} catch (Exception e) {
			
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		break;
		
		

	}
}    


}
