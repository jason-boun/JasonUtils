package com.jason.jasonutils.spinner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class SpinnerDemoActivity extends Activity {
	
	private Spinner spinner_show;
	private TextView tv_selected_info_show;
	private SpinnerAdapter adapter;
	private String[] rawDatas;
	private String[] codesData;
	private String[] namesData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spinner_demo_activity);
		
		rawDatas = getResources().getStringArray(R.array.country_labels);
		if(rawDatas!=null && rawDatas.length>0){
			codesData = new String[rawDatas.length];
			namesData = new String[rawDatas.length];
			for(int i = 0;i<rawDatas.length;i++){
				namesData[i] = rawDatas[i].split("-")[0];
				codesData[i] = rawDatas[i].split("-")[1];
			}
		}
		tv_selected_info_show = (TextView) findViewById(R.id.tv_selected_info_show);
		spinner_show = (Spinner) findViewById(R.id.spinner_show);
		adapter = new SpinnerAdapter(namesData, this);
		spinner_show.setAdapter(adapter);
		
		spinner_show.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(SpinnerDemoActivity.this, "国家："+namesData[position]+"\n国际码："+codesData[position], Toast.LENGTH_LONG).show();
				tv_selected_info_show.setText("选择的信息：\n国家："+namesData[position]+"\n国际码："+codesData[position]);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(SpinnerDemoActivity.this, "国家："+namesData[0]+"\n国际码："+codesData[0], Toast.LENGTH_LONG).show();
				tv_selected_info_show.setText("展示的信息：\n国家："+namesData[0]+"\n国际码："+codesData[0]);
			}
		});
	}

}
