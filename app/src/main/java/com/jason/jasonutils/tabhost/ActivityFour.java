package com.jason.jasonutils.tabhost;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class ActivityFour extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(this);
		tv.setText("tab Activity 4");
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(30);
		tv.setGravity(Gravity.CENTER);
		
		setContentView(tv);
	}
}
