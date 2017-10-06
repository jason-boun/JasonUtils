package com.jason.jasonutils.arrayoperate;

/**
 * 二维数组：
 * ===========================
 * 定义二维数组的格式；
 * 
 * 遍历二维数组元素：
 * for嵌套循环arr[i][j]
 * ===========================
 */

public class TwoDimentionArray {

	public static void main(String[] args) {
		int[][]arr = new int[3][6];			
		System.out.println(arr[0]);			
		System.out.println(arr[0][0]);		
		arr[0][0]=78;
		System.out.println(arr[0][0]);		
		arr[0]=null;
		//System.out.println(arr[0][0]);	//NullPointerException
		
		System.out.println(arr.length);
		System.out.println(arr[1].length);
		
		
		System.out.println("===========");
		int[][]arr2= new int[3][];		
		System.out.println(arr2[0]);
		//System.out.println(arr2[0][0]);	//NullPointerException		
		
		System.out.println("===========");
		arr2[0]=new int[]{0,2};
		System.out.println(arr2[0][0]);
		System.out.println(arr2[0]);
		System.out.println(arr2[1]);
		
		System.out.println(arr2.length);
		System.out.println(arr2[0].length);
		//System.out.println(arr2[1].length);	//NullPointerException	
		
		
		//一维数组定义格式:
		int x1[];
		int[]x2;
		
		//二维数组定义格式:
		int[][]y1;
		int y2[][];
		int[]y3[];
		
		int[]x,y[];	//定义了两个数组，x是一维数组，y是二维数组：int[]x ；int[]y[];
			
		
		int arr3[][]={{1,5,3},{3,5,2,8},{7,9}};
		System.out.println(sumArray(arr3));		
	}

	private static int sumArray(int[][] arr) {
		int sum=0;
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[i].length;j++) {
				sum+=arr[i][j];
			}
		}
		return sum;
	}
}
