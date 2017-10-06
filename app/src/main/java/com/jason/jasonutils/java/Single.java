package com.jason.jasonutils.java;

/**
 * 单例设计模式：
 * ====================================================================
 * 一个类如何实现单例设计模式：
 * 
 * 私有化构造函数；
 * 私有化自身定义的一个实例对象；
 * 对外通过方法提供该实例对象的访问方式；(getSingle)
 * ====================================================================
 * 该方法的实现有两种方式：
 * 
 * 饿汉式：
 * 创立私有化的对象时候，就已经实例化该对象了；
 * getSingle()中直接返回实例对象即可；
 * 
 * 懒汉式：
 * 创立私有化对象时，该对象值为null；
 * getSingle()中通过 "双重判断+synchronized(Single.class)" 的方式来返回实例；
 * 
 * 二者区别：
 * 饿汉式在类加载的时候，就创立了自己的实例对象；
 * 懒汉式在调用者调用getSingle()方法时，才实例化该对象；
 * ====================================================================
 */

public class Single  {
	private int num;
	private Single(){}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	//懒汉式（单例设计模式）
	private static Single s = null;
	public static Single getSingle() {
		if(s == null) {	
			synchronized (Single.class) {
				if(s == null){
					s = new Single();
				}
			}					
		}
		return s;
	}

	//饿汉式（单例设计模式）	
/*	private static Single s = new Single();		
	public static Single getSingle()
	{return s;}
*/	
	
}
