package com.jason.jasonutils.alertdialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class AlertDialogActivity extends Activity {
	
	private TextView alert_title, alert_message;
	private Button btn_alert_cancel;
	private String title , msg;
	private boolean showCancelTB;
	public static final String TITLE = "title";
	public static final String MESSAGE = "message";
	public static final String SHOW_CANCEL_BT = "showCancelBT";
	public static final String CONFIRM = "confirm";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog_activity);
		getIntentData();
	}

	private void getIntentData() {
		alert_title = (TextView) findViewById(R.id.alert_title);
		alert_message = (TextView) findViewById(R.id.alert_message);
		btn_alert_cancel = (Button) findViewById(R.id.btn_alert_cancel);
		
		Intent intent = getIntent();
		title = intent.getStringExtra(TITLE);
		msg = intent.getStringExtra(MESSAGE);
		showCancelTB = intent.getBooleanExtra(SHOW_CANCEL_BT, false);
		if(!TextUtils.isEmpty(title)){
			alert_title.setVisibility(View.VISIBLE);
			alert_title.setText(title);
		}else{
			alert_title.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(msg)){
			alert_message.setVisibility(View.VISIBLE);
			alert_message.setText(msg);
		}else{
			alert_message.setVisibility(View.GONE);
		}
		if(showCancelTB){
			btn_alert_cancel.setVisibility(View.VISIBLE);
		}else{
			btn_alert_cancel.setVisibility(View.GONE);
		}
	}
	
	public void confirm(View view){
		setResult(RESULT_OK, new Intent().putExtra(CONFIRM, true));
		finish();
	}
	
	public void cancel(View view){
		finish();
	}

}
