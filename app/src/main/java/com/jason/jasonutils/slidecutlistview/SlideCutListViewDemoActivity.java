package com.jason.jasonutils.slidecutlistview;

import java.util.ArrayList;

import com.jason.jasonutils.R;
import com.jason.jasonutils.slidecutlistview.MySlideCutListView.RemoveListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SlideCutListViewDemoActivity extends Activity {
	private MySlideCutListView slideCutListView;
	private ItemAdapter adapter;
	private ArrayList<String> dataSourceList = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_cut_listview_activity_main);
		init();
	}

	private void init() {
		slideCutListView = (MySlideCutListView) findViewById(R.id.slideCutListView);
		slideCutListView.setRemoveListener(new RemoveListener() {
			@Override
			public void removeItem(int position) {
				MySlideCutListView.isSlide = false;
				MySlideCutListView.itemView.findViewById(R.id.tv_coating).setVisibility(View.VISIBLE);
				dataSourceList.remove(position);
				adapter.notifyDataSetChanged();
			}
		});

		for (int i = 0; i < 20; i++) {
			dataSourceList.add((i > 9 ? "ITEM" + " " + i : "ITEM" + " 0" + i));
		}

		adapter = new ItemAdapter(this);
		adapter.setData(dataSourceList);
		slideCutListView.setAdapter(adapter);

		slideCutListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
				intent.putExtra("item", dataSourceList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_from_right, R.anim.remain_original_location);
			}

		});
	}
}
