package com.jason.jasonutils.map;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
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
 * 兴趣点搜索
 * 
 * @author Administrator
 * 
 */
public class SearchPOI extends BaseActivity {
	private MKSearch search;
	private MKSearchListener listener;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initSearch();
		search();
	}

	private void search() {
		GeoPoint ptLB = new GeoPoint((int) (40.035 * 1E6), (int) (116.296 * 1E6));
		GeoPoint ptRT = new GeoPoint((int) (40.051 * 1E6), (int) (116.303 * 1E6));
		// 近似一个矩形区域内的检索
		search.poiSearchInbounds("加油站", ptLB, ptRT);

	}

	private void initSearch() {
		search = new MKSearch();
		listener = new MySearchResult();
		search.init(bmapMan, listener);
	}

	/**
	 * 结果的处理
	 * 
	 * @author Administrator
	 * 
	 */
	private class MySearchResult implements MKSearchListener {

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
			if (result != null && iError == 0) {
				//POI
				//PoiOverlay
				PoiOverlay overlay=new PoiOverlay(SearchPOI.this, mapView);
				
				overlay.setData(result.getAllPoi());
				
				mapView.getOverlays().add(overlay);
				mapView.invalidate();
			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
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
