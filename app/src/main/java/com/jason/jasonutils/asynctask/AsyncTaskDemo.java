package com.jason.jasonutils.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.SystemClock;
import com.jason.jasonutils.tools.MLog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class AsyncTaskDemo extends Activity {

	private ProgressBar mProgressBar;
	private TextView mTextView;
	private ProgressAsyncTask mProgressAsyncTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.asynctask_demo_activity);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mTextView = (TextView) findViewById(R.id.textView1);
		mProgressAsyncTask = new ProgressAsyncTask();
	}
	
	/**
	 * 点击事件
	 * @param view
	 */
	public void onClick_Start(View view){
		if(mProgressAsyncTask!=null && Status.PENDING.equals(mProgressAsyncTask.getStatus())){
			mProgressBar.setProgress(0);
			mTextView.setText(0+"%");
			mProgressAsyncTask.execute("str1","str2","str3","str4","str5");
		}
	}
	
	/**
	 * 点击事件
	 * @param view
	 */
	public void onClick_Cancel(View view){
		mProgressAsyncTask.cancel(true);
	}
	
	/**
	 * 自定义异步任务类
	 */
	private class ProgressAsyncTask extends AsyncTask<String, Integer, Integer>{
		
		private int mCount;
		
		@Override
		protected Integer doInBackground(String... params) {
			mCount = params.length;
			mProgressBar.setMax(mCount);
			for(int i=0;i<mCount;i++){
				publishProgress(i+1);
				if(isCancelled()){
					break;
				}
				SystemClock.sleep(2000);
			}
			return params.length;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Toast.makeText(getApplicationContext(), "处理完成，总计处理了"+result+"个事件！", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressBar.setProgress(values[0]);
			mTextView.setText(100*values[0]/mCount+"%");
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			Toast.makeText(AsyncTaskDemo.this, "任务已取消", Toast.LENGTH_SHORT).show();
			mProgressBar.setProgress(0);
			mTextView.setText(0+"%");
			super.onCancelled();
		}
	}
	
}
