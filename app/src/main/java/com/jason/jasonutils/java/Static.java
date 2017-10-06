package com.jason.jasonutils.java;

public class Static {
	String name;
	static String country = "CN";	//静态修饰的成员随类的加载而加载；
	
	public void show() {
		System.out.println(name + "::"+ country);
	}
	//测试方法：
	static void testMethod() {
		System.out.println(Static.country);	//静态修饰的成员可以通过类名调用；
		
		Static st = new Static();
		st.name = "lxm";
		st.show();
	}	
}
