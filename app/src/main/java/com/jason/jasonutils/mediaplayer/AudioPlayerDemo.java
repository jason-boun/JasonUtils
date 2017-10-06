package com.jason.jasonutils.mediaplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class AudioPlayerDemo extends SysRingtonePick implements OnClickListener {
	
	protected MediaPlayer mediaPlayer;
    protected final static int MUSIC_FINISHED = 30;
    private static final float MUSIC_VOLUME = 0.50f;
    private Button bt_select_sys_ringtone, bt_assets_file_to_play,bt_assets_file_to_stop,bt_assets_file_switch_audiomode;
	private AudioManager am ;
	public String ringtonePath = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.read_assets_file_toplay_activity);
		
		init();
	}
    
	private void init() {
		bt_assets_file_to_play = (Button) findViewById(R.id.bt_assets_file_to_play);
		bt_assets_file_to_stop = (Button) findViewById(R.id.bt_assets_file_to_stop);
		bt_assets_file_switch_audiomode = (Button) findViewById(R.id.bt_assets_file_switch_audiomode);
		bt_select_sys_ringtone = (Button) findViewById(R.id.bt_select_sys_ringtone);
		
		bt_assets_file_to_play.setOnClickListener(this);
		bt_assets_file_to_stop.setOnClickListener(this);
		bt_assets_file_switch_audiomode.setOnClickListener(this);
		bt_select_sys_ringtone.setOnClickListener(this);
		
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * 初始化播放器对象，开启播放提示音乐
	 * @param path
	 */
	protected void palyTipMusic(final Handler handler,String audioPath){
		try {
			if(null == mediaPlayer){
				mediaPlayer = new MediaPlayer();
			}
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			am.setMode(AudioManager.MODE_IN_CALL);//听筒模式
			
			if(audioPath!=null && audioPath.length()>0){
				mediaPlayer.setDataSource(audioPath);
			}else{
				//AssetFileDescriptor afd = getResources().openRawResourceFd(tipAudioResourceId);//如果是在资源文件夹res的raw目录下，则传入该资源的id即可。
				AssetFileDescriptor afd = getAssets().openFd("busy.ogg");//读取assets目录下文件，必须写出后缀名。
				mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				afd.close();
			}
			mediaPlayer.setVolume(MUSIC_VOLUME, MUSIC_VOLUME);
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
					handler.sendEmptyMessage(MUSIC_FINISHED);
				}
			});
		} catch (Exception e) {
			mediaPlayer = null;
			e.printStackTrace();
		} 
	}
	
	/**
	 * 停止播放音乐，释放资源，销毁播放器对象
	 */
	protected void StopTipMusic(){
		if(null!=mediaPlayer ){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MUSIC_FINISHED:
				Toast.makeText(AudioPlayerDemo.this, "播放完毕",Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onStop() {
		super.onPause();
		StopTipMusic();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_select_sys_ringtone:
			picSysRingTone();
			break;
		case R.id.bt_assets_file_to_play:
			ringtonePath = getSharedPreferences("config", MODE_PRIVATE).getString(SYS_RINGTONE_PATH_KEY, "");
			palyTipMusic(handler,ringtonePath);
			break;
		case R.id.bt_assets_file_to_stop:
			StopTipMusic();
			break;
		case R.id.bt_assets_file_switch_audiomode:
			if(am.getMode()==AudioManager.MODE_NORMAL){
				am.setMode(AudioManager.MODE_IN_CALL);//听筒模式
			}else if(am.getMode()==AudioManager.MODE_IN_CALL){
				am.setMode(AudioManager.MODE_NORMAL);//外音模式
			}
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		StopTipMusic();
	}
}
