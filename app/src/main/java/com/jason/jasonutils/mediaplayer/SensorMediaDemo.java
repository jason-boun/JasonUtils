package com.jason.jasonutils.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class SensorMediaDemo extends Activity implements OnClickListener {
	
	private Button btn_start, btn_stop, btn_open, btn_close;
	private MediaPlayer mediaPlayer;
	private AudioManager audioManager;
	private SensorManager sensorManager;
	private Sensor distanceSensor;
	private MySensorEventListener sensorListener;
	private float f_proximiny; // 当前传感器距离
	
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sensor_mediaplayer_demo_activity);
		
		init();
		initData();
		initPowerManager();
	}

	private void initPowerManager() {
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "哈哈");
		wakeLock.setReferenceCounted(false);
	}

	private void init() {
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		btn_open = (Button) findViewById(R.id.btn_open);
		btn_close = (Button) findViewById(R.id.btn_close);
		
		btn_start.setOnClickListener(this);
		btn_stop.setOnClickListener(this);
		btn_open.setOnClickListener(this);
		btn_close.setOnClickListener(this);
	}

	private void initData() {
		if(null==mediaPlayer){
			mediaPlayer = new MediaPlayer();
		}
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		distanceSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sensorListener = new MySensorEventListener();
		sensorManager.registerListener(sensorListener, distanceSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			palyMusic();
			break;
		case R.id.btn_stop:
			StopTipMusic();
			break;
		case R.id.btn_open:
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			break;
		case R.id.btn_close:
			audioManager.setMode(AudioManager.MODE_NORMAL);
			break;
		}
	}

	public void palyMusic(){
		try {
			if(null == mediaPlayer){
				mediaPlayer = new MediaPlayer();
			}
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			AssetFileDescriptor afd = getAssets().openFd("busy.ogg");//读取assets目录下文件，必须写出后缀名。
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			afd.close();
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
				}
			});
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer.seekTo(0);
					Toast.makeText(SensorMediaDemo.this, "播放完毕", 0).show();
				}
			});
		} catch (Exception e) {
			mediaPlayer = null;
			e.printStackTrace();
		} 
	}
	
	protected void StopTipMusic(){
		if(null!=mediaPlayer ){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	
	public class MySensorEventListener implements SensorEventListener{
		@Override
		public void onSensorChanged(SensorEvent event) {
			f_proximiny = event.values[0];
			if(f_proximiny == distanceSensor.getMaximumRange()){
				MLog.i("哈哈", "距离变大");
				audioManager.setMode(AudioManager.MODE_NORMAL);
				wakeLock.acquire();
				
			}else{
				MLog.i("哈哈", "距离变小");
				audioManager.setMode(AudioManager.MODE_IN_CALL);
				if(wakeLock !=null && wakeLock.isHeld()){
					wakeLock.release();
				}
				//pm.goToSleep(SystemClock.uptimeMillis());//熄灭屏幕
			}
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(sensorListener);
	}
	
}
