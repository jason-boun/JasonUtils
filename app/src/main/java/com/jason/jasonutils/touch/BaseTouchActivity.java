package com.jason.jasonutils.touch;

import android.app.Activity;
import android.os.Bundle;
import com.jason.jasonutils.tools.MLog;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseTouchActivity extends Activity {
	
	protected GestureDetector mGestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			/**
			 * 手指在屏幕上滑动的时候 调用的方法
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				
				if (Math.abs(velocityX) < 200) {
					MLog.i("哈哈", "移动的太慢了...");
					return false;
				}

				if (Math.abs(e1.getRawY() - e2.getRawY()) > 200) {
					MLog.i("哈哈", "竖屏移动无效");
					return false;
				}

				if (e1.getRawX() - e2.getRawX() > 200) {// 向左滑动 显示下一个界面
					showNext();
					return false;
				}

				if (e2.getRawX() - e1.getRawX() > 200) {//
					showPre();
					return false;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		} );
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);//让手势识别生效
		return super.onTouchEvent(event);
	}
	
	public void next(View view){
		showNext();
	}
	
	public void pre(View view){
		showPre();
	}
	
	public abstract void showNext();
	public abstract void showPre();

}
