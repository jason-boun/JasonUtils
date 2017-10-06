package com.jason.jasonutils.filter;

public class PersonBean {

	private String name;
	private int age;
	private String salary;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "PersonBean [name=" + name + ", age=" + age + ", salary="
				+ salary + "]";
	}
	
}
