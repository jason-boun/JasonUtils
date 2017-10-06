package com.jason.jasonutils.listnerdemo;

import android.content.Context;
import com.jason.jasonutils.tools.MLog;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

/**
 * 实现监听滑动的事件，哪个view需要的时候直接setOnTouchListener后传入该类的实例就可以了
 * @author LinZhiquan
 *
 */
public class GestureListener extends SimpleOnGestureListener implements OnTouchListener {
	/** 左右滑动的最短距离 */
	private int distance = 100;
	/** 左右滑动的最大速度 */
	private int velocity = 200;
	
	private GestureDetector gestureDetector;
	
	public GestureListener(Context context) {
		super();
		gestureDetector = new GestureDetector(context, this);
	}

	/**
	 * 向左滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public boolean left() {
		return false;
	}
	
	/**
	 * 向右滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public boolean right() {
		return false;
	}
	
	/**
	 * 向上滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public boolean up(){
		return false;
	}
	/**
	 * 向下滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public boolean down(){
		return false;
	}
	/**
	 * 触摸屏幕时处理的事件，子类应该重写
	 * @return
	 */
	public boolean touchDown(){
		return false;
	}
	/**
	 * 离开屏幕时处理的事件，子类应该重写
	 * @return
	 */
	public boolean touchUp(){
		return false;
	}
	/**
	 * 手指滑动的时候处理的事件，子类应该重写
	 * @return
	 */
	public boolean touchMove(){
		return false;
	}
	/**
	 * 滑动屏幕的处理事件方法，子类应该重写
	 * @return
	 */
	public boolean actionProcess(){
		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度（像素/秒）
		// velocityY：Y轴上的移动速度（像素/秒）

		// 向左滑
		if (e1.getX() - e2.getX() > distance && Math.abs(velocityX) > velocity) {
			left();
		}
		// 向右滑
		if (e2.getX() - e1.getX() > distance && Math.abs(velocityX) > velocity) {
			right();
		}
		
		//向上滑动
		if(e1.getY()-e2.getY()>distance && Math.abs(velocityY) >velocity){
			up();
		}
		
		//向下滑动
		if(e2.getY()-e1.getY()>distance && Math.abs(velocityY) >velocity){
			down();
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchDown();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			touchUp();
			break;
		}
		return true;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
}

