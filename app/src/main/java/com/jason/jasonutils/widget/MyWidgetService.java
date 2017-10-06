package com.jason.jasonutils.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.StorageUtil;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class MyWidgetService extends Service {

	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm ;
	private String AUTO_KILL_ACTION = "com.jason.jasonutils.autokill";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		awm = AppWidgetManager.getInstance(getApplicationContext());
		if(timer == null){
			timer = new Timer();
		}
		if(task==null){
			task = new TimerTask() {
				@Override
				public void run() {
					String tip1 = "正在运行进程数：" + StorageUtil.getRunningProcessCount(getApplicationContext())+"个";
					String tip2 = "可用内存为：" + Formatter.formatFileSize(getApplicationContext(), StorageUtil.getAvailRam(getApplicationContext()));
					PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent().setAction(AUTO_KILL_ACTION), 0);
					
					RemoteViews views = new RemoteViews(getPackageName(), R.layout.my_process_widget_layout);
					views.setTextViewText(R.id.process_count, tip1);
					views.setTextViewText(R.id.process_memory, tip2);
					views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
					
					ComponentName provider = new ComponentName(getApplicationContext(), MyWidget.class);
					
					awm.updateAppWidget(provider, views);
				}
			};
		}
		timer.schedule(task, 3000, 3000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer !=null){
			timer.cancel();
			timer = null;
		}
		task = null;
	}

}
