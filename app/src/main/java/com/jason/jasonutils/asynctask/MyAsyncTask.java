package com.jason.jasonutils.asynctask;

import android.os.Handler;
import android.os.Message;

/**
 * 自定义异步任务类.
 */
public abstract class MyAsyncTask {
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			onPostExecute();
		};
	};

	protected abstract void onPreExectue();
	protected abstract void doInbackgroud();
	protected abstract void onPostExecute();
	
	public void execute(){
		onPreExectue();
		new Thread(){
			public void run() {
				doInbackgroud();
				Message msg = new Message();
				handler.sendMessage(msg);
			};
		}.start();
	}
}

