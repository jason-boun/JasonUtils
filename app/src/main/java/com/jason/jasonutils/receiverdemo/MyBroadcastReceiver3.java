package com.jason.jasonutils.receiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jason.jasonutils.tools.MLog;

public class MyBroadcastReceiver3 extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MLog.i("哈哈", "reveriver 3 接收到了广播");
		abortBroadcast();
	}

	
}
