package com.jason.jasonutils.surfaceview;

import android.app.Activity;
import android.os.Bundle;

public class SurfaceViewDemo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MySurfaceView(this));
	}

}
