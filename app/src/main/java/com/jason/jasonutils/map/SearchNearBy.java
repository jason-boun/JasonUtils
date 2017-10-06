package com.jason.jasonutils.map;

import android.os.Bundle;

import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.PoiOverlay;

/**
 * 周边检索
 * @author Administrator
 *
 */
public class SearchNearBy extends BaseActivity {
	private MKSearch search;
	private MKSearchListener listener;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initSearch();
		search();
	}
	
	private void initSearch() {
		search=new MKSearch();
		listener=new MySearchResult();
		
		search.init(bmapMan, listener);

	}
	
	private void search()
	{
		//半径为1000米区域内的加油站检索
		search.poiSearchNearBy("加油站", xinwei, 1000);
	}
	
	private class MySearchResult implements MKSearchListener{

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			if(result!=null&&iError==0)
			{
				PoiOverlay overlay=new PoiOverlay(SearchNearBy.this,mapView);
				overlay.setData(result.getAllPoi());
				
				mapView.getOverlays().add(overlay);
				mapView.invalidate();
			}
			
		}

		@Override
		public void onGetRGCShareUrlResult(String result, int iError) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
