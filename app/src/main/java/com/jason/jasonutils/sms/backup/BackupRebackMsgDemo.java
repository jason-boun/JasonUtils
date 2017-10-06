package com.jason.jasonutils.sms.backup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.sms.backup.SmsUtils.BackUpStatusListener;

public class BackupRebackMsgDemo extends Activity {
	
	private ProgressDialog pd;
	private TextView tv_progress;
	private int totalMsg = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup_reback_sms_operate);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
	}
	
	public void backup(View view){
		new AsyncTask<Void, Integer, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(BackupRebackMsgDemo.this);
				pd.setTitle("提醒:");
				pd.setMessage("正在备份短信");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				if(values[0]==totalMsg){
					tv_progress.setText("总计备份"+totalMsg+"条短信");
				}else{
					tv_progress.setText("正在备份:"+values[0]);
				}
				super.onProgressUpdate(values);
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					SmsUtils.backupSms(getApplicationContext(), new BackUpStatusListener() {
						@Override
						public void onProgress(int progress) {
							pd.setProgress(progress);
							//tv_progress.setText("正在备份:"+progress);
							//发布程序执行的进度
							publishProgress(progress);
						}
						
						@Override
						public void beforeBack(int max) {
							totalMsg = max;
							pd.setMax(max);
						}
					});
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pd.dismiss();
				if(result){
					Toast.makeText(getApplicationContext(), "备份成功", 1).show();
				}else{
					Toast.makeText(getApplicationContext(), "备份失败", 1).show();
				}
			}
		}.execute();
	}
	public void recovery(View view){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提醒:");
		builder.setMessage("是否清除旧的短信");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SmsUtils.deleteAllSms(getApplicationContext());
				Toast.makeText(getApplicationContext(), "重新初始化短信成功", 0).show();
				try {
					SmsUtils.restroeSms(getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					SmsUtils.restroeSms(getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		builder.show();
	}

}
