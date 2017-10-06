package com.jason.jasonutils.enhance.generic;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Vector;

/**
 * 通过反射的方式来获取某个类中方法的泛型参数列表的具体类型：
 * ==========================================================================================
 * 先用该类的字节码，通过反射getMethod方法得到具体制定方法的反射方法applyVectorMethod； 
 * 再通过applyVectorMethod方法的属性得到所有的参数类型，存入集合中，Type[] genericTypes
 * 
 * 再取出集合中的元素并强转类型ParameterizedType pType = (ParameterizedType) genericTypes[0];
 * 
 * 剩下的就是对该元素的属性进行获取：
 * pType.getRawType()
 * pType.getActualTypeArguments()[0]
 * ==========================================================================================
 * 用到的泛型API知识：
 * 
 * Class类：中的getMethod()方法
 * Method类：中的getGenericParameterTypes()方法
 * 
 * Type类：Type[] genericTypes=...
 * ParameterizedType类：中的getRawType()方法和getActualTypeArguments()[i]方法
 * ==========================================================================================
 * 泛型知识到此告一段落。
 */

public class GenericReflect {
	
	public static void main(String[] args) throws Exception {
		
		Method applyVectorMethod = GenericReflect.class.getMethod("applyVector", Vector.class);
		
		Type[] genericTypes = applyVectorMethod.getGenericParameterTypes();	//Method类中的方法：getGenericParameterTypes()
		
		ParameterizedType pType = (ParameterizedType) genericTypes[0];	//如果有多个参数，可通过遍历方式获得所有类型；
		
		System.out.println(pType.getRawType());
		System.out.println(pType.getActualTypeArguments()[0]);	//数组的方式，因为可能是<K,V>型的；
	}
	
	public static void applyVector(Vector<Date> v){
	}

}
