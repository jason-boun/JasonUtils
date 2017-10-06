
package com.jason.jasonutils.arrayoperate;

/**
 * 这是一个可以对数组操作的工具类；该类提供了获取最值、排序、打印、交换元素等功能。
 * @author Jason
 * @version V1.0 
 */
public class ArrayTool {
	/**
	 * 空参数构造函数（如果私有化，则禁止调用者创造实例对象）。
	 */
	private ArrayTool(){}
	
	/**
	 * 获取一个数组中元素的最大值。
	 * @param arr 接收一个int型数组。
	 * @return 返回一个该数组中的最大值，并说明该最大值是数组中的那个元素。
	 */
	public static String getMax(int[]arr) {
		int max=arr[0];
		int count=0;
		for(int i=0;i<arr.length;i++) {
			if(max<arr[i]) {
				max=arr[i];
				count=i;
			}
		}
		return ("max="+max+";"+"arr["+count+"]");
	}
	
	/**
	 * 获取一个数组中元素的最小值。
	 * @param arr 接收一个int型数组。
	 * @return 返回一个该数组中的最小值，并说明该最小值是数组中的那个元素。
	 */
	public static String getMin(int[]arr) {
		int count=0;
		int min=arr[0];
		for(int i=0;i<arr.length;i++) {
			if(min>arr[i]) {
				min=arr[i];
				count=i;
			}
		}
		return ("min="+min+";"+"arr["+count+"]");
	}
	
	/**
	 * 采用选择排序法，给int型数组元素从小到大排序。
	 * @param arr 接收一个int型数组。
	 */
	public static void selectSort(int[]arr)	 {
		for(int x=0;x<arr.length-1;x++) {
			for(int y=x+1;y<arr.length;y++) {
				if(arr[x]>arr[y]) {
					swap(arr, x, y);
				}
			}
		}
	}
	
	/**
	 * 采用选择排序法，给int型数组元素从大到小排序。
	 * @param arr 接收一个int型数组。
	 */
	public static void selectSort2(int[]arr) {
		for(int x=0;x<arr.length-1;x++) {
			for(int y=x+1;y<arr.length;y++) {
				if(arr[x]<arr[y]) {
					swap(arr, x, y);
				}
			}
		}
	}
	
	/**
	 * 采用冒泡法，给int型数组元素从小到大排序。
	 * @param arr 接收一个int型数组。
	 */
	public static void bubbleSort(int[]arr) {
		for(int x=0;x<arr.length-1;x++) {
			for(int y=0;y<arr.length-x-1;y++) {
				if(arr[y]>arr[y+1]) {
					swap(arr, y, y+1);
				}
			}
		}
	}
	/**
	 * 给int型数组元素元素位置进行置换。
	 * @param arr 接收一个int型数组。
	 * @param x 要置换的位置。
	 * @param y 要置换的位置。
	 */
	public static void swap(int[]arr,int x,int y) {
		int temp=arr[x];
		arr[x]=arr[y];
		arr[y]=temp;
	}

	/**
	 * 用于打印数组中的元素，打印形式为：[element1,element2...]
	 */
	public static void printArray(int[]arr) {
		for(int x=0;x<arr.length;x++) {
			if(x==0)
				System.out.print("["+arr[x]+",");
			else if(x>0 & x<arr.length-1)
				System.out.print(arr[x]+",");
			else
				System.out.println(arr[x]+"]");
		}
	}
}
