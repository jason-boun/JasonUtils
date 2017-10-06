package com.jason.jasonutils.collection;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * Map集合，和Collection集合并列：里边存储的都是键值对；
 * 
 * 常用方法：
 * put();get();
 * containsKey();
 * remove();
 * 
 * 两个方法：将key和value分别与集合关联：
 * mp.keySet()；(得到了key的集合，这就可以通过get方法获取对应的value，有用)
 * mp.values()；(得到了只存有values的集合)
 * 
 * 一个方法：将map中的元素转换为Entry类型（Map键值对数据结构）；(键值对数据类型)
 * mp.entrySet()
 */
public class MapDemo {
	
	public static void main(String[] args) {
		studentDemo();
		mapComplexDemo1();
		mapConplexDemo2();
	}
	
	/**
	 * map常用方法测试
	 */
	public static void mapMethodTest(){
		Map<String,String> mp = new HashMap<String,String>();
		
		mp.put("01","zhangsan");
		mp.put("02", "lisi");
		mp.put("03", "wangwu");
		
		System.out.println(mp.containsKey("02"));
		System.out.println(mp.remove("02"));
		
		System.out.println(mp);
		
		System.out.println(mp.get("03"));
		
		mp.put("04", null);
		System.out.println(mp.get("04"));
		
		System.out.println(mp.get("06"));

		mp.put("04","zhaoliu");//替换元素；
		
		System.out.println("===========Map成员与Collection存储============");
		
		Set<String> set = mp.keySet();
		Collection <String> values = mp.values();
		Set<Entry<String,String>> me = mp.entrySet();
		
		System.out.println(mp);
		System.out.println(set);
		System.out.println(values);	
		System.out.println(me);	
	
	}
	
	/**
	 * 获取Map元素的方法：keySet()
	 */
	public static void keySetTest(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("01","zhangsan");
		map.put("02", "lisi");
		map.put("03", "wangwu");
		Set<String> keySet = map.keySet();	//通过keySet方法获取所有的key，并存入Set型集合keySet中；
		Iterator<String> it = keySet.iterator();	//通过Set集合的迭代器来获取每一个key；再通过hashMap的get方法获取每个Key对应的value值。
		while(it.hasNext()) {
			String key = it.next();
			String value= map.get(key);
			System.out.println(key +" = "+value);		
		}
	}
	/**
	 * 获取Map元素的方法：entrySet()
	 */
	public static void entrySetTest(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("01","zhangsan");
		map.put("02", "lisi");
		map.put("03", "wangwu");
		
		//通过entrySet方法获取所有的键值关系对数据，并存入Set型集合entrySet中；
		Set<Entry<String, String>> entrySet = map.entrySet();	
		
		//通过Set集合的迭代器来获取每一个键值对应对数据；
		//再通过Map.Entry的getKey()和getValue()方法获取每个Key以及对应的value值。
		Iterator<Entry<String, String>> it = entrySet.iterator();	
		while(it.hasNext()) {
			Entry<String, String> me = it.next();
			String key= me.getKey();
			String value= me.getValue();
			System.out.println(key +" = "+value);		
		}
	}

	/**
	 * 测试Bean：Student
	 */
	public static class Student implements Comparable<Student> {
		private String name;
		private int age;
		
