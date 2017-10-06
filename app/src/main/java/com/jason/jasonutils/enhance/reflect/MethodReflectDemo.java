package com.jason.jasonutils.enhance.reflect;

import java.lang.reflect.Method;

/**
 * 反射之Method：
 * ==================================================================
 * 普通调用charAt()方法：通过实例对象来调用：str1.charAt()
 * 
 * 通过反射来调用charAt():
 * 先通过String字节码的方法getMethod()返回Method类型实例methodCharAt；
 * 再通过methodCharAt实例的invoke方法来操作str1；
 * 
 * 如果将str换位null，则methodCharAt肯定是静态方法，因为可以不通过实例对象来调用静态方法；
 * System.out.println(methodCharAt.invoke(null, 1));
 * ==================================================================
 * 通过反射调用给定的一个类名的main方法：
 *
 * 普通实现方式：TestReflect.main(new String[]{new String("111"),new String("222"),new String("333")});
 * 
 * 通过反射实现方式：
 * 将给定的类名作为参数传递给要所在的类主函数；
 * 获取主函数的参数(String[]args)中字符串数组的第一个元素agrs[0],并将该字符串传递给反射需要的类名位置：
 * Method methodMain = Class.forName(StartingClassName).getMethod("main", String[].class);	
 * 通过反射invoke方法，来实现调用。
 * 
 * 这里需要注意的是：
 * main方法是静态方法，所以invoke第一个参数为null；
 * 第二个参数是一个字符串型数组，需要将该数组强转成Object类型，这样就变成一个参数了(在拆开的时候就不是三个字符串了)
 * 
 * =====================================================================================================
 * java.lang.reflect.Method
 * getGenericReturnType()返回值类型： Type 
 * getParameterTypes()返回值类型： Class<?>[]
 * 
 * getName()返回值类型：String
 * =====================================================================================================
 * 普通方法最常用格式：
 * getMethod();
 * invoke();
 * =====================================================================================================
 */

public class MethodReflectDemo {

	public static void main(String[] args) throws Exception {

		String str1 = "adoisd";

		Method methodCharAt = String.class.getMethod("charAt", int.class);// 通过字节码和反射技术来获取到某个方法

		System.out.println(methodCharAt.invoke(str1, 1));// 通过invoke来处理某个给定的字符串
		System.out.println(methodCharAt.invoke(str1, new Object[] { 2 }));
		// jdk1.4版本传递参数是通过数组形式，1.5通过可变参数形式；

		System.out.println("==========通过反射调用给定类的main方法==============");

		String StartingClassName = args[0];
		Method methodMain = Class.forName(StartingClassName).getMethod("main", String[].class);// 调用给定字符串对应类的main方法
		methodMain.invoke(null, (Object) new String[] { "111", "222", "333" });// 静态方法，第一个参数为null，第二个参数用来传递具体需要处理的字符串数组。
	}
}
	
class TestReflect {

	public static void main(String[] args) {
		for (String arg : args) {
			System.out.println(arg);
		}
	}
}
