package com.jason.jasonutils.dbframeworktool.dao;

import com.jason.jasonutils.dbframeworktool.base.DBConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context) {
		super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DBConstants.TABLE_NEWS_NAME + " (" + //
				DBConstants.TABLE_ID + " integer primary key autoincrement, " + //
				DBConstants.TABLE_NEWS_TITLE + " varchar(50), " + //
				DBConstants.TABLE_NEWS_SUMMARY + " VARCHAR(200))"//
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
