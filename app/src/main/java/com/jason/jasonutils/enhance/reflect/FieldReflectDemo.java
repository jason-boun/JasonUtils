package com.jason.jasonutils.enhance.reflect;

import java.lang.reflect.Field;

/**
 * 反射之Field；
 * ================================================================
 * 通过类字节码的getField方法获得的Field对象fieldX(或fieldY)；
 * fieldX(或fieldY)代表的是：PointReflect类的字段，而不是具体的变量值；
 * 
 * 对于public修饰的成员变量，通过反射得到值，需要方法：
 * getField()
 * 
 * 对于private修饰的成员变量，通过反射得到值，需要方法：
 * getDeclaredField()
 * setAccessible(true)
 *==================================================================
 * 反射中Field的应用：给一个实例对象，我就可以通过反射修改类中的成员信息；
 * changeFieldStringValue(Object obj)功能的实现：
 * 
 * 常用方法：
 * field.get(obj);
 * field.set(obj,newValue);
 * getType()：返回值类型为 Class<?>：表示成员变量类型字节码
 * ==================================================================
 */

public class FieldReflectDemo {
	
	public static void main(String[] args) throws Exception {
		ReflectPoint pt1= new ReflectPoint(3,5);
		ReflectPoint pt2= new ReflectPoint(8,9);
		
		Field fieldY = pt1.getClass().getField("y");//通过类的字节码的方法来获取字段，记住，得到的是变量，而不是具体变量值
		
		Field fieldX = pt1.getClass().getDeclaredField("x");//private变量
		fieldX.setAccessible(true);	//暴力反射；
		
		System.out.println(fieldY.get(pt1));//获取具体对象中对应的字段值
		System.out.println(fieldY.get(pt2));
		
		System.out.println(fieldX.get(pt1));
		System.out.println(fieldX.get(pt2));
		
		
		System.out.println("===============修改String成员中的某个字符================");
		
		System.out.println(pt1);
		changeFieldStringValue(pt1);
		System.out.println(pt1);
	
	}

	
	/**
	 * 反射的理解和复杂应用：反射中Field的应用：给我一个实例对象，我就可以通过反射修改类中的成员信息；
	 * String oldValue = (String)field.get(obj);
	 * field.set(obj, newValue);
	 * @param obj
	 * @throws Exception
	 */
	public static void changeFieldStringValue(Object obj) throws Exception {
		Field[] fields = obj.getClass().getFields();//重点在于给一个实例对象，可以获取到对应的类的字节码，从而获取到存有所有字段的数组。
		for(Field field:fields) {
			//必须也要public修饰ReflectPoint类中的String变量，才可以访问得到；
			if(field.getType()==String.class) {				
				String oldValue = (String)field.get(obj);
				String newValue = oldValue.replace('b','a');
				field.set(obj, newValue);	
			}
		}
	}
}
