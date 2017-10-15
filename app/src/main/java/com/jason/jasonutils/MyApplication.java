package com.jason.jasonutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jason.jasonutils.pwdlock.Constant;
import com.jason.jasonutils.pwdlock.PwdLockActivity;
import com.jason.jasonutils.tools.MLog;

public class MyApplication extends Application {

	private boolean userMode = false;//如果是用户模式。则启动错误日志打印记录功能，如果是开发者模式则不开启该模式
	
	@Override
	public void onCreate() {
		super.onCreate();
		fixErrorLogHandler();
		fixMLog();
		startVerify();
	}
	
	
	/**
	 * 部署全局APP错误捕获机制
	 */
	public void fixErrorLogHandler(){
		if(userMode){
//			Thread.currentThread().setUncaughtExceptionHandler(new MyUnCaughtExceptionHandler(getApplicationContext()));
			Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler(getApplicationContext()));
		}
	}
	
	/**
	 * 部署全局Log日志标签和级别
	 */
	public void fixMLog(){
		MLog.setTag("哈哈");
		MLog.setEnableLevel(MLog.INFO);
	}
	
	/**
	 * Manager TaskStack
	 */
	public static List<Activity> runningActivities = new ArrayList<Activity>();
	
	public static void addActivity(Activity act){
		runningActivities.add(act);
	}
	
	public static void removeActivity(Activity act){
		runningActivities.remove(act);
	}
	
	public static void finishAllActivity(Activity act){
		int taskStackSize = runningActivities.size();
		if(taskStackSize>0){
			for(int i=0;i<taskStackSize;i++){
				if(act.getClass().getSimpleName().equals(runningActivities.get(i).getClass().getSimpleName())){
					continue;
				}
				runningActivities.get(i).finish();
				runningActivities.remove(i);
				i--;//每次都从第一个开始移除
			}
			runningActivities.remove(act);
			act.finish();
		}
		quitApp();
	}
	
	/**
	 * 是否在前台运行判断
	 * @param context
	 * @return
	 */
	public boolean isRunningForeground(Context context){
		ActivityManager am= (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		RunningTaskInfo runningTaskInfo = am.getRunningTasks(1).get(0);
		ComponentName topAct = runningTaskInfo.topActivity;
		String packageName = topAct.getPackageName();
		if(!TextUtils.isEmpty(packageName) && packageName.equals(context.getPackageName())){
			return true;
		}
		return false;
	}
	
	/**
	 * @return process PID
	 */
	public int getPid(){
		return android.os.Process.myPid();
	}
	
	/**
	 * @return MAC address
	 */
	public String getMacAddress(){
		WifiManager wifiM = (WifiManager) getSystemService(WIFI_SERVICE);
		if(wifiM!=null){
			WifiInfo wifiInfo = wifiM.getConnectionInfo();
			if(wifiInfo !=null){
				return  wifiInfo.getMacAddress();// 48位，如FA:34:7C:6D:E4:D7
			}
		}
		return "";
	}

	/**
	 * device model name, e.g: GT-I9100
	 * @return the user_Agent
	 */
	public String getDevice() {
		return Build.MODEL;
	}

	/**
	 * device factory name, e.g: SamSung
	 * @return the vENDOR
	 */
	public String getVendor() {
		return Build.BRAND;
	}

	/**
	 * @return the SDK version
	 */
	public int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * @return the OS version
	 */
	public String getOSVersion() {
		return Build.VERSION.RELEASE;
	}
	
	/**
	 *  内存不足时回调此方法
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	/**
	 * @return VersionName
	 */
	public String getVersionName() {
		String version = "0.0.0";
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	/**
	 * @return VersionCode
	 */
	public int getVersionCode() {
		int code = 1;
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			code = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * @return IMEI
	 */
	public String getDeviceId(){
		TelephonyManager tm  = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	/**
	 * @return phone number
	 */
	public String getSimNumber(Context context){
		TelephonyManager tm  = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}
	
	public static void quitApp() {
		System.exit(0);
	}
	
	/**
	 * 处理监控密码锁
	 */
	private TimerTask task;
	private boolean isMonitor = false;
	private Timer timer ;
	private static final int RESTART_VERIFY = 30;
	@SuppressLint("HandlerLeak")
	private Handler taskHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESTART_VERIFY:
				Verify();
				break;
			}
		}
	}; 
	public void startVerify(){
		if(timer ==null){
			timer = new Timer();
		}
		if(task == null){
			task = new TimerTask() {
				@Override
				public void run() {
					taskHandler.sendEmptyMessage(RESTART_VERIFY);
				}
			};
		}
		if(!isMonitor){
			timer.schedule(task, Constant.LOCK_REVERIFY_PEROID, Constant.LOCK_REVERIFY_PEROID);
			MLog.i("哈哈","Application中密码锁监控开启了");
			isMonitor = true;
		}
	}
	
	public void stopVerify(){
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
		if(task !=null){
			task.cancel();
			task = null;
		}
		isMonitor = false;
	}
	private void Verify() {
		if(isRunningForeground(getApplicationContext()) && isMonitor && !PwdLockActivity.isLock(getApplicationContext())){
			if(Constant.LIMITED_LOCK_TIME > Constant.LOCK_REVERIFY_PEROID){
				PwdLockActivity.cleanLockSPparams(getApplicationContext());//如果是非锁定状态，则清除错误记录
			}
			Intent intent = new Intent(getApplicationContext(),PwdLockActivity.class);
			intent.putExtra(Constant.NEED_BACK, true);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	};

}
