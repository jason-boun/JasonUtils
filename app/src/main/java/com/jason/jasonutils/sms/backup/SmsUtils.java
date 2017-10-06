package com.jason.jasonutils.sms.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsUtils {

	/**
	 * 进度情况的回调接口
	 */
	public interface BackUpStatusListener {
		public void beforeBack(int max);
		public void onProgress(int progress);
	}
	
	/**
	 * 备份短信
	 * @param context
	 * @param listener
	 * @throws Exception
	 */
	public static void backupSms(Context context, BackUpStatusListener listener) throws Exception {
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = context.getContentResolver().query(uri, new String[] { "address", "date", "type", "body" }, null, null, null);
		
		listener.beforeBack(cursor.getCount());//最大值回调
		
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
		FileOutputStream fos = new FileOutputStream(file);
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		int total = 0;
		while (cursor.moveToNext()) {
			serializer.startTag(null, "sms");
			String address = cursor.getString(0);
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			String date = cursor.getString(1);
			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			String type = cursor.getString(2);
			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			String body = cursor.getString(3);
			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");

			serializer.endTag(null, "sms");
			Thread.sleep(400);
			total++;
			listener.onProgress(total);//进度回调方法
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}

	/**
	 * 防止重复，恢复短信之前先删除系统短信
	 * @param context
	 */
	public static void deleteAllSms(Context context) {
		Uri uri = Uri.parse("content://sms/");
		context.getContentResolver().delete(uri, null, null);
	}

	/**
	 * 恢复短信
	 * @param context
	 * @throws Exception
	 */
	public static void restroeSms(Context context) throws Exception {
		Uri uri = Uri.parse("content://sms/");
		XmlPullParser parser = Xml.newPullParser();
		File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
		FileInputStream fis = new FileInputStream(file);
		parser.setInput(fis, "utf-8");

		int type = parser.getEventType();
		SmsInfo smsInfo = null;
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("sms".equals(parser.getName())) {
					smsInfo = new SmsInfo();
				}else if ("address".equals(parser.getName())) {
					smsInfo.address = parser.nextText();
				}else if ("body".equals(parser.getName())) {
					smsInfo.body = parser.nextText();
				}else if ("type".equals(parser.getName())) {
					smsInfo.type = parser.nextText();
				}else if ("date".equals(parser.getName())) {
					smsInfo.date = parser.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				if ("sms".equals(parser.getName())) {
					ContentValues values = new ContentValues();
					values.put("address", smsInfo.address);
					values.put("body", smsInfo.body);
					values.put("type", smsInfo.type);
					values.put("date", smsInfo.date);
					context.getContentResolver().insert(uri, values);
				}
				break;
			}
			type = parser.next();
		}
	}
	
	static class SmsInfo{
		String address;
		String body;
		String type;
		String date;
	}


}
