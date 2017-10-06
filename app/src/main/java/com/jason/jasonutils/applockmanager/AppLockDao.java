package com.jason.jasonutils.applockmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {
	
	private Context context;
	private ApplockDBHelper helper;

	public AppLockDao(Context context) {
		this.context = context;
		helper = new ApplockDBHelper(context);
	}
	
	/**
	 * 添加一条记录到数据库
	 * @param packname 要锁定的包名
	 */
	public boolean add(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		long result = db.insert("applock", null, values);
		db.close();
		if(result>0){
			context.getContentResolver().notifyChange(AppLockService.appLockDBUri, null);//向某个uri发送一个内容变化的通知
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 查找一条记录是否在数据库
	 */
	public boolean find(String packname){
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select id from applock where packname=?", new String[]{packname});
		if(cursor.moveToFirst()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	
	/**
	 * 移除一条包名
	 * @param packname
	 * @return
	 */
	public boolean delete(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("applock", "packname=?", new String[]{packname});
		db.close();
		if(result>0){
			context.getContentResolver().notifyChange(AppLockService.appLockDBUri, null);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查询全部信息
	 * @return
	 */
	public List<String> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> packnames = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select packname from applock", null);
	
		while(cursor.moveToNext()){
			String packname = cursor.getString(0);
			packnames.add(packname);
		}
		db.close();
		return packnames;
	}
	
}
