package com.jason.jasonutils.enhance.enumdemo;

public enum EnumTest {
	
	A(EnumConstant.enum_name,EnumConstant.enum_age);
	
	private String name;
	private int age;
	
	private EnumTest(String name, int age){
		System.out.println("构造");
		this.setName(name);
		this.setAge(age);
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		System.out.println("setName");
		this.name = name;
	}


	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "name=="+name+"  ;age=="+age;
	}
}
