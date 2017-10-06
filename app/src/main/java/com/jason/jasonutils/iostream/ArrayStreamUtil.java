package com.jason.jasonutils.iostream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ArrayStreamUtil {

	/**
	 * DataInputStream 类和 DataOutputStream 类用于操作基本数据类型；封装了各种操作基本数据类型的方法；
	 * @throws IOException 
	 */
	public void dataStreamUtil(String desFile) throws IOException{
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(desFile));
		dos.writeInt(235);
		dos.writeBoolean(false);
		dos.writeDouble(23.2834);
		dos.writeUTF("你好");	//通过DataOutputSream类的writeUTF()方法使按UTF-8编码。
		
		DataInputStream dis = new DataInputStream(new FileInputStream(desFile));
		int num = dis.readInt();
		Boolean b = dis.readBoolean();
		Double d = dis.readDouble();
		
		String s = dis.readUTF();	//通过DataInputSream类的readUTF()方法使按UTF-8解码。
		
		dis.close();
		dos.close();
		
		//通过转换流来设定编码格式。生成的文件大小为4字节；
		OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(desFile),"gbk");			
		os.write("你好");
		os.close();
	}
	
	/**
	 * 内存流的读写操作：内存中的流对象，不涉及系统底层资源调用，不会抛出IOException；
	 * 字节数组流：ByteArrayInputStream、ByteArrayOutputStream
	 * 字符数组流：CharArrayReader、CharArrayWriter
	 * 字符串数组流：StringReader、StringWriter
	 * @throws IOException 
	 */
	public void arrayStreamUtil(byte[] data, String desFile) throws IOException{
		ByteArrayInputStream bis = new ByteArrayInputStream(data);//传一个字节数组。
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		int len = 0;
		while((len=bis.read())!=-1) {
			bos.write(len);
		}
		System.out.println(bos.toString());
		bos.writeTo(new FileOutputStream(desFile));	//只有此方法涉及到底层资源的调用，所以需抛出IOException.
	}
}
