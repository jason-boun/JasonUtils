package com.jason.jasonutils.map;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BaiduMapActivity extends Activity {
	private ListView list;
	private ArrayAdapter<String> adapter;

	private static String[] objects = new String[] { "hello world", "图层", "覆盖物基类", " 我的位置", "标记多点", "范围检索", "周边检索", "全城检索", "自驾", "步行", "公交换乘",
			"地址解析", "公交专线查询" ,"联想词","共享URL"};
	private static Class[] clazzs = new Class[] { HelloWorld.class, LayerDemo.class, OverlayDemo.class, MyLocationOverlayDemo.class,
			ItemOverlayDemo.class, SearchPOI.class, SearchNearBy.class, SearchInCity.class, SearchDriving.class, SearchWalk.class,
			SearchTransit.class, SearchAddr.class, SearchBusLine.class ,SearchKey.class,SearchUrl.class};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fuction_list_activity);

		list = (ListView) findViewById(R.id.list);

		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, objects);

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), clazzs[position]);
				startActivity(intent);
			}
		});
	}
}