package com.jason.jasonutils.map;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;

/**
 * 公交专线查询
 * 
 * @author Administrator
 * 
 */
public class SearchBusLine extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		search();
	}

	private void search() {
		//busLineUid - 公交线路的uid，用户可以通过poi查询返回的结果中获取MKPoiInfo的uid 
		
		MySearchResult result = new MySearchResult() {
			@Override
			public void onGetPoiResult(MKPoiResult result, int type, int iError) {
				Toast.makeText(getApplicationContext(), "页数：" + result.getNumPages() + "每页" + search.getPoiPageCapacity() + "条", 1).show();

				PoiOverlay overlay = new PoiOverlay(SearchBusLine.this, mapView);

				overlay.setData(result.getAllPoi());

				mapView.getOverlays().add(overlay);
				mapView.invalidate();

				for (MKPoiInfo item : result.getAllPoi())
				{
					if(item.ePoiType==2)
					{
						search.busLineSearch("北京", item.uid);
						break;
					}
				}

			}
			
			@Override
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				if (result != null && iError == 0) {
					// RouteOverlay:线路
					RouteOverlay overlay=new RouteOverlay(SearchBusLine.this, mapView);
					overlay.setData(result.getBusRoute());
					
					mapView.getOverlays().add(overlay);
					mapView.invalidate();
					

				} else {
					Toast.makeText(getApplicationContext(), "未搜索到结果", 1).show();
				}
				super.onGetBusDetailResult(result, iError);
			}
			
		};
		search.init(bmapMan, result);
		search.poiSearchInCity("北京", "629");
	}
	
	
}
