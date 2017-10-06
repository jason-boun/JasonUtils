package com.jason.jasonutils.splash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownLoadManager {
	/**
	 * 下载一个文件的方法
	 * @param path 服务器文件路径
	 * @param savedPath 保存文件的路径
	 *@param pd 进度条对话框
	 * @return
	 * @throws Exception 
	 */
	public static File downLoad(String path, String savedPath,ProgressDialog pd) throws Exception {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();
			if(code ==200){
				File file = new File(savedPath);
				int max = conn.getContentLength();
				pd.setMax(max);
				FileOutputStream fos = new FileOutputStream(file);
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len= 0;
				int total = 0;
				while((len = is.read(buffer))!=-1){
					Thread.sleep(10);
					fos.write(buffer, 0, len);
					total+=len;
					pd.setProgress(total);
				}
				is.close();
				fos.close();
				return file;
			}else{
				return null;
			}
		} else {
			throw new IllegalAccessException("sd卡不可用");
		}
	}
}
