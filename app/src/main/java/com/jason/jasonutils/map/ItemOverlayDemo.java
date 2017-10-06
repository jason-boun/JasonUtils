package com.jason.jasonutils.map;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.jason.jasonutils.R;

/**
 * 标记多个点
 * 
 * @author Administrator
 * 
 */
public class ItemOverlayDemo extends BaseActivity {

	private View popview;
	private TextView title;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		overlay();
		initPop();
	}

	private void initPop() {
		popview = View.inflate(getApplicationContext(), R.layout.map_function_search_popview, null);

		title = (TextView) popview.findViewById(R.id.title);

		mapView.addView(popview, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM_CENTER));
		popview.setVisibility(View.GONE);
	}

	private void overlay() {
		MyItemOverlay overlay = new MyItemOverlay(getResources().getDrawable(R.drawable.eat_icon));
		mapView.getOverlays().add(overlay);
	}

	private class MyItemOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items;

		public MyItemOverlay(Drawable drawable) {
			super(drawable);
			initOverlayItem();
		}

		private void initOverlayItem() {
			// TODO 初始化一系列点
			items = new ArrayList<OverlayItem>();

			OverlayItem item = new OverlayItem(xinwei, "信威通信", "mcwill");// point:经纬度；title：标题；snippet：内容摘要
			items.add(item);

			// size变动,内容变动
			populate();

		}

		private void addItems() {

			OverlayItem item = new OverlayItem(new GeoPoint(latitude + 1000, longitude), "向北", "增加纬度");
			items.add(item);

			item = new OverlayItem(new GeoPoint(latitude, longitude + 1000), "向东", "增加经度");
			items.add(item);

			item = new OverlayItem(new GeoPoint(latitude - 1000, longitude - 1000), "向西南", "减少经纬度");
			items.add(item);

			populate();

		}

		@Override
		protected OverlayItem createItem(int location) {
			return items.get(location);
		}

		@Override
		public int size() {
			return items.size();
		}

		@Override
		protected boolean onTap(int location) {
			// Toast.makeText(getApplicationContext(), items.get(location).getTitle(), 1).show();
			OverlayItem item = items.get(location);
			LayoutParams params = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, item.getPoint(),
					MapView.LayoutParams.BOTTOM_CENTER);
			mapView.updateViewLayout(popview, params);

			popview.setVisibility(View.VISIBLE);

			title.setText(item.getTitle());

			addItems();
			return super.onTap(location);
		}

	}
}
