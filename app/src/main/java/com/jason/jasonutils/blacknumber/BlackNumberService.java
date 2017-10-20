package com.jason.jasonutils.blacknumber;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.jason.jasonutils.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlackNumberService extends Service {

	private BlackNumberDao dao;
	private MsgReceiver msgReceiver;
	private TelephonyManager tm;
	private PhoneStateListener listener;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		msgReceiver = new MsgReceiver();
		registerReceiver(msgReceiver, filter);
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	/**
	 * 系统来电监听者
	 */
	private class MyPhoneStateListener extends PhoneStateListener{
		
		long comingCallTime = 0;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				comingCallTime = System.currentTimeMillis();
				String mode = dao.findMode(incomingNumber);
				if("1".equals(mode) || "2".equals(mode)){
					//挂断电话之前，先启动注册通话记录监听者。
					getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls/"), true, new SystemCallLogOberver(new Handler(), incomingNumber));
					endCall();
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				if((System.currentTimeMillis() - comingCallTime)<3000){//是来电一声响情况
					showNotification(incomingNumber);
				}
				break;
			}
		}
		
	}
	
	/**
	 * 通过反射技术调用系统底层接口来挂断电话
	 */
	public void endCall(){
		try {
			Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", new Class[]{String.class});
			IBinder binder = (IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
			ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 系统通话记录变化观察者
	 */
	private class SystemCallLogOberver extends ContentObserver{

		private String comingNumber;
		public SystemCallLogOberver(Handler handler,String comingNumber) {
			super(handler);
			this.comingNumber = comingNumber;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getContentResolver().unregisterContentObserver(this);
			getContentResolver().delete(CallLog.CONTENT_URI, "number = ?", new String[]{comingNumber});
		}
	}
	
	/**
	 * 通知栏弹出来电一声响通知
	 * @param incomingNumber
	 */
	public void showNotification(String incomingNumber){
		
		Notification notif = new Notification(R.drawable.incoming_call, "来电一声响", System.currentTimeMillis());
		notif.flags = Notification.FLAG_AUTO_CANCEL;
		
		Intent intent = new Intent(this, ShowPageLoadActivity.class);
		intent.putExtra("number", incomingNumber);
		
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notif.setLatestEventInfo(this, "发现来单一声响", "来单一声响号码是："+incomingNumber, pi);
		
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(0, notif);
	}
	
	
	/**
	 * 系统短信接收监听者
	 */
	private class MsgReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Object [] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj :objs){
				SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
				String senderNumber = message.getOriginatingAddress();
				String body = message.getMessageBody();
				
				if(body.contains("发票")){//流氓短信拦截
					abortBroadcast();
				}
				String mode = dao.findMode(senderNumber);
				if("1".equals(mode)||"3".equals(mode)){
					abortBroadcast();
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(msgReceiver !=null){
			unregisterReceiver(msgReceiver);
			msgReceiver = null;
		}
		if(listener !=null){
			tm.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
		}
	}

}
