package com.jason.jasonutils.xmlparser;

public class WeatherBean {
	
	private int wendu;
	private int wind;
	private String type;
	private int id;
	
	public WeatherBean() {}
	public WeatherBean(int wendu, int wind, String type, int id) {
		this.wendu = wendu;
		this.wind = wind;
		this.type = type;
		this.id = id;
	}
	
	public int getWendu() {
		return wendu;
	}
	public void setWendu(int wendu) {
		this.wendu = wendu;
	}
	public int getWind() {
		return wind;
	}
	public void setWind(int wind) {
		this.wind = wind;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Weather [wendu=" + wendu + ", wind=" + wind + ", type=" + type
				+ ", id=" + id + "]";
	}
}
