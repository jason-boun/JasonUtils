package com.jason.jasonutils.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jason.jasonutils.R;

public class MyMapDemoAcitvity extends Activity {
	
	private ListView lv_baidumap_demo_list;
	private ArrayAdapter<String> adapter ;

	private static String [] demoTitles= new String[] {"Hello BaiduMap", "覆盖物Demo", "我的位置"};
	private static Class []demoClazzs = new Class[] {HelloBaiduMap.class, OverlayActivity.class, MyLocationActivity.class};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_map_demo_list_activity);
		init();
	}

	private void init() {
		lv_baidumap_demo_list = (ListView) findViewById(R.id.lv_baidumap_demo_list);
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, demoTitles);
		lv_baidumap_demo_list.setAdapter(adapter);
		
		lv_baidumap_demo_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(getApplicationContext(), demoClazzs[position]));
			}
		});
	}

}
