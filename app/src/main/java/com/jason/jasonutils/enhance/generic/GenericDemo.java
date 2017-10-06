package com.jason.jasonutils.enhance.generic;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/**
 * ===========================================================================
 * 泛型作用： 
 * 1.避免强转
 * 2.将错误提前暴露在编译阶段；
 * 
 * 泛型作用阶段：编译阶段，
 * 编译之后，泛型信息就被JVM去掉；
 * ===========================================================================
 * 通配符？：作为引用，可以调用任何与参数无关的方法，但不能引用有具体参数类型的方法
 * ===========================================================================
 */

public class GenericDemo {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("=============泛型在集合中的应用==============");
		
		ArrayList<String> collection = new ArrayList<String>();
		collection.add("aboidfio");
		collection.add("2983");
		//collection.add(1324);	//报错；	
		String str = collection.get(1);
		System.out.println(str);
		
		System.out.println("=============泛型在反射中的应用==============");
		
		Constructor<String> constructor1 = String.class.getConstructor(StringBuffer.class);		
		String str2 = constructor1.newInstance(new StringBuffer("abc"));//不用再强转为(String)
		System.out.println(str2.charAt(2));	
		
		System.out.println("=============泛型的作用阶段=============");
		
		ArrayList<String> arr1 = new ArrayList<String>();
		ArrayList<Integer>arr2 = new ArrayList<Integer>();
		
		System.out.println(arr1.getClass()==arr2.getClass());
		
		//那么如果跳过编译器，则可以将String类型的数据加入arr2中：通过反射的方式跳过编译器；		
		arr2.getClass().getMethod("add",Object.class).invoke(arr2, "abc");
		System.out.println(arr2.get(0));
		
		test3();
		test();
		test2();
	}
	
	
	public static void test() {
		Vector v1 = new Vector<String>();
		v1.add("abc");
		Vector<Integer> v = v1;
	
		System.out.println(v1);
		System.out.println(v);
	}

	public static void test2() throws ClassNotFoundException {
		//Class<? extends Number> c = String.class.asSubclass(Number.class);
		//c = Integer.class;

		String s = "abc";
		
		System.out.println(s.getClass());
		System.out.println(String.class);
		System.out.println(Class.forName("java.lang.String"));

	}
	
	public static void test3() {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		map.put("zhangsan", 28);
		map.put("lisi", 35);
		map.put("wangwu",33);
		
		Set<Entry<String,Integer>> entrySet = map.entrySet();
		
		//wihle循环迭代：
		Iterator<Entry<String,Integer>> it1 = entrySet.iterator();
		while(it1.hasNext()) {
			System.out.println(it1.next());
		}
		
		//普通for循环迭代：
		for(Iterator<Entry<String,Integer>> it2 = entrySet.iterator(); it2.hasNext();) {
			System.out.println(it2.next());
		}
		
		//增强for循环迭代：
		for(Entry<String,Integer> entry : entrySet) {
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
		
		for(Iterator<String> itos = map.keySet().iterator();itos.hasNext();){
			System.out.println(itos.next());
		}
	}
}
