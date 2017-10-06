package com.jason.jasonutils.enhance.classloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ====================================================================================
 * 自定义类加载器：
 * 
 * 理解一个类和三个方法：
 * 超类：ClassLoader
 * 三个方法：
 * loadClass()：继承父类方法即可，无需覆写；
 * findClass()：覆写，
 * defineClass()：调用此方法，将class文件的二进制码传给此方法，此方法会返回一个class文件
 * ===================================================================================
 * 该类的本质：{@link # MyClassLoader}
 * 对本地的一个class文件通过读取流加载进来，加密处理，再通过写入流保存为另一文件；
 * 
 * 原文件：E:\Workspaces\jasonenhance\bin\ClassLoaderAttachment.class
 * 处理方式：加密：cypher(fis,fos);
 * 目标文件：E:\Workspaces\jasonenhance\jasonlib\ClassLoaderAttachment.class
 * 
 * 在IO流读取写入的过程中，涉及到了IO流如何获取路径及文件名的问题;
 * 读取流：main函数的第一个参数args[0]；为绝对路径
 * 写入流：通过截取读取流路径来获取文件名，通过参数args[1],获取路径；
 * 
 * ===================================================================================
 * 通过main函数传递参数：
 * 
 * 1.真正理解通过main传递参数是怎么回事？
 * 2.myEclipse软件中，怎么给一个有main函数的类传递函数；
 * 	选择该类-->Run As-->Run configuration--> (x)arguments:string1 string2
 * 3.对应主函数中本别为args[0]；args[1]
 * 
 * 结合IO流，文件怎么与IO流关联；
 * ===================================================================================
 */

public class CypherUtil {
	
	public static void main(String[] args) throws IOException {
		
		String srcPath = args[0];	//E:\Workspaces\jasonenhance\bin\ClassLoaderAttachment.class
		String destDir = args[1];	//jasonlib
		
		String destFileName = srcPath.substring(srcPath.lastIndexOf('\\')+1); //subString()方法；
		String destPath = destDir +"\\"+ destFileName;
		
		FileInputStream fis = new FileInputStream(srcPath);
		FileOutputStream fos = new FileOutputStream(destPath);
		
		cypher(fis,fos);//加密过程
		
		fis.close();
		fos.close();		
	}
	
	/**
	 * 加密代码
	 * @param ips
	 * @param ops
	 * @throws IOException
	 */
	public static void cypher(InputStream ips, OutputStream ops) throws IOException {
		int b = -1;
		while((b=ips.read())!=-1) {
			ops.write(b^255);
		}
		System.out.println("已加/解密");
	}
}
