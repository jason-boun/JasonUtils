package com.jason.jasonutils.xmlparser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class PullParserDemo {	
	
	/**
	 * 解析获取天气信息
	 */
	public static List<WeatherBean> getWeather(InputStream is) throws Exception {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int eventType = parser.getEventType();
		
		List<WeatherBean> weatherInfos = null;
		WeatherBean weatherInfo = null;
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("weather".equals(parser.getName())) {
					weatherInfos = new ArrayList<WeatherBean>();
				} else if ("day".equals(parser.getName())) {
					weatherInfo = new WeatherBean();
					String id = parser.getAttributeValue(0);
					weatherInfo.setId(Integer.parseInt(id));
				} else if ("wendu".equals(parser.getName())) {
					String wendu = parser.nextText();
					weatherInfo.setWendu(Integer.parseInt(wendu));
				} else if ("wind".equals(parser.getName())) {
					String wind = parser.nextText();
					weatherInfo.setWind(Integer.parseInt(wind));
				} else if ("type".equals(parser.getName())) {
					String type = parser.nextText();
					weatherInfo.setType(type);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("day".equals(parser.getName())) {
					weatherInfos.add(weatherInfo);
				}
				break;
			}
			eventType = parser.next();// 控制解析器 解析下一个节点
		}
		is.close();
		return weatherInfos;
	}
	
	/**
	 * 将短信数据流解析封装为SmsInfoBean
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static List<SmsInfoBean> getSmsBean(InputStream is) throws Exception{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int eventType = parser.getEventType();
		
		List<SmsInfoBean> SmsList = null;
		SmsInfoBean SmsInfoBean = null;
		
		while(eventType!=XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.START_TAG:
				if("smss".equals(parser.getName())){
					SmsList = new ArrayList<SmsInfoBean>();
				}else if("sms".equals(parser.getName())){
					SmsInfoBean = new SmsInfoBean();
					SmsInfoBean.setId(Integer.parseInt(parser.getAttributeValue(0)));
				}else if("body".equals(parser.getName())){
					SmsInfoBean.setBody(parser.nextText());
				}else if("type".equals(parser.getName())){
					SmsInfoBean.setType(Integer.parseInt(parser.nextText()));
				}else if("number".equals(parser.getName())){
					SmsInfoBean.setNumber(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if("sms".equals(parser.getName())){
					SmsList.add(SmsInfoBean);
				}
				break;
			}
			eventType = parser.next();
		}
		is.close();
		return SmsList;
	}
}
