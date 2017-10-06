package com.jason.jasonutils.contact;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OperateSysContactsActivity extends Activity implements OnClickListener {

	private Button bt_insert_contact, bt_delete_contact;
	private int totalCount = 100;
	private static final int INSERT_CONTACTS = 10;
	private static final int DELETE_CONTACTS = 20;
	private static final String PRIFIX_NAME = "信威xinwei";
	
	private int OPERATE_FLAG = 0;
	private ProgressBar pb_progressbar;
	private TextView tv_progress_info_text;
	private EditText et_input_number;
	private Context ctx ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operate_system_contacts_activity);
		init();
	}

	private void init() {
		ctx = OperateSysContactsActivity.this;
		bt_insert_contact = (Button) findViewById(R.id.bt_insert_contact);
		bt_delete_contact = (Button) findViewById(R.id.bt_delete_contact);
		pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
		pb_progressbar.setVisibility(View.GONE);
		tv_progress_info_text = (TextView) findViewById(R.id.tv_progress_info_text);
		et_input_number = (EditText) findViewById(R.id.et_input_number);
		
		bt_insert_contact.setOnClickListener(this);
		bt_delete_contact.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_insert_contact:
			totalCount = getCount();
			OPERATE_FLAG = INSERT_CONTACTS;
			new MyAsyncTask().execute(OPERATE_FLAG);
			break;
		case R.id.bt_delete_contact:
			OPERATE_FLAG = DELETE_CONTACTS;
			new MyAsyncTask().execute(OPERATE_FLAG);
			break;
		}
	}
	
	public class MyAsyncTask extends AsyncTask<Integer, Integer, Integer>{
		Integer count = 0;
		@Override
		protected Integer doInBackground(Integer... params) {
			if(INSERT_CONTACTS==(params[0]) && totalCount>0){
				pb_progressbar.setMax(totalCount);
				pb_progressbar.setProgress(0);
				ContentValues values= null;
				for(int i=0; i<totalCount;i++){
					count = i+1;
					values = new ContentValues();
					Uri rawContactUri = ctx.getContentResolver().insert(RawContacts.CONTENT_URI, values);
					long rawContactId = ContentUris.parseId(rawContactUri);
					//往data表里写入姓名
					values.clear();
					values.put(Data.RAW_CONTACT_ID, rawContactId);
					values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
				    values.put(StructuredName.GIVEN_NAME, PRIFIX_NAME+i);
				    ctx.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    //往data表里写入手机号码
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				    values.put(Phone.NUMBER, "13712334"+i);
				    values.put(Phone.TYPE, Phone.TYPE_MOBILE);
				    ctx.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    //往data表里写入Email
				    values.clear();
				    values.put(Data.RAW_CONTACT_ID, rawContactId);
				    values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				    values.put(Email.DATA, "xinwei"+i+"@163.com");
				    values.put(Email.TYPE, Email.TYPE_WORK);
				    ctx.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
				    
				    values = null;
				    publishProgress(count);
				}
			}else if(DELETE_CONTACTS==(params[0])){
				ContentResolver resolver = ctx.getContentResolver();
				Uri uriRaw_id = Uri.parse("content://com.android.contacts/raw_contacts");
				Uri uriData = Uri.parse("content://com.android.contacts/data");
				Cursor cursor = resolver.query(uriRaw_id, null, "display_name like ?", new String[]{"%"+PRIFIX_NAME+"%"}, null);
				if(cursor!=null && cursor.getCount()>0){
					totalCount = cursor.getCount();
					pb_progressbar.setMax(totalCount);
					pb_progressbar.setProgress(0);
					count=0;
					while(cursor.moveToNext()){
						String id = cursor.getString(cursor.getColumnIndex("contact_id"));
						String name = cursor.getString(cursor.getColumnIndex("display_name"));
						if(null!= id){
							resolver.delete(uriData, "contact_id = ?", new String[]{id});
							resolver.delete(uriRaw_id, "contact_id = ?", new String[]{id});
						}else if(null!=name && !TextUtils.isEmpty(name.trim())){
							resolver.delete(uriData, "display_name = ?", new String[]{name});
							resolver.delete(uriRaw_id, "display_name = ?", new String[]{name});
						}
						count++;
						publishProgress(count);
					}
				}
			}
			return count;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pb_progressbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(OPERATE_FLAG==(INSERT_CONTACTS) && result>0){
				Toast.makeText(getApplicationContext(), "操作完毕", Toast.LENGTH_SHORT).show();
				tv_progress_info_text.setText("处理完毕：" + "共生成"+result+"个系统联系人");
			}else if(OPERATE_FLAG==(DELETE_CONTACTS) && result>0){
				Toast.makeText(getApplicationContext(), "操作完毕", Toast.LENGTH_SHORT).show();
				tv_progress_info_text.setText("处理完毕：" + "共删除"+result+"个系统联系人");
			}
			pb_progressbar.setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pb_progressbar.setProgress(values[0]);
			if(OPERATE_FLAG==(INSERT_CONTACTS)){
				tv_progress_info_text.setText("正在生成：	" + values[0] + "/" + totalCount);
			}else if(OPERATE_FLAG==(DELETE_CONTACTS)){
				tv_progress_info_text.setText("正在删除：	 " + values[0] + "/" + totalCount);
			}
		}
	}
	
	/**
	 * 批量插入联系人操作
	 */
	public Integer insertOperate(Context context, int count){
		ContentValues values= null;
		count = 0;
		for(int i=0; i<totalCount;i++){
			values = new ContentValues();
			Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);
			
			//往data表里写入姓名
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
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
		    */
		    
		    //往data表里写入单位电话
		    /* values.clear();
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
		    count++;
		}
		return count;
	}
	
	/**
	 * 批量删除联系人操作
	 */
	public int deleteOperate(Context context){
		ContentResolver resolver = context.getContentResolver();
		Uri uriRaw_id = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uriRaw_id, null, "display_name like ?", new String[]{"%信威%"}, null);
		if(cursor!=null && cursor.getCount()>0){
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex("contact_id"));
				String name = cursor.getString(cursor.getColumnIndex("display_name"));
				if(null!= id){
					resolver.delete(uriData, "contact_id = ?", new String[]{id});
					resolver.delete(uriRaw_id, "contact_id = ?", new String[]{id});
				}else if(null!=name && !TextUtils.isEmpty(name.trim())){
					resolver.delete(uriData, "display_name = ?", new String[]{name});
					resolver.delete(uriRaw_id, "display_name = ?", new String[]{name});
				}
			}
		}
		return 0;
	}
	
	/**
	 * 获取需要插入的系统联系人数目
	 * @return
	 */
	public int getCount(){
		String num = et_input_number.getText().toString().trim();
		if(!TextUtils.isEmpty(num) && Integer.parseInt(num)>0){
			if(Integer.parseInt(num)<=500){
				return Integer.parseInt(num);
			}else {
				Toast.makeText(getApplicationContext(), "数字太大", Toast.LENGTH_SHORT).show();
				return 0;
			}
		}else{
			Toast.makeText(getApplicationContext(), "请输入有效的数字", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}
	
}
