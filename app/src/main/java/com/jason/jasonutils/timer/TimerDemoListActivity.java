package com.jason.jasonutils.timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jason.jasonutils.R;
import com.jason.jasonutils.asynctask.AsyncTaskDemo;
import com.jason.jasonutils.tools.ViewUtil;

public class TimerDemoListActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_demo_activity);
		
		ViewUtil.setChildsOnClickLisener(this, R.id.ll_timer_container, 0, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_chronometer_demo:
			startActivity(new Intent(this, ChronometerDemo.class));
			break;
		case R.id.bt_timer_task_demo:
			startActivity(new Intent(this, TimerTaskDemo.class));
			break;
		case R.id.bt_alarm_manager_demo:
			startActivity(new Intent(this, AlarmManagerDemo.class));
			break;
		case R.id.bt_handler_demo:
			startActivity(new Intent(this, HandlerDemo.class));
			break;
		case R.id.bt_asynctask_demo:
			startActivity(new Intent(this, AsyncTaskDemo.class));
			break;
		}
	}

}
