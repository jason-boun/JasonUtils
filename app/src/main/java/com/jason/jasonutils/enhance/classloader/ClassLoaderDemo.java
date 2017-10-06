package com.jason.jasonutils.enhance.classloader;



/**
 * ============================================================================================
 * 类加载器：
 * BootStrap 		--> JRE\lib\rt.jar 
 * ExtClassLoader	--> JRE\lib\ext\*.jar
 * AppClassLoader	--> ClassPath指定目录下的所有jar或者目录
 * ============================================================================================
 * 类加载器的委托机制：树状结构：
 * 
 * 调用新类的当前线程所对应的类加载器，会将加载新类的任务委托给上一级类加载器，以此类推，直至顶层，
 * 如果顶层的类加载器没有加载成功，则会发回下一级加载器去加载，以此类推，
 * 当最底层的加载器仍然没有加载成功，则会报告异常：ClassNoFoundException.
 * =============================================================================================
 * 这样的委托机制的好处：
 * 
 * 如果底层加载器遇到类就自己加载，则会出现同一个类，多次加载的现象！
 * 而委托加载机制，可以集中管理，避免了同一个类在内存中多次加载造成的内存文件混乱和内存资源浪费；
 * 
 * 所以，如果你自己写一个java.lang.System类，通常没有用，因为这个类在classPath下，
 * 而JVM的BootStrap类加载器自己会在JRE\lib\rt.jar 中找到系统的System类直接加载，自己写的那个类没用！
 * 
 * 但也可以写一个自己的类加载器，指定该加载器来加载所写的类！
 * 自己写的类成功走过委托机制后回到自己的类加载器上来加载，这就要求该类具备特有加载器才能加载的特性！
 * ==============================================================================================
 *
 */

public class ClassLoaderDemo {
	
	public static void main(String[] args) throws Exception  {
		//System.out.println(ClassLoaderDemo.class.getClassLoader().getClass().getName());
		classLoaderTree(ClassLoaderDemo.class);				
	}

	public static void classLoaderTree(Class<?> clazz) {
		ClassLoader loader = clazz.getClassLoader();
		do {
			System.out.println(loader.getClass().getName());
			loader = loader.getParent();
		}while(loader!=null);{
			System.out.println(loader);
		}
	}
}
