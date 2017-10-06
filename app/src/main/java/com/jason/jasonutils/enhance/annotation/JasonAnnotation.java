package com.jason.jasonutils.enhance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jason.jasonutils.enhance.enumdemo.EnumDemo;

/**
 * 元注解：
 * 注解的属性的返回值类型：
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})

public @interface JasonAnnotation {
	//注解的属性的返回值类型：
	
	String color() default "bule";	//字符串类型属性
	String value();
	int[]arr() default {1,2,3};		//整型数组类型属性
	int[]arr2();
	
	EnumDemo.TrafficLamp lamp() default EnumDemo.TrafficLamp.RED;	//枚举类型属性
	
	MetaAnnotation annotationArr() default  @MetaAnnotation("123");	//注解类型属性
	
	Class clazz() default String.class;	//(Class)类类型属性
}
