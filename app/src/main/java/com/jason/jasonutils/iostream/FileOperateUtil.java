package com.jason.jasonutils.iostream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class FileOperateUtil {
	
	/**
	 * 删除目录及其子文件
	 * @param dir
	 * @return
	 */
	public static void deleteFile(File dir){
		if(dir.exists()){
			if(dir.isFile()){
				dir.delete();//如果是文件，则直接删除
			}else{
				File[] files = dir.listFiles();
				if(files!=null && files.length>0){
					for(File f:files){
						deleteFile(f);//如果内部含有文件夹，则递归
					}
				}
				dir.delete();//包含的子文件遍历完成后，删除该文件夹
			}
		}
	}
	
	/**
	 * 将指定文件夹下的指定后缀名文件对象加入到指定的List中
	 * @param dir
	 * @param suffix
	 * @param list
	 */
	public static void listFileName(File dir, String suffix, ArrayList<File> list){
		if(dir.exists()){
			if(dir.isFile() && dir.getName().endsWith(suffix)){
				list.add(dir);//如果给定的File是文件，则判断力是否添加
			}else{
				File[] files = dir.listFiles();
				if(files!=null && files.length>0){//如果给定的是文件夹，则列出其子目录文件，继续判定
					for(File f: files){
						if(f.isDirectory()){
							listFileName(f, suffix, list);	
						}else if(f.isFile()&& dir.getName().endsWith(suffix)){
							list.add(f);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 将List中的File对象的绝对路径以文本的格式保存到指定文件
	 * @param list
	 * @param desFile
	 * @throws IOException
	 */
	public static void write2File(ArrayList<File> list, File desFile) throws IOException{
		if(list!=null && list.size()>0){
			BufferedWriter buffw = new BufferedWriter(new FileWriter(desFile));
			for(File f:list){
				buffw.write(f.getAbsolutePath());
				buffw.newLine();
				buffw.flush();
			}
			buffw.close();
		}
	}
	
	/**
	 * 列出设备的所有磁盘盘符
	 * @return
	 */
	public static File[] ListRoots(){
		File[] rootsList = File.listRoots();
		if(rootsList!=null && rootsList.length>0){
			return rootsList;
		}
		return null;
		
	}
	
	/**
	 * 打印出给定目录下的文件名
	 */
	public static void ListFile(File dir){
		if(dir.exists()){
			String[] lists = dir.list();
			for(String list:lists){
				System.out.println(list);
			}	
		}
	}
	
	/**
	 * 通过文件"过滤器"来选出符合后缀的文件列表
	 * @param dir
	 * @param suffix
	 */
	public static void myFileFilter(File dir, final String suffix){
		if(dir.exists()){
			String[] lists = dir.list(new FilenameFilter() {//过滤器来过滤指定的文件后缀名
				@Override
				public boolean accept(File dir, String filename) {
					return filename.endsWith(suffix);
				}
			});
			for(String list:lists){
				System.out.println(list);//打印符合条件的文件名		
			}	
		}
	}
	
	/**
	 * 递归思路：以下代码通过转换二进制和加法求和的例子来说明递归思想；
	 * @param n
	 * @return
	 */
	public static int getSum(int n) {
		if(n==1){
			return 1;
		}
		return n+getSum(n-1);
	}
	
	/**
	 * 递归思路：十进制转换为二进制
	 * @param n
	 */
	public static void toBin(int n) {
		if(n>0) {
			toBin(n/2);//递归
			System.out.print(n%2);
		}
	}

}
