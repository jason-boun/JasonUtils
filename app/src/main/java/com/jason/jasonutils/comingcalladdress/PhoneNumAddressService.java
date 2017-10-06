package com.jason.jasonutils.comingcalladdress;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class PhoneNumAddressService extends Service {

	private MyOutgoingCallReceiver mOutGoingCallreceiver ;
	private TelephonyManager tm;
	private MyPhoneListner listener;
	private WindowManager wm ; 
	private View addressView;
	private SharedPreferences sp;
	
	private static final String SP_LASTX = "lastx";
	private static final String SP_LASTY = "lasty";
	
	private int[] icons;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		mOutGoingCallreceiver = new MyOutgoingCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(mOutGoingCallreceiver, filter);
		
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyPhoneListner();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	/**
	 * 窗体上挂载自定义的ToastView
	 * @param address
	 */
	public void showAddressInWindow(String address) {
		if(icons == null){
			icons = new int[]{ R.drawable.call_locate_white, R.drawable.call_locate_orange, 
					R.drawable.call_locate_blue, R.drawable.call_locate_green, R.drawable.call_locate_gray };
		}
		int which = sp.getInt("which", 0);
		if(addressView == null){
			addressView = View.inflate(getApplicationContext(),R.layout.toast_show_address, null);
		}
		((TextView) addressView.findViewById(R.id.tv_toast_address)).setText(address);
		addressView.setBackgroundResource(icons[which]);
		
		final WindowManager.LayoutParams layoutParams = new LayoutParams();
		layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		layoutParams.x = sp.getInt(SP_LASTX, 0);
		layoutParams.y = sp.getInt(SP_LASTY, 0);
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		
		layoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_KEEP_SCREEN_ON;
		layoutParams.format = PixelFormat.TRANSLUCENT;
		layoutParams.type = LayoutParams.TYPE_PRIORITY_PHONE;
		
		addressView.setOnTouchListener(new OnTouchListener() {
			int startX ;
			int startY;
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX  = (int) event.getRawX();
					int newY  = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					
					layoutParams.x +=dx;
					layoutParams.y +=dy;
					wm.updateViewLayout(addressView, layoutParams);
					
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					sp.edit().putInt(SP_LASTX, addressView.getLeft()).putInt(SP_LASTY, addressView.getTop()).commit();
					break;
				}
				return true;
			}
		});
		wm.addView(addressView, layoutParams);
	}
	
	/**
	 * 拨出电话的广播接收者
	 */
	public class MyOutgoingCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String address = AddressDao.getAddress(context, number);
			showAddressInWindow(address);
		}
	}
	
	/**
	 * 来电监听
	 */
	public class MyPhoneListner extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if(addressView!=null){
					wm.removeView(addressView);
					addressView = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				String address = AddressDao.getAddress(PhoneNumAddressService.this, incomingNumber);
				showAddressInWindow(address);
				break;
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(mOutGoingCallreceiver!=null){
			unregisterReceiver(mOutGoingCallreceiver);
			mOutGoingCallreceiver = null;
		}
		if(listener !=null){
			tm.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
		}
	}
}
