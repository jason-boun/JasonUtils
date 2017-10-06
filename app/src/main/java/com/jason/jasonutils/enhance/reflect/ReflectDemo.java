package com.jason.jasonutils.enhance.reflect;

/**
 * 反射之Class:
 * =============================================================
 * Class类的知识点：
 * 
 * 九个预定义的Class的实例对象：
 * byte,short,int,long,float,double,char,boolean，再有一个void
 * 
 * 获取字节码的方式：以String为例：
 * 1.String.class;
 * 2.str.getClass();
 * 3.Class.forName("java.lang.String");
 * 
 * 注：
 * Class类没有构造函数；但有不是静态的常用方法；
 * 那么，必然有至少一个是静态方法且该方法的返回值类型是本类类型；
 * 这个方法就是：forName();(联想Runtime类)
 * 
 * 常用方法：isPrimitive(),isArray(),getName(),getClassLoader()等.
 * 构造函数：getConstructor()、getConstructors() 
 * 普通方法：getMethod()、getMethods() 
 * 成员变量：getField()、getFields()
 * 通过该字节码加载某个文件：getResourceAsStream(String name) 
 * 创建实例对象：newInstance() 
 * =================================================================
 */

public class ReflectDemo {
	
	public static void main(String[] args) throws ClassNotFoundException {
		Class cls1 = int.class;
		Class cls2= short.class;
		Class cls3 = byte.class;
		Class cls4 = void.class;

		String str = "";
		
		Class cls5 = str.getClass();
		Class cls6 = String.class;
		Class cls7 = Class.forName("java.lang.String");
		
		System.out.println(cls5==cls6);
		System.out.println(cls5==cls7);
		
		//方法：isPrimitive()是否是基本数据类型：共九种；
		//方法：isArray()判读是否数组类型；
		
		System.out.println(String.class.isPrimitive());
		
		System.out.println(int.class.isPrimitive());
		System.out.println(void.class.isPrimitive());
		System.out.println(int.class==Integer.TYPE);
		
		System.out.println("+++++++++++");
		System.out.println(Integer.class == Integer.TYPE);
		
		System.out.println(int[].class.isPrimitive());
		System.out.println(int[].class.isArray());
				
	}

}
