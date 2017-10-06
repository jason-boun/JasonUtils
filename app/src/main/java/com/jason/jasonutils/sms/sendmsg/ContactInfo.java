package com.jason.jasonutils.sms.sendmsg;

import java.io.Serializable;

public class ContactInfo implements Serializable{
	
	private String name;
	private String number;
	private boolean isSelected;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	/**
	 * 电话相同视为同一个联系人
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof ContactInfo){
			return this.number==((ContactInfo)o).number;
		}else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "ContactInfo [name=" + name + ", number=" + number
				+ ", isSelected=" + isSelected + "]";
	}
	
	
	
}
