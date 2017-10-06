package com.jason.jasonutils.location;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 一、思路：
 * 获取传感器管理器；得到方向传感器；
 * 传感器管理器注册监听器；(需要参数：1.监听器2.方向传感器3.感应速度)。
 * 
 * 自定义传感器监听器实现SensorEventListener。
 * Activity销毁时，注销监听器。
 * 
 * 二、注意点：
 * 1、float startAngle = event.values[0];
 * 	是手机正上方相对于正北方向的旋转角度。该方法每次得到的都是手机正上方相对于正北方向的角度值；
 * 2、如果手机从北到东顺时针旋转，那么图片应该是从东到北逆时针旋转。（图片旋转角度：startAngle和endAngle的代数和，方向逆时针为正）
 * 	那个RotateAnimation(...)动画，指的是图片的旋转，而不是手机的旋转。
 * 3、那个RotateAnimation(...)动画，前两个参数的代数和，算出来的就是图片本次需要旋转的绝对角度值。
 */

public class CompassDemoActivity extends Activity {
	
	private SensorManager sensorManager ;
	private MySensorListener mySensListener;
	private Sensor sensor;
	private ImageView iv_compass_show;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass_demo_activity);
		iv_compass_show = (ImageView) findViewById(R.id.iv_compass_show);
		init();
	}

	private void init() {
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mySensListener = new MySensorListener();
		sensorManager.registerListener(mySensListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private class MySensorListener implements SensorEventListener{
		private float endAngle = 0;//记录上次角度
		@Override
		public void onSensorChanged(SensorEvent event) {
			float startAngle = event.values[0];
			RotateAnimation rotateAnimation = new RotateAnimation(-endAngle, startAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			iv_compass_show.startAnimation(rotateAnimation);
			endAngle = startAngle;
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(mySensListener);
		mySensListener = null;
		sensor = null;
	}

}
