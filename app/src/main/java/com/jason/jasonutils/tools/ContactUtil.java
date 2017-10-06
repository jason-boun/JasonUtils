package com.jason.jasonutils.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

public class ContactUtil {

	/**
	 * 根据电话号码删除系统联系人
	 * @param context
	 * @param number
	 */
	public static void delSysContactByPhoneNumber(Context context,String number){
		number = PhoneNumberUtils.formatNumber(number);
		ContentResolver resolver = context.getContentResolver();
		
		Uri dataUri = ContactsContract.Data.CONTENT_URI;
		Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
		Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
		
		String [] projection =  new String[]{ContactsContract.Data.RAW_CONTACT_ID};
		String selection = ContactsContract.Data.DATA1 + "=?" ;
		String [] selectionArgs = new String[]{number};
		
		Cursor cursor  = null;
		try{
			cursor= resolver.query(dataUri, projection, selection, selectionArgs, null);
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
				if(!TextUtils.isEmpty(id)){
					resolver.delete(dataUri, ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{id});
					resolver.delete(contactUri, ContactsContract.Contacts._ID + "=?", new String[]{id});
					resolver.delete(rawContactUri, ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{id});
				}
			}
		}finally{
			if(cursor !=null){
				cursor.close();
				cursor = null;
			}
		}
	}
}
