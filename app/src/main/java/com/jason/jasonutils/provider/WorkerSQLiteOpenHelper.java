package com.jason.jasonutils.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkerSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static String DATABASE_NAME = "worker.db";
	public static String TABLE_NAME = "worker";
	private static int DB_VERSION = 1;
	

	public WorkerSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "+TABLE_NAME+" (id integer primary key autoincrement, name varchar(20), age integer, salary integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
