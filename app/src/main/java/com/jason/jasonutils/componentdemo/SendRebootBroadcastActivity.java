package com.jason.jasonutils.componentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class SendRebootBroadcastActivity extends Activity {
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(SendRebootBroadcastActivity.this, "已发送开启广播的通知",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sendbraodcast_startrebootservice);
		findViewById(R.id.bt_send_broadcast).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handler.sendEmptyMessage(0);
						sendBroadcast(new Intent("start_reboot_broadcast"));
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	};
}
