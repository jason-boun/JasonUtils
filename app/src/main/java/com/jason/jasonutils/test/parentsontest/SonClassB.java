package com.jason.jasonutils.test.parentsontest;


public class SonClassB extends AbsBaseClass {

	@Override
	public void setA() {
		testValue = 200;
	}
	
	public void printAvalue(){
		System.out.println("SonClassB--testB:" + testValue);
	}

}
