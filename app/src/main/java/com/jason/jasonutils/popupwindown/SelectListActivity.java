package com.jason.jasonutils.popupwindown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jason.jasonutils.R;

public class SelectListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popwindow_demo_select_list_activity);
	}
	
	public void selectSD1(View view){
		setResult(PopupWindownDemo.SD1, new Intent().putExtra(PopupWindownDemo.SD1+"", PopupWindownDemo.SD1));
		finish();
	}
	public void selectSD2(View view){
		setResult(PopupWindownDemo.SD2, new Intent().putExtra(PopupWindownDemo.SD2+"", PopupWindownDemo.SD2));
		finish();
	}
	public void cancel(View view){
		finish();
	}

}
