package com.jason.jasonutils.map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKMapViewListener;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.jason.jasonutils.R;

public class BaiduMapLocationShareActivity extends Activity {
	
	static MapView mMapView = null;
	private MapController mMapController = null;
	public MKMapViewListener mMapListener = null;
	
	// 定位相关
	Button sendButton = null;
	
	//static BDLocation lastLocation = null;

	public static BaiduMapActivity instance = null;

	ProgressDialog progressDialog;

	ItemizedOverlay<OverlayItem> mAddrOverlay = null;

	// for baidu map
	public BMapManager mBMapManager = null;
	public static final String strKey = "E09BC478FA1292F16905626130682C2E94B19078"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_baidumap_share_demo);
	}
	

}
