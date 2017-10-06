package com.jason.jasonutils.refresh;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class PulldownRefreshDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pull_to_refresh);
		
		String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };
		BaseAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		final RefreshableView refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		
		((ListView) findViewById(R.id.list_view)).setAdapter(adapter);
		((RefreshableView) findViewById(R.id.refreshable_view)).setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				SystemClock.sleep(3000);
				refreshableView.finishRefreshing();
			}
		}, 0);
		
		/*RefreshViewUtil.initRefreshView(adapter, new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				SystemClock.sleep(3000);
				refreshableView.finishRefreshing();
			}
		}) ;*/
	}

}
