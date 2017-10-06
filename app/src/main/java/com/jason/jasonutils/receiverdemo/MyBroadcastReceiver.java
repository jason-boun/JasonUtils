package com.jason.jasonutils.receiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jason.jasonutils.tools.MLog;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MLog.i("哈哈", "reveriver 1 接收到了广播");
	}

	
}
