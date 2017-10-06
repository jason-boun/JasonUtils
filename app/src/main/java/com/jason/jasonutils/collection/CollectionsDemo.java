package com.jason.jasonutils.collection;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * Collections：专门为操作集合封装的一个工具类，所有方法皆为静态方法。
 */
public class CollectionsDemo {
	
	public static void main(String[] args) {
		
	}
	/**
	 * toArray(Object[] obj)，可以将集合变为字符串数组，限制对集合元素的增删操作；
	 * 必须传一个新的字符串数组，而且要指定数组长度。
	 */
	public static void toArrayTest(){
		ArrayList<String> al = new ArrayList<String>();
		al.add("abc01");
		al.add("abc02");
		al.add("abc03");
		String[]a4 = al.toArray(new String[al.size()]);	//使用指定的数组
		for(String str: a4){
			System.out.println(str);
		}
	}
	
	/**
	 * Collections.swap(list, 1, 3)方法：交换指定角标的元素
	 */
	public static void swapTest(){
		List<String> list = new ArrayList<String>();
		list.add("zbc");
		list.add("mde");
		list.add("faefd");		
		list.add("bdweref");
		list.add("cdsdkfasoiff");
		list.add("kk");
		list.add("qq");
		System.out.println(list);
		Collections.swap(list, 1, 3);	//对list集合中指定位置的元素进行位置置换。
		System.out.println(list);
	}
	
	/**
	 * Collections.shuffle(list)方法：随机排列指定list中的元素（洗牌）
	 */
	public static void shuffleTest(){
		List<String> list = new ArrayList<String>();
		list.add("zbc");
		list.add("mde");
		list.add("faefd");		
		list.add("bdweref");
		list.add("cdsdkfasoiff");
		list.add("kk");
		list.add("qq");
		System.out.println(list);
		Collections.shuffle(list);	//对list集合中元素的排序进行随机排序。
		System.out.println(list);
	}
	
	/**
	 * Collections.sort(list,new myComparator());指定比较器来按需排序
	 */
	public static void sortTest(){
		List<String> list = new ArrayList<String>();
		
		list.add("zbc");
		list.add("ade");
		list.add("faefd");
		list.add("bdweref");
		list.add("cdsdkfasoiff");
		list.add("kk");
		list.add("qq");
		
		System.out.println(list);
		
		Collections.sort(list);	//按自然顺序排序
		System.out.println(list);
		
		Collections.sort(list,new myComparator());	//按指定方式排序(字符串长度)
		System.out.println(list);
	}
	
	/**
	 * Collections.max(list,new myComparator());获取排序最大的集合元素
	 */
	public static void maxTest(){
		List<String> list = new ArrayList<String>();
		list.add("zbc");
		list.add("ade");
		list.add("faefd");
		list.add("bdweref");
		list.add("cdsdkfasoiff");
		list.add("kk");
		list.add("qq");
		
		System.out.println(list);
		System.out.println(Collections.max(list));	//返回自然顺序排序的最大元素。"zbc"
		System.out.println(Collections.max(list,new myComparator()));	//返回字符串长度最大值的元素："cdsdkfasoiff"
	}
	
