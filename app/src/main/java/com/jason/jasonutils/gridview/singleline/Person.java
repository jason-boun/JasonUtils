package com.jason.jasonutils.gridview.singleline;

import java.io.Serializable;

public class Person implements Serializable{
	
	private static final long serialVersionUID = 42L;

	private String jid;
	private String name;
	private String nickname;
	private String number;
	private byte[] photo;
	private boolean isChecked;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	@Override
	public String toString() {
		return "Person [jid=" + jid + ", name=" + name + ", nickname="
				+ nickname + ", number=" + number + ", isChecked=" + isChecked
				+ "]";
	}
	
}
