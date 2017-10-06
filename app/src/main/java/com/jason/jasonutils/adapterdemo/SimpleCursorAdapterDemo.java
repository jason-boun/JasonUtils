package com.jason.jasonutils.adapterdemo;

import com.jason.jasonutils.R;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class SimpleCursorAdapterDemo extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Uri uri = Uri.parse("content://com.jason.myprovider/query");
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);//关键点
		
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
				R.layout.simple_cursor_adapter_demo_activity, 
				cursor, 
				new String[]{"name","number"}, 
				new int[]{R.id.iv_simple_cursor_adapter_name,R.id.iv_simple_cursor_adapter_number});
		
		setListAdapter(simpleCursorAdapter);
	}
}
