package com.jason.jasonutils.outgoingcallselecte;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 系统拨号界面拨号过程中，弹出选择拨号方式
 */
public class OutGoingCallActivity extends Activity {
	
	private String phoneNumber ="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phoneNumber = UriUtils.extractNumberFromIntent(getIntent(), this);
		if(!TextUtils.isEmpty(phoneNumber.trim())){
			Toast.makeText(getApplicationContext(), "通过第三方应用拨号", Toast.LENGTH_SHORT).show();
		}
		this.finish();
	}
}