	/**
	 * Collections.reverseOrder();方法的演示
	 * 如果只是想将集合元素顺序逆转，直接往集合中传一个Collections.reverseOrder()方法即可。
	 * 如果想让集合以指定方式排序，则自定义一个比较器new myComparator()传给集合即可。 
	 * 如果想将指定比较器定义的方式进行逆转，则调用Collections.reverseOrder(new myComparator())即可。
	 */
	public static void reverseOrderTest(){
		TreeSet<String> ts = new TreeSet<String>(Collections.reverseOrder(new myComparator()));
		ts.add("kasf");
		ts.add("s");
		ts.add("4od");
		ts.add("wr");
		Iterator<String> it = ts.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}	
	}
	
	/**
	 * 高级for循环：
	 * 高级for循环遍历的对象是Collection集合或者数组，所以对于Map集合，必须将思路通过转换到Collection来。
	 * 注意：如果在定义集合时，没有定义泛型，则无法确定变量类型，不能用高级for循环进行读取遍历。
	 */
	public static void forDemo(){

		System.out.println("==========对集合的遍历============");
		ArrayList<String> al = new ArrayList<String>();
		al.add("abc01");
		al.add("abc02");
		al.add("abc03");
		
		//高级for循环：
		for(String s : al) {
			System.out.println(s);
		}
		//普通for循环：
		for(Iterator<String> it = al.iterator();it.hasNext();) {
			System.out.println(it.next());
		}
		
		System.out.println("==========对数组的遍历============");
		int [] a = {1,2,3};
		for(int i:a) {
			System.out.println(i);
		}
		for(int i=0;i<a.length;i++) {
			System.out.println(a[i]);
		}
		System.out.println("==========对Map的遍历============");
		/**
		 * 高级for循环遍历的对象是Collection集合或者数组，所以对于Map集合，必须将思路通过转换到Collection来。
		 * 注意：如果在定义集合时，没有定义泛型，则无法确定变量类型，不能用高级for循环进行读取遍历。
		 */
		HashMap <Integer,String> hm = new HashMap <Integer,String>();
		hm.put(1, "a");
		hm.put(2, "b");
		hm.put(3, "c");
		
		//遍历方法一：
		for(Integer i :hm.keySet()) {
			System.out.println(i + ":"+ hm.get(i));
		}
		//遍历方法二：
		for(Entry<Integer,String> i:hm.entrySet()) {
			System.out.println(i.getKey()+":"+i.getValue());
		}
	}
	
	/**
	 * 二分法查找元素演示：
	 */
	public static void BinSearchDemo(){
		List<String> list = new ArrayList<String>();
		list.add("zbc");
		list.add("mde");
		list.add("faefd");		
		list.add("bdweref");
		list.add("cdsdkfasoiff");
		list.add("kk");
		list.add("qq");
		
		Collections.binarySearch(list, "mde");//给集合排序
		System.out.println(list);

		System.out.println("--------------自定义折半查找方法--------------------");
		
		int index2 = myBinSearchDemo(list,"mde");
		System.out.println(index2);
		
		System.out.println("--------------自定义比较器进行折半查找--------------------");				
		Collections.sort(list,new myComparator());	//折半查找前先必须保证该list有序；
		System.out.println(list);
		
		int index3 = myBinSearchDemo2(list,"zbc",new myComparator());		
		System.out.println(index3);
	}
	/**
	 * 自定义二分法查找
	 * 先必须让给定的List按给定的比较器进行排序
	 * 如果没有找到匹配值，则返回-(min)-1；
	 */
	public static int myBinSearchDemo(List<String> list,String key){
		int max,min,mid;
		max= list.size()-1;
		min = 0; 
		while(min<=max) {
			mid = (min+max)>>1;//右移一位，表示除以二，得到中间角标
			String str = list.get(mid);
			int num =str.compareTo(key);
			if(num>0)
				max = mid-1;
			else if(num<0)
				min = mid+1;
			else
				return mid;	
		}	
		return -min-1;
	}
	/**
	 * 自定义折半查找二：按比较器给定的规则(如字符串长度)进行查找；传入自定义比较器.
	 */
	public static int myBinSearchDemo2(List<String> list,String key,Comparator<String> comp){
		int max,min,mid;
		max= list.size()-1;
		min = 0;
		while(min<=max) {
			mid = (min+max)>>1;
			String str = list.get(mid);
			int num =comp.compare(str, key);	//用自定义的比较器进行自定义的规则比较。
			if(num>0)
				max = mid-1;
			else if(num<0)
				min = mid+1;
			else
				return mid;		
		}		
		return -min-1;
	}
	
	/**
	 * 自定义比较器：按字符串长度排序
	 */
	static class myComparator implements Comparator<String> {
		@SuppressLint("UseValueOf")
		public int  compare(String s1,String s2) {
			int num = new Integer(s1.length()).compareTo(new Integer(s2.length()));
			if (num==0)
				return s1.compareTo(s2);
			return num;		
		}	
	}
}	
