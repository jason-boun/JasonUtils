package com.jason.jasonutils.test.parentsontest;

public class SonClassA extends AbsBaseClass {
	

	@Override
	public void setA() {
		testValue = 100;
	}
	
	public void printAvalue(){
		System.out.println("SonClassA--testA:" + testValue);
	}

}
