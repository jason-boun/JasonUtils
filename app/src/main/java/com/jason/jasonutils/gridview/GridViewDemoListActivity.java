package com.jason.jasonutils.gridview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jason.jasonutils.R;
import com.jason.jasonutils.expandablelistview.ExpandableListViewDemoActivity;
import com.jason.jasonutils.gridview.complex.GridViewComplexDemo;
import com.jason.jasonutils.gridview.singleline.GridViewComplexSingleLineDemo;

public class GridViewDemoListActivity extends Activity implements OnClickListener {
	
	private Button bt_gridview_demolist_click_simpledemo, bt_gridview_demolist_singleline_simpledemo;
	private Button bt_gridview_demolist_clickdemo, bt_gridview_demolist_singlelinedemo;
	private Button bt_expandableListView_demo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_demolist_activity);
		init();
	}

	private void init() {
		bt_gridview_demolist_click_simpledemo = (Button) findViewById(R.id.bt_gridview_demolist_click_simpledemo);
		bt_gridview_demolist_singleline_simpledemo = (Button) findViewById(R.id.bt_gridview_demolist_singleline_simpledemo);
		bt_gridview_demolist_clickdemo = (Button) findViewById(R.id.bt_gridview_demolist_clickdemo);
		bt_gridview_demolist_singlelinedemo = (Button) findViewById(R.id.bt_gridview_demolist_singlelinedemo);
		bt_expandableListView_demo = (Button) findViewById(R.id.bt_expandableListView_demo);
		
		bt_gridview_demolist_click_simpledemo.setOnClickListener(this);
		bt_gridview_demolist_singleline_simpledemo.setOnClickListener(this);
		bt_gridview_demolist_clickdemo.setOnClickListener(this);
		bt_gridview_demolist_singlelinedemo.setOnClickListener(this);
		bt_expandableListView_demo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_gridview_demolist_click_simpledemo:
			startActivity(new Intent(this, GridViewSimpleDemo.class));
			break;
		case R.id.bt_gridview_demolist_singleline_simpledemo:
			startActivity(new Intent(this, SingleLineGridViewSimpleDemo.class));
			break;
		case R.id.bt_gridview_demolist_clickdemo:
			startActivity(new Intent(this, GridViewComplexDemo.class));
			break;
		case R.id.bt_gridview_demolist_singlelinedemo:
			startActivity(new Intent(this, GridViewComplexSingleLineDemo.class));
			break;
		case R.id.bt_expandableListView_demo:
			startActivity(new Intent(this, ExpandableListViewDemoActivity.class));
			break;
		}
	}

}
