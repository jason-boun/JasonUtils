package com.jason.jasonutils.tools;

import android.annotation.SuppressLint;
import android.os.Handler;

public abstract class MyAsyncTask {
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			onPostExecute();//收尾工作
		};
	};
	
	protected abstract void onPreExecute();
	protected abstract void doInBackgroud();
	protected abstract void onPostExecute();
	
	public void execute(){
		onPreExecute();//准备工作
		new Thread(){
			public void run() {
				doInBackgroud();//后台任务
				handler.sendEmptyMessage(0);//后台任务完成发送消息
			};
		}.start();
	}

}
