package com.jason.jasonutils.blacknumber;

public class BlackNumberBean {
	
	private String number ;
	private String mode;
	
	public BlackNumberBean(){}
	public BlackNumberBean(String number, String mode){
		setNumber(number);
		setMode(mode);
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlackNumberBean [number=" + number + ", mode=" + mode + "]";
	}

}
