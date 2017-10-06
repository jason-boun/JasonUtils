package com.jason.jasonutils.mediaplayer;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.jason.jasonutils.R;

public class VideoPlayerDemo extends Activity implements OnClickListener {
	
	private Button bt_video_play, bt_video_pause, bt_video_replay, bt_video_stop;
	private SeekBar sb_video_seek_bar;
	private SurfaceView sv_video_surfaceview;
	
	private MediaPlayer mediaPlayer;
	private int position;
	private Timer timer;
	private TimerTask task;
	private static final float MUSIC_VOLUME = 0.05f;
	private String filePath = Environment.getExternalStorageDirectory()+"/videoviewdemo.mp4";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_palyer_demo_activity);
		initView();
	}

	private void initView() {
		bt_video_play = (Button) findViewById(R.id.bt_video_pause);
		bt_video_pause = (Button) findViewById(R.id.bt_video_play);
		bt_video_replay = (Button) findViewById(R.id.bt_video_replay);
		bt_video_stop = (Button) findViewById(R.id.bt_video_stop);
		
		bt_video_play.setOnClickListener(this);
		bt_video_pause.setOnClickListener(this);
		bt_video_replay.setOnClickListener(this);
		bt_video_stop.setOnClickListener(this);
		
		sv_video_surfaceview = (SurfaceView) findViewById(R.id.sv_video_surfaceview);
		sb_video_seek_bar = (SeekBar) findViewById(R.id.sb_video_seek_bar);
		sb_video_seek_bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(null!=mediaPlayer){
					mediaPlayer.seekTo(sb_video_seek_bar.getProgress());
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
		
		sv_video_surfaceview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置SurfaceView自己不管理的缓冲区
		sv_video_surfaceview.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					position = mediaPlayer.getCurrentPosition();
					videoStop();
				}
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (position != 0) {
					mediaPlayer.seekTo(position);
					videoPlay();
				}
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_video_play:
			videoPlay();
			break;
		case R.id.bt_video_pause:
			videoPause();
			break;
		case R.id.bt_video_replay:
			videoReplay();
			break;
		case R.id.bt_video_stop:
			videoStop();
			break;
		}
	}
	
	private void videoPlay(){
		//String path = et_video_path.getText().toString().trim();
		File file = new File(filePath);
		if(file.exists() && file.length()>0){
			try {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.reset();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDisplay(sv_video_surfaceview.getHolder());//设置video以SurfaceHolder播放
				
				//AssetFileDescriptor afd = getResources().getAssets().openFd("videoviewdemo.mp4");
				//mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				//afd.close();
				
				mediaPlayer.setDataSource(filePath);
				mediaPlayer.prepare();
				mediaPlayer.start();
				
				//对进度条进行操作
				sb_video_seek_bar.setMax(mediaPlayer.getDuration()); 
				timer = new Timer();
				task = new TimerTask() {
					@Override
					public void run() {
						sb_video_seek_bar.setProgress(mediaPlayer.getCurrentPosition());
					}
				};
				timer.schedule(task, 0, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void videoPause(){
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			// 暂停播放
			mediaPlayer.pause();
			bt_video_pause.setText("继续");
			return;
		}
		if (mediaPlayer != null) {
			if ("继续".equals(bt_video_pause.getText().toString())) {
				mediaPlayer.start();
				bt_video_pause.setText("暂停");
				return;
			}
		}
	}
	private void videoReplay(){
		if (null!= mediaPlayer && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
		} else {
			videoPlay();
		}
	}
	private void videoStop(){
		if(null!=mediaPlayer){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
		bt_video_play.setEnabled(true);
		
		if(null!=timer){
			timer.cancel();
		}if(null!=task){
			task.cancel();
		}
		timer = null;
		task = null;
	}

}
