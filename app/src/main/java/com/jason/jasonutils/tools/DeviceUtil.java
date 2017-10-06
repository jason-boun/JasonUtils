package com.jason.jasonutils.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtil {
	
	/**
	 * 获取屏幕分辨率参数
	 * @param ctx
	 * @return Point 
	 */
	@SuppressLint("NewApi")
	public static Point getDisplayMetrix1(Context ctx){
		Point point = new Point();
		Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		if(android.os.Build.VERSION.SDK_INT>10){
			display.getSize(point);
		}else{
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}

}
