package com.jason.jasonutils.sms.sendmsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jason.jasonutils.R;

public class SelectContactToSendMsgActivity extends Activity implements OnClickListener {
	
	private EditText et_contact_number, et_send_msg_content;
	private Button bt_select_contact, bt_send_msg_send;
	private List<ContactInfo> contactList = new ArrayList<ContactInfo>();
	private String msgContent="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact_to_send_msg_activity);
		init();
	}
	/**
	 * 初始化控件
	 */
	private void init() {
		et_contact_number = (EditText) findViewById(R.id.et_contact_number);
		et_send_msg_content = (EditText) findViewById(R.id.et_send_msg_content);
		
		bt_select_contact = (Button) findViewById(R.id.bt_select_contact);
		bt_send_msg_send = (Button) findViewById(R.id.bt_send_msg_send);
		
		bt_select_contact.setOnClickListener(this);
		bt_send_msg_send.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data !=null){
			List<ContactInfo> resultList =(ArrayList<ContactInfo>) data.getSerializableExtra("selected_contacts_list");
			contactList.addAll(resultList);//处理数据，防止重复
			refreshUI(resultList);
		}
	}
	
	
	@Override
	protected void onDestroy() {
		msgContent = "";
		super.onDestroy();
	}
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_select_contact:
			selectContactsMethod();
			break;
		case R.id.bt_send_msg_send:
			sendMsg();
			break;
		}
	}
	
	/**
	 * 点击去选择联系人
	 */
	private void selectContactsMethod() {
		msgContent = et_send_msg_content.getText().toString().trim();
		Intent intent = new Intent(this, SelectContactActivity.class); 
		if(contactList.size()>0){
			Bundle bundle = new Bundle();
			bundle.putSerializable("current_list", (Serializable) contactList);
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, 0);
	}
	
	/**
	 * 更新显示的联系人数据
	 * @param dataList
	 */
	private void refreshUI(List<ContactInfo> dataList){
		//联系人显示
		if(dataList!=null && dataList.size()>0){
			StringBuilder sb = new StringBuilder();
			for(ContactInfo cif :dataList){
				sb.append(cif.getName());
				sb.append(",");
			}
			if(sb.toString().endsWith(",")){
				sb.deleteCharAt(sb.length()-1);
			}
			et_contact_number.setText(sb.toString());
		}else{
			et_contact_number.setText("");
		}
		//信息内容
		if(!TextUtils.isEmpty(msgContent)){
			et_send_msg_content.setText(msgContent);
		}
	}
	
	/**
	 * 发送短信
	 */
	private void sendMsg() {
	}
}
