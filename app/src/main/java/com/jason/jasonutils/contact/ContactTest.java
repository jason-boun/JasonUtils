package com.jason.jasonutils.contact;
import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.test.AndroidTestCase;
import com.jason.jasonutils.tools.MLog;
/**
 * 通讯录操作示例
 *
 */
public class ContactTest extends AndroidTestCase {
  private static final String TAG = "ContactTest";
  /**
   * 获取通讯录中所有的联系人
   */
  public void testGetContacts() throws Throwable {
    ContentResolver contentResolver = this.getContext().getContentResolver();
    String uriStr = "content://com.android.contacts/contacts";
    Uri uri = Uri.parse(uriStr);
    Cursor cursor = contentResolver.query(uri, null, null, null, null);
    // 遍历联系人
    while (cursor.moveToNext()) {
      // 联系人 ID
      int contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
      // 联系人显示名称
      String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
      
      // 联系人电话号码需要对另一个表进行查询，所以用到另一个 uri：content://com.android.contacts/data/phones 
      Cursor phones = getContext().getContentResolver().query(
                                  ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                  null,
                                  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "   // 根据上一步获取的联系人 id 来查询
                                                       + contactId, null, null);   
      String phone = "";
      while (phones.moveToNext()) {
                                // "data1"字段，所以此处也可以直接写 "data1"，但不推荐
        phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
      }
      
      // 再查询 Email。uri 为 : content://com.android.contacts/data/emails 
      Cursor emails = getContext().getContentResolver().query(
                                 ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                 null,
                                 ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
                                                      + contactId, null, null);
      while (emails.moveToNext()) {
                                /* 一样是 "data1" 字段。现在明白了吧？一个联系人的信息，其实被分成了好几条记录来保存，data1分别保存了各种重要的信息。
                                 * 是时候参照打开数据库我前面所说，去瞄一眼它的表结构了！
                                 */
        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        MLog.i("RongActivity", "emailAddress=" + emailAddress);
      }
      emails.close();
      MLog.i(TAG, "Contact [contactId= "+ contactId +"name=" + name + ", phone=" + phone + "]");
    }
  }
  
  /**
   * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId 
   * 这时后面插入data表的依据，只有执行空值插入，才能使插入的联系人在通讯录里面可见
   */
  public void testInsert() {
    ContentValues values = new ContentValues();
    //首先向RawContacts.CONTENT_URI执行一个空值插入(raw_contacts 表), 为了获取生成的联系人 ID
    Uri rawContactUri = this.getContext().getContentResolver().insert(RawContacts.CONTENT_URI, values);
    
    //然后获取系统返回的rawContactId ， 就是新加入的这个联系人的 ID
    long rawContactId = ContentUris.parseId(rawContactUri);
    
     /* Andorid 中，将联系人的姓名、电话、Email 
      * 分别存放在 data 表的同一个字段的三条记录当中
      * 因此要  Insert 三次 */    
    
    //往data表入姓名数据
    values.clear();
    
    // raw_contacts_id 字段，是 raw_contacts表id 的外键，用于说明此记录属于哪一个联系人
    values.put(Data.RAW_CONTACT_ID, rawContactId); 
    
    // mimitype_id 字段，用于描述此数据的类型，电话号码？Email？....
    values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);   // 注意查看第二个参数的常量值
    
    values.put(StructuredName.GIVEN_NAME, "文白菜");   // 这个名字真好听
    this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
    
    //往data表入电话数据
    values.clear();
    values.put(Data.RAW_CONTACT_ID, rawContactId);
    
    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE); 
    values.put(Phone.NUMBER, "15101689230");
    values.put(Phone.TYPE, Phone.TYPE_MOBILE);
    this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
    
    //往data表入Email数据
    values.clear();
    values.put(Data.RAW_CONTACT_ID, rawContactId);
    values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
    values.put(Email.DATA, "wenlin56@sina.com");
    values.put(Email.TYPE, Email.TYPE_WORK);
    this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
  }
  /**
   * 在同一个事务当中保存联系人
   */
  public void testSave() throws Throwable{
    //文档位置：reference/android/provider/ContactsContract.RawContacts.html
    ArrayList<ContentProviderOperation> infoList = new ArrayList<ContentProviderOperation>();
    
    int rawContactInsertIndex = infoList.size();
    infoList.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                                  .withValue(RawContacts.ACCOUNT_TYPE, null)
                                  .withValue(RawContacts.ACCOUNT_NAME, null)
                                  .build());
    //文档位置：reference/android/provider/ContactsContract.Data.html
    infoList.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                                  .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                                  .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                                  .withValue(StructuredName.GIVEN_NAME, "文萝卜")
                                  .build());
    // 更新手机号码：Data.RAW_CONTACT_ID 获取上一条语句插入联系人时产生的 ID
    infoList.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                                  .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                                              .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                                              .withValue(Phone.NUMBER, "15101689231")  // "data1"
                                              .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                                              .withValue(Phone.LABEL, "手机号")
                                              .build());
    infoList.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                                  .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                                              .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                                              .withValue(Email.DATA, "wenlin56@yahoo.cn")
                                              .withValue(Email.TYPE, Email.TYPE_WORK)
                                              .build());
    
    // 批量插入 -- 在同一个事务当中
    ContentProviderResult[] results = this.getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, infoList);
    for(ContentProviderResult result : results){
      MLog.i(TAG, result.uri.toString());
    }
  }
  
  /**
   * 删除联系人
   * @param context
   */
  public void dele(Context context){
	  ContentResolver cr = context.getContentResolver();
	    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    while (cur.moveToNext()) {
	        try{
	            String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
	            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
	            System.out.println("The uri is " + uri.toString());
	            cr.delete(uri, null, null);
	        }catch(Exception e){
	        	System.out.println(e.getStackTrace());
	        }
	    }
  	}
}