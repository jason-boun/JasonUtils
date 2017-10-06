package com.jason.jasonutils.xmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class XmlSerialParserActivity extends Activity implements OnClickListener {
	
	private Button bt_xml_serial,bt_xml_parser;
	private TextView tv_show_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xml_serail_parser_demo_activity);
		
		init();
	}

	private void init() {
		bt_xml_serial = (Button) findViewById(R.id.bt_xml_serial);
		bt_xml_parser = (Button) findViewById(R.id.bt_xml_parser);
		tv_show_result = (TextView) findViewById(R.id.tv_show_result);
		
		bt_xml_serial.setOnClickListener(this);
		bt_xml_parser.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_xml_serial:
			xmlSerialSmsInfo();
			break;
		case R.id.bt_xml_parser:
			pullParserSmsInfo();
			break;
		}
	}
	
	/**
	 * 将Bean数据序列化保存为本地XML文件数据
	 */
	public void xmlSerialSmsInfo(){
		List<SmsInfoBean> smsInfos = new ArrayList<SmsInfoBean>();
		SmsInfoBean sms1 = new SmsInfoBean("短信1", "123456", 2, 666);
		SmsInfoBean sms2 = new SmsInfoBean("短信2", "123789", 1, 999);
		smsInfos.add(sms1);
		smsInfos.add(sms2);
		
		File file = new File(this.getFilesDir(),"smsback.xml");
		try {
			XmlSerializerDemo.backUpSms(file, smsInfos);
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析SMS的XML文件，封装Bean数据
	 */
	public void pullParserSmsInfo(){
		try {
			StringBuilder sb = new StringBuilder();
			FileInputStream fis = new FileInputStream(new File(this.getFilesDir(),"smsback.xml")) ;
			List<SmsInfoBean> smsList = PullParserDemo.getSmsBean(fis);
			for(SmsInfoBean sms:smsList){
				sb.append(sms.toString());
				sb.append("\n");
			}
			tv_show_result.setText(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析Weather的XML文件，封装Bean数据
	 */
	public void pullParserWeatherInfo(){
		try {
			StringBuilder sb = new StringBuilder();
			List<WeatherBean> weatherList = PullParserDemo.getWeather(getClassLoader().getResourceAsStream("com/jason/jasonutils/xmlparser/weather.xml"));
			for(WeatherBean wb:weatherList){
				sb.append(wb.toString());
				sb.append("\n");
			}
			tv_show_result.setText(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
