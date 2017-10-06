package com.jason.jasonutils.msgqueue;

import java.util.LinkedList;
/**
 * 通过包装LinkedList来模拟具有FIFO特性的队列
 * @author bo.jia
 * @param <T>
 */
public class MyQueue<T> {
	
	private LinkedList<T> msgQueue;
	
	public MyQueue(){
		this.msgQueue = new LinkedList<T>();
	}
	
	public void add(T t){
		msgQueue.addLast(t);
	}
	
	public T remove(){
		if(msgQueue.isEmpty()){
			return null;
		}
		return msgQueue.removeFirst();
	}
	
	public boolean isEmpty(){
		return msgQueue.isEmpty();
	}
}
