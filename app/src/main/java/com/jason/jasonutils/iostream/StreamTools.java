package com.jason.jasonutils.iostream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class StreamTools {
	
	/**
	 * 读取流数据，返回字符串
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String read4Stream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer))!=-1){
			baos.write(buffer, 0, len);
			baos.flush();
		}
		is.close();
		String result = baos.toString();
		baos.close();
		return result;
	}
	
	/**
	 * 将图片文件变为字节码
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readPic2byte(String filePath)throws IOException{
		File file = new File(filePath);
		if(file.exists()){
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = fis.read(buffer))!=-1){
				baos.write(buffer, 0, len);
				baos.flush();
			}
			fis.close();
			return baos.toByteArray();
		}
		return null;
	}
	
	/**
	 * 删除目录及其子文件
	 * @param fileDir
	 */
	public static void deleteDir(File fileDir){
		if(fileDir.exists()){
			if(fileDir.isFile()){
				fileDir.delete();
			}else {
				File[] files = fileDir.listFiles();
				if(files!=null && files.length>0){
					for(File f: files){
						deleteDir(f);
					}
				}
				fileDir.delete();
			}
		}
	}
	
	/**
	 * 保存数据到ROM.
	 * @param context 
	 * @param info 
	 * @throws Exception
	 */
	public static void save2Rom(Context context, String info) throws Exception{
		File file = new File(context.getFilesDir(),"info.txt");//文件："/data/data/com.jason.login/files/info.txt"
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(info.getBytes());
		fos.flush();
		fos.close();
	}
	/**
	 * 从ROM读取数据
	 * @param context
	 * @return
	 */
	public static String read4Rom(Context context) throws Exception{
		File file = new File(context.getFilesDir(),"info.txt");
		FileInputStream fis = new FileInputStream(file);
		String result = read4Stream(fis);
		return result;
	}
	
	/**
	 * 保存数据到SD卡
	 * @param info 
	 * @throws Exception
	 */
	public static void save2SD(String info) throws Exception{
		File file = new File(Environment.getExternalStorageDirectory(),"info.txt");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(info.getBytes());
		fos.flush();
		fos.close();
	}
	
	/**
	 * 保存数据到SP
	 * @param context 
	 * @param name info
	 */
	public static void save2SP (Context context, String info){
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString("info", info).commit();
	}
	
	/**
	 * 读取SP数据
	 * @param context
	 * @return
	 */
	public static Map<?, ?> read4SP(Context context){
		return context.getSharedPreferences("config", Context.MODE_PRIVATE).getAll();
	}
	
}
