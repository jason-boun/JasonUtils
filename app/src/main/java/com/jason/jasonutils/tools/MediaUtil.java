package com.jason.jasonutils.tools;

import java.io.File;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

import com.jason.jasonutils.R;

public class MediaUtil {

	/**
	 * 图片缩放所需的参数Option
	 * @param act
	 * @param fileUri
	 * @return
	 */
	public static BitmapFactory.Options getOption (Activity act,Uri fileUri){
		int wmWidth = act.getWindowManager().getDefaultDisplay().getWidth();
		int wmHeight = act.getWindowManager().getDefaultDisplay().getHeight();
		String path = fileUri.getPath();
		Options option = new Options();
		option.inJustDecodeBounds = true;
		if(new File(path).exists()){
			BitmapFactory.decodeFile(path, option);
		}else {
			BitmapFactory.decodeResource(act.getResources(), R.drawable.fixpicture, option);
		}
		int bitmapWith = option.outWidth;
		int bitmapHeight = option.outHeight;
		
		if(bitmapWith>wmWidth || bitmapHeight>wmHeight){
			int xScale = bitmapWith/wmWidth;
			int yScale = bitmapHeight / wmHeight;
			if(xScale>yScale){
				option.inSampleSize = xScale;
			}else{
				option.inSampleSize = yScale;
			}
		}else{
			option.inSampleSize = 1;
		}
		option.inJustDecodeBounds = false;
		return option;
	}
}
