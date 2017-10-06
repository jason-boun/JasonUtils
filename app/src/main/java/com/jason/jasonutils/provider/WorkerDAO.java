package com.jason.jasonutils.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 本地访问数据库的接口类
 */
public class WorkerDAO {
	
	private WorkerSQLiteOpenHelper workerSQLiteHelper;
	private SQLiteDatabase db;
	private String WORKER_TABLE;
	
	public WorkerDAO (Context context){
		workerSQLiteHelper = new WorkerSQLiteOpenHelper(context);
		db = workerSQLiteHelper.getWritableDatabase();
		WORKER_TABLE = WorkerSQLiteOpenHelper.TABLE_NAME;
	}
	
	/**
	 * 增
	 * @return
	 */
	public boolean insertWorker(ContentValues values){
		long insert = db.insert(WORKER_TABLE, null, values);
		if(insert>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 删
	 * @return
	 */
	public boolean deleteWorker(String whereClause, String[]whereArgs){
		int delete = db.delete(WORKER_TABLE, whereClause, whereArgs);
		if(delete>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 改
	 * @return
	 */
	public boolean updateWorker(ContentValues values, String whereClause, String[]whereArgs){
		int update = db.update(WORKER_TABLE, values, whereClause, whereArgs);
		if(update>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 查
	 * @return
	 */
	public Cursor queryWorker(String[]columns, String selection, String[]selectionArgs,String orderBy){
		return db.query(WORKER_TABLE, columns, selection, selectionArgs, null, null, orderBy);
	}
	
	/**
	 * 释放数据库资源
	 */
	public void releaseDB(){
		WORKER_TABLE = null;
		if(db!=null){
			db.close();
			db =null;
		}
		if(workerSQLiteHelper !=null){
			workerSQLiteHelper.close();
			workerSQLiteHelper = null;
		}
	}

}
