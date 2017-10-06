package com.jason.jasonutils.iostream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileInputStreamOutputSreamUtil {

	/**
	 * 读写字节流
	 * @param srcFileName
	 * @param desFileName
	 * @throws IOException
	 */
	public static void StremInputOutputMethod(String srcFileName,String desFileName) throws IOException{
		FileInputStream fis = new FileInputStream(srcFileName);
		FileOutputStream fos = new FileOutputStream(desFileName);
		
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = fis.read(buffer))!=-1){
			fos.write(buffer, 0, len);
		}
		fis.close();
		fos.close();
		//读取小文件，定义缓冲字节数组大小的时候，可用fis.available();方法来定义
	}
	/**
	 * 用缓冲流读取字节流，提高效率
	 * @param srcFileName
	 * @param desFileName
	 * @throws IOException
	 */
	public static void BufferedStreamMethod(String srcFileName,String desFileName) throws IOException{
		BufferedInputStream buffis = new BufferedInputStream(new FileInputStream(srcFileName));
		BufferedOutputStream buffos = new BufferedOutputStream(new FileOutputStream(desFileName));
		int len = 0;
		while((len = buffis.read())!=-1){
			buffos.write(len);
		}
		buffis.close();
		buffos.close();
	}
	
	
	/**
	 * 自定义缓存字节读取流
	 */
	public class MyBufferedInputStream {
		
		private InputStream is;
		public MyBufferedInputStream(InputStream is){
			this.is = is;
		}
		
		private byte[] buffer = new byte[1024*4];//定义缓冲区数组
		private int pos,count;//数组指针，返回标记计数器
		
		/**
		 * 关键方法
		 * @return
		 * @throws IOException
		 */
		public int myRead() throws IOException{
			if(count == 0){
				count = is.read(buffer);//第一次将读取到的数据写入缓冲区中。
				pos=0;
			}
			if(count>0){
				byte b = buffer[pos];//如果有数据，则每次都从缓存中读取字节，返回给调用者
				count--;
				pos++;
				return b& 255;// b&255 可保证byte型的b提升int型后的返回值保持原来的值。
			}
			return -1;
		}
		
		public void myClose() throws IOException{
			is.close();
		}
	}
}
