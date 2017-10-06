package com.jason.jasonutils.window.poptiputil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;

public class SMSPreviewUtils {

	public static final int MSG_POPU_CLOSE = 1;
	public static final int MSG_POPU_SHOW = 2;
	public static final int MSG_SHOW_DELAYED = 250;

	public WindowManager mWindowManager;
	private ScreenDisplaySmsView smsPreview = null;
	public int TOOL_BAR_HIGH = 0;
	private boolean isAddToWindow;
	public WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_POPU_CLOSE:
				doRemoveSmsPopu();
				break;
			case MSG_POPU_SHOW:
				mWindowManager.addView(smsPreview, params);
				break;
			default:
				break;
			}
		}
	};
	private static SMSPreviewUtils smsUtils;
	public static SMSPreviewUtils getInstance() {
		if (smsUtils == null) {
			smsUtils = new SMSPreviewUtils();
		}
		return smsUtils;
	}


	public void doShow(Context context,String message,String fromName) {
		if (mWindowManager == null) {
			mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
		}
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params.dimAmount = 0.7f;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.RGBA_8888;
		params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
		if (this.smsPreview == null) {
			this.smsPreview = new ScreenDisplaySmsView(context, mHandler);
		}
		try {
			this.smsPreview.addMsg(message,fromName);
			if (!this.smsPreview.isShown() && !isAddToWindow) {
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_POPU_SHOW), MSG_SHOW_DELAYED);
				isAddToWindow = true;
			} else {
				mWindowManager.updateViewLayout(smsPreview, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doRemoveSmsPopu() {
		if (smsPreview != null && smsPreview.isShown()) {
			mWindowManager.removeView(smsPreview);
			smsPreview = null;
			isAddToWindow = false;
		}
	}
}
