package com.jason.jasonutils.phone;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

public class ComingCallService extends Service {

	private TelephonyManager tm ;
	private MyPhoneStateListener mPhoneStateListener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mPhoneStateListener = new MyPhoneStateListener();
		tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE://挂断
				
				break;
			case TelephonyManager.CALL_STATE_RINGING://响铃
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://接通
				
				break;
			}
		}
	}
	
	/**
	 * 挂断电话
	 * @param incomingNumber
	 */
	public void endCall(String incomingNumber) {
		try {
			Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", new Class[] { String.class });
			// IBinder b = ServiceManager.getService(TELEPHONY_SERVICE);
			IBinder b = (IBinder) method.invoke(null, new String[] { TELEPHONY_SERVICE });
			ITelephony iTeletphony = ITelephony.Stub.asInterface(b);//进程间通信：获取接口
			iTeletphony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始录音
	 * @param recorder
	 * @throws Exception
	 */
	public void audioRecord(MediaRecorder recorder) throws Exception{
		if(recorder==null){
			recorder = new MediaRecorder();
		}
		// 设置录制的音频源 从话筒里面获取声音
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(Environment.getExternalStorageDirectory() + "/"+System.currentTimeMillis() + ".3gp");
		recorder.prepare();
		recorder.start();
	}
	/**
	 * 停止录音
	 * @param recorder
	 */
	public void stopRecord(MediaRecorder recorder){
		if (recorder != null) {
			recorder.stop();
			recorder.reset(); 
			recorder.release();
			recorder = null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		mPhoneStateListener = null;
		tm = null;
	}

}
