package com.jason.jasonutils.expandablelistview;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberDao {
	
	public static String path = "/data/data/com.jason.jasonutils/files/commonnum.db";

	/**
	 * 返回数据库里面有多少个分组
	 * @return
	 */
	public static int getGroupCount() {
		int count = 0;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select count(*) from classlist", null);
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		}
		return count;
	}

	/**
	 * 返回每个分组里面有多少个孩子.
	 */
	public static int getChildCountByPosition(int groupPosition) {
		int count = 0;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			int newPosition = groupPosition + 1;
			String sql = "select * from table" + newPosition;
			Cursor cursor = db.rawQuery(sql, null);
			count = cursor.getCount();
			cursor.close();
			db.close();
		}
		return count;
	}

	/**
	 * 返回指定位置分组的名称
	 */
	public static String getGroupNameByPosition(int groupPosition) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		String name = "";
		if (db.isOpen()) {
			String sql = "select name from classlist where idx = ?";
			int newPosition = groupPosition + 1;
			Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(newPosition) });
			if (cursor.moveToFirst()) {
				name = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}
		return name;
	}

	/**
	 * 返回所有分组的名字列表
	 */
	public static List<String> getGroupNames() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		List<String> namesList = new ArrayList<String>();
		if (db.isOpen()) {
			String sql = "select name from classlist";
			Cursor cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String name = cursor.getString(0);
				namesList.add(name);
			}
			cursor.close();
			db.close();
		}
		return namesList;
	}

	/**
	 * 返回某个分组里面孩子的信息.
	 */
	public static String getChildInfoByPosition(int groupPosition, int childPosition) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		String info = "";
		if (db.isOpen()) {
			int tableNumber = groupPosition + 1;
			int newChildPosition = childPosition + 1;
			String sql = "select name,number from table" + tableNumber + " where _id = ?";
			Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(newChildPosition) });
			if (cursor.moveToFirst()) {
				String name = cursor.getString(0);
				String number = cursor.getString(1);
				info = name + "\n" + number;
			}
			cursor.close();
			db.close();
		}

		return info;
	}

	/**
	 * 返回某个分组里面所有的孩子信息
	 * @param groupPosition
	 * @return
	 */
	public static List<String> getChildInfosByPosition(int groupPosition) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		List<String> infos = new ArrayList<String>();
		if (db.isOpen()) {
			int tableNumber = groupPosition + 1;
			String sql = "select name,number from table" + tableNumber;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String name = cursor.getString(0);
				String number = cursor.getString(1);
				String info = name + "\n" + number;
				infos.add(info);
			}
			cursor.close();
			db.close();
		}
		return infos;
	}

}
