package com.jason.jasonutils.enhance.proxydemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 代理类(动态类)的获取：Proxy类。
 * ======================================================================================
 * 理解动态类的作用：
 * 代理技术可以解决AOP(面向切面的技术编程)的问题；
 * ======================================================================================
 * 动态代理类的创建过程：
 * 
 * Proxy类的方法getProxyClass()通过指定类加载器以及要实现的接口，在JVM中创建一个代理类；
 * 得到的字节码clazzProxy0对应的代理类，其属性：是Collection和Object的子类；
 * 
 * 由以下两个方法，得到代理类的所有构造函数和普通函数列表数组：
 * clazzProxy1.getConstructors();
 * clazzProxy1.getMethods();
 * 
 * 注意：
 * 1.StringBuilder缓冲器的使用，append();delete();length();
 * 2.在遍历方法(或构造函数)的过程中用了for循环；
 * 3.在循环的过程中对于参数列表的遍历再次用到了for循环；(循环嵌套)
 * ======================================================================================
 * 理解动态代理类创建实例对象的过程：
 * 
 * 实例对象的创建是通过构造函数来实现的，所以只要分析构造函数的结构即可；
 * 理解了构造函数的结构：也就明白了为什么动态代理类可以解决AOP的问题；
 * 理解了构造函数，也就明白了动态代理类的内部结构（成员变量，构造函数）；
 * 
 * 构造函数的参数有三个：类加载器，实现的接口，InvocationHandler子类实例对象:
 * 也就说明:
 * class proxy0
 * {
 * 		InvocationHandler h；
 * 		proxy0(loader,interface,InvocationHandler h) {
 * 			this.h = h;
 * 		}		
 * 		... (接口方法) 		
 * }
 * 
 * 这里有一个重点就是，InvocationHandler本身是一个接口，
 * 该接口只有一个方法：invoke(Object proxy, Method method, Object[] args) 
 * 而invoke方法中才定义了该代理特有的系统方法(创建代理的用户根据需要自定义功能)；
 * 
 * 所以当动态代理实例对象创建后，通过实例来调用其实现接口的方法，每一个方法都会委托给invoke方法，
 * 而invoke方法添加了自身特有的系统方法，接口方法会通过反射直接调用目标(接口的子类)的方法；
 * 这样就实现了系统方法封装在每个接口方法上的最终目的！
 * 
 * (看动态代理的工作原理图会更直观的理解)
 * ======================================================================================
 * 注意，得到的类应该当做Collection来使用。
 */

public class ProxyDemo 
{
	public static void main(String[] args) throws Exception
	{
		//JDK中的代理API：java.lang.reflect.Proxy：
		Class clazzProxy0 = Proxy.getProxyClass(Collection.class.getClassLoader(),Collection.class);
		System.out.println(clazzProxy0.getName());	//该代理类的类名：$Proxy0
		
		System.out.println(String.class.getName());	//String类名：java.lang.String
		System.out.println();	//隔行
		
		System.out.println("============begin constructor list==============");
		
		Constructor [] constructors = clazzProxy0.getConstructors();
		for(Constructor con:constructors)
		{
			String name = con.getName();
			StringBuilder sb = new StringBuilder(name);	//首先存入构造函数名字；
			
			sb.append("(");
			
			Class[] clazzParams = con.getParameterTypes();
			for(Class para:clazzParams) {
				sb.append(para.getName()).append(","); //StringBuilder的级联操作；
			}
			
			if(clazzParams!=null && clazzParams.length!=0) {
				sb.deleteCharAt(sb.length()-1);	//去掉最后一个逗号；
			}						
			sb.append(")");
			
			System.out.println(sb.toString());
			System.out.println();
		}
				
		System.out.println("============begin method list==============");
		
		Method [] methods = clazzProxy0.getMethods();
		for(Method met:methods)
		{
			String name = met.getName();
			StringBuilder sb = new StringBuilder(name);	
			
			sb.append("(");
			
			Class[] clazzParams = met.getParameterTypes();
			for(Class para:clazzParams) {
				sb.append(para.getName()).append(",");
			}
			
			if(clazzParams!=null && clazzParams.length!=0) {
				sb.deleteCharAt(sb.length()-1);
			}	
			sb.append(")");
			
			System.out.println(sb.toString());
			System.out.println();
		}
		
		
		System.out.println("============第一种创建动态类实例对象方法： ==============");
		Collection proxy1 = (Collection)clazzProxy0.getConstructor(InvocationHandler.class).newInstance(new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return null;
			}
		});
		System.out.println(proxy1.toString());
		//注意，得到的类应该当做Collection来使用；
		proxy1.clear();	//没有返回值的方法；
		//proxy1.size();	//有返回值的方法；报异常；
		
		System.out.println("================第二种创建实例对象方法：newProxyInstance()==================");
		System.out.println("===============通过调用二getProxy()方法实现：创建动态类、实现系统功能=============");
		
		//参数：目标target，系统功能类MyAdvice()
		final ArrayList target = new ArrayList();		
		Collection<String> proxy3 = (Collection<String>)getProxy(target,new MyAdvice());
		
		//对创建的动态代理类实例对象proxy4,调用其实现接口的功能；
		proxy3.add("zxx");
		proxy3.add("lhm");
		proxy3.add("bxd");
		System.out.println(proxy3.size());		
		System.out.println(proxy3.getClass().getName());	
	}

	private static Object getProxy(final Object target,final Advice advice) 
	{
		Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),
				new InvocationHandler(){
					public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {			
						advice.beforeMethod(method);//系统层面的方法(AOP方案的解决)				
						Object retVal = method.invoke(target, args);//目标方法的实现
						advice.afterMethod(method);//系统层面的方法(AOP方案的解决)							
						return retVal;
					}						
				});
		return proxy;
	}	
}
