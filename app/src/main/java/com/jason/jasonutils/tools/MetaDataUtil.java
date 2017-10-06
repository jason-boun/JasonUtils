package com.jason.jasonutils.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MetaDataUtil {
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

}
