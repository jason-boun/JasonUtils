package com.jason.jasonutils.fragment.fragmentlife;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;

import android.app.Activity;
import android.os.Bundle;

public class FragmentLifeDemoActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_fragment_life_demo);
		
		MLog.i("i", "Activity        onCreate ");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		MLog.i("i", "Activity        onStart ");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MLog.i("i", "Activity        onResume ");
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		MLog.i("i", "Activity        onPause ");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MLog.i("i", "Activity        onStop ");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MLog.i("i", "Activity        onDestroy ");
	}

}
