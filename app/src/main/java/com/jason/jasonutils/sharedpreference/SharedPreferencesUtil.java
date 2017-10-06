package com.jason.jasonutils.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

	/**
	 * 单例
	 */
	private SharedPreferencesUtil() {};
	private static SharedPreferencesUtil instance = new SharedPreferencesUtil();
	public static SharedPreferencesUtil getInstance() {
		return instance;
	}
	
	private SharedPreferences sp ;
	
	/**
	 * 保存参数
	 * @param context：上下文环境参数
	 * @param spFileName：保存在哪个文件里
	 * @param key：需保存的参数
	 * @param value：需保存的值
	 */
	public void setConfig(Context context, String spFileName, String key, Object value) {
		sp = context.getSharedPreferences(spFileName, context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if(value instanceof Boolean){
        	boolean v = (Boolean) value;
        	editor.putBoolean(key, v);
        }else if(value instanceof String){
        	String v = (String) value;
        	editor.putString(key, v);
        }else if(value instanceof Integer){
        	Integer v = (Integer) value;
        	editor.putInt(key, v);
        }
		editor.commit();
	}

	/**
	 * 从ShareFile文件夹中读取参数配置信息
	 * @param context：上下文环境参数
	 * @param spFileName：从哪个文件里读取
	 * @param key：需读取哪个参数
	 * @param defaultvalue：默认返回值
	 * @return
	 */
	public Boolean getConfig(Context context, String spFileName, String key, Boolean defaultvalue) {
		return context.getSharedPreferences(spFileName, context.MODE_PRIVATE).getBoolean(key, defaultvalue);
	}
	public int getConfig(Context context, String spFileName, String key, int defaultvalue) {
		return context.getSharedPreferences(spFileName, context.MODE_PRIVATE).getInt(key, defaultvalue);
	}
	public String getConfig(Context context, String spFileName, String key, String defaultvalue) {
		return context.getSharedPreferences(spFileName, context.MODE_PRIVATE).getString(key, defaultvalue);
	}
	
	/**
	 * 删除sp文件夹中指定文件名和key的标签
	 * @param context
	 * @param spFileName
	 * @param key
	 * @return
	 */
	public boolean removeKeyFromConfig(Context context, String spFileName, String key){
		sp = context.getSharedPreferences(spFileName, context.MODE_PRIVATE);
		if(sp.contains(key)){
			sp.edit().remove(key).commit(); 
			return true;
		}else{
			return false;
		}
	}

}
