package com.jason.jasonutils.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;

/**
 * 覆盖物
 * @author Administrator
 *
 */
public class OverlayDemo extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initOverLay();
	}
	
	
	private void initOverLay() {
		MyOverlay overlay=new MyOverlay();
		mapView.getOverlays().add(overlay);
		
		controller.setZoom(16);
	}


	/**
	 * 自定义的覆盖物
	 * @author Administrator
	 *
	 */
	private class MyOverlay extends Overlay{

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			//x、y手机屏幕坐标
			//经纬度
			// 如何将经纬度转换成手机屏幕的坐标
			Point point = mapView.getProjection().toPixels(xinwei, null);
			canvas.drawText("⊙这里是信威通信", point.x, point.y, new Paint());
		}
		
		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			Toast.makeText(getApplicationContext(), "lat:"+p.getLatitudeE6(), 1).show();
			return super.onTap(p, mapView);
		}
		
	}
}
