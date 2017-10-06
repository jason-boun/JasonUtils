package com.jason.jasonutils.enhance.beansdemo;

import java.util.Date;

public class ObjectDemo {
	
	private Date birthday = new Date();
	private int x ;
	private int y ;
	
	ObjectDemo(int x,int y) {
		this.x = x;
		this.y= y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "ObjectDemo [x=" + x + ", y=" + y + "]";
	}
}
