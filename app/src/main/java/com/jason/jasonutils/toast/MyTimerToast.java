package com.jason.jasonutils.toast;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MyTimerToast {
	
	private Context context;
	private String text;
	private Timer timer;
	private TimerTask task;
	private final int perShowTime = 3500;
	private int count = 0;
	private int targetCount;
	private Toast toast;
	public static final int TOAST_TYPE_TIMER = 0;
	public static final int TOAST_TYPE_ETERNAL = 1;
	private int SHOW_TOAST_TYPE = 1;
	
	/**
	 * 构造函数
	 * @param context 上下文环境
	 * @param text 显示内容
	 * @param toastType 显示类型，TOAST_TYPE_TIMER为计时显示，TOAST_TYPE_ETERNAL为持久显示
	 * @param duration 显示时间，单位是秒，计时显示则大于0的整数；持久显示则传入为0.
	 */
	public MyTimerToast(Context context, String text, int toastType, int duration){
		this.context = context;
		this.text = text;
		this.SHOW_TOAST_TYPE = toastType;
		if(SHOW_TOAST_TYPE == TOAST_TYPE_TIMER && duration > 0){
			this.targetCount = duration*1000/perShowTime+1;
		}
	}
	
	/**
	 * 发送显示Toast消息
	 */
	public void showToast(){
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(SHOW_TOAST_TYPE);
			}
		};
		timer.schedule(task, 0, perShowTime);
	}
	
	/**
	 * 取消Toast
	 */
	public void cancelToast(){
		if(timer !=null){
			timer.cancel();
		}if(task!=null){
			task.cancel();
		}
		timer = null;
		task = null;
	}
	
	/**
	 * 控制Toast
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case TOAST_TYPE_TIMER:
				try {
					if(count < targetCount){
						Toast.makeText(context, text,Toast.LENGTH_LONG).show();
						count++;
					}else{
						cancelToast();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TOAST_TYPE_ETERNAL:
				if(toast==null){
					toast = Toast.makeText(context, text,Toast.LENGTH_LONG);
				}
				toast.show();
				break;
			}
		}
	};
}
