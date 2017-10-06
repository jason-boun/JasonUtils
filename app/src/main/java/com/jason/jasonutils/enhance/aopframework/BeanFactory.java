package com.jason.jasonutils.enhance.aopframework;

import java.io.InputStream;
import java.util.Properties;

import com.jason.jasonutils.enhance.proxydemo.Advice;

/**
 * ②
 * javaBean工厂：
 * ================================================================================
 * 该Bean工厂类初始化时，接收一个配置文件读取流，读取配置信息；
 * 
 * 如果是接口的普通子类，则直接通过该类字节码newInstance一个实例对象，并返回该实例对象；
 * 
 * 如果是接口的动态代理类，则通过ProxyFactoryBean类调用其getProxy()方法，
 * 创建一个动态类后返回该动态类对象；并且在该实例对象中封装了系统功能；
 * ================================================================================
 */

public class BeanFactory {
	
	private Properties props = new Properties();
	
	public BeanFactory(InputStream ips)throws Exception {
		props.load(ips);
	}
	
	public Object getBean(String name)throws Exception {
		
		String className = props.getProperty(name);		
		Class<?> clazz = Class.forName(className);
		Object bean = clazz.newInstance();//重点1：JavaBean工厂：通过配置文件获取到的类名信息，通过clazz.newInstance()创建实例对象
		
		if(bean instanceof ProxyFactoryBean) {
			ProxyFactoryBean proxyFactoryBean = (ProxyFactoryBean)bean;
			
			Advice advice = (Advice)Class.forName(props.getProperty(name + ".advice")).newInstance();
			Object target = Class.forName(props.getProperty(name + ".target")).newInstance();
			
			proxyFactoryBean.setAdvic(advice);
			proxyFactoryBean.setTarget(target);			
			Object proxy = proxyFactoryBean.getProxy();//重点2：创建动态代理类的对象
			
			return proxy;
		}
		return bean;
	}
}
