package com.jason.jasonutils.componentdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;

/**
 * 同一个Activity中可以只注册一个BroadcastReceiver，通过添加不同的Action来接收多个广播
 */
public class McWillReceiverDemo extends Activity implements OnClickListener {
	
	private TextView tv_phone_status;
	private Button bt_send;
	private String TAG = McWillReceiverDemo.class.getSimpleName();
	
	private static final String BROADCAST_ACTION_MCWILL = "android.intent.action.PHONE_STATE";
	private static final String BROADCAST_ACTION_MYDEFINE = "jason.broadcast";
	
	MyReceiver myReceiver ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mcwill_phonestatus_activity);
		
		init();
		registMBroadCast(myReceiver);
	}
	public void init() {
		tv_phone_status = (TextView) this.findViewById(R.id.tv_phone_status);
		bt_send = (Button) findViewById(R.id.bt_send);
		bt_send.setOnClickListener(this);
	}
	/**
	 * 注册自己的广播接收者
	 */
	private void registMBroadCast(BroadcastReceiver myReceiver) {
		if(myReceiver == null){
			myReceiver = new MyReceiver();
		}
		IntentFilter mfilter = new IntentFilter();
		mfilter.addAction("jason.broadcast");
		mfilter.addAction("android.intent.action.PHONE_STATE");
		registerReceiver(myReceiver, mfilter);
	}
	
	public class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent !=null){
				if(intent.getAction().equals(BROADCAST_ACTION_MYDEFINE)){
					MLog.i(TAG, "我自己发送的广播, 数据为="+ intent.getStringExtra("data"));
					String info = intent.getStringExtra("data");
					tv_phone_status.setText("我自己发送的广播, 数据为="+ info);
				}else if(intent.getAction().equals(BROADCAST_ACTION_MCWILL)){
					int phoneStatus = intent.getIntExtra("com.android.phone.id", 0);
					String comNumber = intent.getStringExtra("incoming_number");
					if(phoneStatus==2 && !TextUtils.isEmpty(comNumber)){
						MLog.i(TAG+"来单号码", intent.getStringExtra("incoming_number")+"");
						tv_phone_status.setText(TAG+"来单号码"+ intent.getStringExtra("incoming_number"));
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.bt_send:
			Intent intent = new Intent();
			intent.setAction(BROADCAST_ACTION_MYDEFINE);
			intent.putExtra("data", "123456");
			sendBroadcast(intent);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(myReceiver != null){
			this.unregisterReceiver(myReceiver);
			myReceiver = null;
		}
	}
}
