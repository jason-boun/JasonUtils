package com.jason.jasonutils.collection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ===========================================================================
 * 泛型：用<>表示，定义限定和接受传进来的数据类型。
 * ===========================================================================
 * 泛型作用：
 * 1.避免强转；
 * 2.错误提醒由运行时期提前到编译时期。
 * ============================================================================
 * {}代码区间(作用域)、()参数、[]数组、<>泛型
 * ============================================================================
 * 本例通过传入TreeSet()集合比较器方式：
 * 判断条件：主要条件--按照元素(字符串)的长度；次要条件--字符的自然顺序；
 * 比较器实现方式：通过定义Comparator接口的匿名内部类方式实现；
 * ============================================================================
 * 本节知识点：
 * 在TreeSet集合中传递比较器时，可直接定义匿名内部类，而无需单独定义比较器myComp。
 * ============================================================================
 * ===========================================================================================================
 * 如果在类上定义泛型，那么类中的各个方法数据类型必须一致；
 * 只要创建对象时传入类型固定，所有方法中的数据类型也就固定了。
 * 
 * 但如果泛型定义在方法上，那么你传进去什么类型，就接受什么类型。
 * 不仅不同方法间没有约束关系，即是同一方法也可接收不同类型的数据；
 * 
 * 如果既在类上定义了泛型，又在其内部的方法上也定义了泛型，
 * 那么定义泛型的方法可以不受类泛型的限制，但其他没定义泛型的方法必须和类中的泛型方法一致。
 * 
 * 特殊情况：静态方法不能访问类上定义的泛型，因为静态先于对象加载，而类定义的泛型类型是在建立对象的时候才确定的。
 * 如果静态方法定义泛型，必须定义在静态方法上。
 * ===========================================================================================================
 * 泛型限定：
 * 上限定：<? extends E>
 * 下限定：<? super E>
 * 可以用一个占位符？来表示一个不确定的类型
 * ===========================================================================================================
 */
public class GenericDemo {

	public static void main(String[] args) {
		genericTest();
	}
	
	/**
	 * 泛型测试
	 */
	public static void genericTest(){
		Demo<String> d = new Demo<String>();
		
		d.show("hahah");
		d.print("hehhe");
		
		Demo<Integer> d1 = new Demo<Integer>();
		d1.show(new Integer(5));
		d1.print(4);
		
		System.out.println("===============");
		
		MethGen mg = new MethGen();
		
		mg.show("heihei");
		mg.show(4);
		
		mg.print("hiahia");
		mg.print(new Integer(5));
		
		
		System.out.println("===============");
		
		ClasMethGen<String> cm = new ClasMethGen<String>();
		cm.show("hahaha");
		//cm.show(4);	//报错	
		
		cm.print("heheheheh");
		cm.print(4);
		
		ClasMethGen.stat("hehehehehe");//直接通过类名调用静态方法；
		ClasMethGen.stat(5);
	}
	/**
	 * 接口泛型的测试
	 */
	public void interfaceGenericTest(){
		InterImpl<Comparable> in = new InterImpl<Comparable>();
		in.show("hehehe");
		in.show(3.23f);
		
		System.out.println("==============");
		
		InterImpl2<Object> in2 = new InterImpl2<Object>();
		in2.show("hehehe");
		in2.show(3.23f);
	}
	/**
	 * 泛型限定测试
	 */
	public static void genericLimitedTest(){
		ArrayList<Per> al = new ArrayList<Per>();		
		al.add(new Stu("zhangsan",24));
		al.add(new Per("lisi",25));
		al.add(new Doc("wangwu",28));
		printArrayList(al);
	}
	
	public static void printArrayList(ArrayList<? extends Per> al) {		
		Iterator<? extends Per> it = al.iterator();
		while(it.hasNext()) {			
			System.out.println(it.next());
		}		
	}

}

/**
 * 类上定义泛型
 * @param <T>
 */
class Demo <T> {
	
	public void show(T t) {
		System.out.println("show: " + t);
	}

	public void print(T t) {
		System.out.println("print: " + t);
	}	
}

/**
 * 方法上定义泛型
 */
class MethGen {
	public <T> void show(T t) {
		System.out.println("show: " + t);
	}
	public <Q> void print(Q q) {
		System.out.println("print: " + q);
	}	
}


/**
 * 类上、普通方法、静态方法上定义泛型
 * @param <T>
 */
class ClasMethGen<T> {
	public void show(T t) {
		System.out.println("show: " + t);
	}
	public <Q> void print(Q q) {
		System.out.println("print: " + q);
	}	
	
	/**
	 * 静态方法不接受类上方法的限制；需要自定义泛型；
	 */
	public static <W> void stat(W w) {
		System.out.println("static: " + w);
	}
}

/**
 * 泛型在接口上的应用
 */
interface Inter<T> {
	public void show(T t);
}

class InterImpl<String> implements Inter<String> {
	public void show(String t) {
		System.out.println("InterImpl: " +t);
	}
}

class InterImpl2<T> implements Inter<T> {
	public void show(T t) {
		System.out.println("InterImpl2: " +t);
	}
}

/**
 * 测试泛型限定
 * 父类Per
 */
class Per {
	private String name;
	private int age;
	
	Per(String name,int age) {
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}

	public String toString() {
		return this.getClass().getName()+": "+"[name=" + name + ", age=" + age + "]";
	}
	
}

/**
 * Stu：Per的一个子类
 */
class Stu extends Per {
	Stu(String name,int age) {
		super(name, age);
	}
}

/**
 * Doc：Per的一个子类
 */
class Doc extends Per {
	Doc(String name,int age) {
		super(name, age);
	}
}