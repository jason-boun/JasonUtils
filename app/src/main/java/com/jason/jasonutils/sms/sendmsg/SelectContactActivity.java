package com.jason.jasonutils.sms.sendmsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class SelectContactActivity extends Activity implements OnClickListener {

	private ListView lv_select_contact;
	private List<ContactInfo> contactInfos;
	private Button bt_select_contact_confirm,bt_select_contact_cancle;
	private List<ContactInfo> selectedList = new ArrayList<ContactInfo>();
	private ContactAdapter contactAdapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		init();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
		contactInfos = ContactInfoTool.getContactInfos(this);
		if(contactInfos!=null && contactInfos.size()>0){
			for(ContactInfo ci :contactInfos){
				ci.setSelected(false);
			}
		}
		bt_select_contact_confirm = (Button) findViewById(R.id.bt_select_contact_confirm);
		bt_select_contact_cancle = (Button) findViewById(R.id.bt_select_contact_cancle);
		bt_select_contact_confirm.setOnClickListener(this);
		contactAdapter = new ContactAdapter(contactInfos);
		lv_select_contact.setAdapter(contactAdapter);
		lv_select_contact.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				ContactInfo info = contactInfos.get(position);
				if(info.isSelected()){
					info.setSelected(false);
					if(selectedList.contains(info)){
						selectedList.remove(info);
					}
				}else{
					info.setSelected(true);
					if(!selectedList.contains(info)){
						selectedList.add(info);
					}
				}
				contactAdapter.notifyDataSetChanged();//刷新页面
			}
		});
	}
	
	/**
	 * 初始化传递过来的数据
	 */
	private void initData() {
		Intent intent = getIntent();
		if(intent!=null){
			List<ContactInfo> transformList = (ArrayList<ContactInfo>) intent.getSerializableExtra("current_list");
			if(transformList!=null && transformList.size()>0){
				selectedList.clear();
				selectedList.addAll(transformList);
				for(ContactInfo trans:transformList){
					iner:for(ContactInfo cif:contactInfos){
						if(trans.getNumber().equals(cif.getNumber())){
							cif.setSelected(true);
							break iner;
						}
					}
				}
				contactAdapter.notifyDataSetChanged();//刷新页面
			}
		}
	}

	/**
	 * 适配器
	 */
	private class ContactAdapter extends BaseAdapter{
		
		private List<ContactInfo> contactInfosList;
		public ContactAdapter(List<ContactInfo> contactInfos){
			this.contactInfosList = contactInfos;
		}
		
		@Override
		public int getCount() {
			return contactInfosList.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactInfo info = contactInfosList.get(position);
			View view = View.inflate(getApplicationContext(), R.layout.send_smg_select_contact_item, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_contact_name);
			TextView tv_number = (TextView) view.findViewById(R.id.tv_contact_number);
			ImageView iv_select_status = (ImageView) view.findViewById(R.id.iv_select_contact_status);
			tv_name.setText(info.getName());
			tv_number.setText(info.getNumber()+"");
			if(info.isSelected()){
				iv_select_status.setBackgroundResource(R.drawable.checkbox_checked);
			}else{
				iv_select_status.setBackgroundResource(R.drawable.checkbox_normal);
			}
			return view;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_select_contact_confirm:
			setBackData(selectedList);
			break;
		case R.id.bt_select_contact_cancle:
			setBackData(null);
			break;
		}
	}

	/**
	 * 封装并传递数据
	 */
	private void setBackData(List<ContactInfo> list) {
		Intent resultIntent = new Intent();
		if(list!=null && list.size()>0){
			Bundle bundle = new Bundle();
			bundle.putSerializable("selected_contacts_list", (Serializable) list);
			resultIntent.putExtras(bundle);
		}
		setResult(1, resultIntent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			setBackData(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
