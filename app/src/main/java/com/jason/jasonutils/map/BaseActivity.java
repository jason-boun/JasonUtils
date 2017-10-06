package com.jason.jasonutils.map;

import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionInfo;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.jason.jasonutils.R;

public abstract class BaseActivity extends MapActivity {
	private static final String TAG = "BaseActivity";
	protected MapView mapView;
	protected BMapManager bmapMan;// 地图引擎
	protected MapController controller;// 处理地图移动和缩放的工具类

	protected int latitude = (int) (40.051751 * 1E6);// 维度
	protected int longitude = (int) (116.305491 * 1E6);// 经度
	protected GeoPoint xinwei = new GeoPoint(latitude, longitude);

	protected MKSearch search;
	protected MKSearchListener listener;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.map_fuction_show_map);
		// setContentView();
		init();
		initSearch();
	}

	private void initSearch() {
		search = new MKSearch();
		listener = new MySearchResult();

		search.init(bmapMan, listener);

	}

	// protected abstract void setContentView();

	private void init() {
		mapView = (MapView) findViewById(R.id.mapview);

		bmapMan = new BMapManager(getApplicationContext());
		bmapMan.init(ConstantValue.KEY, new MKGeneralListener() {

			@Override
			public void onGetPermissionState(int iError) {
				// TODO 校验Key
				// MKEvent
				if (iError == MKEvent.ERROR_PERMISSION_DENIED) {

				}
			}

			@Override
			public void onGetNetworkState(int iError) {
				// TODO 判断网络
				if (iError == MKEvent.ERROR_NETWORK_CONNECT) {

				}
			}
		});// 不调用或null,"";无法使用百度地图

		// AsyncTask

		// key校验
		super.initMapActivity(bmapMan);
		mapView.setBuiltInZoomControls(true);// 添加放大缩小按钮

		initController();// 改变地图的中点
	}

	private void initController() {
		controller = mapView.getController();

		controller.setCenter(xinwei);
		controller.setZoom(16);
	}

	@Override
	protected void onDestroy() {
		if (bmapMan != null) {
			bmapMan.destroy();
			bmapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		bmapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		bmapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 结果的处理
	 * 
	 * @author Administrator
	 * 
	 */
	protected class MySearchResult implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result != null && iError == 0) {
				if (result.type == MKAddrInfo.MK_GEOCODE) {
					// result.geoPt;
					// result.strAddr
					// result.strBusiness

					String strInfo = String.format("纬度：%f 经度：%f 地址：%s\r\n",//
							result.geoPt.getLatitudeE6() / 1e6,//
							result.geoPt.getLongitudeE6() / 1e6,//
							result.strAddr + result.strBusiness//
					);
					Toast.makeText(getApplicationContext(), strInfo, Toast.LENGTH_LONG).show();

				} else {
					String strInfo = String.format("纬度：%f 经度：%f 地址：%s\r\n",//
							result.geoPt.getLatitudeE6() / 1e6,//
							result.geoPt.getLongitudeE6() / 1e6,//
							result.addressComponents.city + //
									result.addressComponents.district + //
									result.addressComponents.street //
					);
					Toast.makeText(getApplicationContext(), strInfo, Toast.LENGTH_LONG).show();

					for (MKPoiInfo item : result.poiList) {
						MLog.i(TAG, item.name);
					}

				}

			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
			if (result != null && iError == 0) {
				// RouteOverlay:线路
				RouteOverlay overlay = new RouteOverlay(BaseActivity.this, mapView);
				Toast.makeText(getApplicationContext(), "搜索到" + result.getNumPlan() + "条结果", 1).show();
				if (result.getNumPlan() > 0) {
					overlay.setData(result.getPlan(0).getRoute(0));
					mapView.getOverlays().add(overlay);
					mapView.invalidate();
				}

			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			if (result != null && iError == 0) {
				// POI
				// PoiOverlay
				PoiOverlay overlay = new PoiOverlay(BaseActivity.this, mapView);

				overlay.setData(result.getAllPoi());

				mapView.getOverlays().add(overlay);
				mapView.invalidate();
			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}

		}

		@Override
		public void onGetRGCShareUrlResult(String result, int iError) {
			if (result != null && iError == 0) {
				MLog.i(TAG, result);

			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			if (result != null && iError == 0) {
				for (MKSuggestionInfo item : result.getAllSuggestions()) {
					MLog.i(TAG, item.city + ":" + item.key);
				}

			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
			if (result != null && iError == 0) {
				// 公交换乘：TransitOverlay
				Toast.makeText(getApplicationContext(), "搜索到" + result.getNumPlan() + "条结果", 1).show();

				for (int i = 0; i < result.getNumPlan(); i++) {
					TransitOverlay overlay = new TransitOverlay(BaseActivity.this, mapView);
					overlay.setData(result.getPlan(i));

					mapView.getOverlays().add(overlay);
					mapView.invalidate();
				}

			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
			if (result != null && iError == 0) {
				// RouteOverlay:线路
				RouteOverlay overlay = new RouteOverlay(BaseActivity.this, mapView);
				Toast.makeText(getApplicationContext(), "搜索到" + result.getNumPlan() + "条结果", 1).show();
				if (result.getNumPlan() > 0) {
					overlay.setData(result.getPlan(0).getRoute(0));
					mapView.getOverlays().add(overlay);
					mapView.invalidate();
				}

			} else {
				Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
			}

		}

	}

}
