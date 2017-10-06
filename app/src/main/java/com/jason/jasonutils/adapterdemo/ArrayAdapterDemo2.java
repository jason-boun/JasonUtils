package com.jason.jasonutils.adapterdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArrayAdapterDemo2 extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		List<String> dataList = new ArrayList<String>();
		dataList.add("测试数据1");
		dataList.add("测试数据2");
		dataList.add("测试数据3");
		dataList.add("测试数据4");
		
		ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, dataList));
        setContentView(lv);//注意事项
	}
}
