package com.jason.jasonutils.enhance.beansdemo;

import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * 通过javaBeans来获取或者修改：给定类实例对象中的变量值；
 * ================================================================================
 * 包：java.beans
 * 类：java.beans.PropertyDescriptor：属性描述器
 * 
 * new PropertyDescriptor("x", od1.getClass())
 * 
 * getReadMethod() ：设置应该用于读取属性值的方法。
 * setWriteMethod()： 设置应该用于写入属性值的方法。
 * ================================================================================
 * 实现获取实例对象变量值的另一种方式：通过内省类来操作：java.beans.Introspector类，
 * 将给定的实例对象的对应的类看做JavaBean类，获取相应的Bean属性BeanInfo，再进行操作：
 * 
 * 通过类Introspector.getBeanInfo(od.getClass());得到BeanInfo对象beanInfo
 * 再获取属性描述数组：PropertyDescriptor [] pds = beanInfo.getPropertyDescriptors();
 * 然后对pds进行遍历和判断，寻找符合给定的变量；
 * 
 * 再通过反射进行获取操作；
 * Method methodGet = pd.getReadMethod();
 * value = methodGet.invoke(od);
 * return value;
 * =================================================================================
 * 通过加载jar包来扩展功能：
 * 
 * 1.导入jar包步骤：建立工程下lib-->复制jar包到lib-->右键jar包build path
 * commons-beanutils.jar
 * commons-logging.jar
 * 
 * 2.调用方法：
 * BeanUtils.getProperty(od1, "x")
 * BeanUtils.setProperty(od1, "x", 12);
 * 
 * 3.beanutils.jar的属性级联操作：birthday-->birthday.time
 * setProperty(od1,"birthday.time","121");
 * BeanUtils.getProperty(od1,"birthday.time")
 * 
 * 4.除BeanUtils类，还有：PropertyUtils工具类
 * PropertyUtils.setProperty(od1,"x",20);
 * PropertyUtils.getProperty(od1, "x"));
 * 
 * 5.BeanUtils和PropertyUtils的区别：
 * 前者操作字符串，后者操作数据类型本身的类型；
 * =================================================================================
 * javaBean的知识告一段路；
 */

public class JavaBeansDemo  {
	
	public static void main(String[] args) throws Exception {
		
		ObjectDemo od1= new ObjectDemo(8,9);	//需求：给一个某个类的实例对象，获取和修改该实例对象的参数值；
		
		System.out.println("==================获取、设置	x======================");
		
		System.out.println(getProperties(od1, "x"));	
		System.out.println(getProperties2(od1, "x"));
		
		setProperties(od1, "x", 8);	
		setProperties2(od1, "x",18);
		System.out.println(od1.getX());
						
		System.out.println("==================获取、设置 	y======================");	
		System.out.println(getProperties(od1, "y"));	
		setProperties(od1, "y", 10);		
		System.out.println(od1.getY());	
		
		System.out.println("=======通过加载BeanUtils包中的方法进行获取和设置操作=========");
		
		System.out.println(BeanUtils.getProperty(od1, "x"));//get方法
		BeanUtils.setProperty(od1, "x", 12);				//set方法
		System.out.println(od1.getX());
		
		BeanUtils.setProperty(od1,"birthday.time","121");	//属性的级联操作：birthday-->birthday.time
		System.out.println(BeanUtils.getProperty(od1,"birthday.time"));
		
		//小知识点：
		PropertyUtils.setProperty(od1,"x",20);
		System.out.println(PropertyUtils.getProperty(od1, "x"));
		
 		//BeanUtils和PropertyUtils的区别：前者操作字符串，后者操作数据类型本身的类型；
	}

	/**
	 * 自定义方法：通过反射，获取给定javabean实例的指定字段值
	 * @param od
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	private static Object getProperties(Object od, String propertyName)throws Exception  {
//		PropertyDescriptor pd = new PropertyDescriptor(propertyName, od.getClass());
//		Method methodGet = pd.getReadMethod();
//		Object retVal = methodGet.invoke(od);
		return null;
	}

	/**
	 * 自定义方法：通过反射，设置给定javabean实例指定的字段值
	 * @param od
	 * @param propertyName
	 * @param value
	 * @throws Exception
	 */
	private static void setProperties(Object od, String propertyName,Object value) throws Exception {
//		PropertyDescriptor pd = new PropertyDescriptor(propertyName, od.getClass());
//		Method methodSet = pd.getWriteMethod();
//		methodSet.invoke(od,value);
	}

	/**
	 * 自定义方法：通过内省的方法来获取给定javabean实例对象的指定字段值。
	 * @param od
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	private static Object getProperties2(Object od, String propertyName) throws Exception  {	
		
//		BeanInfo beanInfo = Introspector.getBeanInfo(od.getClass());
//		PropertyDescriptor [] pds = beanInfo.getPropertyDescriptors();
//		Object value = null ;
//
//		for(PropertyDescriptor pd:pds) {
//			if(pd.getName().equals(propertyName)) {
//				Method methodGet = pd.getReadMethod();
//				value = methodGet.invoke(od);
//				return value;
//			}
//		}
		return null;
	}
	
	/**
	 * 自定义方法：通过内省的方法来设定给定javabean实例对象的指定字段值。
	 * @param od
	 * @param propertyName
	 * @param value
	 * @throws Exception
	 */
	private static void setProperties2(Object od, String propertyName,int value) throws Exception  {
		
//		BeanInfo beanInfo = Introspector.getBeanInfo(od.getClass());
//		PropertyDescriptor [] pds = beanInfo.getPropertyDescriptors();
//
//		for(PropertyDescriptor pd:pds) {
//			if(pd.getName().equals(propertyName)) {
//				Method methodSet = pd.getWriteMethod();
//				methodSet.invoke(od,value);
//			}
//		}
	}	
}
