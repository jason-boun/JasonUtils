package com.jason.jasonutils.arrayoperate;

/**
 * ===================================================================
 * 数组最大的特点：有角标；
 * 这样就可以通过角标来对数组的元素进行操作；
 * ===================================================================
 * 第一次出现的思想：遍历；
 * ===================================================================
 * 通过遍历找出数组中的最值；
 * 
 * 思路：
 * 指定其中一个元素赋给max；遍历数组；
 * 遍历过程中，对每一个遍历到的元素和max比较；
 * 如果大于max，则将此元素值赋给max，继续往下遍历；直至遍历结束；
 * 
 * remark：
 * 1.如果同时想获得究竟是数组中哪个元素(或哪个角标)为最大值；
 * 则在每次比较赋值后，将此元素角标赋值给记录变量，遍历完成后返回该变量即可；
 * 2.获取最小值思路同获取最大值一样；
 * ===================================================================
 */

public class ArrayMaxMin {

	public static void main(String[] args) {
		int arr[]= new int[]{22,33,4,34,19,82,9,94,48};
		System.out.println(arrayMax(arr));
		System.out.println(arrayMin(arr));
	}

	private static String arrayMax(int[] arr) {
		int max= arr[0];
		int index = 0;//该方法中定义记录器的目的：获取那个角标的元素是最大值；
		for (int i=1;i<arr.length;i++) {
			if (max<arr[i]){
				max=arr[i];
				index = i;//count跟踪每一个大值角标；直至跟踪到最大值；
			}
			else{
				continue;
			}
		}
		return ("max="+max+";"+"arr["+index+"]");
	}

	private static String arrayMin(int[] arr) {
		int min= arr[0];
		int index = 0; 
		for (int i=1;i<arr.length;i++) {
			if (min>arr[i]) {
				min=arr[i];
				index = i;
			}				
			else{
				continue;			
			}
		}
		return ("min="+min+";"+"arr["+index+"]");
	}
	
}
