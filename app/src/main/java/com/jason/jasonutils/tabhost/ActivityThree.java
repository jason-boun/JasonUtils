package com.jason.jasonutils.tabhost;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class ActivityThree extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(this);
		tv.setText("tab Activity 3");
		tv.setTextColor(Color.RED);
		tv.setTextSize(30);
		tv.setGravity(Gravity.CENTER);
		
		setContentView(tv);
	}
}
