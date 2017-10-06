package com.jason.jasonutils.map;

import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKSearch;

/**
 * 自驾
 * 
 * @author Administrator
 * 
 */
public class SearchWalk extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		search();
	}

	private void search() {
		MKPlanNode start = new MKPlanNode();
		start.pt = xinwei;

		MKPlanNode end = new MKPlanNode();
		end.name = "海淀区上地南口";
		search.walkingSearch("北京", start, "北京", end);
	}
	
	
}
