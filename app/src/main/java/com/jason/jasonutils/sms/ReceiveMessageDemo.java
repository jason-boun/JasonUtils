package com.jason.jasonutils.sms;

import java.util.HashMap;
import java.util.Map;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReceiveMessageDemo extends Activity implements OnClickListener {
	
    private final static String TAG = ReceiveMessageDemo.class.getSimpleName();
    private final static String SMS_URI_ALL = "content://sms/";   //所有    //table :sms
    private final static String SMS_URI_INBOX = "content://sms/inbox";    //收件箱
    private final static String SMS_URI_SEND = "content://sms/sent";      //发件箱
    private final static String SMS_URI_DRAFT = "content://sms/draft";     //草稿箱
    private final static String SMS_URI_CONVERSATION = "content://sms/conversations";   //table: threads
    private final static String SMS_URI_CANONICAL_ADDRESSES = "content://mms-sms/canonical-addresses";  // table :canonical_addresses
    
    private Button bt_read_new_msg;
    private TextView tv_show_latest_msg;
    private String number = "10010";
    
    private SysCallRecordsSmsChangedReceiver sysChangeReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reaceive_latestmsg_activity);
		init();
		registerReceiver();
	}
	
	private void init() {
		bt_read_new_msg = (Button) findViewById(R.id.bt_read_new_msg);
		tv_show_latest_msg = (TextView) findViewById(R.id.tv_show_latest_msg);
		
		bt_read_new_msg.setOnClickListener(this);
	}
	
	private void registerReceiver() {
		if(sysChangeReceiver== null){
			sysChangeReceiver = new SysCallRecordsSmsChangedReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(CallRecordsAndSmsRecordsChangeedService.SYSTEM_CALLRECORDS_CHANGED);
		filter.addAction(CallRecordsAndSmsRecordsChangeedService.SYSTEM_MSGRECORDS_CHANGED);
		registerReceiver(sysChangeReceiver, filter);
	}

	@Override
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_read_new_msg:
			showMsg();
			break;
		}
	}
	
	private void showMsg() {
		Map latestMsg = getMsgByTel(this, number, true);
		if(latestMsg.size()>0){
			String readNumber = (String) latestMsg.get("tel");
			if(number.equals(readNumber)){
				String content = (String) latestMsg.get("content");
				tv_show_latest_msg.setText(content);
			}
		}
	}

	
	   /**
     * 获取最新一条信息
     * @param context
     * @param tel
     * @param latestMsg
     * @return
     */
    public static Map getMsgByTel(Context context, String tel, boolean getLatestMsg) {
    	if(getLatestMsg){
            Map map = null;
            Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_INBOX), null, "address = ? ", new String[]{tel}, null);
            if (cursor.moveToFirst()) {
                map = new HashMap();
                map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
                map.put("thread_id", cursor.getInt(cursor.getColumnIndex("thread_id")));
                map.put("tel", cursor.getString(cursor.getColumnIndex("address")));
                map.put("person", cursor.getInt(cursor.getColumnIndex("person")));
                map.put("date", cursor.getLong(cursor.getColumnIndex("date")) + "");
                map.put("protocol", cursor.getInt(cursor.getColumnIndex("protocol")));
                map.put("read", cursor.getInt(cursor.getColumnIndex("read")));
                map.put("status", cursor.getInt(cursor.getColumnIndex("status")));
                map.put("type", cursor.getInt(cursor.getColumnIndex("type")));//类型 1是接收到的，2是发出的
                map.put("reply_path_present", cursor.getInt(cursor.getColumnIndex("reply_path_present")));
                map.put("subject", cursor.getString(cursor.getColumnIndex("subject")));
                map.put("content", cursor.getString(cursor.getColumnIndex("body")));
                map.put("service_center", cursor.getString(cursor.getColumnIndex("service_center")));
                map.put("locked", cursor.getInt(cursor.getColumnIndex("locked")));
            }
            if (cursor != null)
                cursor.close();
            return map;
    	}
        return null;
    }

    /**
     * 拦截最新一条短信
     * @param intent
     */
	public void getMsg(Intent intent){
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		if(messages!=null){
			byte[][] pduObjs = new byte[messages.length][];
		}
	}
	/**
	 * 接收到系统短信和通话记录变化广播之后的处理动作
	 */
	public class SysCallRecordsSmsChangedReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(CallRecordsAndSmsRecordsChangeedService.SYSTEM_CALLRECORDS_CHANGED.equals(intent.getAction())){
				MLog.i(TAG, "系统通话记录发生变化了");
			}else if(CallRecordsAndSmsRecordsChangeedService.SYSTEM_MSGRECORDS_CHANGED.equals(intent.getAction())){
				MLog.i(TAG, "系统短信发生变化了");
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(sysChangeReceiver != null){
			unregisterReceiver(sysChangeReceiver);
			sysChangeReceiver = null;
		}
	}
}
