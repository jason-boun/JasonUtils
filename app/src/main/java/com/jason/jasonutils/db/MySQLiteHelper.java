package com.jason.jasonutils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "sipcall_records.db";
	private final static int DATABASE_VERSION = 1;
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建SIP电话通话记录表格
		String sql = "create table if not exists t_sipcall_records ("  
				+ "id integer primary key autoincrement," 
				+ "jid varchar(30)," //CamTalk用户JID
				+ "name varchar(30)," //号码备注，如果没有则使用numbe
				+ "number varchar(30)," //号码
				+ "date varchar(30)," //通话日期
				+ "duration integer," //通话时长
				+ "type integer," //通话类型：1 ：打进电话，2：打出电话， 3 ：未接电话
				+ "voip_type integer," //电话方式：1 ：VOIP电话；2：直拨电话
				+ "photo binary" //头像
				+ ")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try{
			//创建SIP电话通话记录表格
			db.beginTransaction();
			String sql = "create table if not exists t_sipcall_records ("  
					+ "id integer primary key autoincrement," 
					+ "jid varchar(30)," //CamTalk用户JID
					+ "name varchar(30)," //号码备注，如果没有则使用number
					+ "number varchar(30)," //号码
					+ "date varchar(30)," //通话日期
					+ "duration integer," //通话时长
					+ "type integer," //通话类型：1 ：打进电话，2：打出电话， 3 ：未接电话
					+ "voip_type integer," //电话方式：1 ：VOIP电话；2：直拨电话
					+ "photo binary" //头像
					+ ")";
			db.execSQL(sql);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}

}
