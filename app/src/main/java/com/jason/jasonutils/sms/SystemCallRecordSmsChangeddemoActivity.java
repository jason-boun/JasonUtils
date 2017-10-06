package com.jason.jasonutils.sms;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SystemCallRecordSmsChangeddemoActivity extends Activity {
	
	private TextView tv_show_service_info;
	private MySysListenerInfoReceiver myReceiver;
	private Intent sysCommunicationServiceIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.system_sms_callrecords_changed_activity);
		
		init();
		startSysListenerService();
		registerReceiver();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		tv_show_service_info = (TextView) findViewById(R.id.tv_show_service_info);
	}

	/**
	 * 开启新的服务：监控系统短信和通话记录的变化
	 */
	public void startSysListenerService(){
		if(sysCommunicationServiceIntent == null){
			sysCommunicationServiceIntent = new Intent(this, CallRecordsAndSmsRecordsChangeedService.class);
		}
		startService(sysCommunicationServiceIntent);
	}
	/**
	 * 停止服务
	 */
	public void stopService(){
		if(sysCommunicationServiceIntent!=null){
			stopService(sysCommunicationServiceIntent);
			sysCommunicationServiceIntent = null;
		}
	}

	/**
	 * 注册广播接收者
	 */
	private void registerReceiver() {
		
		if(myReceiver == null){
			myReceiver = new MySysListenerInfoReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(CallRecordsAndSmsRecordsChangeedService.SYSTEM_CALLRECORDS_CHANGED);
		filter.addAction(CallRecordsAndSmsRecordsChangeedService.SYSTEM_MSGRECORDS_CHANGED);
		registerReceiver(myReceiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onStop();
		if(myReceiver!=null){
			unregisterReceiver(myReceiver);
			myReceiver = null;
		}
	}

	
	public class MySysListenerInfoReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(CallRecordsAndSmsRecordsChangeedService.SYSTEM_CALLRECORDS_CHANGED.equals(intent.getAction())){
				Toast.makeText(SystemCallRecordSmsChangeddemoActivity.this, "系统通话记录发生了变化", 0).show();
				tv_show_service_info.setText("系统通话记录发生了变化");
			}else if(CallRecordsAndSmsRecordsChangeedService.SYSTEM_MSGRECORDS_CHANGED.equals(intent.getAction())){
				Toast.makeText(SystemCallRecordSmsChangeddemoActivity.this, "系统短信发生了变化", 0).show();
				tv_show_service_info.setText("系统短信发生了变化");
			}
		}
		
	}
}
