package com.jason.jasonutils.iostream.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.filter.PersonBean;
import com.jason.jasonutils.tools.Utils;
import com.jason.jasonutils.tools.ZipUtils;

public class ZipIOStreamDemo extends Activity implements OnClickListener {
	
	private Button bt_zip_encode, bt_zip_decode;
	private ListView lv_zipdemo_info_show;
	private List<PersonBean> data;
	private ArrayAdapter<String> adapter;
	private List<String> dataStrList; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_zip_stream_demo);
		init();
		initData();
	}


	private void init() {
		bt_zip_encode = (Button) findViewById(R.id.bt_zip_encode);
		bt_zip_decode = (Button) findViewById(R.id.bt_zip_decode);
		lv_zipdemo_info_show = (ListView) findViewById(R.id.lv_zipdemo_info_show);
		
		bt_zip_encode.setOnClickListener(this);
		bt_zip_decode.setOnClickListener(this);
	}

	private void initData() {
		if(data == null){
			data = new ArrayList<PersonBean>();
		}
		data.clear();
		data.addAll(Utils.generateData());
		dataStrList= new ArrayList<String>();
		if(data.size()>0){
			for(PersonBean bean:data){
				dataStrList.add(bean.toString());
			}
		}
		adapter = new ArrayAdapter<String>(ZipIOStreamDemo.this, android.R.layout.simple_expandable_list_item_2, dataStrList);
		lv_zipdemo_info_show.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_zip_encode:
			zipCompress();
			break;
		case R.id.bt_zip_decode:
			zipUnCompress();
			break;
		}
	}
	
	public void zipCompress(){
		FileOutputStream fos = null;
		try{
			File srcFile = new File(Environment.getExternalStorageDirectory(), "zipSrcTest.txt");
			File desFile = new File(Environment.getExternalStorageDirectory(), "zipDesTest.zip");
			fos = new FileOutputStream(srcFile);
			fos.write(Base64.encode(dataStrList.toString().getBytes(), Base64.DEFAULT));
			
			List<File> files = new ArrayList<File>();
			files.add(srcFile);
			ZipUtils.zipFiles(files, desFile);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void zipUnCompress(){
	}

}
