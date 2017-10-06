package com.jason.jasonutils.enhance.proxydemo;

import java.lang.reflect.Method;

/**
 * 动态代理类的系统功能类：
 */

public class MyAdvice implements Advice  {
	
	long startTime=0;
	
	public void beforeMethod(Method method) {
		
		System.out.println("程序开始运行");
		startTime = System.currentTimeMillis();	
	}

	public void afterMethod(Method method) {
		
		long endTime = System.currentTimeMillis();
		System.out.println(method.getName()+"方法运行时间："+(endTime-startTime)+"ms");
		System.out.println("程序结束运行");
	}
}
