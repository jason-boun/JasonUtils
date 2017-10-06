package com.jason.jasonutils.map;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MyLocationOverlay;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 我的位置
 * 
 * @author Administrator
 * 
 */
public class MyLocationOverlayDemo extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		overlay();
	}

	private void overlay() {
		MyLocationOverlay overlay = new MyLocationOverlay(getApplicationContext(), mapView);
		overlay.enableMyLocation();// 获取位置
		overlay.enableCompass();// 指南针

		mapView.getOverlays().add(overlay);

		MKLocationManager locationManager = bmapMan.getLocationManager();
		locationManager.requestLocationUpdates(new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				Toast.makeText(getApplicationContext(), "位置变动了", 1).show();
				// mapView.getController().setCenter(point);
				int lat = (int) (location.getLatitude() * 1E6);
				int lon = (int) (location.getLongitude() * 1E6);
				controller.animateTo(new GeoPoint(lat, lon));
			}
		});

	}
}
