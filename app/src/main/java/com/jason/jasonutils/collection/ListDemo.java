package com.jason.jasonutils.collection;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class ListDemo {
	
	/**
	 * 通过迭代器读取ArrayList集合数据
	 */
	public static void iteratorDemo(ArrayList<Object> list){
		for(Iterator<Object> it = list.iterator(); it.hasNext();){
			it.next();
		}
	}
	/**
	 * 通过迭代器读取ArrayList集合数据
	 */
	public static void iteratorDemo2(ArrayList<Object> list){
		Iterator<Object> it = list.iterator();
		while(it.hasNext()){
			it.next();
		}
	}
	/**
	 * Vector迭代Demo
	 */
	public static <T> void vectorDemo(Vector<T> ve){
		Enumeration<T> en = ve.elements();
		while(en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}
	}
	
	/**
	 * 自定义一个队列数据结构类：先进先出：First in first out ：FIFO
	 */
	public class DuiLie<T>{
		private LinkedList<T> link;
		public DuiLie(){
			link = new LinkedList<T>();
		}
		public void push (T t){
			link.addFirst(t);
		}
		public void pop(){
			link.removeLast();
		}
		public boolean isEmpty(){
			return link.isEmpty();
		}
	}
	
	/**
	 * 自定义一个堆栈数据结构类：先进后出：First in last out：FILO
	 */
	public class DuiZhan<T>{
		private LinkedList<T> link;
		public DuiZhan(){
			link = new LinkedList<T>();
		}
		public void push (T t){
			link.addFirst(t);
		}
		public void pop(){
			link.removeFirst();
		}
		public boolean isEmpty(){
			return link.isEmpty();
		}
	}
}
