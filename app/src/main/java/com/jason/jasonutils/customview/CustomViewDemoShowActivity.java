package com.jason.jasonutils.customview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jason.jasonutils.R;

public class CustomViewDemoShowActivity extends Activity {
	
	private MyCustomSettingView mySettingView ;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_view_demo_activity);
		
		init();
	}

	private void init() {
		mySettingView = (MyCustomSettingView) findViewById(R.id.mysettingview_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean checked = sp.getBoolean("my_setting_checked", false);
		mySettingView.setChecked(checked);
		mySettingView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mySettingView.isChecked()){
					mySettingView.setChecked(false);
					sp.edit().putBoolean("my_setting_checked", false).commit();
				}else{
					mySettingView.setChecked(true);
					sp.edit().putBoolean("my_setting_checked", true).commit();
				}
			}
		});
	}

}
