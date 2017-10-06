package com.jason.jasonutils.adapterdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.jason.jasonutils.R;

public class SimpleAdatperDemo extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SimpleAdapter adapter = new SimpleAdapter(this, 
				getSimpleAdapterData(), 
				R.layout.simple_adapter_demo_activity, 
				new String[]{"photo","name"}, 
				new int[]{R.id.iv_simple_adapter_img,R.id.tv_simple_adapter_title});
		setListAdapter(adapter);
	}
	
	public List<Map<String,Object>> getSimpleAdapterData(){
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("photo", R.drawable.contact_photo);
        map.put("name", "张三");
        dataList.add(map);
        
        map = new HashMap<String, Object>();
        map.put("photo", R.drawable.contact_photo);
        map.put("name", "李四");
        dataList.add(map);
        
        map = new HashMap<String, Object>();
        map.put("photo", R.drawable.contact_photo);
        map.put("name", "王五");
        dataList.add(map);
		
		return dataList;
	}
}
