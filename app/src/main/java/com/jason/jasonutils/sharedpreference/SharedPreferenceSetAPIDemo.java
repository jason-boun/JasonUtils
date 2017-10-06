package com.jason.jasonutils.sharedpreference;

import java.util.HashSet;
import java.util.Set;

import com.jason.jasonutils.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SharedPreferenceSetAPIDemo extends Activity implements OnClickListener {

	private Button bt_save_data_to_sp, bt_read_data_from_sp;
	private TextView tv_show_sp_data;
	private SharedPreferences sp;
	private Set<String> saveDataSet;
	private Set<String> readDataSet;
	private String spKey = "timeSet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharedpreference_demo_activity);
		init();
	}

	private void init() {
		bt_save_data_to_sp = (Button) findViewById(R.id.bt_save_data_to_sp);
		bt_read_data_from_sp = (Button) findViewById(R.id.bt_read_data_from_sp);
		tv_show_sp_data = (TextView) findViewById(R.id.tv_show_sp_data);

		bt_save_data_to_sp.setOnClickListener(this);
		bt_read_data_from_sp.setOnClickListener(this);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_save_data_to_sp:
			saveDataSet = (HashSet<String>)sp.getStringSet(spKey, new HashSet<String>());
			if(sp.contains(spKey)){
				sp.edit().remove(spKey).commit();
			}
			saveDataSet.add(System.currentTimeMillis()+"");
			sp.edit().putStringSet(spKey, saveDataSet).commit();
			break;
		case R.id.bt_read_data_from_sp:
			readDataSet = (HashSet<String>)sp.getStringSet(spKey, new HashSet<String>());
			tv_show_sp_data.setText(readDataSet.toString());
			break;
		}
	}
}
