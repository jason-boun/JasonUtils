package com.jason.jasonutils.baidumap;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.jason.jasonutils.R;

public class HelloBaiduMap extends MapActivity {

	private MapView mapview;
	private BMapManager mBmManager;
	private MapController controller ;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.hello_baidumap_activity);
		init();
		initController();
	}

	private void init() {
		mapview = (MapView) findViewById(R.id.mapview);
		mBmManager = new BMapManager(getApplicationContext());
		mBmManager.init(ConstantValue.AppKey, new MKGeneralListener() {
			@Override
			public void onGetPermissionState(int iError) {
				if(iError== MKEvent.ERROR_PERMISSION_DENIED){
					Toast.makeText(getApplicationContext(), "授权失败", 1).show();
				}
			}
			
			@Override
			public void onGetNetworkState(int iError) {
				if(iError == MKEvent.ERROR_NETWORK_CONNECT){
					Toast.makeText(getApplicationContext(), "网络连接失败", 1).show();
				}
			}
		});
		initMapActivity(mBmManager);
		mapview.setBuiltInZoomControls(true);
	}
	

	private void initController() {
		controller = mapview.getController();
		int latitude=(int) (40.051*1E6);//维度
		int longitude=(int) (116.303*1E6);//经度
		GeoPoint xinwei=new GeoPoint(latitude, longitude);
		
		controller.setCenter(xinwei);
		controller.setZoom(16);
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
