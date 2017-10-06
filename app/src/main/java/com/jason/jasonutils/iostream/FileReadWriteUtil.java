package com.jason.jasonutils.iostream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class FileReadWriteUtil {
	
	/**
	 * FileReader和FileWriter单个字符读写
	 * @param srcFileName
	 * @param desFileName
	 * @throws IOException
	 */
	public static void readWriteMethod1(String srcFileName,String desFileName) throws IOException {
		Reader rd = new FileReader(srcFileName);
		Writer wr = new FileWriter(desFileName);
		int ch=0;
		while((ch=rd.read())!=-1){
			wr.write(ch);
			wr.flush();
		}
		rd.close();
		wr.close();
	}
	/**
	 * FileReader和FileWriter通过字符数组缓存来读写
	 * @param srcFileName
	 * @param desFileName
	 * @throws IOException
	 */
	public static void readWriteMethod2(String srcFileName,String desFileName) throws IOException{
		Reader rd = new FileReader(srcFileName);
		Writer wr = new FileWriter(desFileName);
		char[] buff = new char[1024];
		int len = 0;
		while((len=rd.read(buff))!=-1){
			wr.write(buff, 0, len);
			wr.flush();
		}
		rd.close();
		wr.close();
	}
	
	/**
	 * FileWriter第二个参数：是否通过追加写入
	 * @param content
	 * @param desFileName
	 * @throws IOException
	 */
	public static void writeMethod(String content, String desFileName) throws IOException{
		Writer wr = new FileWriter(desFileName,true);//向目标文件进行内容追加写入操作
		wr.write(content);
		wr.flush();
		wr.close();
	}

}
