package com.testsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSHandler{
	

	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";
	
	BroadcastReceiver senderBroadcast;
	BroadcastReceiver receiverBroadcast;
	Context context;
	
	public SMSHandler(Context context){
		this.context = context;
		registerBroadcast(context);
	}
	
	public SMSHandler(Context context, String phoneNum, String message){
		this.context = context;
		
		sendSMS(context,phoneNum,message);
	}
	
	private void sendSMS(Context context,String phoneNum, String message) {
		// IntentFilter intentFilter = new IntentFilter(SENT);
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(
				SENT), 0);
		// Register broadcast
		registerBroadcast(context);
		
		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
				new Intent(DELIVERED), 0);

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNum, null, message, sentPI, deliveredPI);
	}

	
	public void registerBroadcast(Context context){
		senderBroadcast = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				switch(getResultCode()){
				case Activity.RESULT_OK:
					Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(context, "Generic Failure", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(context, "No Service", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
					break;
				}
			}
			
		};
		
		context.registerReceiver(senderBroadcast,new IntentFilter(SENT));
		
		receiverBroadcast = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				switch(getResultCode()){
				case Activity.RESULT_OK:
					Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(context, "SMS note delivered", Toast.LENGTH_SHORT).show();
					break;
				}
				
			}
			
		};
		
		context.registerReceiver(receiverBroadcast, new IntentFilter(DELIVERED));
		
	}
	
	
	public void unregisterBroadcast(Context context){
		context.unregisterReceiver(senderBroadcast);
		context.unregisterReceiver(receiverBroadcast);
	}
	
	
}
