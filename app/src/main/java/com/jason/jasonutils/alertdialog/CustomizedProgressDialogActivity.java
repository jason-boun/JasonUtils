package com.jason.jasonutils.alertdialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MLog;

public class CustomizedProgressDialogActivity extends Activity {
	
	private static final String TAG = "ProgressDialogDemo"; 
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_method_demo);
		
		progressDialog = ProgressDialog.show(this, "", "", true, true, new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Toast.makeText(CustomizedProgressDialogActivity.this, "被取消了", Toast.LENGTH_SHORT).show();
			}
		});
		progressDialog.setContentView(View.inflate(this, R.layout.progress_dialog_loading_layout, null));
		progressDialog.setCancelable(false);//设置为true则点击返回键可以取消该进度条
		progressDialog.setCanceledOnTouchOutside(false);
		timerStop(progressDialog);//定时取消该进度条
	}
	
	/**
	 * 处理返回键点击事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ){
			MLog.i(TAG, "返回键点击1");
			return super.onKeyDown(keyCode, event);
		}else{
			MLog.i(TAG, "其他键盘被点击（比如menu键）");
			return true;
		}
	}
	
	/**
	 * 计时器
	 */
	public void timerStop(final ProgressDialog progressDialog){
		Timer time = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(progressDialog!=null && progressDialog.isShowing()){
					progressDialog.dismiss();
				}
			}
		};
		time.schedule(task, 5000);
	}

}
