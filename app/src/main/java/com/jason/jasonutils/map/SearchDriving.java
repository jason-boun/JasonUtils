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
public class SearchDriving extends BaseActivity {
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
		end.name = "金燕龙办公楼";
		search.drivingSearch("北京", start, "北京", end);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_1:
				mapView.getOverlays().clear();
				search.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
				search();
				break;
			case KeyEvent.KEYCODE_2:
				mapView.getOverlays().clear();
				search.setDrivingPolicy(MKSearch.ECAR_FEE_FIRST);
				search();
				break;
			case KeyEvent.KEYCODE_3:
				mapView.getOverlays().clear();
				search.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
				search();
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
