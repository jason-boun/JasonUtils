package com.jason.jasonutils.intent;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntentDemoActivity extends Activity implements OnClickListener {
	
	private Button bt_intent_openbaidu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intent_demo_activity);
		init();
	}
	
	private void init() {
		bt_intent_openbaidu = (Button) findViewById(R.id.bt_intent_openbaidu);
		bt_intent_openbaidu.setOnClickListener(this);
	}

	/**
	 * 通过隐式意图打开网页
	 */
	public void openBaidu(){
		Intent intent = new  Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.baidu.com"));
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_intent_openbaidu:
			openBaidu();
			break;
		}
	}
}
