package com.jason.jasonutils.msgqueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.os.Message;

public class MsgQueue {
	
	private final Lock lock;
	private final Condition processCondition;
	private final MyQueue<Message> myMsgQueue;
	
	public MsgQueue(){
		lock = new ReentrantLock();
		processCondition = lock.newCondition();
		myMsgQueue = new MyQueue<Message>();
	}

	public void insertMsg(Message msg){
		lock.lock();
		try{
			myMsgQueue.add(msg);
			processCondition.notifyAll();
		}finally{
			lock.unlock();
		}
	}
	
	public void processMsg(){
		lock.lock();
		try{
			while(true){
				Message msg = myMsgQueue.remove();
				if(msg == null){
					processCondition.wait();//wait阻塞后会释放锁
				}else{
					msg.getTarget().handleMessage(msg);
				}
			}
		}catch(Exception e){
			lock.unlock();
		}finally{
			lock.unlock();
		}
	}

}
