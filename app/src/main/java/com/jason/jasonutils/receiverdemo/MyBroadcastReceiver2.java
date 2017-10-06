package com.jason.jasonutils.receiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jason.jasonutils.tools.MLog;
import android.widget.Toast;

public class MyBroadcastReceiver2 extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MLog.i("哈哈", "reveriver 2  接收到了广播");
	}

	
}
