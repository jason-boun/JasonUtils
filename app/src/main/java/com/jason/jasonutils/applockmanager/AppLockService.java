package com.jason.jasonutils.applockmanager;

import java.util.ArrayList;
import java.util.List;

import com.jason.jasonutils.tools.MLog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class AppLockService extends Service {
	
	private ActivityManager am;
	private AppLockDao dao;
	private List<String> lockedPacknames;
	private List<String> tempReleaseList = new ArrayList<String>();
	private Intent authIntent;
	private boolean flag;
	private AppLockAuthReceiver lockReceiver;
	
	public static final String ACTION_RELEASE_APPLOCK = "com.jason.jasonutils.releaseapplock";
	public static final String PACKAGE_NAME = "package_name";
	public static final Uri appLockDBUri = Uri.parse("content://com.jason.jasonutils/applockdbchanged");
	
	public MyContentObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		MLog.i("服务开启");
		registReceiver();
		registObserver();
		
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new AppLockDao(this);
		lockedPacknames = dao.findAll();
		authIntent = new Intent(this, AppLockAuthActivity.class);
		authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startWatchDog();
	}

	private void startWatchDog() {
		new Thread(){
			public void run() {
				flag = true;
				while(flag){
					List<RunningTaskInfo> tasks = am.getRunningTasks(1);
					RunningTaskInfo taskInfo = tasks.get(0);
					String packName = taskInfo.topActivity.getPackageName();
					if(lockedPacknames.contains(packName) ){
						if(!tempReleaseList.contains(packName)){
							authIntent.putExtra(PACKAGE_NAME, packName);
							startActivity(authIntent);
						}
					}
					SystemClock.sleep(1000);
				}
			};
		}.start();
	}
	
	/**
	 * 自定义广播接收者
	 */
	private class AppLockAuthReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ACTION_RELEASE_APPLOCK)){
				tempReleaseList.add(intent.getStringExtra(PACKAGE_NAME));
			}else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
				tempReleaseList.clear();
				flag = false;
			}else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				startWatchDog();
			}
		}
	}
	
	/**
	 * 自定义程序锁数据库内容观察者
	 */
	private class MyContentObserver extends ContentObserver{
		public MyContentObserver(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			MLog.i("程序锁数据库发生变化了");
			lockedPacknames = dao.findAll();
		}
	}

	private void registReceiver() {
		if(lockReceiver==null){
			lockReceiver = new AppLockAuthReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_RELEASE_APPLOCK);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(lockReceiver, filter);
	}
	
	private void unregistReceiver(){
		if(lockReceiver!=null){
			unregisterReceiver(lockReceiver);
			lockReceiver = null;
		}
	}
	
	private void registObserver(){
		if(observer == null){
			observer = new MyContentObserver(new Handler());
		}
		getContentResolver().registerContentObserver(appLockDBUri, true, observer);
	}
	
	private void unRegistObserver(){
		if(observer !=null){
			getContentResolver().unregisterContentObserver(observer);
			observer = null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		unregistReceiver();
		unRegistObserver();
	}

}
