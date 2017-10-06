package com.jason.jasonutils.string;

public class StringUtil {
	
	/**
	 * 遍历数组
	 * @param arr
	 * @return
	 */
	public static String printArrayOfString(Object[] arr) {
		if(arr!=null && arr.length>0){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<arr.length;i++) {
				if(i==0) {
					sb.append("["+arr[i]+",");
				} else if(i<arr.length-1){
					sb.append(arr[i]+",");
				}else{
					sb.append(arr[i]+"]");
				}
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * 自定义trim功能
	 * @param str
	 * @return
	 */
	public static String myTrim(String str){
		if(str!=null && str.length()>0){
			int start=0, end = str.length()-1;
			while(start<=end && str.charAt(start)==' '){
				start++;
			}
			while(start<=end && str.charAt(end)==' '){
				end--;
			}
			return str.substring(start, end);
		}
		return str;
	}
	
}
