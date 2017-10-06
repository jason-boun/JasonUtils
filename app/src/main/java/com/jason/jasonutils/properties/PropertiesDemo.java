package com.jason.jasonutils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PropertiesDemo {
	
	public static void main(String[] args) throws IOException {
		softWareUseCount("c:/text/config.ini");
	}
	
	/**
	 * 软件试用配置文件
	 * @param fileName
	 * @throws IOException
	 */
	public static void softWareUseCount(String fileName) throws IOException{
		File file = new File(fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		FileInputStream fis = new FileInputStream(fileName);
		Properties prop = new Properties();
		prop.load(fis);//加载数据
		String value = prop.getProperty("time");//获取属性
		int count = 0;
		if(value!=null){
			count = Integer.parseInt(value);
			if(count>=5){
				System.out.println("use timeout");
				return ;
			}
		}
		count++;
		prop.setProperty("time", String.valueOf(count));//更新属性
		FileOutputStream fos = new FileOutputStream(file);
		prop.store(fos, new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss").format(new Date()));//保存数据
	}

}
