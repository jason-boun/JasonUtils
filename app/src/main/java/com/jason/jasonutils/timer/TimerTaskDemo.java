package com.jason.jasonutils.timer;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class TimerTaskDemo extends Activity {
	
	private static ProgressBar pb_progress;
	private static Timer timer;
	private static int progress ;
	private static TextView tv_show_progress;
	private MyHandler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timertask_demo_activity);
		initData();
	}

	private void initData() {
		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
		tv_show_progress = (TextView) findViewById(R.id.tv_show_progress);
		
		mHandler = new MyHandler(this);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		}, 1,500);
	}

	/**
	 * 处理进度情况
	 */
	public static void handleProgress() {
		if(pb_progress.getProgress()<90){
			progress = pb_progress.getProgress() + new Random().nextInt(8);//每次增加幅度在1~8之间。
		}else{
			progress = pb_progress.getProgress()+2;
		}
		if(progress>pb_progress.getMax()){//在布局中已经设置了max属性值为100
			progress = 0;
		}
		//界面显示设置
		pb_progress.setProgress(progress);
		tv_show_progress.setText(100*progress/pb_progress.getMax()+"%");
	}
	
	/**
	 * 防止handler导致内存泄露
	 */
	static class MyHandler extends Handler {
		
		private WeakReference<TimerTaskDemo> mOuter;
		public MyHandler(TimerTaskDemo act){
			this.mOuter = new WeakReference<TimerTaskDemo>(act);
		}
		
		@Override
		public void handleMessage(Message msg) {
			TimerTaskDemo act = mOuter.get();
			if(act!=null){
				switch (msg.what) {
				case 0:
					handleProgress();
					break;
				}
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
		if(mHandler!=null){
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		if(pb_progress !=null){
			pb_progress.clearAnimation();
			pb_progress = null;
		}
		tv_show_progress = null;		
	}

}
