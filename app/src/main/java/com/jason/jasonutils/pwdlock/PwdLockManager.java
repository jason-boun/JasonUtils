package com.jason.jasonutils.pwdlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jason.jasonutils.R;
import com.jason.jasonutils.customview.MyCustomSettingView;

public class PwdLockManager extends Activity {
	
	private MyCustomSettingView cv_applock_manager_checkbox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applock_manager_activity);
		
		cv_applock_manager_checkbox = (MyCustomSettingView) findViewById(R.id.cv_applock_manager_checkbox);
		cv_applock_manager_checkbox.setChecked(getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getBoolean(Constant.APP_LOCK_TURN_ON, false));
		cv_applock_manager_checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PwdLockManager.this, PwdLockActivity.class);
				intent.putExtra(Constant.APP_LOCK_OPERATE_FLAG, cv_applock_manager_checkbox.isChecked()? Constant.TURN_OFF_APPLOCK : Constant.TURN_ON_APPLOCK);
				intent.putExtra(Constant.NEED_BACK, true);
				startActivityForResult(intent, 0);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		cv_applock_manager_checkbox.setChecked(getSharedPreferences(Constant.MYSP_CONFIG_FILENAME, MODE_PRIVATE).getBoolean(Constant.APP_LOCK_TURN_ON, false));
	}
}
