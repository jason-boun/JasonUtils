package com.jason.jasonutils.map;

import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKSearch;

/**
 * 公交换乘
 * 
 * @author Administrator
 * 
 */
public class SearchTransit extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		search();
	}

	private void search() {
		MKPlanNode start = new MKPlanNode();
//		start.pt = hmPos;
		start.name="";

		MKPlanNode end = new MKPlanNode();
		end.name = "金燕龙办公楼";
		search.transitSearch("北京", start, end);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_1:
				mapView.getOverlays().clear();
				search.setTransitPolicy(MKSearch.EBUS_NO_SUBWAY);
				search();
				break;
			case KeyEvent.KEYCODE_2:
				mapView.getOverlays().clear();
				search.setTransitPolicy(MKSearch.EBUS_TIME_FIRST);
				search();
				break;
			case KeyEvent.KEYCODE_3:
				mapView.getOverlays().clear();
				search.setTransitPolicy(MKSearch.EBUS_TRANSFER_FIRST);
				search();
				break;
			case KeyEvent.KEYCODE_4:
				mapView.getOverlays().clear();
				search.setTransitPolicy(MKSearch.EBUS_WALK_FIRST);
				search();
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
