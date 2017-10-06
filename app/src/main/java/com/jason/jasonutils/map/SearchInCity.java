package com.jason.jasonutils.map;

import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.PoiOverlay;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 全城范围检索
 * 
 * @author Administrator
 * 
 */
public class SearchInCity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		search();
	}

	private int num = 1;

	private void search() {
		MySearchResult result = new MySearchResult() {
			@Override
			public void onGetPoiResult(MKPoiResult result, int type, int iError) {
				Toast.makeText(getApplicationContext(), "页数：" + result.getNumPages() + "每页" + search.getPoiPageCapacity() + "条", 1).show();

				PoiOverlay overlay = new PoiOverlay(SearchInCity.this, mapView);

				overlay.setData(result.getAllPoi());

				mapView.getOverlays().add(overlay);
				mapView.invalidate();

//				for (MKPoiInfo item : result.getAllPoi())
//				{
//					if(item.ePoiType==2)
//					{
//						search.busLineSearch("北京", item.uid);
//						break;
//					}
//				}

			}
		};

		search.init(bmapMan, result);

		search.poiSearchInCity("北京", "加油站");

		controller.setZoom(12);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_1) {
			num++;
			mapView.getOverlays().clear();
			search.goToPoiPage(num);
		}
		return super.onKeyDown(keyCode, event);
	}
}
