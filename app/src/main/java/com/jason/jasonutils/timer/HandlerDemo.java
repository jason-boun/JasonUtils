package com.jason.jasonutils.timer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class HandlerDemo extends Activity implements OnClickListener, Runnable{
	
	private Button btnStart, btnStop, btnShowToast ;
	private TextView tvCount;
	private Handler handler;
	private int count = 0;
	private ToastRun run ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handler_demo_activity);
		init();
	}

	private void init() {
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnShowToast = (Button) findViewById(R.id.btnShowToast);
		tvCount = (TextView) findViewById(R.id.tvCount);
		
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnShowToast.setOnClickListener(this);
		
		handler = new Handler();
		run = new ToastRun(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnStart:
				handler.postDelayed(run, 5000);
				break;
			case R.id.btnStop:
				handler.removeCallbacks(this);
				handler.removeCallbacks(run);
				break;
			case R.id.btnShowToast:
				handler.postAtTime(run, 5000);
				break;
		}
	}
	
	class ToastRun implements Runnable{
		private Context mContext;
		public ToastRun(Context context){
			this.mContext = context;
		}
		@Override
		public void run() {
			Toast.makeText(mContext, "显示Toast提示信息", Toast.LENGTH_SHORT).show();
			handler.postAtTime(HandlerDemo.this, 5000);
		}
	}

	//主线程的Run方法
	@Override
	public void run() {
		Toast.makeText(this, "主线程run方法运行", Toast.LENGTH_SHORT).show();
		tvCount.setText("Count：" + String.valueOf(++ count));
		handler.postDelayed(run, 5000);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		handler.removeCallbacks(this);
		handler.removeCallbacks(run);
		handler = null;
	}

}
