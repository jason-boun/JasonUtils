package com.jason.jasonutils.blacknumber;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MyAsyncTask;

public class ShowBatchLoadActivity extends Activity {
	
	private LinearLayout ll_data_loading;
	private ListView lv_black_number_list;
	private List<BlackNumberBean> dataList ;
	private BlackNumberAdapter adapter;
	private BlackNumberDao dao;
	private Context ctx;
	
	private int startIndex=0;
	private int blockSize = 20;
	private boolean isLoadingData = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black_number_show_data_activity);
		ctx = ShowBatchLoadActivity.this;
		ll_data_loading = (LinearLayout) findViewById(R.id.ll_data_loading);
		lv_black_number_list = (ListView) findViewById(R.id.lv_black_number_list);
		lv_black_number_list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int position = lv_black_number_list.getLastVisiblePosition();
					int totalSize = dataList.size();
					if(position == totalSize -1){
						startIndex += blockSize;
						if(isLoadingData){
							return;
						}
						fillData();
					}
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
		dao = new BlackNumberDao(ShowBatchLoadActivity.this);
		fillData();
	}
	
	private void fillData() {
		new MyAsyncTask() {
			@Override
			protected void onPreExecute() {
				ll_data_loading.setVisibility(View.VISIBLE);
				isLoadingData = true;
			}
			@Override
			protected void onPostExecute() {
				if(adapter ==null){
					adapter = new BlackNumberAdapter(ctx,dataList,dao);
					lv_black_number_list.setAdapter(adapter);
				}else{
					adapter.notifyDataSetChanged();
				}
				ll_data_loading.setVisibility(View.INVISIBLE);
				isLoadingData = false;
			}
			@Override
			protected void doInBackgroud() {
				if(dataList == null){
					dataList = dao.batchQuery(startIndex, blockSize,false);
				}else{
					dataList.addAll(dao.batchQuery(startIndex, blockSize,false));
				}
			}
		}.execute();
	}

}
