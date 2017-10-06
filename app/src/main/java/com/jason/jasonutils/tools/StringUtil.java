package com.jason.jasonutils.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtil {
	
	/**
	 * 截取字符串长度
	 * @param srcStr
	 * @param lenght
	 */
	public static String getSubName(String srcStr,int lenght){
		if(!TextUtils.isEmpty(srcStr)){
			String TempStr = srcStr.trim();
			if(TempStr.length()>lenght){
				return TempStr.substring(0,lenght)+"..";
			}else{
				return TempStr;
			}
		}
		return srcStr;
	}
	
	/**
	 * 给定字符串中是否包含中文字符
	 * @param str
	 * @return
	 */
	public static boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}
	
	/**
	 * 判断字符串中是否仅包含字母数字和汉字 各种字符的unicode编码的范围：
	 * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]） 数字：[0x30,0x39]（或十进制[48, 57]）
	 * 小写字母：[0x61,0x7a]（或十进制[97, 122]） 大写字母：[0x41,0x5a]（或十进制[65, 90]）
	 */
	public static boolean isLetterDigitOrChinese(String str) {
		// String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
		String regex = "^[a-z0-9A-Z]+$";
		return str.matches(regex);
	}

}
