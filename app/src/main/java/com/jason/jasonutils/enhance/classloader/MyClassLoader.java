package com.jason.jasonutils.enhance.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Date;

/**
 * 真正的自定义类加载器：实现原理与过程
 * =================================================================
 * 自定义类加载器：
 * 继承ClassLoader
 * 构造函数(包括有参数和无参数)
 * 覆写findClass()方法
 * 
 * 备注：覆写findClass()的最终目的：
 * 是通过IO流读取到指定.class文件，变为字节数组传给defineClass()方法，
 * 该方法会返回一个字节码文件；通过return将该返回值返回到内存中。 
 * =================================================================
 * 覆写findClass的方法的实质：通过该方法实现加载指定的.class文件到内存；
 * 
 * 具体过程分析如下：
 * 
 * 1.通过读取流关联要加载的".class"类文件；并创建输出流；
 * 2.读取指定类文件后：调用解密方法进行解密：MyCyPher.cypher(fis,bos);
 * 3.通过输出流，将解密后的类文件变为字节数组：
 * 		byte[] bytes = bos.toByteArray();
 * 4.将该字节数组传递给defineClass()方法；
 * 		defineClass()会将返回最终的二进制字节码文件return给内存中；
 * 最终实现了类的加载。
 * =================================================================
 * 类加载器的知识到此告一段落。
 */

public class MyClassLoader extends ClassLoader {
	
	/**
	 * 需要load进内存中的class文件所在的文件夹路径
	 */
	private String classDir;
	
	/**
	 * 构造函数
	 */
	MyClassLoader(){ }//构造
	
	MyClassLoader(String classDir){//带参数的构造
		this.classDir = classDir;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String classFileName  = classDir + "\\" + name;
		try {
			FileInputStream fis = new FileInputStream(classFileName);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			CypherUtil.cypher(fis,bos);//解密
			fis.close();
			byte[] bytes = bos.toByteArray();
			
			Class<?> myClazz = defineClass(null, bytes, 0, bytes.length);//创建对应的字节码
			return myClazz;
		}catch(Exception e){
			e.printStackTrace();
		}				
		return super.findClass(name);
	}
	
	/**
	 * 程序入口
	 * @param args
	 * @throws Exception
	 */
	public static void main(String []args)throws Exception {
		//以下是演示调用自定义类的代码：
		Class<?> clazz = new MyClassLoader("jasonlib").loadClass("ClassLoaderAttachment.class");
		Date d1 = (Date)clazz.newInstance();
		System.out.println(d1);
	}
	
}
