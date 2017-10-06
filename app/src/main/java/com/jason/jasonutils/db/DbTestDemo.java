package com.jason.jasonutils.db;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class DbTestDemo extends Activity implements OnClickListener {
	
	private Button bt_insert_db;
	private Button bt_query_db;
	private TextView tv_show_db;
	private MySQLiteHelper dbHelper = new MySQLiteHelper(this);
	
	private String databaseInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_testdemo_activity);
		
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		bt_insert_db = (Button) findViewById(R.id.bt_insert_db);
		bt_query_db = (Button) findViewById(R.id.bt_query_db);
		tv_show_db = (TextView) findViewById(R.id.tv_show_db);
		
		bt_insert_db.setOnClickListener(this);
		bt_query_db.setOnClickListener(this);
	}

	/**
	 * 处理点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_insert_db:
			write2Database();
			Toast.makeText(this, "写数据成功", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_query_db:
			databaseInfo = readFromDatabase();
			if(TextUtils.isEmpty(databaseInfo)){
				return;
			}
			tv_show_db.setText(databaseInfo.trim());
			break;
		}
	}

	/**
	 * 插入假数据：SIP通话记录
	 */
	private void write2Database() {
		SipCallRecordsDao.insertSipCallRecords(dbHelper.getWritableDatabase(),"mmy123321", "mmy1", "1233211", System.currentTimeMillis()+"", 20, 1, 1, new byte[]{});//已接通
		SipCallRecordsDao.insertSipCallRecords(dbHelper.getWritableDatabase(),"mmy123456", "mmy2", "1234562", System.currentTimeMillis()+"", 20, 2, 1, new byte[]{});//已拨出
		SipCallRecordsDao.insertSipCallRecords(dbHelper.getWritableDatabase(),"mmy123789", "mmy3", "1237893", System.currentTimeMillis()+"", 20, 3, 1, new byte[]{});//未接通
	}
	
	/**
	 * 从数据库读取数据
	 * @return
	 */
	private String readFromDatabase() {
		List<SipCallRecordsBean> sipCallRecordsList = SipCallRecordsDao.queryAllSipCallRecords(dbHelper.getReadableDatabase(),0);
		if(sipCallRecordsList.size()>0){
			StringBuffer sb = new StringBuffer();
			for(SipCallRecordsBean scrb :sipCallRecordsList){
				sb.append(scrb.toString());
				sb.append("\n");
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
			}
			return sb.toString();
		}
		return null;
	}
}
