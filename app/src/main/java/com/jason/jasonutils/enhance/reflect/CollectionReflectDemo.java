package com.jason.jasonutils.enhance.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

/**
 * 重点类：Properties及其方法，以及加载配置文件的三种方法
 * 
 * 反射的作用：建立框架
 * =============================================================================
 * 框架的概念：对比：房子、门、锁；来理解三者的关系：框架，开发的类，工具类；
 * 
 * 建立框架的核心：
 * 写框架时无法知道被调用的类名，所以在写的时候无法直接new一个对象来直接调用类的成员；
 * 所以必须要用反射的方式来完成框架。
 * 
 * 通过用反射的方式来建立集合的方式说明：反射在这个小框架中的作用；
 * =============================================================================
 * 填写和调用properties文件，
 * 
 * Properties类的特点：HashTable的子类
 * 其方法：
 * getProperty();
 * load();
 * store();
 * =============================================================================
 * 关于配置文件的放置路径：
 * 
 * 只要放在myEclipse的src文件夹下，
 * myEclipse会自动将非.java文件复制一份到编译后的运行文件(.class)目录下；
 * 
 * 这时就要注意读取流：有三种方式进行读取流的配置：
 * 
 * 第一种方式：直接让读取流与文件关联，这样可对该文件进行写入操作；
 * new FileInputStream("config.properties");
 * 
 * 第二种方式：本类的类加载器：
 * CollectionReflectDemo.class.getClassLoader().getResourceAsStream("cn\\jasonenhance\\reflect\\config.properties");
 * 
 * 第三种方式：
 * 直接通过字节码的方法加载，且只需给定要加载的文件名，无需配置路径；
 * CollectionReflectDemo.class.getResourceAsStream("config.properties");
 * =============================================================================
 * 基础加强对于反射的知识告一段路。
 */

public class CollectionReflectDemo  {
	
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//配置文件的加载读取流方式有三种：
		
		//第一种：
		//InputStream in = new FileInputStream("config.properties");	//读取流关联配置文件:config.properties
		
		//第二种：类加载器：
		//InputStream in = CollectionReflectDemo.class.getClassLoader().getResourceAsStream("cn\\jasonenhance\\reflect\\config.properties");		
		
		//第三种：直接通过本类的Class的方法就可以加载，而且只要文件名，无需配置路径；
		InputStream in = CollectionReflectDemo.class.getResourceAsStream("config.properties");
				
		Properties prop = new Properties();
		prop.load(in);	//载入配置文件信息
		in.close();
		
		String className = prop.getProperty("className");
		
		Collection collection = (Collection)Class.forName(className).newInstance();//得到一个实例对象
		
		//Collection collection = new HashSet();
		
		ReflectPoint pt6 = new ReflectPoint(8,9);
		ReflectPoint pt7 = new ReflectPoint(7,5);
		ReflectPoint pt8 = new ReflectPoint(8,9);
		
		collection.add(pt6);
		collection.add(pt7);
		collection.add(pt8);
		collection.add(pt6);
		
		pt6.y = 7;
		collection.remove(pt6);
		
		System.out.println(collection.size());
	}
}
