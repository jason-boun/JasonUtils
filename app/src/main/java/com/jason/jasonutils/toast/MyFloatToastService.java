package com.jason.jasonutils.toast;

import com.jason.jasonutils.R;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.jason.jasonutils.tools.MLog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MyFloatToastService extends Service {

	public static final String TAG = "MyFloatToastService";

	private TelephonyManager telehponyManager;
	private MyPhoneListener listener;
	private InnerBroadcastReceiver receiver;
	private WindowManager windowManager;

	private View toastView;
	private String tipStr = "哈哈，我的浮层提示";

	private class InnerBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			showAddressInWindow(tipStr);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MLog.i("哈哈", "服务已经创建");
		//注册广播
		receiver = new InnerBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		//获取相关服务
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		telehponyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		telehponyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	// 其他人拨号码给我，来电
	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			
			//响铃
			case TelephonyManager.CALL_STATE_RINGING:
				showAddressInWindow(tipStr);
				break;

			//挂断
			case TelephonyManager.CALL_STATE_IDLE:
				if (toastView != null) {
					windowManager.removeView(toastView);
					toastView = null;
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	@Override
	public void onDestroy() {
		telehponyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private WindowManager.LayoutParams wmParams;
	
	public void showAddressInWindow(String text) {

		toastView = new TextView(getApplicationContext());
		
		//toastView = View.inflate(getApplicationContext(),R.layout.my_float_toast_show, null);		
		toastView.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();

					int dx = newX - startX;
					int dy = newY - startY;

					wmParams.x += dx;
					wmParams.y += dy;

					windowManager.updateViewLayout(toastView, wmParams);

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return true;
			}
		});

		((TextView)toastView.findViewById(R.id.tv_mytip_text)).setText(text);
		
		wmParams = new LayoutParams();
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 55;
		wmParams.y = 55;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		wmParams.format = PixelFormat.TRANSLUCENT;
		wmParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		
		windowManager.addView(toastView, wmParams);
	}
}
