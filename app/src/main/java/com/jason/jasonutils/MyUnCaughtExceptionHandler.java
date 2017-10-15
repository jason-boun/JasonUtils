package com.jason.jasonutils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.jason.jasonutils.tools.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 收集APK崩溃后的ErrorLog信息
 * 调用方式：Thread.currentThread().setUncaughtExceptionHandler(new MyUnCaughtExceptionHandler(this));
 */
public class MyUnCaughtExceptionHandler implements UncaughtExceptionHandler {
	
	private final Context ctx;
	private final String logFileName = "JasonUtils_ErrorLog.ini";

	public MyUnCaughtExceptionHandler(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			
			String errorLogInfo = sw.toString();
			
			StringBuilder sb = new StringBuilder();
			sb.append("================= "+ DateUtil.dateToString(new Date(), DateUtil.FORMAT_ONE) +" =================\n\n");//①时间信息
			
			Field[] fields = Build.class.getDeclaredFields();
			sb.append("设备信息：\n\n");
			for(Field field: fields){
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name +"=="+value);//②设备信息
				sb.append("\n");
			}
			sb.append("\n");
			sb.append("错误Log信息：\n\n");
			sb.append(errorLogInfo);//③错误日志信息
			sb.append("\n");
			sb.append("	---- Log End ----");
			sb.append("\n\n");
			
			File file = null;
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				file = new File(Environment.getExternalStorageDirectory(), logFileName);
			}else{
				file = new File(ctx.getFilesDir(), logFileName);
			}
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}

