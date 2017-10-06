package com.jason.jasonutils.sms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import com.jason.jasonutils.tools.MLog;

public class CallRecordsAndSmsRecordsChangeedService extends Service{

    public static final String SYSTEM_CALLRECORDS_CHANGED = "system_callrecords_changed";
    public static final String SYSTEM_MSGRECORDS_CHANGED = "system_msgrecords_changed";
    
    private CallRecordsObserver callRecordsObserver;
    private SystemSMGObserver sysSmsObserver;
    
    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}
    
    /**
     * 服务开启的时候，创建 ：短信和通话记录变化监听器
     */
	@Override
	public void onCreate() {
		super.onCreate();
		MLog.i(this.getClass().getSimpleName(), "服务创建了");
		callRecordsObserver = new CallRecordsObserver(new Handler());
		getContentResolver().registerContentObserver(Calls.CONTENT_URI, true, callRecordsObserver);
		
		sysSmsObserver = new SystemSMGObserver(new Handler());
		getContentResolver().registerContentObserver(Uri.parse("content://mms-sms/conversations?simple=true"), true, sysSmsObserver);
	}

	/**
	 * 服务销毁的时候，注销：短信和通话记录变化监听器
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(callRecordsObserver !=null){
			getContentResolver().unregisterContentObserver(callRecordsObserver);
			callRecordsObserver = null;
		}
		if(sysSmsObserver !=null){
			getContentResolver().unregisterContentObserver(sysSmsObserver);
			sysSmsObserver = null;
		}
	}

	/**
	 * 更新通话记录的广播
	 * @param context
	 */
	public static void sendBroadcast4UpdateCallRecords(Context context){
		Intent intent = new Intent();
		intent.setAction(SYSTEM_CALLRECORDS_CHANGED);
		context.sendBroadcast(intent);
	}
	/**
	 * 更新message记录的广播
	 * @param context
	 */
	public static void sendBroadcast4UpdateMsgRecords(Context context){
		Intent intent = new Intent();
		intent.setAction(SYSTEM_MSGRECORDS_CHANGED);
		context.sendBroadcast(intent);
	}
	
	/**
	 * 系统通话记录观察者
	 */
	class CallRecordsObserver extends ContentObserver{
		public CallRecordsObserver(Handler handler) { 
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			sendBroadcast4UpdateCallRecords(CallRecordsAndSmsRecordsChangeedService.this);
		}
	}
	/**
	 * 系统短信记录观察者
	 */
	class SystemSMGObserver extends ContentObserver{
		public SystemSMGObserver(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			sendBroadcast4UpdateMsgRecords(CallRecordsAndSmsRecordsChangeedService.this);
		}
	}
}
