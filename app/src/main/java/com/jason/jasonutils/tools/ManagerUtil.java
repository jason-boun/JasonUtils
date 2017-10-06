package com.jason.jasonutils.tools;

import java.lang.reflect.Method;
import java.util.Iterator;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

public class ManagerUtil {
	
	/**
	 * 是否在前台运行
	 * @param context
	 * @return
	 */
	public boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());
	}
	
	/**
	 * 获取应用名称
	 */
	@SuppressWarnings("unused")
	private String getAppName(Context ctx) {
		String processName = null;
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		Iterator<?> i = am.getRunningAppProcesses().iterator();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == android.os.Process.myPid()) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	
	 /** 
     * screen是否打开状态 
     * @param pm 
     * @return 
     */  
    public static boolean isScreenOn(Context context) {  
        boolean screenState;  
        PowerManager manager = (PowerManager) context.getSystemService(Activity.POWER_SERVICE); 
        try {  
        	Method mReflectScreenState = PowerManager.class.getMethod("isScreenOn", new Class[] {}); 
        	 screenState = (Boolean) mReflectScreenState.invoke(manager);  
        } catch (Exception e) {  
        	e.printStackTrace();
            screenState = false;  
        }  
        return screenState;  
    }
    
	/**
	 * 获取配置清单中application节点下的元数据
	 */
	public static String getApplicationMetaData(Context ctx,String key){
		try {
			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			return ai.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取配置清单中Activity节点下的元数据
	 */
	public static String getActMetaData(Context ctx,Class<?> actClazz, String key){
		try {
			ComponentName cn=new ComponentName(ctx, actClazz);
			ActivityInfo ai = ctx.getPackageManager().getActivityInfo(cn, PackageManager.GET_META_DATA);
			return ai.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获取配置清单中Service节点下的元数据
	 */
	public static String getServiceMetaData(Context ctx,Class<?> serClazz, String key){
		try {
			ServiceInfo si = ctx.getPackageManager().getServiceInfo(new ComponentName(ctx, serClazz), PackageManager.GET_META_DATA);
			return si.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获取配置清单中BroadcastReceiver节点下的元数据
	 */
	public static String getReceiverMetaData(Context ctx,Class<?> receiverClazz, String key){
		try {
			ActivityInfo ai = ctx.getPackageManager().getReceiverInfo(new ComponentName(ctx, receiverClazz), PackageManager.GET_META_DATA);
			return ai.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	/**
	 * 检测网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		return mNetworkInfo != null && mNetworkInfo.isAvailable();
	}

}
