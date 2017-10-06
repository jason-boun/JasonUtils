package com.jason.jasonutils.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jason.jasonutils.R;

public class LocationDemoListActivity extends Activity implements OnClickListener {
	
	private Button bt_location_demolist_compass, bt_location_demolist_position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_demo_list);
		
		init();
	}

	private void init() {
		bt_location_demolist_compass = (Button) findViewById(R.id.bt_location_demolist_compass);
		bt_location_demolist_position = (Button) findViewById(R.id.bt_location_demolist_position);
		
		bt_location_demolist_compass.setOnClickListener(this);
		bt_location_demolist_position.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_location_demolist_compass:
			startActivity(new Intent(this, CompassDemoActivity.class));//指南针Demo
			break;
		case R.id.bt_location_demolist_position:
			
			break;
		}
	}

}
