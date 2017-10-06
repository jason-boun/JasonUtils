package com.jason.jasonutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.jason.jasonutils.tools.MLog;

public class MyService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		MLog.i("MyService", "onBind");
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		MLog.i("MyService", "onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		MLog.i("MyService", "onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		MLog.i("MyService", "onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		MLog.i("MyService", "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		MLog.i("MyService", "onRebind");
		super.onRebind(intent);
	}
	
	private String getServiceNameInfo(){
		return "服务数据:" + MyService.class.getSimpleName();
	}
	
	public class MyBinder extends Binder implements IMyService{
		MyService getService(){
			return MyService.this;
		}
		@Override
		public String getServiceInfo() {
			return getServiceNameInfo();
		}
	}

}
