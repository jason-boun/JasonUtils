package com.jason.jasonutils.iostream;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class EncodeDemo 
{
	public static void main(String[] args) throws IOException 
	{
		//writeText();
		//readText();
		
		//StringAndByteArray();
		//dealWithWrong();
		lianTong();
	}
	
	/**
	 * 通过转换流指定编码格式，来保存数据
	 * @throws IOException
	 */
	public static void writeText()throws IOException
	{
		//OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("E:\\IOFiles\\utf1.txt"),"UTF-8");
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("E:\\IOFiles\\gbk1.txt"),"gbk");
		
		osw.write("你好");
		osw.close();
	}
	
	/**
	 * 通过转换流指定解码格式，来读取数据
	 * @throws IOException
	 */
	public static void readText()throws IOException
	{
		InputStreamReader isr = new InputStreamReader(new FileInputStream("E:\\IOFiles\\utf1.txt"),"gbk");
		//InputStreamReader isr = new InputStreamReader(new FileInputStream("E:\\IOFiles\\gbk1.txt"),"UTF-8");
		
		char[]buf = new char[10];		
		int len = 0;
		while((len = isr.read(buf))!=-1)
		{
			String s = new String(buf,0,len);			
			System.out.println(s);
		}		
		isr.close();
	}
	
	/**
	 * 字符串--字节数组，转换过程的：编码解码方法
	 * @throws UnsupportedEncodingException
	 */
	public static void StringAndByteArray() throws UnsupportedEncodingException
	{
		//编码过程：		
		String s = "你好";		
		//byte[]b1 = s.getBytes();		//[-60, -29, -70, -61]
		byte[]b1 = s.getBytes("gbk");	//[-60, -29, -70, -61]
		//byte[]b1 = s.getBytes("utf-8");	//[-28, -67, -96, -27, -91, -67]		
		System.out.println(Arrays.toString(b1));
		
		//解码过程：
		String s2 = new String(b1,"GBK");
		//String s2 = new String(b1,"UTF-8");		
		System.out.println(s2);
	}
	
	/**
	 * 错误解码后的处理与还原
	 * @throws UnsupportedEncodingException
	 */
	public static void dealWithWrong() throws UnsupportedEncodingException
	{	
		String s1 = "你好";
		byte[]b1 = s1.getBytes("gbk");			//正确编码：[-60, -29, -70, -61]		
		System.out.println(Arrays.toString(b1));
		
		String s2 = new String(b1,"ISO8859-1");	//错误解码：出现乱码；
		System.out.println(s2);
		
		byte[]b2 = s2.getBytes("ISO8859-1");	//还原原码：[-60, -29, -70, -61]	
		System.out.println(Arrays.toString(b2));
		
		String s3 = new String(b2,"gbk");		//正确解码：你好。
		System.out.println(s3);
		
		//如果两张码表都识别中文，但编码方式不同(三字节编码，两字节编码)，则上述处理方式不能解决问题；
		//比如上例中，如果把ISO8859-1换成UTF-8,就会出错！
	}
	
	/**
	 * 获取中文字符对应的：二进制数据
	 * @throws UnsupportedEncodingException
	 */
	public static void lianTong() throws UnsupportedEncodingException
	{
		String s = "联通";
		byte[]by = s.getBytes("gbk");
		for(byte b:by){
			System.out.println(Integer.toBinaryString(b&255));
		}
	}
}
