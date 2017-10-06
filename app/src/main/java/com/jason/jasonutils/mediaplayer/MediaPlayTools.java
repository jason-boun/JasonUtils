/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jason.jasonutils.mediaplayer;

import java.io.File;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.TextUtils;

/**
 * 
* <p>Title: MediaPlayTools.java</p>
* <p>Description: 
*  case R.id.start:
			File file = new File(Environment.getExternalStorageDirectory(),"voiceDemo/fn_2013100916_99e9ba8bf0924f269e4e25ab1df6c726_1142.amr");
			MediaPlayTools.getInstance().playVoice(file.getAbsolutePath(), false);
			break;
		case R.id.puse:
			
			MediaPlayTools.getInstance().pause();
			break;
		case R.id.resume:
			
			MediaPlayTools.getInstance().resume();
			break;
		case R.id.stop:
			
			MediaPlayTools.getInstance().stop();
			break;</p>
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: http://www.cloopen.com</p>
* @author Jorstin Chan
* @date 2013-10-16
* @version 3.5
 */
public class MediaPlayTools {
	
	private static final String TAG = "MediaPlayTools";

	private static MediaPlayTools mInstance = null;
	
	/**
	 * The definition of the state of play 
	 * Play error
	 */
	private static final int STATUS_ERROR 				= -1;
	
	/**
	 * Stop playing
	 */
	private static final int STATUS_STOP 				= 0;
	
	/**
	 * Voice playing
	 */
	private static final int STATUS_PLAYING 			= 1;
	
	/**
	 * Pause playback
	 */
	private static final int STATUS_PAUSE 				= 2;
	
	private MediaPlayer mediaPlayer = new MediaPlayer();
	
	/**
	 * The local path voice file
	 */
	private String urlPath = "";
	
	private int status = 0;

	public MediaPlayTools() {

		setOnCompletionListener();
		setOnErrorListener();
	}
	
	 synchronized public static MediaPlayTools getInstance() {
	        if (null == mInstance) {
	            mInstance = new MediaPlayTools();
	        }
	        return mInstance;
	    }
	
	 /**
	 * <p>Title: play</p>
	 * <p>Description: Speech interface, you can set the start position to play, 
	 * and to select the output stream (Earpiece or Speaker)</p>
	 * @param isEarpiece 
	 * @param seek
	  */
	private void play(boolean isEarpiece , int seek) {
		
		int streamType = AudioManager.STREAM_MUSIC;
		if(TextUtils.isEmpty(urlPath) || !new File(urlPath).exists()) {
			return ;
		}
		
		if(isEarpiece) {
			streamType = AudioManager.STREAM_VOICE_CALL;
		}
		
		if(mediaPlayer == null ) {
			mediaPlayer = new MediaPlayer();
			setOnCompletionListener();
			setOnErrorListener();
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(streamType);
			mediaPlayer.setDataSource(urlPath);
			mediaPlayer.prepare();
			if(seek > 0) {
				mediaPlayer.seekTo(seek);
			}
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* <p>Title: play</p>
	* <p>Description: </p>
	* @param urlPath
	* @param isEarpiece
	* @param seek
	* @return
	* 
	* @see #play(boolean, int)
	 */
	private boolean play(String urlPath , boolean isEarpiece , int seek) {
		
		if(status != STATUS_STOP) {
			return false;
		}
		
		this.urlPath = urlPath;
		
		boolean result = false;
		try {
			play(isEarpiece, seek);
			this.status = STATUS_PLAYING;
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				play(true, seek);
				result = true;
			} catch (Exception e1) {
				e1.printStackTrace();
				result = false;
			}
			
		}
		
		return result;
		
	}
	
	/**
	 * Using the speaker model play audio files
	* <p>Title: playVoice</p>
	* <p>Description: </p>
	* @param urlPath
	* @param isEarpiece
	* @return
	 */
	public boolean playVoice(String urlPath , boolean isEarpiece) {
		
		return play(urlPath, isEarpiece, 0);
	}
	
	/**
	 * 
	* <p>Title: resume</p>
	* <p>Description: Recovery pause language file, from the last to suspend the position to start playing</p>
	* @return
	 */
	public boolean resume() {
		
		if( this.status != STATUS_PAUSE) {
			
			return false;
		}
		
		boolean result = false;
		
		try {
			mediaPlayer.start();
			this.status = STATUS_PLAYING ;
			result = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			this.status = STATUS_ERROR;
			result = false;
		}
		
		return result;
		
	}
	
	/**
	 * 
	* <p>Title: stop</p>
	* <p>Description: Stop playing audio files
	*    If you need to play, you will need to call
	*    @see MediaPlayTools</p>
	*    @see MediaPlayTools#playVoice(String, boolean)
	* @return
	 */
	public boolean stop () {
		
		if(status != STATUS_PLAYING && status != STATUS_PAUSE)  {
			
			return false;
		}

		boolean result = false;
		try {
			this.mediaPlayer.stop();
			this.mediaPlayer.release();
			this.mediaPlayer = null;
			this.status = STATUS_STOP;
			result = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			this.status = STATUS_ERROR;
			result = false;
			
		}

		return result;
	}
	
	
	private boolean calling = false;
	
	
	/**
	 * 
	* <p>Title: setSpeakerOn</p>
	* <p>Description: Set the output device mode (the Earpiece or Speaker) to play voice
	* </p>
	* @param speakerOn
	 */
	public void setSpeakerOn(boolean speakerOn) {
		
		
		if(mediaPlayer == null ) {
			mediaPlayer = new MediaPlayer();
		}
		
		if(calling) {
			// ������Ҫ�жϵ�ǰ��״̬�Ƿ�������ϵͳ�绰������߽�����
			
		} else {
			int currentPosition = mediaPlayer.getCurrentPosition();
			
			stop();
			
			setOnCompletionListener();
			setOnErrorListener();
			
			play(urlPath, !speakerOn, currentPosition);
		}
	}
	
	
	public boolean pause () {
		if(this.status != STATUS_PLAYING) {
			return false;
		}
		
		boolean result = false;
		
		try {
			
			mediaPlayer.pause();
			this.status = STATUS_PAUSE;
			result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			status = STATUS_ERROR;
		}
		
		return result;
	}
	
	
	public int getStatus() {
		return status;
	}
	
	public boolean isPlaying () {
		
		if(this.status == STATUS_PLAYING) {
			return true;
		}
		
		return false;
	}

	/**
	 * 
	* <p>Title: setOnCompletionListener</p>
	* <p>Description: Set the playback end of speech monitoring, 
	*    again will play the status is set to an initial state</p>
	*    @see MediaPlayTools#status
	*    {@link MediaPlayTools#STATUS_ERROR}
	*    {@link MediaPlayTools#STATUS_PLAYING}
	*    {@link MediaPlayTools#STATUS_PAUSE}
	*    {@link MediaPlayTools#STATUS_STOP}
	 */
	private void setOnCompletionListener() {
		
		// 
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				status = STATUS_STOP;
			}
		});
	}

	/**
	 * 
	* <p>Title: setOnErrorListener</p>
	* <p>Description: Set the language player initialization error correction</p>
	 */
	private void setOnErrorListener() {
		
		//
		mediaPlayer.setOnErrorListener(null);
	}
}
