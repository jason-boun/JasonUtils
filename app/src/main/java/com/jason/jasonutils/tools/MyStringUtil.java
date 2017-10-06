package com.jason.jasonutils.tools;

public class MyStringUtil {

	/**
	 * 获取两个字符串中包含的最长子字符串
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String getMaxlengthSubStr(String s1,String s2){
		String max = s1.length()>=s2.length()? s1:s2;
		String min = max==s1? s2:s1;
		for(int x=0;x<min.length();x++){
			for(int y=0,z=min.length()-x;z!=min.length()+1;y++,z++){
				String temp = min.substring(y,z);
				if(max.contains(temp)){
					return temp;
				}
			}
		}
		return "";
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isStrEmpty(String str){
		if(null!=str && str.length()>0){
			return str.trim().length()==0;
		}else{
			return true;
		}
	}
}
