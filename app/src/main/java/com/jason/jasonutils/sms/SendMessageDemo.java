package com.jason.jasonutils.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class SendMessageDemo extends Activity implements OnClickListener {
	
	private EditText et_number,et_content;
	private Button bt_send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message_activity);
		
		init();
	}

	private void init() {
		et_number = (EditText) findViewById(R.id.et_number);
		et_content = (EditText) findViewById(R.id.et_content);
		bt_send = (Button) findViewById(R.id.bt_send);
		
		bt_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.bt_send:
			send();
			break;
		}
	}

	private void send() {
		String number = et_number.getText().toString().trim();
		String content = et_content.getText().toString().trim();
		if(TextUtils.isEmpty(number) || TextUtils.isEmpty(content)){
			Toast.makeText(this, "号码和内容都不能为空", Toast.LENGTH_SHORT).show();
		}else{
			//sendSMS1(number, content);
			sendSMS2(number, content);
		}
	}
	
	/**
	 * 不跳转到Android短信界面，直接将短信发送出去
	 * @param phoeNumber
	 * @param smsContet
	 */
	public void sendSMS1(String phoeNumber, String smsContet){
		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> divideMessage = smsManager.divideMessage(smsContet);
		for(String sms :divideMessage){
			smsManager.sendTextMessage(phoeNumber, null, sms, null, null);
		}
	}
	/**
	 * 跳转到Android短信界面，用户点击发送按钮将短信发送出去
	 * @param phoeNumber
	 * @param smsContent
	 */
	public void sendSMS2(String phoeNumber, String smsContent){
		Intent it = new Intent();
		it.setAction(Intent.ACTION_SENDTO);
		it.setData(Uri.parse("smsto:"+phoeNumber));
		it.putExtra("sms_body", smsContent);            
		this.startActivity(it);  
	}

}
