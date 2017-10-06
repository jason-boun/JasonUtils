package com.jason.jasonutils.map;

import android.os.Bundle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.jason.jasonutils.R;

public class HelloWorld extends MapActivity {
	// ①MapView
	// ②key
	// ③开启引擎
	// ④展示地图
	private MapView mapView;
	private BMapManager bmapMan;// 地图引擎
	private MapController controller;//处理地图移动和缩放的工具类

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.map_fuction_show_map);
		init();
	}

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
		mapView.setBuiltInZoomControls(true);//添加放大缩小按钮
		
		initController();//改变地图的中点
	}

	private void initController() {
		int latitude=(int) (40.051*1E6);//维度
		int longitude=(int) (116.303*1E6);//经度
		
		controller = mapView.getController();

		GeoPoint hmPos=new GeoPoint(latitude, longitude);
		
		controller.setCenter(hmPos);
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

}
