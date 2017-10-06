package com.jason.jasonutils.enhance.generic;

import java.lang.reflect.Array;
import java.util.Vector;

/**
 * 自定义泛型：
 * =======================================================================
 * 1.泛型的类型推断：取两变量类型的最近父类(相当于最小公倍数)
 * 如：Integer，和Float -->Number
 * 
 * 2.注意基本数据类型在泛型中不能被泛型所识别，必须转换成相应的包装类：
 * 如：int 必须转为 Integer
 * 
 * 3.普通方法、静态方法、自定义方法都可以定义泛型；
 * 
 * 备注：
 * 编译器不允许创建类型变量的数组：
 * 即在创建数组实例时，数组的元素不能使用参数化的类型，例如下面语句有错：
 * Vector<Integer> v [] = new Vector<Integer>[10];
 * 注意，是创建数组实例时，是实例时，不能参数化！上代码可以写为：
 * Vector<Integer> v [] = new Vector[10]; 
 * 
 * 4.异常处理情况：
 * 可以抛出泛型异常<T>，但是在处理异常时catch(T e),必须指明要处理那个具体异常；
 *
 * public <T extends Exception> void add throws T
 * try{}catch(Exception e){throws T;}
 * 
 * 5.泛型中多个参数之间用逗号隔开；
 * 如：Map<K,V>
 * ========================================================================
 */

public class DefinedGeneric {
	
	public static void main(String[] args) {
		
		//类型推断：
		Integer x1 = add(4,6);
		Number x2 = add(8,9.6f);	//如果Number-->Float 则编译器会报错；
		Object x3 = add(95,"abc");	//Object-->String 则编译器会报错；
		
		//数组元素交换：
		Object[] arr = new String[]{"woief","233","sdf","e923"};
		System.out.println(Array.get(arr,1));
		swap(arr,1,2);
		System.out.println(Array.get(arr,1));
		
		
		//基本数据类型数组：
		Integer[]arr2 = new Integer[]{1,3,6,8,9,0};	//如果Integer-->int，则调用swap方法时会报错；		
		swap(arr2,5,3);
				
		Vector<Integer> v [] = new Vector[10];//右边不能使用泛型
	}
	
	public static <T> T add(T a, T b) {
		return null;
	}

	public static <T> void swap(T[]arr,int x,int y) {
		T temp = arr[x];
		arr[x] = arr[y];
		arr[y] = temp;
	}
}
