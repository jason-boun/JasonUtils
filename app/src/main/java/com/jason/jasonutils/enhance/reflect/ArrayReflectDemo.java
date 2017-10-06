package com.jason.jasonutils.enhance.reflect;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 反射之数组：
 * =============================================================
 * java.lang.reflect 这个包下：
 * 
 * 反射在数组中的应用：
 * 1.Class class = obj.getClass();if(class.isArray){}else{}
 * 2.java.lang.reflect.Array，该类中的所有方法都为静态；
 * 
 * 获取长度：Array.getLength(array);
 * 获取元素：Array.get(array,index);
 * ==============================================================
 * 数组的数据类型和维数相同，则对应的字节码相同；
 * 
 * 一维基本类型的数组，可看做一个Object的对象obj；
 * 其他的非基本数据类型，可看做一个Object的对象obj，比如，String；
 * ==============================================================
 * 强烈备注：
 * 之所以会有这样的一个类：java.lang.reflect.Array，该类中的所有方法都为静态；
 * 是因为当一个对象还没具体给定的时候，你还必须搞定关于这个对象的处理方法(或称函数)，
 * 这个时候必须用反射的手段来处理：Class(通过字节码)去判断isArray，然后用java.lang.reflect.Array的来调用关于处理数组的方法。
 * ==============================================================
 */

public class ArrayReflectDemo {
	
	public static void main(String[] args) {
		
		int[] a1 = new int [] {3,4,5};
		int[] a2 = new int [4];
		int[][] a3 = new int [2][3];
		String[] a4 = new String[]{"a","b","c"};
		
		System.out.println(a1.getClass() == a2.getClass());
		
		System.out.println(a1.getClass().getName());
		System.out.println(a3.getClass().getName());
		System.out.println(a4.getClass().getName());
		
		System.out.println(a1.getClass().getSuperclass().getName());
		
		System.out.println("===========数组，Object，基本数据类型================");
		
		Object obj1 = a1;
		Object obj2 = a4;
		//Object[] obj3 = a1;
		Object[] obj3 = a3;
		Object[] obj4 = a4;
		
		//System.out.println(a1);
		//System.out.println(a4);
		
		System.out.println(Arrays.asList(a1));	//List中的基本类型数组：[[I@1bab50a]
		System.out.println(Arrays.asList(a4));
		
		printObject(a1);
		printObject("abc");
	}
	
	//反射在数组中的应用：java.lang.reflect.Array
	//java.lang.reflect.Array，该类中的所有方法都为静态；
	//Class类中的判定方法：isArray();
	
	public static void printObject(Object obj) {
		
		Class clazz = obj.getClass();
		if(clazz.isArray()) {
			int n = Array.getLength(obj);
			for(int i=0;i<n;i++) {
				System.out.println(Array.get(obj, i));
				System.out.println((Array.get(obj, i)).getClass());
			}
		}
		else {
			System.out.println(obj);
			System.out.println(obj.getClass()); 
		}
	}
}
