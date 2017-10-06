package com.jason.jasonutils.enhance.reflect;


/**
 * 学习反射相关的知识所用到的测试类：ReflectPoint
 */

public class ReflectPoint {
	private int x;
	public int y;
	
	public String str1 = "ball";
	public String str2 = "basketball";
	public String str3 = "itcast";

	public ReflectPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReflectPoint other = (ReflectPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
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

	@Override
	public String toString() {
		return "ReflectPoint [str1=" + str1 + ", str2=" + str2 + ", str3=" + str3 + "]";
	}	
}
