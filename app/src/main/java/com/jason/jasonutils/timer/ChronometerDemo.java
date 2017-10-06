package com.jason.jasonutils.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class ChronometerDemo extends Activity implements OnClickListener, OnChronometerTickListener{
	
	private Chronometer time_meter;
	private Button bt_start_timer, bt_end_timer, bt_reset_timer;
	private TextView tv_show_time, tv_current_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chronometer_demo_activity);
		
		init();
		initChronometer();
	}
	
	private void init() {
		time_meter = (Chronometer) findViewById(R.id.time_meter);
		
		bt_start_timer = (Button) findViewById(R.id.bt_start_timer);
		bt_end_timer = (Button) findViewById(R.id.bt_end_timer);
		bt_reset_timer = (Button) findViewById(R.id.bt_reset_timer);
		
		tv_show_time  = (TextView) findViewById(R.id.tv_show_time);
		tv_current_time  = (TextView) findViewById(R.id.tv_current_time);
		
		bt_reset_timer.setOnClickListener(this);
		bt_start_timer.setOnClickListener(this);
		bt_end_timer.setOnClickListener(this);
	}
	
	private void initChronometer() {
		time_meter.setOnChronometerTickListener(this);
		time_meter.setFormat("计时器：%s");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start_timer:
			startTimer();
			break;
		case R.id.bt_end_timer:
			stopTimer();
			break;
		case R.id.bt_reset_timer:
			resetTimer();
			break;
		}
	}
	
	/**
	 * 开始计时
	 */
	private void startTimer(){
		time_meter.setBase(SystemClock.elapsedRealtime());
		time_meter.start();
	}
	
	/**
	 * 结束计时
	 */
	private void stopTimer(){
		time_meter.stop();
		
		long dTime = SystemClock.elapsedRealtime() - time_meter.getBase();
		dTime /= 1000;
		if(!TextUtils.isEmpty(String.valueOf(dTime))){
			tv_show_time.setText("计时结果为："+String.valueOf(dTime));
		}
	}

	/**
	 * 重置计时基准
	 */
	private void resetTimer() {
		time_meter.setBase(SystemClock.elapsedRealtime());
		tv_show_time.setText("计时结果为："+"");
	}

	/**
	 * 计时器控件设置setOnChronometerTickListener监听器后，会每一秒中触发执行该方法一次
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onChronometerTick(Chronometer chronometer) {
		tv_current_time.setText("此刻系统时间为："+new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
}
