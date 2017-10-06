package com.jason.jasonutils.iostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import android.content.ContentProvider.PipeDataWriter;

public class OtherImportantStreamUtil {
	
	public static void main(String[] args) throws IOException {
		//String [] strs = {"c:/text/part1","c:/text/part2","c:/text/part3","c:/text/part4"};
		//splitFile("c:/text/1.mp3", strs, 3);//切割
		//mergeFile(strs,"c:/text/marge.mp3");//合并
	}

	//************************ SequenceInputStream 流切割合并文件 ************************************************
	
	/**
	 * 通过SequenceInputStream流，来进行合并文件操作
	 * @param parts：需要合并的流文件名
	 * @param desFile：合并后的文件
	 * @throws IOException
	 */
	public static void mergeFile(String[]parts, String desFile) throws IOException{
		if(parts !=null && parts.length>0){
			ArrayList<FileInputStream> list = new ArrayList<FileInputStream>();
			for(String part:parts){
				if(new File(part).exists()){
					list.add(new FileInputStream(part));
				}
			}
			final Iterator<FileInputStream> iterator = list.iterator();
			Enumeration<FileInputStream> em = new Enumeration<FileInputStream>() {
				@Override
				public boolean hasMoreElements() {
					return iterator.hasNext();
				}
				@Override
				public FileInputStream nextElement() {
					return iterator.next();
				}
			};
			SequenceInputStream sis = new SequenceInputStream(em);//合并流
			FileOutputStream fos = new FileOutputStream(desFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len=sis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
			}
			fos.close();
			sis.close();
		}
	}
	
	/**
	 * 通过不断创建不同的写入流来实现文件的切割
	 * @param srcFile：需要切割的源文件
	 * @param desFiles：切割后的每一个文件名，组成的数组
	 * @param partSize：每个切割后的文件大小
	 * @throws IOException
	 */
	public static void splitFile(String srcFile, String[]desFiles, int partSize) throws IOException{
		File file = new File(srcFile);
		if(file.exists()){
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(desFiles[0]);//切割的第一部分
			int part=1;
			byte[] buffer = new byte[1024*1024];//定义缓存数组为1M
			int len = 0;
			int count = 1;
			while((len=fis.read(buffer))!=-1){
				if(count<partSize+1){
					fos.write(buffer, 0, len);
					fos.flush();
					count++;
				}
				if(count==partSize+1){
					fos = new FileOutputStream(desFiles[part]);//如果到达指定的大小，则重新创建一个写入流，切割第二部分
					count=1;
					part++;
				}
			}
			fis.close();
			fos.close();
		}
	}
	
	//**************************** ObjectInputStream、ObjectOutputStream 保存、持久化数据的流 ****************************************
	
	/**
	 * 通过ObjectOutputStream流来持久化对象数据
	 * @param desFile
	 * @throws IOException
	 */
	public static void write2Obj(String desFile) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(desFile));
		oos.writeObject(new Worker("张三", 18, "female", "en"));
		oos.writeObject(new Worker("李四", 25, "male", "cn"));
		oos.writeObject(new Worker("王五", 28, "female", "zh"));
		oos.close();
	}
	
	/**
	 * 通过ObjectInputStream流来读取持久化的对象数据
	 * @param srcFile
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void readFromObj(String srcFile) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(srcFile));
		Worker worker1 = (Worker) ois.readObject();
		Worker worker2 = (Worker) ois.readObject();
		Worker worker3 = (Worker) ois.readObject();
		
		System.out.println(worker1);
		System.out.println(worker2);
		System.out.println(worker3);
		ois.close();
	}
	
	
	
	//******************************** 管道流示例 *******************************
	
	/**
	 * 管道流示例
	 */
	public static void PipedStreamDemo() throws IOException{
		PipedInputStream pis = new PipedInputStream();
		PipedOutputStream pos = new PipedOutputStream();
		pos.connect(pis);
		
		new Read(pis).start();
		new Write(pos).start();
	}
	
	/**
	 * 随机访问文件流示例
	 * @throws IOException 
	 */
	public static void RandomAccessFileDemo(String desFile) throws IOException{
		RandomAccessFile raf = new RandomAccessFile(desFile, "rw");
		
		raf.write("张三".getBytes());
		raf.writeInt(12);
		
		raf.seek(8);
		
		raf.write("李四".getBytes());
		raf.writeInt(32);
		
		byte[] buffer = new byte[20];
		raf.seek(8);//跳转到指定位置，读取文件
		raf.skipBytes(8);//随机读取：跳过对象的指针；
		raf.read(buffer);
		
		String readResult = new String(buffer);
		System.out.println(readResult);
		
		raf.close();
	}

}

	/**
	 * 本地持久化对象使用的Bean对象
	 */
	class Worker implements Serializable{
		public static final long serialVersionUID = 42L;//自定义序列化标记。SerialVersionUID
		
		private String name;
		private Integer age;
		transient String sex;//被transient修饰的变量不能被ObjectInputStream类持久化。（该关键字的含义：瞬时的）
		static String country = "cn";//被static修饰的变量不能被ObjectInputStream类持久化。
		
		Worker(String name,int age,String male,String country){
				this.name=name;
				this.age=age;
				this.sex=male;
				Worker.country=country;
		}
		@Override
		public String toString() {
			return "Worker [name=" + name + ", age=" + age + ", sex=" + sex + "]";
		}
	}
	
	
	/**
	 * 管道流使用的线程对象
	 */
	class Read extends Thread{
		private PipedInputStream pis;
		public Read(PipedInputStream pis){
			this.pis= pis;
		}
		@Override
		public void run() {
			super.run();
			try {
				byte[] buffer = new byte[1024];
				int len;
				len=pis.read(buffer);
				String result = new String(buffer,0,len);
				System.out.println(result);
				pis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 管道流写入的线程
	 */
	class Write extends Thread{
		private PipedOutputStream pos;
		public Write(PipedOutputStream pos){
			this.pos = pos;
		}
		@Override
		public void run() {
			super.run();
			try {
				pos.write("管道流数据".getBytes());
				pos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

