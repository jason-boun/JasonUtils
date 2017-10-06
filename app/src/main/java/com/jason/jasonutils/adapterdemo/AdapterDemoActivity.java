package com.jason.jasonutils.adapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.ViewUtil;

public class AdapterDemoActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adapter_demo_activity);
		ViewUtil.setChildsOnClickLisener(this, R.id.ll_adapterdemo_container, 0, this);
		sendBroadcast(new Intent(CreateDBdataReceiver.CREATE_DATABASE_INFO));//
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_array_adapter:
			startActivity(new Intent(AdapterDemoActivity.this, ArrayAdapterDemo.class));
			break;
		case R.id.bt_array_adapter2:
			startActivity(new Intent(AdapterDemoActivity.this, ArrayAdapterDemo2.class));
			break;
		case R.id.bt_simple_adapter_demo:
			startActivity(new Intent(AdapterDemoActivity.this, SimpleAdatperDemo.class));
			break;
		case R.id.bt_simple_cursor_adapter_demo:
			startActivity(new Intent(AdapterDemoActivity.this, SimpleCursorAdapterDemo.class));
			break;
		case R.id.bt_cursor_adapter_demo:
			startActivity(new Intent(AdapterDemoActivity.this, CursorAdapterDemo.class));
			break;
		case R.id.bt_base_adapter_demo:
			startActivity(new Intent(AdapterDemoActivity.this, BaseAdapterDemo.class));
			break;
		}
	}
}
