package com.jason.jasonutils.processmanager;

import java.util.ArrayList;
import java.util.List;

import com.jason.jasonutils.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ProcessInfoUtil {
	/**
	 * 获取系统进程的列表.
	 * @param context
	 * @return
	 */
	public static List<ProcessInfoBean> getTaskInfos(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runingAppProcessInfos = am.getRunningAppProcesses();
		List<ProcessInfoBean> processInfos = new ArrayList<ProcessInfoBean>();
		for (RunningAppProcessInfo runningAppProcessInfo : runingAppProcessInfos) {
			ProcessInfoBean taskInfo = new ProcessInfoBean();
			String packname = runningAppProcessInfo.processName;
			taskInfo.setPackName(packname);
			try {
				PackageManager pm = context.getPackageManager();
				PackageInfo info = pm.getPackageInfo(packname, 0);
				taskInfo.setAppIcon(info.applicationInfo.loadIcon(pm));
				taskInfo.setAppName(info.applicationInfo.loadLabel(pm).toString());
				if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					taskInfo.setUserTask(true);// 用户进程
				} else {
					taskInfo.setUserTask(false);// 系统进程
				}
			} catch (NameNotFoundException e) {
				taskInfo.setAppIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				taskInfo.setAppName(packname);
				e.printStackTrace();
			}
			long meminfo = am.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid })[0].getTotalPrivateDirty() * 1024;
			taskInfo.setMensize(meminfo);
			processInfos.add(taskInfo);
		}
		return processInfos;

	}}
