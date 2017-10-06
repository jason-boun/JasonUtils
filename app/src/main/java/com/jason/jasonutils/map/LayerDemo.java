package com.jason.jasonutils.map;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.jason.jasonutils.R;

/**
 * 图层
 * 
 * @author Administrator
 * 
 */
public class LayerDemo extends MapActivity {

	private MapView mapView;
	private BMapManager bmapMan;
	
	private RadioGroup group;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.map_function_layer);
		init();
		findView();
	}

	private void findView() {
		group=(RadioGroup) findViewById(R.id.group);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				switch (checkedId) {
					case R.id.base:
						mapView.setTraffic(false);
						mapView.setSatellite(false);
						break;
					case R.id.trafic:
						mapView.setTraffic(true);
						mapView.setSatellite(false);
						break;
					case R.id.satellite:
						mapView.setTraffic(false);
						mapView.setSatellite(true);
						break;
					default:
						break;
				}
			}
		});
		
	}

	private void init() {
		mapView = (MapView) findViewById(R.id.mapview);

		bmapMan = new BMapManager(getApplicationContext());
		bmapMan.init(ConstantValue.KEY, new MKGeneralListener() {

			@Override
			public void onGetPermissionState(int iError) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetNetworkState(int iError) {
				// TODO Auto-generated method stub

			}
		});

		super.initMapActivity(bmapMan);
		
		mapView.setBuiltInZoomControls(true);
	}
	
	

	@Override
	protected void onDestroy() {
		bmapMan.destroy();
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
