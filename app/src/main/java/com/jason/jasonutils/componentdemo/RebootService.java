package com.jason.jasonutils.componentdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.jason.jasonutils.tools.MLog;

public class RebootService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	@Override
	public void onCreate() {
		MLog.i("哈哈", "服务被创建了");
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
