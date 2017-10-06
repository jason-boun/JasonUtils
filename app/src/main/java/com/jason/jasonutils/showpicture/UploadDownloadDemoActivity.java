package com.jason.jasonutils.showpicture;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UploadDownloadDemoActivity extends Activity implements OnClickListener {
	
	private EditText et_upload_file_path;
	private Button bt_upload_demo,bt_download_demo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
	}

	private void init() {
		et_upload_file_path = (EditText) findViewById(R.id.et_upload_file_path);
		bt_upload_demo = (Button) findViewById(R.id.bt_upload_demo);
		bt_download_demo = (Button) findViewById(R.id.bt_download_demo);
		bt_upload_demo.setOnClickListener(this);
		bt_download_demo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_upload_demo:
			
			break;
		case R.id.bt_download_demo:
			
			break;
		}
	}
	
	/**
	 * 上传操作
	 * @return
	 */
	public boolean upload(){
		String filePath = et_upload_file_path.getText().toString().trim();
		if(TextUtils.isEmpty(filePath)){
			
		}else{
			Toast.makeText(this, "文件路径不正确", 0).show();
		}
		return false;
	}
	
	/**
	 * 下载操作
	 * @return
	 */
	public boolean download(){
		return false;
	}

}
