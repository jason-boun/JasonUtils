package com.jason.jasonutils.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class StorageUtil {
	
	/**
	 * 获取当前ROM可用大小
	 * @param ctx
	 * @return
	 */
	public static String getAvailableROM(Context ctx){
		File file = Environment.getDataDirectory();
		StatFs fs = new StatFs(file.getAbsolutePath());
		long size = fs.getAvailableBlocks()*fs.getBlockSize();
		return Formatter.formatFileSize(ctx, size);
		
	}
	
	/**
	 * 获取当前SD可用大小
	 * @param ctx
	 * @return
	 */
	public static String getAvailabeSD(Context ctx){
		File file = Environment.getExternalStorageDirectory();
		StatFs fs = new StatFs(file.getAbsolutePath());
		long size = fs.getAvailableBlocks()*fs.getBlockSize();
		return Formatter.formatFileSize(ctx, size);
	}
	
	
	/**
	 * 得到手机正在运行的进程的个数
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
		ActivityManager  am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	/**
	 * 获取手机的可用内存
	 * @param context
	 * @return
	 */
	public static long getAvailRam(Context context){
		ActivityManager  am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	/**
	 * 获取手机的总内存
	 * @param context
	 * @return
	 */
	public static long getTotalRam(Context context){
		//配置文件路径： /proc/meminfo
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String memStr  = reader.readLine();
			//MemTotal:         516452 kB
			char[] arr = memStr.toCharArray();
			StringBuffer sb = new StringBuffer();
			for(char c : arr){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			return Integer.parseInt(sb.toString())*1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

}
