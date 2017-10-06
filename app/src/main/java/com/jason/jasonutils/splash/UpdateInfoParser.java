package com.jason.jasonutils.splash;

import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class UpdateInfoParser {
	/**
	 * 解析 获取更新信息
	 * @param is 服务器返回的流
	 * @return 更新信息对象 如果返回null 代表解析失败
	 */
	public static UpdateInfoBean getUpdateInfo(InputStream is) {
		XmlPullParser parser = Xml.newPullParser();
		UpdateInfoBean info = new UpdateInfoBean();
		try {
			parser.setInput(is, "UTF-8");
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						String version = parser.nextText();
						info.setVersion(version);
					} else if ("description".equals(parser.getName())) {
						String description = parser.nextText();
						info.setDescription(description);
					} else if ("path".equals(parser.getName())) {
						String path = parser.nextText();
						info.setPath(path);
					}
					break;
				}
				type = parser.next();
			}
			is.close();
			return info;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
