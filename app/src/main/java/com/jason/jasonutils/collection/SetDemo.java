package com.jason.jasonutils.collection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;


/**
 * ArrayList判断元素唯一性的依据：equals()方法；
 * HashSet判断元素唯一性：hashCode()+equals()两个方法结合；
 * TreeSet判断元素唯一性的依据：自定义类实现Comparable下的compareTo()方法，或者传递自定义比较器(覆写Comparator下的compare()方法)
 * 
 * 总结两点：
 * 1.无论是自定义类通过覆写Comparable接口的compareTo()方法来具有自然比较性，还是容器传入比较器来具有容器自身比较性；
 * 	二者内部比较的实质都是用元素封装成自身类对象，使其具有compareTo性质来决定；
 * 2.名字：this.Name.compareTo(s.name)；年龄：new Integer(this.age).compareTo(new Integer(s.age))；
 * 3.元素通过自身类型的compareTo性质返回的值类型是int型(-1,0,+1)；
 * 4.equals方法则不同，它的返回值类型为boolean型；(this.name.equals(s.name)；this.age.equals(s.age))；
*/
public class SetDemo {
	
	public static void main(String[] args) {
		//hashSetDemo();
		treeSetDemo();
	}
	
	/**
	 * HashSet存储对象唯一性的判断过程:
	 * 先算出哈希值，如hashSet容器里没有，则直接存入；
	 * 如该哈希值已存在则继续调用该对象的equals方法判断是否相同，来确定是否存入。
	 * 注：hashSet存储数据是唯一的，但对数据的取出时无序的。
	 */
	public static void hashSetDemo(){
		HashSet<Person> hashSet = new HashSet<SetDemo.Person>();
		hashSet.add(new Person("张三", 11));
		hashSet.add(new Person("李四", 15));
		hashSet.add(new Person("王五", 18));
		hashSet.add(new Person("赵六", 20));
		hashSet.add(new Person("赵六", 20));
		
		System.out.println(hashSet.size());
		
		Iterator<Person> it = hashSet.iterator();
		while(it.hasNext()){
			System.out.println(it.next().toString());
		}
	}
	
	/**
	 * TreeSet，通过底层二叉树数据结构对存储的数据进行排序
	 * 所以存储到TreeSet集合中数据必须自身具备比较性(即该Bean类实现Comparable接口的compareTo方法)
	 * 如果想要按照自己的想要的方式进行排序，还需要对集合传递一个自定义的比较器(自定义比较器实现Comparator接口的compare方法)来实现
	 */
	public static void treeSetDemo(){
		TreeSet<Person> ts = new TreeSet<Person>(new MyComparator());
		ts.add(new Person("lisi", 26));
		ts.add(new Person("wangwu", 31));
		ts.add(new Person("zhangsan", 38));
		System.out.println(ts);
	}
	
	/**
	 * 自定义测试Bean，实现Comparable接口及其compareTo方法，元素自身具有比较性
	 */
	static public class Person implements Comparable<Person>{
		
		private String name;
		private int age;
		
		/**
		 * 构造函数
		 */
		public Person(String name,int age ){
			this.name = name;
			this.age = age;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Person))
				return false;
			Person com = (Person)o;
			return this.name.equals(com.name) && this.age == com.age;//name 和 age 都相同，算作同一个对象
		}
		@Override
		public int hashCode() {
			return this.name.hashCode()+ this.age*42;//name对应的hashCode，已经和字段Age，共同决定该对象的哈希值
		}

		/**
		 * 定义该类具有自然排序属性
		 */
		@Override
		public int compareTo(Person another) {
			int result = this.name.compareTo(another.name);//主要排序因素：name
			if(result == 0){
				return Integer.valueOf(this.getAge()).compareTo(Integer.valueOf(another.getAge()));//次要排序因素：age
			}
			return result;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}
	}
	
	
	/**
	 * 自定义比较器
	 */
	public static class MyComparator implements Comparator<Person>{
		@Override
		public int compare(Person lhs,Person rhs) {
			int num = lhs.getName().compareTo(rhs.getName());
			if(num == 0){
				num = Integer.valueOf(lhs.getAge()).compareTo(Integer.valueOf(rhs.getAge()));
			}
			return num;
		}
	}

}
