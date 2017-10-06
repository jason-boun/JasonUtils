package com.jason.jasonutils.iostream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

public class Stream2ReaderWriterUtil {
	
	/**
	 * 转换流操作：
	 * 将输入的字节流InputStream 通过 转换流InputStreamReader 转换为字符流Reader；
	 * 再用缓冲区BufferedReader进行按行读取；
	 */
	public static void keyInputReader() throws IOException{
		BufferedReader buffr = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		String line = null;
		while((line=buffr.readLine())!=null){
			buffw.write(line.toUpperCase());
			buffw.newLine();
			buffw.flush();
			if("over".equals(line)){
				break;
			}
		}
		buffw.close();
		buffr.close();
	}
	
	/**
	 * 改变和设置默认的输入/输出设备：
	 * @throws IOException
	 */
	public static void keyInputReader2(String srcFileName,String desFileName) throws IOException{
		
		//设置初读取和写入流
		System.setIn(new FileInputStream(srcFileName));
		System.setOut(new PrintStream(desFileName));//注意：PrintStream类继承自FileOutputStream类
		
		BufferedReader buffr = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		String line = null;
		while((line=buffr.readLine())!=null){
			buffw.write(line.toUpperCase());
			buffw.newLine();
			buffw.flush();
			if("over".equals(line)){
				break;
			}
		}
		buffw.close();
		buffr.close();
	}
	
	
}
