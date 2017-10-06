package com.jason.jasonutils.sms.sendmsg;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoTool {
	/**
	 * 返回系统所有的联系人的信息
	 * @return
	 */
	public static List<ContactInfo> getContactInfos(Context context){
		//创建一个系统所有联系人的集合
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();
		// raw_contact 表的uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		// data 表的uri
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" }, null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			if (id != null) {
				Cursor dataCursor = resolver.query(dataUri, new String[] {"data1", "mimetype" }, "raw_contact_id=?", new String[] { id }, null);
				ContactInfo contactInfo = new ContactInfo();
				while (dataCursor.moveToNext()) {
					String data = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if("vnd.android.cursor.item/name".equals(mimetype)){
						contactInfo.setName(data);
					}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
						contactInfo.setNumber(data);
					}
				}
				contactInfos.add(contactInfo);
				dataCursor.close();
			}
		}
		cursor.close();
		return contactInfos;
	}
}
