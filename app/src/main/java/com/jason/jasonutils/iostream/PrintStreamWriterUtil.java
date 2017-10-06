package com.jason.jasonutils.iostream;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * printStream；printWriter；
 * ============================================================
 * 字节打印流：printStream ：
 * 
 * 构造函数可以接收的参数类型：
 * 1.file对象；
 * 2.字符串路径：String
 * 3.字节输出流：OutputStrem
 * =============================================================
 * 字符打印流：printWriter：
 * 
 * 构造函数可以接收的参数类型：
 * 1.file对象
 * 2.字符串路径：String
 * 3.字节输出流：OutputStrem
 * 4.字符输出流：Writer(重要)
 * ============================================================
 * new PrintWriter(new FileWriter(),true);
 * 如果为true，则pw.println()自动刷新。
 * ============================================================
 */
public class PrintStreamWriterUtil {
	
	
	public static void main(String[] args) throws IOException {
		PrintDemo("c:/text.txt");
	}

	public static void PrintDemo(String desFile) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new FileWriter(desFile),true);
		
		String line = null;
		while((line=br.readLine())!=null){
			if("over".equals(line)){
				break;
			}
			pw.println(line.toUpperCase());
		}
		br.close();
		pw.close();
	}
	
}
