package com.jason.jasonutils.iostream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 将异常处理的信息以日志的形式，输出保存到指定目录文件里；
 * ===========================================================================================
 * 1.建立目标流文件：PrintStream ps = new PrintStream("E:\\IOFiles\\Exception.txt");
 * 2.自定义(设置)输出方式：System.setOut(ps);
 * 3.异常信息输出：e.printStackTrace(System.out);
 * ===========================================================================================
 * 可加入其他日志信息：
 * ps.println(time);
 * ===========================================================================================
 */
public class LogSave2FileDemo {
	
	public static void main(String[] args) throws IOException {
		saveExceptionInfo("c:/text/log.ini");
	}
	
	public static void saveExceptionInfo(String desFilePath) throws IOException{
		try{
			char[] ch = new char[2];
			System.out.println(ch[3]);//通过角标越界异常来模拟异常，以此来产生日志打印。
		}catch(Exception e){
			try{
				PrintStream ps = new PrintStream(desFilePath);
				ps.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//记录日期
				System.setOut(ps);//设置异常日志的打印存储路径
			}catch(IOException ioe){
				throw new RuntimeException("日志文件无法找到");
			}
			e.printStackTrace(System.out);
		}
	}
}
