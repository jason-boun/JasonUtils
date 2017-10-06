/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jason.jasonutils.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;

/**
 * Config info Manager.
 * 
 * 
 * @version 1.0.0
 * @see #Config()
 */
public final class CCPConfig {
	

	private static Properties properties;

	public static String LOCAL_PATH = Environment.getExternalStorageDirectory() + "/config.properties";
	
	/**
	 * 初始化属性
	 */
	public static boolean initProperties(Context context) {
		
		if (properties == null) {
			properties = new Properties();
		}
		
		if (isExistExternalStore()) {
			String content = readContentByFile(LOCAL_PATH);
			if (content != null) {
				try {
					properties.load(new FileInputStream(LOCAL_PATH));
					return loadConfigByProperties();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} 
		} 
		return false;
	}

	private CCPConfig() {

	}

	public static String readContentByFile(String path) {
		BufferedReader reader = null;
		String line = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new FileReader(file));
				while ((line = reader.readLine()) != null) {
					sb.append(line.trim());
				}
				return sb.toString().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	private static boolean loadConfigByProperties() {
		XMPP_server_IP = (String)properties.getProperty("XMPP_server_IP");
		REGISTER_ADDRESS = (String)properties.getProperty("REGISTER_ADDRESS");
		return true;
	}
	  public static String XMPP_server_IP ="";//测试环境
	  public static String REGISTER_ADDRESS = ""; //测试环境

	  

}
