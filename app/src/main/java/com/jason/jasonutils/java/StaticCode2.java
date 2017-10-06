package com.jason.jasonutils.java;

public class StaticCode2 {

	// 构造代码块
	{
		System.out.println("c");
	}

	// 静态代码块
	static {
		System.out.println("d");
	}

	// 无参构造函数
	StaticCode2() {
		System.out.println("a");
	}

	// 有参构造函数
	StaticCode2(int i) {
		System.out.println("b");
	}

	// 普通函数
	public static void show() {
		System.out.println("show run");
	}

}

class StaticCodeDemo {
	// 静态代码块：随类的加载而加载；只执行一次；优先于主函数；给类初始化。
	static {
		System.out.println("b");
	}

	public static void main(String[] args) {
		new StaticCode2();
		System.out.println("====");
		new StaticCode2(5);
	}

	static {
		System.out.println("c");
	}
}
