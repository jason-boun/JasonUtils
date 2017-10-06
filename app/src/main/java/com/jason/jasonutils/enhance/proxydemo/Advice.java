package com.jason.jasonutils.enhance.proxydemo;

import java.lang.reflect.Method;

/**
 * 标签接口，系统功能类应该实现此接口；
 */

public interface Advice {
	
	void beforeMethod(Method method);
	void afterMethod(Method method);
}
