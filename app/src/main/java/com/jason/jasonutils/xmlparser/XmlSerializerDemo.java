package com.jason.jasonutils.xmlparser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;
import android.util.Xml;

public class XmlSerializerDemo {

	public static void backUpSms(File file, List<SmsInfoBean> smsInfos) throws Exception{
		
		FileOutputStream fos = new FileOutputStream(file);
		
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(fos, "utf-8"); 
		
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		
		for(SmsInfoBean info: smsInfos){
			serializer.startTag(null, "sms");
			serializer.attribute(null, "id", String.valueOf(info.getId()));
			
			serializer.startTag(null, "body");
			serializer.text(info.getBody());
			serializer.endTag(null, "body");
			
			serializer.startTag(null, "type");
			serializer.text(info.getType()+"");
			serializer.endTag(null, "type");
			
			serializer.startTag(null, "number");
			serializer.text(info.getNumber());
			serializer.endTag(null, "number");
			
			serializer.endTag(null, "sms");
		}
		
		serializer.endTag(null, "smss");
		serializer.endDocument();
		
		fos.flush();
		fos.close();
	}
}
