package com.jason.jasonutils.blacknumber;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.text.TextUtils;

public class BlackNumberDao {
	
	private BlackNumberDBhelper dbHelper;
	private String table = "blacknumber";
	
	public BlackNumberDao(Context context){
		dbHelper = new BlackNumberDBhelper(context);
	}
	
	/**
	 * 添加数据
	 * @param bean
	 * @return
	 */
	public int add(BlackNumberBean bean){
		if(bean !=null){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("number", bean.getNumber());
			values.put("mode", bean.getMode());
			int result = (int) db.insert(table, null, values);
			db.close();
			return result;
		}
		return -1;
	}
	
	/**
	 * 根据电话号码删除数据
	 * @param number
	 * @return
	 */
	public int delete(String number){
		if(!TextUtils.isEmpty(number)){
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			int result = db.delete(table, "number=?", new String[]{number});
			db.close();
			return result;
		}
		return -1;
	}
	
	/**
	 * 根据电话号码更新mode
	 * @param number
	 * @param newMode
	 * @return
	 */
	public int update(String number,int newMode){
		if(!TextUtils.isEmpty(number)){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("number",number);
			values.put("mode", newMode);
			int result = db.update(table, values, "number = ?", new String[]{number});
			db.close();
			return result;
		}
		return -1;
	}
	
	/**
	 * 根据电话号码查询是否存在
	 * @param number
	 * @return
	 */
	public boolean exist(String number){
		boolean result = false;
		if(!TextUtils.isEmpty(number)){
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.query(table, null, "number = ?", new String[]{number}, null, null, null);
			result = cursor.moveToFirst();
			cursor.close();
			db.close();
		}
		return result;
	}
	
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<BlackNumberBean> queryAll(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+table, null);
		if(cursor.getCount() == 0){
			return null;
		}
		List<BlackNumberBean> list = new ArrayList<BlackNumberBean>();
		BlackNumberBean bean = null;
		SystemClock.sleep(2000);
		while(cursor.moveToNext()){
			bean = new BlackNumberBean();
			bean.setNumber(cursor.getString(cursor.getColumnIndex("number")));
			bean.setMode(cursor.getString(cursor.getColumnIndex("mode")));
			list.add(bean);
		}
		cursor.close();
		db.close();
		return list;
	}
	
	/**
	 * 查询指定起始位置、指定size的数据
	 * @param startIndex
	 * @param pageSize
	 * @param pageQury 是否分页查找
	 * @return
	 */
	public List<BlackNumberBean> batchQuery(int startIndex, int blockSize, boolean pageQury){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor;
		if(pageQury){
			cursor = db.rawQuery("select * from blacknumber order by id desc limit ? offset ? ", new String[]{String.valueOf(blockSize), String.valueOf(startIndex)});
		}else{
			cursor = db.rawQuery("select * from blacknumber limit ? offset ? ", new String[]{String.valueOf(blockSize), String.valueOf(startIndex)});
		}
		List<BlackNumberBean> list = new ArrayList<BlackNumberBean>();
		BlackNumberBean bean = null;
		while(cursor.moveToNext()){
			bean = new BlackNumberBean();
			bean.setNumber(cursor.getString(cursor.getColumnIndex("number")));
			bean.setMode(cursor.getString(cursor.getColumnIndex("mode")));
			list.add(bean);
		}
		cursor.close();
		db.close();
		return list;
	}
	
	/**
	 * 查询指定页的数据
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<BlackNumberBean> queryInfoByPage(int pageNumber, int pageSize){
		int startIndex = (pageNumber-1) * pageSize;
		return batchQuery(startIndex, pageSize, true);
	}
	
	/**
	 * 查询数据库表格中数据实例条数
	 * @return
	 */
	public int getDbTotalSize(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber", null);
		return cursor.getCount();
	}
	
	
	/**
	 * 查找一条记录在数据库的拦截模式
	 * @return 返回null 代表数据库不存在这条记录
	 * 返回String 代表拦截模式. 1.全部 2电话 3短信
	 */
	public String findMode(String number){
		String result = null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mode from blacknumber where number=?", new String[]{number});
		if(cursor.moveToFirst()){
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}

}
