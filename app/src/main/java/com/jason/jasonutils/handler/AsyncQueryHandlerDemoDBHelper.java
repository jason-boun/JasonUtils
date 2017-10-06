package com.jason.jasonutils.handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AsyncQueryHandlerDemoDBHelper extends SQLiteOpenHelper {
	
	public final static String DATABASE__NAME = "async_query_handler_database.db";
	public final static String TABLE_NAME = "async_query_handler_table";
	
	public AsyncQueryHandlerDemoDBHelper(Context context) {
		super(context, DATABASE__NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists "+ TABLE_NAME + "("
		+"_id integer primary key autoincrement,"
		+"photo binary,"
		+"name varchar(30),"
		+"pinyin varchar(30),"
		+"number varchar(30)"
		+")";
		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}
