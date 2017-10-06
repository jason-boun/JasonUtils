package com.jason.jasonutils.map;

import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKSearch;

/**
 * 联想词检索
 * 
 * @author Administrator
 * 
 */
public class SearchKey extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		search();
	}

	private void search() {
		search.suggestionSearch("黑马");
	}
	
	
}
