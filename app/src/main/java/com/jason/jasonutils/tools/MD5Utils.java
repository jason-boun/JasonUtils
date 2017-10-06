package com.jason.jasonutils.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	/**
	 * md5工具类
	 * @param srcString
	 * @return
	 */
	public static String encode(String srcString){ 
		try {
			byte[] result = MessageDigest.getInstance("MD5").digest(srcString.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b ; 
				String str = Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
