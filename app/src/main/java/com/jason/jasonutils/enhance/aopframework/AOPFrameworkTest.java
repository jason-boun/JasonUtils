package com.jason.jasonutils.enhance.aopframework;

import java.io.InputStream;
import java.util.Collection;

/**
 * ①
 * 通过配置文件中的信息切换；BeanFactory、ProxyFactoryBean实现一个AOP小框架；
 * ===========================================================================
 * 框架的功能及原理：
 * 
 * 通过本类的加载器加载配置文件；将加载流传给BeanFactory类，
 * 调用该类的getBean()方法；
 * 如果是接口的普通子类，则直接返回子类对象；
 * 如果是接口的动态代理类，则通过ProxyFactoryBean创建一个动态类后返回该动态类对象；
 * ============================================================================
 */

public class AOPFrameworkTest {
	
	public static void main(String[] args) throws Exception {
		
		InputStream ips = AOPFrameworkTest.class.getResourceAsStream("config.properties");
		Object bean = new BeanFactory(ips).getBean("xxx");
		System.out.println(bean.getClass().getName());
		
		System.out.println("===========调用该bean对象的方法===========");
		
		InputStream ips1 = AOPFrameworkTest.class.getResourceAsStream("config.properties");
		Collection bean1 = (Collection)new BeanFactory(ips1).getBean("xxx");
		bean1.toString();
		bean1.add("lixiaoming");
		System.out.println(bean1.size());		
	}
}
