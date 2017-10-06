package com.jason.jasonutils.mediaplayer;

import com.jason.jasonutils.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MediaPlayerDemo extends Activity implements OnClickListener {
	
	private Button bt_audio_mediaplayer, bt_video_mediaplayer, bt_sensor_mediaplayer_demo,bt_sound_pool_demo;
	private SoundPool soundPool;//声音池
	private int soundId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer_demo_activity);
		init();
		initSoundPool();
	}

	private void initSoundPool() {
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 10);
		soundId = soundPool.load(this, R.raw.ring, 1);
	}

	private void init() {
		bt_audio_mediaplayer = (Button) findViewById(R.id.bt_audio_mediaplayer);
		bt_video_mediaplayer = (Button) findViewById(R.id.bt_video_mediaplayer);
		bt_sound_pool_demo = (Button) findViewById(R.id.bt_sound_pool_demo);
		bt_sensor_mediaplayer_demo = (Button) findViewById(R.id.bt_sensor_mediaplayer_demo);
		
		bt_audio_mediaplayer.setOnClickListener(this);
		bt_video_mediaplayer.setOnClickListener(this);
		bt_sensor_mediaplayer_demo.setOnClickListener(this);
		bt_sound_pool_demo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_audio_mediaplayer:
			startActivity(new Intent(MediaPlayerDemo.this, AudioPlayerDemo.class));
			break;
		case R.id.bt_video_mediaplayer:
			startActivity(new Intent(MediaPlayerDemo.this, VideoPlayerDemo.class));
			break;
		case R.id.bt_sensor_mediaplayer_demo:
			startActivity(new Intent(MediaPlayerDemo.this, SensorMediaDemo.class));
			break;
		case R.id.bt_sound_pool_demo:
			playSound();
			break;
		}
	}
	
	public void playSound(){
		soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
	}

}
