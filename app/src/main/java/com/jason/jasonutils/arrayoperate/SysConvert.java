package com.jason.jasonutils.arrayoperate;

/**
 * 进制转换；
 * 进制转换的本质，就是除以基数取余数，继续用商除以基数取余数，...（即每次都是取模）
 * 
 * 所以用到循环；
 * =================================================================================
 * 自定义进制转换：
 * 本质：循环{截取末尾几位(&)、移位(>>>)；}
 * 
 * 查表法：注意定义的两个数组都是char类型的数组；
 * 定义表：chs[]={0,1,2,...,E,F};
 * 定义数组arr[i],能截取i次（16进制截取32/4=8次；8进制截取32/3=11次；2进制截取32/1=32次）
 * 循环截取和移位：temp =i & base ; i>>>offset；pos=arr.length;
 * 查表：chs[temp]；存入：arr[--pos]=chs[temp];即查好后存入数组，从后边依次向前存；
 * 取出与打印：sop(arr[pos++])
 * =================================================================================
 * 一个给力的缓冲容器：StringBuffer；
 * 方法有append(); reverse();
 * append()存储原理是每次都添加到原内容的后边；这样在sb.toString()时就会遵循先进先出；
 * 而sb.reverse()方法正好将可以将顺序颠倒：先进后出；
 * =================================================================================
 */

public class SysConvert  {
	
	public static void main(String[] args) {
		toBinary(60);
		toHex1(60);
		toHex2(60);
		toHex3(60);
		sysConvert(60,2);
		// toHex1(60) ;
	}

	/**
	 * 将给定的十进制数转换为二进制数
	 * @param i
	 */
	private static void toBinary(int i)  {
		StringBuffer sb=new StringBuffer();
		while(i>0) {
			sb.append(i%2);
			i=i>>1;
		}
		System.out.println(sb.reverse());
	}

	/**
	 * 将给定的十进制数转换为十六进制数
	 * @param i
	 */
	private static void toHex1(int i) {
		StringBuffer sb=new StringBuffer();
		//整型i为32位，每次取4位，最多循环8次。
		for(int x=0;x<8;x++) {
			int temp=i&15;
			if(temp>9){
				sb.append((char)(temp-10+'A'));
			} else{
				sb.append(temp);
			}
			i=i>>>4;	//无符号右移四位。
			if(i==0) {
				break;//如果右移后的数字变为零，则结束循环。
			}
		}
		System.out.println(sb.reverse());
	}
	
	private static void toHex2(int i) {
		StringBuffer sb=new StringBuffer();
		while(i!=0) {
			int temp=i&15;			
			if(temp>9)
				sb.append((char)(temp-10+'A'));
			else
				sb.append(temp);			
			i=i>>>4;
		}
		System.out.println(sb.reverse());
	}
	
	/**
	 * 查表法进制转换，十进制到十六进制
	 * @param i
	 */
	private static void toHex3(int i){
		char[]chs ={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[]arr =new char[8];
		int pos=arr.length;
		while(i!=0) {
			int temp=i&15;
			arr[--pos]=chs[temp];
			i=i>>>4;
		}
		//System.out.println("pos=" + pos);
		for(int x=pos;x<arr.length;x++) {
			System.out.print(arr[x]);
		}
		System.out.println();
	}
	
	/**
	 * 查表法进制转换
	 * @param i：给定的十进制数
	 * @param sys：需要转换的进制
	 */
	private static void sysConvert(int i,int sys) {
		int base=0,offset=0;//自定义的变量：基数和偏移量
		switch(sys) {
			case 16:
				base=15;
				offset=4;
				break;
			case 8:
				base=7;
				offset=3;
				break;
			case 2:
				base=1;
				offset=1;
				break;
			default:
				throw new RuntimeException("进制输入错误");
		}
		char[]chs ={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[]arr =new char[32];//如果转为二进制，则最多为32位。
		int pos=arr.length;
		while(i!=0) {
			int temp=i&base;
			arr[--pos]=chs[temp];//之所以先减后赋值，是因为数组角标从零开始，到length-1结束。
			i=i>>>offset;
		}
		//System.out.println("pos=" + pos);	//标记arr数组从后向前存储，到哪一个角标了。
		for(int x=pos;x<arr.length;x++) {
			System.out.print(arr[x]);
		}
	}
	
}
