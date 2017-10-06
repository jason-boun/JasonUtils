package com.jason.jasonutils.receiverdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SendBroadCastReceiverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setAction("com.jason.broadcast.demo");
		//sendBroadcast(intent); //发一条无序广播.所有的广播接受者 都会接受到这个事件
		//sendOrderedBroadcast(intent, null);//发一条有序广播， 广播接收者会按照优先级接受到广播，高优先级的广播接受者 可终止掉 广播
		sendOrderedBroadcast(intent, null, new FinalRecevier(), null, 0, null, null);//发一个都可以指定最终接收广播的接收者的有序广播
	}
}
