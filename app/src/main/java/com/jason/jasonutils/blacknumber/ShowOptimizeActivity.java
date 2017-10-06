package com.jason.jasonutils.blacknumber;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MyAsyncTask;

public class ShowOptimizeActivity extends Activity {
	
	private LinearLayout ll_data_loading;
	private ListView lv_black_number_list;
	private List<BlackNumberBean> dataList;
	private BlackNumberAdapter adapter;
	private BlackNumberDao dao;
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black_number_show_data_activity);
		ctx = ShowOptimizeActivity.this;
		ll_data_loading = (LinearLayout) findViewById(R.id.ll_data_loading);
		lv_black_number_list = (ListView) findViewById(R.id.lv_black_number_list);
		dao = new BlackNumberDao(ShowOptimizeActivity.this);
		fillData();
	}
	
	private void fillData() {
		new MyAsyncTask() {
			@Override
			protected void onPreExecute() {
				ll_data_loading.setVisibility(View.VISIBLE);
			}
			@Override
			protected void onPostExecute() {
				adapter = new BlackNumberAdapter(ctx,dataList,dao);
				lv_black_number_list.setAdapter(adapter);
				ll_data_loading.setVisibility(View.INVISIBLE);
			}
			@Override
			protected void doInBackgroud() {
				dataList = dao.queryAll();
			}
		}.execute();
	}
}
