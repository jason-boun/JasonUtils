package com.jason.jasonutils.location;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;

public class LocationUtil {
	
	private static LocationManager lm;
	private static MyLocationListner locationListner;
	private static SharedPreferences sp;
	
	private static String LATITUDE_KEY = "latitude";
	private static String LONGITUDE_KEY = "longitude";
	private static Context mContext;
	/**
	 * 单例，初始化并获取经纬度信息
	 */
	private static LocationUtil locationUtil;
	private LocationUtil(){}
	public static LocationUtil getInstance(Context context){
		if(locationUtil == null){
			locationUtil = new LocationUtil();
		}
		mContext = context;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		String provider = lm.getBestProvider(criteria, true);
		locationListner = locationUtil.new MyLocationListner();
		lm.requestLocationUpdates(provider, 0, 0, locationListner);
		
		return locationUtil;
	}
	
	/**
	 * 获取经度
	 * @return
	 */
	public double getLatitude(){
		return Double.parseDouble(sp.getString(LATITUDE_KEY, "0"));
	}
	/**
	 * 获取维度
	 * @return
	 */
	public double getLongitude(){
		return Double.parseDouble(sp.getString(LONGITUDE_KEY, "0"));
	}
	
	/**
	 * 获取修正后的经纬度信息
	 * @return
	 */
	public String getLocationParams(){
//		return getS2C(mContext, getLongitude(), getLatitude(), true);
		return getStr();
	}
	
	/**
	 * 位置变换监听器
	 */
	public class MyLocationListner implements LocationListener{
		@Override
		public void onLocationChanged(Location location) {
			MLog.i("哈哈","获取位置监听器");
			sp.edit().putString(LATITUDE_KEY, location.getLatitude()+"").putString(LONGITUDE_KEY, location.getLongitude()+"").commit();
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onProviderDisabled(String provider) {
		}
	}
	
	public void unRegistLocationListner(){
		if(locationListner != null){
			lm.removeUpdates(locationListner);
			locationListner = null;
		}
	}
	
	/**
	 * 经纬度数据转换工具
	 * @param context
	 * @param longitude
	 * @param latitude
	 * @param s2c
	 * @return
	 */
	private String getS2C(Context context, double longitude,double latitude, boolean s2c){
		try {
			AssetManager am = context.getResources().getAssets();
			InputStream is = am.open("axisoffset.dat");
			ModifyOffset modifyOffset = ModifyOffset.getInstance(is);
			MLog.i("哈哈", "转化实例==="+modifyOffset.toString());
			PointDouble pointDouble;
			if(s2c){
				pointDouble = modifyOffset.s2c(new PointDouble(longitude, latitude));
			}else{
				pointDouble = modifyOffset.c2s(new PointDouble(longitude, latitude));
			}
			is.close();
			am.close();
			return pointDouble.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getStr(){
		try {
			ModifyOffset modifyOffSet = ModifyOffset.getInstance(LocationUtil.class.getClassLoader().getResourceAsStream("axisoffset.dat"));
			//116.29090117,40.043416830,
			PointDouble marsPoint =  modifyOffSet.s2c(new PointDouble(116.29090117, 40.043416830));
			MLog.i("哈哈", "转化实例==="+marsPoint.toString());
			return marsPoint.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		   
	}

}
