package com.jason.jasonutils.enhance.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * 反射之Constructor： 构造器
 * ===============================================================================
 * 通过字节码来得到一个Constructor 类型的constructor对象，（参数为某个类的字节码）
 * 再用constructor对象来newInstance一个具体对象；（参数为某类构造函数的具体参数实例）
 * ===============================================================================
 * API中的位置：
 * 
 * lang的子包：java.lang.reflect 包
 * Constructor类的位置：java.lang.reflect.Constructor<T>
 * 
 * 常用方法：
 * newInstance(Object... initargs) 通过得到的构造函数调用此方法来创建实例对象；
 * getParameterTypes()：返回一个Class[]，为该构造函数的成员变量类型的列表；
 * getName()：获取该构造函数的名称，返回值为String类型；
 * getGenericParameterTypes()：返回值类型： Type[]，返回泛型参数类型列表；
 * ================================================================================
 * 构造函数类最常用的方法：newInstance();
 * ================================================================================
 */

public class ConstructorReflectDemo {
	public static void main(String[] args) throws Exception  {
		
		Constructor<String> constructor = String.class.getConstructor(StringBuffer.class);
		String str = (String)constructor.newInstance(new StringBuffer("abc"));
		System.out.println(str.charAt(2));
		
		String name = constructor.getName();
		System.out.println(name);
		
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		for(Class<?> clazz :parameterTypes){
			System.out.println("clazz == " + clazz.getSimpleName());
		}
		
		
		Type[] types = constructor.getGenericParameterTypes();
		for(Type tp: types){
			System.out.println("tp == " + tp.getClass().getSimpleName());
		}
	}
}
