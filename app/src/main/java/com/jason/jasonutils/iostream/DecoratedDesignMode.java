package com.jason.jasonutils.iostream;

/**
 * ==================================================================================================================
 * 包装设计模式：
 * 
 * 当想要对已有的类对象功能增强时，可以定义一个类，
 * 将已有类对象传入，基于已有功能，并提供加强功能；
 * 定义的这个类叫做——包装类。
 * =================================================================================================================== 
 * 包装设计模式：
 * MyReader//专门读取数据的类
 * 	|--MyTextReader
 * 	|--MyMediaReader
 *	|--MyDataReader
 *	|--MyBufferedReader (这个类就是装饰类)
 *
 *	(以组合的形式出现：即如果MyReader不存在了，MyBufferedReader也就不存在了，但是后者存在与否不影响前者的存在。)
 *	(那么，有个问题：装饰类究竟需要不需要继承被装饰的类，组合和继承的关系又是怎么样的呢？)
 *	(MyBufferedReader与MyReader是子父类的关系，但是与其他三个子类则是装饰关系。)
 *	
 *	找到子类的共同参数，通过多态的形式，极大地提高了扩展性:
 *	class MyBufferedReader extends MyReader
 *	{
 *		private MyReader r;
 *		MyBufferedReader(MyReader r)
 *		{}
 *	}
 *	
 * 装饰类比继承类要灵活，避免了继承体系的臃肿，降低了类与类之间的关系；
 * 装饰类增强了已有对象，且具备的功能和已有类相同的，只不过提供了更为强大的功能；
 * 所以装饰类和被装饰类通常都是一个体系的。
 * ====================================================================================================================
 */


public class DecoratedDesignMode 
{
	public static void main(String[] args) 
	{
		Person p = new Person();
		SuperPerson sp = new SuperPerson(p);
		sp.superMeal();
	}

}

class Person {
	public void meal() {
		System.out.println("吃饭");
	}
}

class SuperPerson {
	
	private Person p;
	SuperPerson(Person p) {
		this.p = p;
	}
	
	public void superMeal() {
		
		System.out.println("开胃酒");
		p.meal();
		System.out.println("甜点");
		System.out.println("水果");
		System.out.println("一根烟");
	}
}