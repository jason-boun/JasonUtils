package com.jason.jasonutils.map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jason.jasonutils.baidumap.MyMapDemoAcitvity;

public class BaiduMapDemoList extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String [] items = new String[]{"Map Demo1","Map Demo2","Map Demo3"};
		ListView lv = getListView();
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(BaiduMapDemoList.this, BaiduMapActivity.class));
					break;
				case 1:
					startActivity(new Intent(BaiduMapDemoList.this, MyMapDemoAcitvity.class));
					break;
				case 2:
					startActivity(new Intent(BaiduMapDemoList.this, BaiduMapLocationShareActivity.class));
					break;
				}
			}
		});
	}

}
