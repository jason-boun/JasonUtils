package com.jason.jasonutils.map;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 地址处理： 地理编码：由街道名称转换为坐标值;反地理编码：由坐标转换为街道名称
 * 
 * @author Administrator
 * 
 */
public class SearchAddr extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		search();
	}

	private void search() {
		search.geocode("金燕龙办公楼", "北京");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_1:
				search.reverseGeocode(xinwei);
				break;
			
		}
		return super.onKeyDown(keyCode, event);
	}
}
