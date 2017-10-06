package com.jason.jasonutils.receiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jason.jasonutils.tools.MLog;

public class FinalRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MLog.i("哈哈", "我是最终的receiver");
	}

}
