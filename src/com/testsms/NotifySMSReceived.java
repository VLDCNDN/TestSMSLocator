package com.testsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;

public class NotifySMSReceived extends Activity {

	private static final String LOG_CAT = "SMS_RECEIVER";
	private static final int NOTIFICATION_ID_RECEIVED = 0x1221;
	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		GPSTrack gps = new GPSTrack(this);
		gps.showAlert();
		
		
	}
}
