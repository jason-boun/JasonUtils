package com.jason.jasonutils.widgetcomponent;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class EditTextDemo extends Activity implements OnClickListener {
	
	private EditText et_demo_edit_info;
	private Button bt_get_all_content,bt_get_index_content,bt_get_select_content;
	private TextView tv_demo_show_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittext_demo_activity);
		init();
	}

	private void init() {
		et_demo_edit_info = (EditText) findViewById(R.id.et_demo_edit_info);
		et_demo_edit_info.setText("aosidgnoasdflnaos");
		tv_demo_show_info = (TextView) findViewById(R.id.tv_demo_show_info);
		
		bt_get_all_content = (Button) findViewById(R.id.bt_get_all_content);
		bt_get_index_content = (Button) findViewById(R.id.bt_get_index_content);
		bt_get_select_content = (Button) findViewById(R.id.bt_get_select_content);
		
		bt_get_all_content.setOnClickListener(this);
		bt_get_index_content.setOnClickListener(this);
		bt_get_select_content.setOnClickListener(this);
		
		et_demo_edit_info.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Toast.makeText(EditTextDemo.this, String.valueOf(actionId), Toast.LENGTH_SHORT).show(); 
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_get_all_content:
			getAllContent();
			break;
		case R.id.bt_get_index_content:
			getIndexContent();
			break;
		case R.id.bt_get_select_content:
			getSelectedContent();
			break;
		}
	}
	
	/**
	 * 选中全部文本
	 */
	public void getAllContent(){
		et_demo_edit_info.selectAll();
	}
	/**
	 * 选中指定文本
	 */
	public void getIndexContent(){
		Editable editable = et_demo_edit_info.getText();
		if(editable.length()>=2){
			Selection.setSelection(editable, editable.length()/2,editable.length());
		}
	}
	
	/**
	 * 复制选中文本
	 */
	public void getSelectedContent(){
		int start = et_demo_edit_info.getSelectionStart();
		int end = et_demo_edit_info.getSelectionEnd();
		CharSequence subSequence = et_demo_edit_info.getText().subSequence(start, end);
		if(!TextUtils.isEmpty(subSequence)){
			tv_demo_show_info.setText(subSequence);
		}
	}

}
