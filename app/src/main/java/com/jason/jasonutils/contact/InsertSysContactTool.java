package com.jason.jasonutils.contact;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

public class InsertSysContactTool {

	/**
	 * 向系统通讯录中插入数据
	 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
	 * 这时后面插入data表的数据，只有执行空值插入，才能使插入的联系人在通讯录里面可见
	 */
	public static void testInsert(final Context context) {
		new Thread(){
			public void run() {
				ContentValues values= null;
				for(int i=0; i<300;i++){
					values = new ContentValues();
					//首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
					Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
					long rawContactId = ContentUris.parseId(rawContactUri);
					//往data表里写入姓名
					values.clear();
					values.put(Data.RAW_CONTACT_ID, rawContactId);
					values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); //内容类型
				    values.put(StructuredName.GIVEN_NAME, "信威"+i);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    //往data表里写入手机号码
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "13712334"+i);
				    values.put(Phone.TYPE, Phone.TYPE_MOBILE);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    //往data表里写入家庭电话
				   /* values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "138122384"+i);
				    values.put(Phone.TYPE, Phone.TYPE_HOME);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    //往data表里写入单位电话
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "13921009"+i);
				    values.put(Phone.TYPE, Phone.TYPE_WORK);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);*/
				    
				    //往data表里写入Email
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				    values.put(Email.DATA, "xinwei"+i+"@163.com");
				    values.put(Email.TYPE, Email.TYPE_WORK);
				    context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    values = null;
				}
			};
		}.start();
	}
}
