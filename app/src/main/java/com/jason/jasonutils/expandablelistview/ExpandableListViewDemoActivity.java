package com.jason.jasonutils.expandablelistview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class ExpandableListViewDemoActivity extends Activity {

	private ExpandableListView elv_common_number_show;
	private MyExpandableListViewAdapter adapter ;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_expandablelistview_demo);
		
		elv_common_number_show = (ExpandableListView) findViewById(R.id.elv_common_number_show);
		adapter  = new MyExpandableListViewAdapter(this);
		elv_common_number_show.setAdapter(adapter);
		elv_common_number_show.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				TextView tv = (TextView) v;
				String number = tv.getText().toString().split("\n")[1];
				Dial(number);
				return false;
			}
		});
	}
	
	/**
	 * 传递号码到拨号盘
	 */
	private void Dial(String number) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:"+number));
		startActivity(intent);
	}
}
