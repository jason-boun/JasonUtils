package com.jason.jasonutils.timer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jason.jasonutils.JasonUtilsMainActivity;
import com.jason.jasonutils.R;

public class AlarmManagerDemo extends Activity implements OnClickListener {

	private AlarmManager alarmManager;
	private Button btnShowActivity, btnStopActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmmanager_demo_activity);
		
		init();
	}

	private void init() {
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		btnShowActivity = (Button) findViewById(R.id.btnShowActivity);
		btnStopActivity = (Button)findViewById(R.id.btnStopActivity);
		
		btnShowActivity.setOnClickListener(this);
		btnStopActivity.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, JasonUtilsMainActivity.class);
		PendingIntent pendingActivityIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		switch (v.getId()) {
		case R.id.btnShowActivity:
			alarmManager.setRepeating(AlarmManager.RTC, 0, 10000, pendingActivityIntent);
			break;
		case R.id.btnStopActivity:
			alarmManager.cancel(pendingActivityIntent);
			break;

		}
	}
}
