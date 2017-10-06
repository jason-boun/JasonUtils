package com.jason.jasonutils.enhance.generic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * =========================================================
 * 被调用的泛型方法，一定要在返回值类型之前用尖括号注明数据类型。
 * 只要传递给它的是什么类型，返回值类型就是什么；
 * 
 * 类型推断：取最小公倍数--->最近的共同父类；
 * 参数类型的一致性和传播性；
 * =========================================================
 */

public class GenericMethod {
	
	public static void main(String[] args) {
		
		Object obj = "aboidf";
		String str = convert(obj);	//左边定义什么类型，调用的方法就返回什么类型；
		
		System.out.println("===============集合元素复制到数组=================");
		
		ArrayList<Integer> col = new ArrayList<Integer>();
		col.add(1);
		col.add(4);
		col.add(8);
		col.add(9);
		
		Integer[]arr = collectionToArray(col,new Integer[0]);		
		for(int i=0;i<col.size();i++) {
			System.out.println(Array.get(arr,i));
		}
		
		System.out.println("===============数组元素复制到数组=================");
		
		String[]arr1 = {"asdof","weri","128","e8dsv","bby"};
		String[]arr2= new String[arr1.length];
		arrayToArray(arr1,arr2);
		for(int i=0;i<arr2.length;i++) {
			System.out.println(arr2[i]);
		}
		
		System.out.println("==================类型推断=======================");
		
		arrayToArray(new Date[12],new String[12]);		//类型推断为Object[]
		collectionToArray(new ArrayList<String>(),new String[0]);//都是String类型
		//collectionToArray(new ArrayList<Date>(),new String[0]);	//类型传播，确认是Date类型，后边数组又出现String，所以会报错；
	}
	
	/**
	 * 定义一个泛型方法，将Object类型数据自动转换成所定义的类型(强转功能)
	 * @param obj
	 * @return
	 */
	public static <T> T convert(Object obj) {
		return (T)obj;
	}
	
	/**
	 * 定义一个方法，可以将任意类型的数组中的所有元素填充为相应类型的某个对象；	
	 * @param arr
	 * @param obj
	 */
	public static <T> void fillArray(T[]arr, T obj) {
		for(int i=0;i<arr.length;i++) {
			arr[i] = obj;
		}
	}
	
	/**
	 * 定义一个泛型方法，打印任意类型的集合中的所有元素；
	 * @param collection
	 */
	public static <T> void printT(Collection<T> collection) {
		for(T t :collection) {
			System.out.println(t);
		}
	}
	
	/**
	 * 定义一个泛型方法，把一个任意类型的集合中的元素，安全的复制到相应类型的数组中来；（集合的toArray()方法）
	 * @param col
	 * @param arr
	 * @return
	 */
	public static <T> T[] collectionToArray(Collection<T>col,T[]arr) {					
		return col.toArray(arr);
	}
	
	/**
	 * 定义一个泛型方法，把一个任意类型的数组中的元素，安全的复制到相应类型的数组中来；
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static <T> T[] arrayToArray(T[]arr1,T[]arr2) {					
		for(int i=0;i<arr1.length;i++) {
			arr2[i]=arr1[i];
		}
		return arr2;
	}
}
