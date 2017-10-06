package com.jason.jasonutils.view.touchdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.jasonutils.R;

/**
 * 主要测试onTouchListner接口（它的onTouch方法）、onTouchEvent方法、onClickListner接口（onClick）方法的调用优先级关系
 * 同时测试dispatchTouchEvent、onInterceptTouchEvent、onTouchEvent三个方法的调用顺序
 * @author bo.jia
 */
public class OnTouchTestDemo extends Activity{
	
	
	TextView tv_touchtestdemo_title ;
	RelativeLayout rl_touchetestdemo_container;
	ImageView iv_touchtest_photo;
	Button bt_touchetestdemo_confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ontouchtest_demotest);
		
		tv_touchtestdemo_title = (TextView) findViewById(R.id.tv_touchtestdemo_title);
		rl_touchetestdemo_container = (RelativeLayout) findViewById(R.id.rl_touchetestdemo_container);
		iv_touchtest_photo = (ImageView) findViewById(R.id.iv_touchtest_photo);
		bt_touchetestdemo_confirm = (Button) findViewById(R.id.bt_touchetestdemo_confirm);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

}
