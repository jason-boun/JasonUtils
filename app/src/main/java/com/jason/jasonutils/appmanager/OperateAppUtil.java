package com.jason.jasonutils.appmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class OperateAppUtil {

	/**
	 * 打开一个APP
	 * @param ctx
	 * @param packageNmae
	 * @return
	 */
	public static boolean openApp(Context ctx, String packageNmae){
		try {
			PackageInfo packageinfo = ctx.getPackageManager().getPackageInfo(packageNmae, PackageManager.GET_ACTIVITIES);
			ActivityInfo[] activities = packageinfo.activities;
			if(activities!=null && activities.length>0){
				
				ActivityInfo firstInfo = activities[0];
				String className = firstInfo.name;
				String packageName = firstInfo.packageName;
				ctx.startActivity(new Intent().setClassName(packageName, className));
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 分享一个APP
	 * @param ctx
	 * @param packageNmae
	 */
	public static void shareApp(Context ctx, String packageNmae){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐你使用一款软件,下载地址: https://play.google.com/store/apps/details?id="+packageNmae);
		ctx.startActivity(intent);
	}
	
	/**
	 * 卸载一个APP
	 * @param act
	 * @param packageNmae
	 */
	public static void uninstallApp(Activity act, String packageNmae){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DELETE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:"+packageNmae));
		act.startActivityForResult(intent, 0);
	}
	
}
