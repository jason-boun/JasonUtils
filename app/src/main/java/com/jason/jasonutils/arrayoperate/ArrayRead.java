package com.jason.jasonutils.arrayoperate;

/**
 * 
 * ==========================================
 * 遍历数组，打印每个元素：两种打印形式：
 * 1.arr[element1,element2,element3,...]
 * 2.arr[i];
 * 思路：循环遍历，拼凑；
 * ==========================================
 *
 */

public class ArrayRead 
{

	public static void main(String[] args) 
	{
		   int arr1[]= new int [4];
		   //System.out.println(arr1[4]);	//java.lang.ArrayIndexOutOfBoundsException。
		   //arr1[0]=(Integer) null;		//java.lang.NullPointerException
		   System.out.println(arr1[0]);
		   
		   
		   int arr[]= new int[]{22,33,4,34,19,82,9,94,48};
		   		      
		   readArray(arr);
		   System.out.println(arr.length);
		   readArray2(arr);		   
		   System.out.println(sumArray(arr));
	}

	//以数组的格式打印出内容，格式：arr[element1,element2,element3,...]
	//遍历、拼凑而已；
	public static void readArray(int arr[]) {
		for(int i=0;i<arr.length;i++){
		   if(i==0)
			   System.out.print("arr["+arr[i]+",");
		   else if(0<i & i<arr.length-1)
			   System.out.print(arr[i]+",");
		   else
			   System.out.println(arr[i]+"]");
		}
	}
	//每次打印一个元素，格式：arr[element];
	//遍历、拼凑；
	private static void readArray2(int[] arr) {
		for(int i=0;i<arr.length;i++){
		   System.out.println("arr["+i+"]"+"="+arr[i]);
		}
	}
	
	//对整型数组元素求和；
	//遍历，sum+=arr[i];
	private static int sumArray(int[] arr) {
		int sum=0;
		for(int i=0;i<arr.length;i++){
			sum+=arr[i];
		}
		return sum;
	}
	
}
