package com.jason.jasonutils.comingcalladdress;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	
	//public static final String path = "/data/data/com.jason.jasonutils/files/address.db";
	/**
	 * 查询号码归属地
	 * @param context
	 * @param number
	 * @return
	 */
	public static String getAddress(Context context, String number){
		String address = number ;
		File file = new File(context.getFilesDir(),"address.db");
		if(!file.exists()){
			return number;
		}
		String path = file.getAbsolutePath();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		
		if(number.matches("^1[3458]\\d{9}$")){
			String sql = "select location from data2 where id = (select outkey from data1 where id=?)";
			Cursor cursor = db.rawQuery(sql, new String[]{(String) number.subSequence(0, 7)});
			if (cursor.moveToFirst()) {
				address = cursor.getString(0);
			}
			cursor.close();
		}else{
			switch (number.length()) {
			case 3:
				address = "特殊号码";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "特殊号码";
				break;
			case 7:
				address = "本地号码";
				break;
			case 8:
				address = "本地号码";
				break;
			default:
				if(number.length() >= 10 && number.startsWith("0")){
					//取前三位 查询.
					Cursor cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});
					if(cursor.moveToFirst()){
						String str = cursor.getString(0);
						//上海联通
						//黑龙江联通
						address = str.substring(0, str.length()-2);
					}
					cursor.close();
					//取前四位查询
					cursor = db.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});
					if(cursor.moveToFirst()){
						String str = cursor.getString(0);
						//上海联通
						//黑龙江联通
						address = str.substring(0, str.length()-2);
					}
					cursor.close();
				}
				break;
			}
		}
		db.close();
		return address;
	}

}
