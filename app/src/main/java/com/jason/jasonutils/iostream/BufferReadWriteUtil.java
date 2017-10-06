package com.jason.jasonutils.iostream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class BufferReadWriteUtil {

	/**
	 * 通过读写缓冲包装类来读写字符流
	 * @param srcFileName
	 * @param desFileName
	 */
	public static void BufferedReadWriteMethod(String srcFileName,String desFileName){
		BufferedReader br;
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(srcFileName));
			bw = new BufferedWriter(new FileWriter(desFileName));
			
			String line = null;
			while((line=br.readLine())!=null){//读入缓冲技术，读取一行
				bw.write(line);
				bw.newLine();//写入缓冲技术，新起一行
				bw.flush();
			}
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过读取包装类readWithLineNumber来读写文件的行号
	 * @param srcFileName
	 */
	public static void readWithLineNumber(String srcFileName){
		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(srcFileName));
			lnr.setLineNumber(100);
			String line = null;
			while((line=lnr.readLine())!=null){
				System.out.println(lnr.getLineNumber()+":"+line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 自定义缓冲读取流
	 * 重点方法myReadLine()
	 */
	public class MyBufferedReader extends Reader{
		private Reader reader;
		public MyBufferedReader (Reader reader){
			this.reader = reader;
		}
		
		/**
		 * 核心方法：自定义读取一行
		 * @return
		 * @throws IOException
		 */
		public String myReadLine() throws IOException{
			StringBuilder sb = new StringBuilder();
			int ch = 0;
			while((ch= reader.read())!=-1){
				if(ch=='\r'){
					continue;
				}else if(ch=='\n'){
					return sb.toString();
				}else{
					sb.append((char)ch);
				}
			}
			if(sb.length()!=0){
				return sb.toString();
			}
			return null;
		}
		
		/**
		 * 自定义关闭流的方法
		 * @throws IOException
		 */
		public void myClose() throws IOException{
			if(reader!=null){
				reader.close();
			}
		}
		@Override
		public void close() throws IOException {
			reader.close();
		}
		@Override
		public int read(char[] buf, int offset, int count) throws IOException {
			return reader.read(buf, offset, count);
		}
	}
	
	/**
	 * 自定义可读行号的字符读取流
	 * 继承自MyBufferedReader类
	 * 自定义字段：LineNumber
	 */
	public class MyLineNumberReader extends MyBufferedReader{
		private int LineNumber = 0;//默认初始值为0
		public MyLineNumberReader(Reader reader) {
			super(reader);
		}
		public int getLineNumber() {
			return LineNumber;
		}
		public void setLineNumber(int lineNumber) {
			LineNumber = lineNumber;
		}
		@Override
		public String myReadLine() throws IOException {
			LineNumber++;//相当于每行读操作开始都调用了setLineNumber方法，对行号进行自增
			return super.myReadLine();
		}
	}
	
}
