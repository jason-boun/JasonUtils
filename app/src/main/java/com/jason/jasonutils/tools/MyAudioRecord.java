package com.jason.jasonutils.tools;

import java.io.File;

import android.content.Context;
import android.media.MediaRecorder;

public class MyAudioRecord {
	private int tickMillSeconds = 100;

	private MediaRecorder recorder = null; // 录音变量
	private boolean recording = false;
	private File f;// 文件变量
	private long record_start_time;// 记录录音开始时间
	private long record_end_time;// 记录录音结束时间
	private ProcessListener listener = null;
	
	public interface ProcessListener {
		public void onProgress(int maxAmplitude, long millSeconds);
	}

	public MyAudioRecord(Context c) {
		recording = false;
//		AudioManager audio = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
//		int current = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
//		audio.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_PLAY_SOUND);

	}

	/**
	 * @return 毫秒
	 */
	public long getStartTime() {
		return record_start_time;
	}

	/**
	 * @return 毫秒
	 */
	public long getEndTime() {
		return record_end_time;
	}

	public void release() {
		recording = false;
		if (recorder != null) {
			recorder.release();
			recorder = null;
		}
	}

	public void setRecordListener(int tickMillSeconds, ProcessListener listener) {
		this.tickMillSeconds = tickMillSeconds;
		this.listener = listener;
	}

	private int getMaxAmplitude() {
		if (recorder != null) {
			int am = recorder.getMaxAmplitude();
			MLog.i("AM:" + am);
			return am;
		}
		return 0;
	}

	public File getAudioFile() {
		MLog.i("get Audio File: " + f.getAbsolutePath());
		return f;
	}

	private Runnable tick = new Runnable() {

		@Override
		public void run() {
			if (recording) {
				if (listener != null) {
					listener.onProgress(getMaxAmplitude(), System.currentTimeMillis() - record_start_time);
				}
			}
			if (recording) {
				ThreadUtil.getUIHandler().postDelayed(tick, tickMillSeconds);
			}
		}
	};

	public boolean begin(File file) {
		if (recording) {
			MLog.e("already recoding");
			return false;
		}
		recorder = new MediaRecorder();
		MLog.i("begin recording...");
		f = file;
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);//RAW_AMR 设置MediaRecorder录制的音频格式
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 设置音频编码Encoder
		recorder.setOutputFile(f.getAbsolutePath());
		try {
			recorder.prepare();// 准备录制
			recorder.start();// 开始录制

			recording = true;
			record_start_time = System.currentTimeMillis();
			ThreadUtil.getUIHandler().post(tick);
			return true;
		} catch (Exception e) {
			MLog.e(e);
			release();
		}
		return false;
	}

	public boolean end() {
		MLog.i("end recording...");
		if (!recording) {
			MLog.e("no not recording!!");
			return false;
		}
		stop();
		return true;
	}

	public void cancel() {
		MLog.i("cancel recording...");
		stop();
		if (f.exists()) {
			f.delete();
		}
	}

	private void stop() {
		record_end_time = System.currentTimeMillis();
		recorder.stop();
		recorder.reset();
		release();
	}
	public boolean isRecording() {
		return recording;
	}
}
