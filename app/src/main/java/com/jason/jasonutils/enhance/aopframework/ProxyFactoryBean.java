package com.jason.jasonutils.enhance.aopframework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.jason.jasonutils.enhance.proxydemo.Advice;

/**
 * ③
 * 该类为创建动态类的工厂；
 * ============================================================
 * 该类包含内容：
 * 
 * 成员变量：
 * 目标类对象：Object target
 * 系统功能对象：Advice advice
 * 
 * Bean方法：
 * getXxx()
 * setXxx()
 * 
 * 重要方法：（即本类的主要功能实现方法：获取Proxy的Bean实例对象）
 * getBean()
 * =============================================================
 */

public class ProxyFactoryBean {
	
	private Advice advice;
	private Object target;
	
	public Advice getAdvic() {
		return advice;
	}
	public void setAdvic(Advice advice) {
		this.advice = advice; 
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	
	public Object getProxy() {
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(),
				target.getClass().getInterfaces(),
				new InvocationHandler(){
					public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {			
						advice.beforeMethod(method);						
						Object retVal = method.invoke(target, args);
						advice.afterMethod(method);																	
						return retVal;
					}						
				});
	}	
}