		public Student(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public int hashCode() {
			return this.name.hashCode()+this.age*37;
		}
		
		public boolean equals(Object obj) {
			if (!(obj instanceof Student)){
				throw new ClassCastException("比较类型不匹配");
			}
			Student s = (Student)obj;
			return this.name.equals(s.name)&& this.age == s.age;
		}
		
		@SuppressLint("UseValueOf")
		public int compareTo(Student s) {
			int num = new Integer(this.age).compareTo(new Integer(s.age));
			if (num ==0){
				return this.name.compareTo(s.name);	
			}
			return num;
		}
		
		public String getName()  {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge()  {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString()  {
			return name + "-" + age;
		}
	}
	/**
	 * 学生对象和所在地可以形成一个Map映射关系；
	 */
	public static void studentDemo(){
		HashMap<Student,String> hm = new HashMap<Student,String>();
		hm.put(new Student("ZhangSan",21), "beijing");
		hm.put(new Student("ZhangSan",21), "tianjin");	//key相同，会替代之前存进的元素。
		hm.put(new Student("LiSi",23), "shanghai");
		hm.put(new Student("WangWu",27), "guangzhou");
		hm.put(new Student("ZhaoLiu",28), "shenzhen");
		
		//第一种取出方式：
		Set<Student>keySet = hm.keySet();
		Iterator<Student> it = keySet.iterator();
		while(it.hasNext()) {
			Student stu = it.next();
			String value = hm.get(stu);
			System.out.println(stu + " : "+value);
		}
		
		System.out.println("==========================================");
		
		//第二种取出方式：
		Set<Entry<Student,String>> entrySet = hm.entrySet();	
		Iterator<Entry<Student, String>> iter = entrySet.iterator();		
		while(iter.hasNext()) {
			Entry<Student, String> me = iter.next();
			Student stu = me.getKey();
			String val = me.getValue();
			System.out.println(stu + " : "+val);		
		}
		
		System.out.println("==========================================");
		
		//用TreeSet存储学生对象，并对其按年龄排序打印。
		
		TreeSet <Student> ts = new TreeSet <Student>(new myComparator2());	//按学生姓名排序
		//TreeSet <Student> ts = new TreeSet <Student>(new myComparator());	//按学生年龄排序	
		ts.add(new Student("ZhangSan",21));
		ts.add(new Student("ZhangSan",21));
		ts.add(new Student("LiSi",23));
		ts.add(new Student("WangWu",27));
		ts.add(new Student("ZhaoLiu",28));

		Iterator<Student> iterator = ts.iterator();
		while(iterator.hasNext()) {
			Student s =iterator.next();
			System.out.println(s);
		}
		
		System.out.println("==========================================");
		
		TreeMap<Student,String> tm = new TreeMap<Student,String>(new myComparator2());
		tm.put(new Student("ZhangSan",21), "beijing");
		tm.put(new Student("bZhangSan",21), "tianjin");	//key相同，会替代之前存进的元素。
		tm.put(new Student("LiSi",23), "shanghai");
		tm.put(new Student("aWangWu",27), "guangzhou");
		tm.put(new Student("bWangWu",27), "guangzhou");
		tm.put(new Student("ZhaoLiu",28), "shenzhen");
		
		Set<Entry<Student, String>> entrySet2 = tm.entrySet();	
		Iterator<Entry<Student, String>> it2 = entrySet2.iterator();
		while(it2.hasNext()) {
			Entry<Student,String> me2 = it2.next();
			Student stu = me2.getKey();
			String s = me2.getValue();
			System.out.println(stu+" : "+s);			
		}		
	
	}
	
	/**
	 * 自定义比较器：排序主要条件：年龄
	 */
	static class myComparator implements Comparator<Student> {
		public int compare(Student s1,Student s2) {
			int num = new Integer(s1.getAge()).compareTo(new Integer(s2.getAge()));
			if (num == 0)
				return s1.getName().compareTo(s2.getName());
			return num;
		}
	}

	/**
	 * 自定义比较器：排序主要条件：姓名
	 */
	static class myComparator2 implements Comparator<Student> {
		public int compare(Student s1,Student s2) {
			int num = s1.getName().compareTo(s2.getName());				
			if (num == 0)
				return new Integer(s1.getAge()).compareTo(new Integer(s2.getAge()));
			return num;
		}
	}
	
	/**
	 * map容器复杂使用
	 */
	public static void mapComplexDemo1(){
		HashMap<String,HashMap<String,String>> xwtx =new HashMap<String,HashMap<String,String>>();
		HashMap<String,String> ydhl =new HashMap<String,String>();
		HashMap<String,String> zdcp =new HashMap<String,String>();
		HashMap<String,String> yfzx =new HashMap<String,String>();
		
		xwtx.put("ydhl", ydhl);
		xwtx.put("zdcp", zdcp);
		xwtx.put("yfzx",yfzx);
		
		ydhl.put("01", "zhangsan");
		ydhl.put("02", "lisi");
		
		zdcp.put("01", "wangwu");
		zdcp.put("02", "zhaoliu");
		
		yfzx.put("01","zhouqi");
		yfzx.put("02", "wangba");
						
		Set<String> keySet = xwtx.keySet();	
		Iterator<String> it = keySet.iterator();
		while(it.hasNext()) {
			String banji = it.next();			
			HashMap<String, String> jiaoshi = xwtx.get(banji);

			System.out.println(banji+":");//打印记录每一个班级名称
			
			Set<Entry<String, String>> entrySet = jiaoshi.entrySet();			
			Iterator<Entry<String, String>> it2 = entrySet.iterator();
			while(it2.hasNext()) {
				Entry<String, String> me = it2.next();	
				String id = me.getKey();
				String name = me.getValue();
				
				System.out.println(id+"--"+name);//打印每个教室中的学生名单			
			}			
		}
	}
	/**
	 * map容器实际使用Demo
	 */
	public static void mapConplexDemo2(){
		HashMap<String,List<Cooker>> xwtx =new HashMap<String,List<Cooker>>();
		
		ArrayList<Cooker> ydhl = new ArrayList<Cooker>();
		ArrayList<Cooker> xtzd = new ArrayList<Cooker>();
		ArrayList<Cooker> yfzx = new ArrayList<Cooker>();
		
		xwtx.put("ydhl", ydhl);
		xwtx.put("xtzd", xtzd);
		xwtx.put("yfzx",yfzx);
		
		ydhl.add(new Cooker("01", "zhangsan"));
		ydhl.add(new Cooker("02", "lisi"));
		
		xtzd.add(new Cooker("01", "wangwu"));
		xtzd.add(new Cooker("02", "zhaoliu"));
		
		yfzx.add(new Cooker("01", "zhouqi"));
		yfzx.add(new Cooker("02", "wangba"));
				
		Iterator<String> it = xwtx.keySet().iterator();
		while(it.hasNext()) {
			String s = it.next();		
			System.out.println(s+":");
			
			Iterator<Cooker> it2 =xwtx.get(s).iterator();
			while(it2.hasNext()) {
				System.out.println(it2.next());
			}			
			System.out.println();
		}		
	}
	
	static class Cooker {
		private String name;
		private String id;
		
		Cooker(String id,String name) {
			this.id = id;
			this.name = name;
		}
		public String toString(){
			return id + "-" + name;
		}	
	}
}

