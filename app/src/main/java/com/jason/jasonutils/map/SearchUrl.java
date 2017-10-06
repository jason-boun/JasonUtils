package com.jason.jasonutils.map;

import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKSearch;

/**
 * 共享Url
 * 
 * @author Administrator
 * 
 */
public class SearchUrl extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		search();
	}

	private void search() {
		search.rGCShareUrlSearch(xinwei, "", "");
	}
	
	
}
