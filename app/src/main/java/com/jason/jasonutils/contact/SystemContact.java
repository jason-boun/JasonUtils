package com.jason.jasonutils.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContactsEntity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import com.jason.jasonutils.tools.MLog;

public class SystemContact {
	List<Map<String,Object> > list = new ArrayList<Map<String,Object> >();

	private Activity Context;

	public SystemContact(Activity context) {
		this.Context = context;
	}

	public void preparContacts() {
		if (Context != null)
			getContactsInformation2(Context);
	}
	
	public void getContactsInformation2(Activity context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = null;
		try{
			cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME }, null, null, null);
			if (cursor!=null&&cursor.getCount() > 0) {
				Map<String,Object> map = null;
				while (cursor.moveToNext()) {
					map = new HashMap<String,Object>();
					//获取id
					String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					map.put("id", id);
					//获取名字
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					map.put("name", name);
					
					//获取FirstName、LastName：
					Cursor nameCur = null;
					try{
						nameCur = resolver.query(RawContactsEntity.CONTENT_URI, null, RawContacts.CONTACT_ID + "=?" , new String[]{id}, null);
						if(nameCur!=null && nameCur.moveToFirst()) {
							String firstName = nameCur.getString(nameCur.getColumnIndex(StructuredName.GIVEN_NAME));
							String lastName = nameCur.getString(nameCur.getColumnIndex(StructuredName.FAMILY_NAME));
							map.put("firstname", firstName);
							map.put("lastname", lastName);
							nameCur.close();
						}
					}finally{
						if(nameCur != null){
							nameCur.close();
						}
					}
					
					//获取头像
					String phoneWhere = ContactsContract.Data.CONTACT_ID + " = ? and " + ContactsContract.Data.MIMETYPE + " = ?";
					String[] phoneWhereParams = new String[] {id, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE };
					Cursor photoCur =null;
					try{
						photoCur = resolver.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, phoneWhere,phoneWhereParams, null);
						if (photoCur!=null&&photoCur.moveToFirst()) {
							byte[] image = photoCur.getBlob(photoCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
							map.put("image", image);
						}
					}finally{
						if(photoCur!=null){
							photoCur.close();	
						}
					}

					//获取号码
					Cursor pCur = null;
					try{
						pCur = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
						while (pCur!=null&&pCur.moveToNext()) {
							String num = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							String type = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							if (TextUtils.isEmpty(num))
								continue;
							if (type.equals("1"))
								map.put("homeTel", num.replace("-", ""));
							else if (type.equals("2"))
								map.put("telephone", num.replace("-", ""));
							else if (type.equals("3"))
								map.put("workTel", num.replace("-", ""));
						}
					}finally{
						if(pCur!=null){
							pCur.close();
						}
					}

					//获取邮箱
					Cursor emailCur = null;
					try{
						emailCur = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Email.DATA}, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
						if (emailCur!=null&&emailCur.moveToNext()) {
							String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							if (!TextUtils.isEmpty(email)){
								map.put("email", email);
							}
						}
					}finally{
						if(emailCur!=null){
							emailCur.close();
						}
					}
					
					//获取地址
					String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					String[] addrWhereParams = new String[] {id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
					Cursor addrCur  = null;
					try{
						addrCur = resolver.query( ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.StructuredPostal.STREET}, addrWhere, addrWhereParams, null);
						if (addrCur!=null&&addrCur.moveToFirst()) {
							String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
							if (!TextUtils.isEmpty(street)) {
								map.put("address", street);
							}
						}
					}finally{
						if(addrCur!=null){
							addrCur.close();
						}
					}
					list.add(map);
				}
			}
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			System.gc();	
		}
	}

	public List<Map<String,Object> > getSysContacts() {
		return list;
	}
	
	/**
	 * 根据电话号码删除系统联系人
	 * @param context
	 * @param number
	 */
	public static void delSysContactByPhoneNumber(Context context,String number){
		number = PhoneNumberUtils.formatNumber(number);
		ContentResolver resolver = context.getContentResolver();
		
		Uri dataUri = ContactsContract.Data.CONTENT_URI;//dataUri===content://com.android.contacts/data
		Uri contactUri = ContactsContract.Contacts.CONTENT_URI;//contactUri===content://com.android.contacts/contacts
		Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;//rawContactUri===content://com.android.contacts/raw_contacts
		String [] projection =  new String[]{ContactsContract.Data.RAW_CONTACT_ID};
		String selection = ContactsContract.Data.DATA1 + "=?" ;
		String [] selectionArgs = new String[]{number};
		
		Cursor cursor = null;
		try{
			cursor = resolver.query(dataUri, projection, selection, selectionArgs, null);
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
				if(!TextUtils.isEmpty(id)){
					int result1 = resolver.delete(dataUri, ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{id});//删除Data表中的数据
					int result2 = resolver.delete(contactUri, ContactsContract.Contacts._ID + "=?", new String[]{id});//删除contact表中的数据
					int result3 = resolver.delete(rawContactUri, ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{id});//删除raw_contact表中的数据
					MLog.i("哈哈","删除结果：result1==="+result1+";;;;result2 =="+result2+";;;result3==="+result3);
				}
			}
		}finally{
			if(cursor != null){
				cursor.close();
				cursor = null;
			}
		}
	}

}
