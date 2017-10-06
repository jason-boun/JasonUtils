package com.jason.jasonutils.camera;

import com.jason.jasonutils.R;
import com.jason.jasonutils.camera.mycamera.BuildMyCameraDemo;
import com.jason.jasonutils.camera.syscamera.UseSysCameraDemoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CameraDemoActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_and_my_camera_demo_acitvity);
		
		findViewById(R.id.bt_use_syscamera_demo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CameraDemoActivity.this,UseSysCameraDemoActivity.class));
			}
		});
		
		findViewById(R.id.bt_build_mycamera_demo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CameraDemoActivity.this,BuildMyCameraDemo.class));
			}
		});
	}

}
