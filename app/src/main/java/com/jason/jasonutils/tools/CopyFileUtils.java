package com.jason.jasonutils.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

public class CopyFileUtils {
	
	/**
	 * 拷贝资产目录下的文件
	 * @param context 上下文
	 * @param filename 文件名
	 * @param destPath 目标文件的路径
	 * @return 拷贝成功 返回文件对象 拷贝失败 返回null
	 */
	public  static File copyAssetsFile(Context context,String filename,String destPath){
		try {
			InputStream is = context.getAssets().open(filename);
			File file = new File(destPath);
			
			FileOutputStream fos = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len = is.read(buffer))!=-1){
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
