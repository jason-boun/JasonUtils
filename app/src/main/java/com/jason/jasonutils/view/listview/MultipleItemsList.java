package com.jason.jasonutils.view.listview;

import android.app.ListActivity;
import android.os.Bundle;

public class MultipleItemsList extends ListActivity {

	private MyCustomAdapter mAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyCustomAdapter(this);
        for (int i = 1; i < 35; i++) {
            mAdapter.addItem("item " + i);
            if (i % 4 == 0) {
                mAdapter.addSeparatorItem("separator " + i);
            }
        }
        setListAdapter(mAdapter);
    }

}
