package com.jason.jasonutils.enhance.annotation;

import java.lang.reflect.Array;

import com.jason.jasonutils.enhance.enumdemo.EnumDemo.TrafficLamp;

/**
 * 注解：Annotation
 * ================================================
 * 知识点：
 * 
 * 定义注解：@interface
 * 定义注解属性；
 * 注解作用区域；
 * 注解存在阶段；
 * 
 * 注：Annotation是JDK1.5新特性
 * ================================================
 * @SuppressWarnings("deprecation")//压缩警告 ;(SOURCE)
 * 告诉编译器不要提醒，自己已经知道
 * 
 * @Deprecated  //过时警告 (RUNTIME)
 * 告诉编译器，提醒调用者该方法已经过时；
 * 
 * @Override	//覆写警告(SOURCE)
 * 告诉编译器，提醒是否覆写父类方法；
 * ===================================================
 * @Retention	//定义注解存在阶段：
 * 包含三个阶段：
 * (RetentionPolicy.SOURCE)
 * (RetentionPolicy.CLASS)
 * (RetentionPolicy.RUNTIME)
 * 
 * @Target({ElementType.METHOD,ElementType.TYPE})
 * 作用目标：方法，类，成员变量...
 * ===================================================
 * 自定义一个注释：ItcastAnnotation
 * 1.自定义注解(@interface)
 * 2.在已知类上加载自定义注释；
 * 3.通过反射来分析已知类是否有自定义注释，并处理；
 * ===================================================
 * 需要熟悉的几个类和接口：
 * 
 * 1.java.lang下面的三个注解类型
 * Deprecated 
 * Override 
 * SuppressWarnings
 * 
 * 2.java.lang.annotation 下：
 * 
 * 接口：
 * Annotation
 * 
 * 枚举：
 * ElementType 
 * RetentionPolicy
 * 
 * 注释类型  ：
 * Documented 
 * Inherited 
 * Retention 
 * Target
 * 
 * ====================================================
 * 注解的属性返回值类型有哪些，应该怎么取出打印该返回值
 * ====================================================
 * 注解知识到此，告一段路。
 */

@JasonAnnotation(clazz=Integer.class,color="red",value="abc",arr={3,5,7,9},arr2=1,lamp=TrafficLamp.GREEN,annotationArr=@MetaAnnotation("wey"))
public class AnnotationDemo {

	@SuppressWarnings("deprecation")//压缩警告
	@JasonAnnotation(value = "dcd",arr2=3)
	
	public static void main(String[] args)  {
		
		if(AnnotationDemo.class.isAnnotationPresent(JasonAnnotation.class)) {
			
			JasonAnnotation annotation = AnnotationDemo.class.getAnnotation(JasonAnnotation.class);
			
			System.out.println(annotation.color());
			System.out.println(annotation.value());
			System.out.println(annotation.arr().length);
		
			for(int i=0;i<annotation.arr().length;i++) {
				System.out.println(Array.get(annotation.arr(),i));
			}
			
			System.out.println(annotation.lamp().nextLamp());
			System.out.println(annotation.annotationArr());
			System.out.println(annotation.annotationArr().value());
			
			System.out.println(annotation.clazz());
		}
	}
	
	@Deprecated  //过时警告
	public static void sayHello() {
		System.out.println("hi 传智播客");
	}
}
