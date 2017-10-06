package com.jason.jasonutils.widget;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoKillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
		if(processes!=null && processes.size()>0){
			for(RunningAppProcessInfo porcessinfo: processes){
				am.killBackgroundProcesses(porcessinfo.processName);//processName和packageName一样
			}
		}
	}

}
