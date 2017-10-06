package com.jason.jasonutils.xmlparser;

/**
 * 短信Bean
 */
public class SmsInfoBean {
	
	private String body;
	private String number;
	private int type;
	private long id;

	public SmsInfoBean() {}
	public SmsInfoBean(String body, String number, int type,long id) {
		this.body = body;
		this.number = number;
		this.type = type;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "SmsInfoBean [body=" + body + ", number=" + number + ", type="
				+ type + ", id=" + id + "]";
	}

}
