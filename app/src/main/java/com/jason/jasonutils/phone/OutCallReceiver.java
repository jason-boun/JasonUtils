package com.jason.jasonutils.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 对拨出电话的广播进行捕获：
 * 权限：<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
 * 过滤器：<action android:name="android.intent.action.NEW_OUTGOING_CALL" />
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String outCallNumber = getResultData();
		String ip = context.getSharedPreferences("config", Context.MODE_PRIVATE).getString("IPnumber", "");//设置本地IP电话
		setResultData(ip+outCallNumber);
		abortBroadcast();
	}

}
