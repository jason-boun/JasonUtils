package com.jason.jasonutils.arrayoperate;

/**
 * ===========================================
 * 折半查找：(二分法查找)
 * 寻找位置插入元素：
 * ===========================================
 * 前提：必须是有序数组；
 * 
 * 思路：max\min\mid\key；
 * 取中间值与key比较，根据大小缩小区间，循环比较较；
 * 
 * min=0；max=arr.length-1;mid = (max+min)/2;
 * while(min<=max)
 * {
 *		if(mid!=key)
 *		{
 *			max=mid-1；
 *			(or min=mid+1；)
 *			mid = (max+min)/2;
 *		}			
 *		else
 *			return mid;
 * }
 * ===========================================
 * 如果是插入元素：
 * return min；
 * API:return -min-1；
 * ===========================================
 *
 */

public class ArraySearch {

	public static void main(String[] args) {
		int arr[]= new int[]{4,9,19,22,33,34,48,82,94};		
		System.out.println(halfSearch1(arr, 94));//折半查找，前提是给定的数组为有序数组。
		System.out.println(halfSearch2(arr, 48));
		System.out.println(insertSearch(arr, 21));//将一个数字插入有序数组中，得到的新数组仍然有序。
	}

	private static int halfSearch1(int[] arr, int key) {		
		int mid,min,max;
		min=0;
		max=arr.length-1;
		mid=(min+max)>>1;
		
		while(arr[mid]!=key) {
			if(arr[mid]<key)
				min=mid+1;
			else
				max=mid-1;						
			if(min>max)
				return -1;	
			mid=(min+max)>>1;
		}		
		return mid;			
	}
	
	private static int halfSearch2(int[] arr, int key) {		
		int min=0,max=arr.length-1;	
		
		while(min<=max) {
			int mid=(min+max)>>1;
			if(key>arr[mid])
				min=mid+1;
			else if(key<arr[mid])
				max=mid-1;
			else 
				return mid;		
		}		
		return -1;			
	}
	
	//将一个数字插入有序数组中，得到的新数组仍然有序。
	private static int insertSearch(int[] arr, int key) {		
		int min=0,max=arr.length-1;	
		while(min<=max) {
			int mid=(min+max)>>1;
			if(key>arr[mid])
				min=mid+1;
			else if(key<arr[mid])
				max=mid-1;
			else 
				return mid;		
		}		
		return min;	//将折半查找中的return -1改为return min 即可
	}
}
