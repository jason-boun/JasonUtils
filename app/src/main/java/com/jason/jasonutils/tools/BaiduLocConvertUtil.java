package com.jason.jasonutils.tools;

import com.baidu.mapapi.GeoPoint;

public class BaiduLocConvertUtil {

	static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	public static GeoPoint gcjToBaidu(double lat, double lng) {
		double x = lng, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bdLng = z * Math.cos(theta) + 0.0065;
		double bdLat = z * Math.sin(theta) + 0.006;
		return new GeoPoint((int) (bdLat * 1e6), (int) (bdLng * 1e6));
	}

	public static GeoPoint baiduToGcj(double lat, double lng) {
		double x = lng - 0.0065, y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gcjLng = z * Math.cos(theta);
		double gcjLat = z * Math.sin(theta);
		return new GeoPoint((int) (gcjLat * 1e6), (int) (gcjLng * 1e6));
	}

}
