package com.jason.jasonutils.listviewlimited;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewLimited extends Activity {
	
	private String[] names = new String[] { "功能1", "功能2", "功能3", "功能4", "功能5", "功能2", "功能3", "功能4", "功能5", "功能2", "功能3", "功能4", "功能5", "功能2", "功能3",
			"功能4", "功能5" };
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_listview_limited);
		listView = (ListView) findViewById(R.id.gv);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
		setListViewHeightBasedOnChildren(listView);
	}
	
	/**
	 * 通过计算出来ListView或者GridView中的子列高度和 进行显示
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		//获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) return;

		int totalHeight = 0;
		//遍历item，计算item的宽高，统计出所有item的总高度
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		//加上item间分隔符占用的高度，最后得到整个ListView完整显示需要的高度
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		//((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除  
		listView.setLayoutParams(params);
		listView.requestLayout();
		Log.i("bqt", "++++++" + params.height);
	}

}
