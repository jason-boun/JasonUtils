package com.jason.jasonutils.baidumap;

import android.location.Location;
import android.os.Bundle;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MyLocationOverlay;

public class MyLocationActivity extends BaseMapActivity {
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		initMyLocation();
	}

	private void initMyLocation() {
		MyLocationOverlay locationOverlay = new MyLocationOverlay(getApplicationContext(), mapview);
		locationOverlay.enableMyLocation();
		locationOverlay.enableCompass();
		
		mapview.getOverlays().add(locationOverlay);
		
		MKLocationManager locationManager = mBmManager.getLocationManager();
		locationManager.requestLocationUpdates(new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				//Toast.makeText(getApplicationContext(), "位置变动了", Toast.LENGTH_SHORT).show();
				int lat = (int) (location.getLatitude() * 1E6);
				int lon = (int) (location.getLongitude() * 1E6);
				controller.animateTo(new GeoPoint(lat, lon));
			}
		});
	}

}
