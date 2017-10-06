package com.jason.jasonutils.baidumap;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

public class OverlayActivity extends BaseMapActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initMyOverlay();
	}
	
	private void initMyOverlay() {
		Overlay mOverlay = new MyOverlay();
		mapview.getOverlays().add(mOverlay);
	}

	public class MyOverlay extends Overlay{

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			Point point = mapview.getProjection().toPixels(xinwei, null);
			canvas.drawText("⊙信威通信(Jason)", point.x, point.y, new Paint());
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			Toast.makeText(getApplicationContext(), "纬度==="+p.getLatitudeE6()+", 经度=="+p.getLongitudeE6(), Toast.LENGTH_SHORT).show();
			controller.animateTo(p);
			controller.setZoom(12);
			return super.onTap(p, mapView);
		}
		
	}

}
