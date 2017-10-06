package com.jason.jasonutils.powermanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.ViewUtil;

public class PowerMangerDemo extends Activity implements OnClickListener {
	
	private static final String TAG = PowerMangerDemo.class.getSimpleName();
	private PowerManager pm ;
	private WakeLock wakeLock;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.powermanagerdemo_activity);
		
		pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
		
		LinearLayout container = (LinearLayout) findViewById(R.id.ll_powerdemo_container);
		ViewUtil.setChildsOnClickLisener(container, 0, container.getChildCount(), this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_isscreen_on:
			Toast.makeText(getApplicationContext(), "屏幕状态是："+pm.isScreenOn()+"", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_goto_sleep:
			pm.goToSleep(8000);
			break;
		case R.id.bt_reboot:
			pm.reboot("重启设备");
			break;
		case R.id.bt_wakeup:
			pm.wakeUp(5000);
			break;
		case R.id.bt_wakelock:
			showWakeLock(wakeLock);
			break;
		case R.id.bt_check_whetherlock:
			Toast.makeText(getApplicationContext(), "屏幕是否锁定："+wakeLock.isHeld()+"", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void showWakeLock(WakeLock wakeLock) {
		wakeLock.acquire();
		SystemClock.sleep(5000);
		wakeLock.release();
		Toast.makeText(getApplicationContext(), "解锁了", Toast.LENGTH_SHORT).show();
	}
}
