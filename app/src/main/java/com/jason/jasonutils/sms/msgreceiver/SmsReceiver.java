package com.jason.jasonutils.sms.msgreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	private static final String targetNumber = "5556";
	private static final String targetContent = "我拦截了你的短信";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			String body = smsMessage.getMessageBody();
			String sender = smsMessage.getOriginatingAddress();
			System.out.println("body====" + body+", sender===="+sender);
			if (targetNumber.equals(sender)) {
				abortBroadcast();
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(sender, null, targetContent, null, null);
			}
		}
	}
}
