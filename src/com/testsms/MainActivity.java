package com.testsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	Button btnSendSMS;
	
	TextView txtLong;
	TextView txtLat;
	
	TextView txtLong2;
	TextView txtLat2;
	
	GPSTrack gps;
	String s_longitude, s_latitude;
	boolean error;
	
	SMSHandler sms;
	BroadcastReceiver smsreceive;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gps = new GPSTrack(this);
		
		
		btnSendSMS = (Button) findViewById(R.id.btnSend);
		txtLong= (TextView) findViewById(R.id.main_long);
		txtLat = (TextView) findViewById(R.id.main_lat);
		txtLong2 = (TextView) findViewById(R.id.main_long2);
		txtLat2 = (TextView) findViewById(R.id.main_lat2);
		
		if(gps.canGetLocation){
			double longitude = gps.getLongitude();
			double latitude = gps.getLatitude();
			
			txtLong.setText(""+longitude);
			txtLat.setText(""+latitude);
		}
		
		btnSendSMS.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					// sendSMS(phone, message);
					sms = new SMSHandler(MainActivity.this,"09272975972","getlocation");
				
			}
		});
		
	}
	
	public void onBackPressed(){
		this.finish();
	}
	
	public void onResume(){
		super.onResume();
		
	Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			//	phone = extras.getString("phone");
				s_longitude = extras.getString("longitude");
				s_latitude = extras.getString("latitude");
				txtLong2.setText(s_longitude);
				txtLat2.setText(s_latitude);
					
		}
		
		sms = new SMSHandler(this);
		
	}
	
	protected void onPause(){
		Log.w("Activity","Paused");
		super.onPause();
	}
	
	protected void onStart(){
		super.onStart();
	}
	
	protected void onStop(){
		Log.w("Activity", "Stopped");
		super.onStop();
	}
	
	protected void onDestroy(){
		Log.w("Activity", "Destroy");
		super.onDestroy();
	}
	

	public void ocRefresh(View view){
		GPSTrack gps = new GPSTrack(this);
		if(gps.canGetLocation){
			double longitude1 = gps.getLongitude();
			double latitude1 = gps.getLatitude();
			
			txtLong.setText(""+longitude1);
			txtLat.setText(""+latitude1);
			
		}else{
			gps.showAlert();
			//gps.turnGPSOn();
			
		}
	}
}
