package com.jason.jasonutils.baidumap;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.jason.jasonutils.R;

public class BaseMapActivity extends MapActivity {

	protected MapView mapview;
	protected BMapManager mBmManager;
	protected MapController controller ;
	
	protected int latitude = (int) (40.051751 * 1E6);// 维度
	protected int longitude = (int) (116.305491 * 1E6);// 经度
	protected GeoPoint xinwei = new GeoPoint(latitude, longitude);
	
	protected MKSearch search;
	protected MKSearchListener searchListener;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.hello_baidumap_activity);
		init();
		initController();
		initSearchListener();
	}

	/**
	 * 初始化MapView、BMapManager
	 */
	private void init() {
		mapview = (MapView) findViewById(R.id.mapview);
		mBmManager = new BMapManager(getApplicationContext());
		mBmManager.init(ConstantValue.AppKey, new MKGeneralListener() {
			@Override
			public void onGetPermissionState(int iError) {
				if(iError== MKEvent.ERROR_PERMISSION_DENIED){
					Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onGetNetworkState(int iError) {
				if(iError == MKEvent.ERROR_NETWORK_CONNECT){
					Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		initMapActivity(mBmManager);
		mapview.setBuiltInZoomControls(true);
	}
	
	/**
	 * 初始化MapController
	 */
	private void initController() {
		controller = mapview.getController();
		controller.setCenter(xinwei);
		controller.setZoom(12);
	}
	
	/**
	 * 初始化回调接口
	 */
	private void initSearchListener() {
		search = new MKSearch();
		searchListener = new MySearchListener();
		search.init(mBmManager, searchListener);
	}
	
	/**
	 * 回调结果处理
	 */
	protected class MySearchListener implements MKSearchListener{
		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mBmManager !=null){
			mBmManager.destroy();
			mBmManager = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mBmManager !=null){
			mBmManager.stop();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mBmManager !=null){
			mBmManager.start();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
