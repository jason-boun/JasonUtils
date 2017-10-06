package com.jason.jasonutils.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import android.annotation.SuppressLint;
import android.os.Environment;

/**
 * Properties工具类
 */
public class PropertiesUtil { 
	
	//************************** 从SD卡中读取配置文件中的数据 ******************************************
	
	private static String IP = "";
	private static String PORT = "";
	private static String PATH = Environment.getExternalStorageDirectory()+"/config.properties";
	
	/**
	 * 加载配置文件数据
	 */
	private static Properties properties;
	static {
		if(properties == null){
			properties = new Properties();
		}
		if(SDCardAvailable()){
			File file = new File(PATH);
			if(file.exists()){
				try {
					properties.load(new FileInputStream(file));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String getIP() {
		IP = properties.getProperty("IP");
		return IP;
	}
	public static String getPORT() {
		PORT = (String) properties.get("PORT");
		return PORT;
	}
	
	/**
	 * 判断SD卡是否存在
	 * @return
	 */
	private static boolean SDCardAvailable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * 获取SD卡路径
	 * @return ：/mnt/sdcard
	 */
	public static String getSDdir(){
		if(SDCardAvailable()){
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	//*************************** 工厂设计模式：来隔离接口和具体业务类 ******************************
	
	/**
	 * 通过配置获取实例，隔离接口和具体实现类
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T getImpl(Class<T> clazz){
		String key = clazz.getSimpleName();
		String className = properties.getProperty(key);
		try {
			return (T)Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//********************************* Properties类的解析 *****************************************
	
	/**
	 * Properties：是HashTable的子类，所以具备Map的特性；且键值对都是String类型。
	 * Map类有keySet()方法； 而Properties有stringPropertyNames()方法：
	 * Set<String> set = prop.stringPropertyNames()：返回此属性列表中的键集，其中该键及其对应值是字符串。
	 */
	
	@SuppressLint("NewApi")
	public void getPropertiesValue(){
		Properties prop = new Properties();
		prop.setProperty("zhangsan", "30");
		prop.setProperty("lisi", "35");
		
		Set<String> names = prop.stringPropertyNames();
		if(names.size()>0){
			for(String name :names){
				System.out.println(names+"=="+prop.getProperty(name));
				System.out.println(names+"=="+prop.get(name));
			}
		}
	}
	
	/**
	 * 加载配置文件，修改配置文件，保存配置文件
	 * @param fileName
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	public void getSavePropertiesFileData(String fileName) throws IOException{
		if(new File(fileName).exists()){
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream(fileName);
			
			prop.load(fis);//载入配置文件到内存
			prop.setProperty("wangwu","28");//修改参数；
			
			FileOutputStream fos = new FileOutputStream(fileName);
			prop.store(fos, new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss").format(new Date()));//保存配置信息到本地设备，第二个参数是备注信息
			
			fis.close();
			fos.close();
		}
	}
	
	/**
	 * Properties类的封装原理
	 * @param fileName
	 * @throws IOException
	 */
	public void propertiesOperate(String fileName) throws IOException{
		BufferedReader bufr = new BufferedReader(new FileReader(fileName));
		Properties prop = new Properties();
		String line = null;
		while((line=bufr.readLine())!=null)
		{
			String[]s = line.split("=");//String类的方法：split()，对象调用后，返回一个字符串型数组。元素就是以"="法则切割的各部分。
			prop.setProperty(s[0],s[1]);			
		}
		System.out.println(prop);		
	}
	

	/**
	 * 获取设备系统信息
	 * @param desFilePath
	 * @throws IOException
	 */
	public static void getSysPropertiesInfo(String desFilePath) throws IOException{
		Properties properties = System.getProperties(); //获取系统配置信息；
		properties.list(new PrintStream(desFilePath));  //输出到指定文件中；
		properties.list(System.out);                    //输出到打印台；  
	}
	
	/**
	 * 软件试用配置文件，软件每次初始化的时候调用该方法
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
				//TODO:中断软件加载的处理
				return ;
			}
		}
		count++;
		prop.setProperty("time", String.valueOf(count));//更新属性
		FileOutputStream fos = new FileOutputStream(file);
		prop.store(fos, new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss").format(new Date()));//保存数据
	}
}
