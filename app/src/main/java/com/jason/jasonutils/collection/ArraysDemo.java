package com.jason.jasonutils.collection;

import static java.lang.System.out;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.sort;

import java.util.Arrays;
import java.util.List;
/**
 * Arrays操作数组的工具类：
 */
public class ArraysDemo {

	/**
	 * Arrays工具类中asList()方法：将数组转换为List集合。
	 * 
	 * 将数组转化为List集合的好处：
	 * 
	 * 可以进行对原来数组的元素用集合的思想和方法进行操作；
	 * 可以进行get，contains，subList，indexOf操作。
	 * 但是不能进行增删操作；否则会发生不支持操作异常：UnSupportedOperationException.
	 * 
	 * 注意：
	 * 如果数组是基本数据类型数组，则这个数组通过asList后变为List集合的一个元素。如：int[]nums = {2,4,6,7,4};
	 * 如果数组中的元素是对象，则每个元素通过asList后变为List 集合的元素。如Integer[] nums2 =  {2,4,6,7,4};
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	/**
	 * 可变参数示例：如果还有其他非可变参数，那么应该将可变参数定义到参数列表的最后。
	 * Arrays.toString(arr)：将数组转为字符串类型
	 * @param s
	 * @param arr
	 */
	public void variParamDemo(String s,int... arr){
		System.out.println(s+"," + Arrays.toString(arr));
	}
	
	/**
	 * 自定义toString方法
	 * @param arr
	 */
	public void myArraysToString(Object [] arr){
		StringBuffer sb = new StringBuffer();		
		for (int x=0;x<arr.length;x++) {
			if(x==0)
				sb.append("["+arr[x]+", ");
			else if (x==arr.length-1)
				sb.append(arr[x]+"]");
			else
				sb.append(arr[x]+", ");
		}		
		System.out.println(sb.toString());
	}
	
	/**
	 * 
	 */
	public void asListDemo(){
		String[]arr1 = {"abc","oawei","oichi","wioi"};
		List<String> list = Arrays.asList(arr1);
		System.out.println(list);
		
		System.out.println(list.contains("abc"));
		System.out.println(list.get(2));
		System.out.println(list.indexOf("oichi"));
		System.out.println(list.subList(2, list.size()));	
		//list.add("deadg");	//UnsupportedOperationException
	}
	
	public void asListDemo2(){
		//如果数组是基本数据类型数组，则这个数组通过asList后变为List 集合的一个元素。
		//（比如：下例变为List集合中的一个int型的数组这样的元素。）
		int[]nums = {2,4,6,7,4};		
		List<int[]> list = Arrays.asList(nums);
		System.out.println(list);
		
		//如果数组中的元素是对象，则每个元素通过asList后变为List 集合的元素。
		////（比如：下例转换为List集合，集合中的每一个元素都是Integer类型）
		Integer[] nums2 =  {2,4,6,7,4};
		List<Integer> list2 = Arrays.asList(nums2);
		System.out.println(list2);
	}
	/**
	 * 静态导入：JDK1.5 新特性；
	 * 静态导入；
	 * 当import后加(java.XXX.类.*;)，表示导入该类；
	 * 当import后加(static java.XXX.类.*;)，表示导入该类中的所有静态成员；
	 * 
	 * 注意：
	 * 当类名重名时，需要指定具体的包名；
	 * 当方法名重名时，需要指定具体的类名。
	 * 
	 * 基本类型的数组打印元素：Integer[]arr= {5,8,9}
	 * syso(arr):[I@4f1d0d
	 * syso(Arrays.toString(arr)):[5, 8, 9]
	 * import static java.util.Arrays.*;
	 * import static java.lang.System.*;
	 */
	public void importStaticDemo(){
		int[]arr = {12,8,345};
		sort(arr);
		int index = binarySearch(arr,12);
		
		out.println(index);
		out.println(Arrays.toString(arr));	//当方法名重名时，需要指定具体所属的类名。
											//因为Object/Collection中也有toString()方法，但不带参数。
	
	}
}
