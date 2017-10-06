package com.jason.jasonutils.tools;

import com.jason.jasonutils.sharedpreference.SharedPreferencesUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
/**
 * 两个权限：
 * com.android.launcher.permission.INSTALL_SHORTCUT
 * com.android.launcher.permission.READ_SETTINGS
 */
public class ShortCutUtil {
	
	private final static String CREATE_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";
	private final static String ANDROID_SYSTEM_SETTING_URI_BELOW8 = "content://com.android.launcher.settings/favorites";
	private final static String ANDROID_SYSTEM_SETTING_URI = "content://com.android.launcher2.settings/favorites";
	
	/**
	 * 创建快捷图标
	 * @param act 上下文
	 * @param clazz 程序的主入口才可以创建
	 * @param appName 快捷图标名字
	 * @param icon 快捷图标
	 */
	public static void createShortCut(Activity act, Class<?> clazz, String appName, int icon) {
    	if(!isExist(act,appName)){
    		//点击动作Intent
    		Intent click = new Intent();
        	click.setAction(Intent.ACTION_MAIN);
        	click.addCategory(Intent.CATEGORY_LAUNCHER);
        	click.setComponent(new ComponentName(act, clazz));
        	
        	Intent shortIconSet = new Intent(CREATE_SHORTCUT_ACTION);
        	ShortcutIconResource showIcon = Intent.ShortcutIconResource.fromContext(act,icon);
        	shortIconSet.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, showIcon);//指定图标
        	shortIconSet.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);//名称
        	shortIconSet.putExtra("duplicate", false);//不重复创建
        	shortIconSet.putExtra(Intent.EXTRA_SHORTCUT_INTENT, click);//设置点击intent
			
        	act.sendBroadcast(shortIconSet);
    	}
	}
 
    /**
     * 查询系统数据库，快捷图标是否存在 
     * @param context
     * @return
     */
    private static boolean isExist(Context context,String appName) {
    	boolean exist = false;
    	int version = android.os.Build.VERSION.SDK_INT;
    	Uri uri = null;
    	if(version >= 8){
    		uri = Uri.parse(ANDROID_SYSTEM_SETTING_URI);
    	}else{
    		uri = Uri.parse(ANDROID_SYSTEM_SETTING_URI_BELOW8);
    	}
    	String selection = " title = ?";
    	String[] selectionArgs = new String[]{appName};
		
    	Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);
    	if(cursor==null){
    		exist = SharedPreferencesUtil.getInstance().getConfig(context, "config", "shortCut_hasCreated", false);
    		if(!exist){
    			SharedPreferencesUtil.getInstance().setConfig(context, "config", "shortCut_hasCreated", true);
    		}
    	}else{
    		if(cursor.moveToFirst()){
    			exist = true;
        	}
    		cursor.close();
    	}
    	return exist;
    }
    
	/**
	 * 创建快捷图标
	 * @param act
	 * @param iconResId
	 * @param appnameResId
	 */
	public static void createShortCut(Activity act, int appnameResId, int iconResId) { 
    	if(!isExist(act, act.getString(appnameResId))){
	        Intent shortcutintent = new Intent(CREATE_SHORTCUT_ACTION);
	        shortcutintent.putExtra("duplicate", false);  // 不允许重复创建
	        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,act.getString(appnameResId));  //名称
	        Parcelable icon = Intent.ShortcutIconResource.fromContext(act.getApplicationContext(), iconResId); //图片
	        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);  
	        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,new Intent(act.getApplicationContext(), act.getClass()));  
	        act.sendBroadcast(shortcutintent);
    	}
    }
	
	/**
	 * 创建快捷图标：卸载程序可删除快捷图标(适配各种特殊情况)
	 * @param act 上下文
	 * @param clazz 程序入口类字节码
	 * @param appNameId 名称
	 * @param iconId 图标
	 */
	public static void createShortCut(Activity act, Class<?> clazz, int appNameId, int iconId) {
		boolean hasCreated = false;
		String CREATE_SHORTCUT_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";
		String uri = "content://com.android.launcher2.settings/favorites";
		String uri_below8 = "content://com.android.launcher.settings/favorites";
    	Cursor cursor = act.getContentResolver().query(Uri.parse(android.os.Build.VERSION.SDK_INT>8?uri:uri_below8),null," title = ?",new String[]{act.getString(appNameId)},null);
    	if(cursor==null){
    		hasCreated = SharedPreferencesUtil.getInstance().getConfig(act, "config", "shortCut_hasCreated", false);
    		if(!hasCreated){
    			SharedPreferencesUtil.getInstance().setConfig(act, "config", "shortCut_hasCreated", true);
    		}
    	}else{
    		if(cursor.moveToFirst()){
        		hasCreated = true;
        	}
    		cursor.close();
    	}
    	if(!hasCreated){
    		//点击动作Intent
    		Intent click = new Intent();
        	click.setAction(Intent.ACTION_MAIN);
        	click.addCategory(Intent.CATEGORY_LAUNCHER);
        	click.setComponent(new ComponentName(act, clazz));
        	
        	Intent shortIconSet = new Intent(CREATE_SHORTCUT_ACTION);//动作
        	ShortcutIconResource showIcon = Intent.ShortcutIconResource.fromContext(act,iconId);
        	shortIconSet.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, showIcon);//指定图标
        	shortIconSet.putExtra(Intent.EXTRA_SHORTCUT_NAME, act.getString(appNameId));//名称
        	shortIconSet.putExtra("duplicate", false);//不重复创建
        	shortIconSet.putExtra(Intent.EXTRA_SHORTCUT_INTENT, click);//设置点击intent
			
        	act.sendBroadcast(shortIconSet);
    	}
	}

}
