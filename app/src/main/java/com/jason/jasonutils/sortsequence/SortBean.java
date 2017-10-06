package com.jason.jasonutils.sortsequence;

public class SortBean {
	
	private String name;
	private Long number;
	private String pinYin;
	private byte[] photo;

	public SortBean(){};
	public SortBean(String name,Long number,String pinYin,byte[] photo){
		this.name = name;
		this.number = number;
		this.pinYin = pinYin;
		this.photo = photo;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	@Override
	public String toString() {
		return "SortBean [name=" + name + ", number=" + number + ", pinYin="
				+ pinYin + "]";
	}
}
