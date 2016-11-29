package com.testsms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		// GPSTrack gps = ((GPSTrack)context);

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		String message = "";
		String senderNum = "";
		boolean error = true;
		String longitude = "";
		String latitude = "";

		Bundle bundleToSend = new Bundle();

		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " : ";
				str += msgs[i].getMessageBody().toString();
				str += "n";

				message += msgs[i].getMessageBody().toString();
				senderNum += msgs[i].getOriginatingAddress().toString();

			}

			if (message.startsWith("MyLocation")) {
				/**
				 * message = "MyLocation::longitude::*value*::latitude::*value*
				 */
				Intent startIntent = new Intent(
						context.getApplicationContext(), MainActivity.class);
				// Start new task and clear the top
				startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				String[] split = message.split("::");
				longitude = split[2];
				latitude = split[4];
				
				bundleToSend.putBoolean("error", error);
				bundleToSend.putString("longitude", longitude);
				bundleToSend.putString("latitude", latitude);
				
				
				startIntent.putExtras(bundleToSend);
				context.startActivity(startIntent);
				
			//	showNotification(context,message,bundleToSend);
			}

			if (message.contains("getlocation")) {
				// sms.sendSMS(phone, message);
				GPSTrack gps = new GPSTrack(context.getApplicationContext());

				if (gps.canGetLocation) {

					longitude = String.valueOf(gps.getLongitude());
					latitude = String.valueOf(gps.getLatitude());
					
					SMSHandler sms = new SMSHandler(
							context.getApplicationContext(), senderNum,
							"MyLocation::longitude::" + longitude + "::"
									+ " latitude::" + latitude);

				} else {
					// SMSHandler sms = new
					// SMSHandler(context.getApplicationContext(),senderNum,"Cant detect user location");
					// gps.showAlert();
//					error = true;
//					Intent startIntent = new Intent(
//							context.getApplicationContext(), MainActivity.class);
//					startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					// context.startActivity(startIntent);
//					bundleToSend.putBoolean("error", error);
//					bundleToSend.putString("phone", senderNum);
//					startIntent.putExtras(bundleToSend);
//
//					context.getApplicationContext().startActivity(startIntent);
					showNotification(context,"Admin want to track your location");
					SMSHandler sms = new SMSHandler(context.getApplicationContext(),senderNum,"Cant detect the user, retry again.");
				}
			}

		}

	}
	
	private void showNotification(Context context,String message){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());
		builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
		Intent startIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, startIntent, 0);
		
		builder.setContentIntent(pendingIntent);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		builder.setContentTitle("GPS is not enabled");
		builder.setContentText(message);
		builder.setSubText("Tap to open GPS Settings");
		builder.setAutoCancel(true);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.notify(1,builder.build());
				
	}

}
