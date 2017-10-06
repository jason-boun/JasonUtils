package com.jason.jasonutils.fragment;

import com.jason.jasonutils.fragment.fragmentlife.FragmentLifeDemoActivity;
import com.jason.jasonutils.fragment.splitshow.FragmentSplitShowActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentDmeoList extends ListActivity {
	
	private String [] datas = new String []{"Fragment Tab Demo","Fragment Lift Demo","Frament splitShow Demo"};
	@SuppressWarnings("rawtypes")
	private Class[] clazzs = new Class []{FragmentActivityDemo.class, FragmentLifeDemoActivity.class, FragmentSplitShowActivity.class};
	ListView mListView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mListView = getListView();
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, datas));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(new Intent(getApplicationContext(), clazzs[position]));
	}

}
