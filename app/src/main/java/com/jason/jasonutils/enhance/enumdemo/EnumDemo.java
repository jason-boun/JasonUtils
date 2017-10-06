package com.jason.jasonutils.enhance.enumdemo;

/**
 * =============================================================================
 * 枚举类的特点：
 * 1.让该类的取值为指定的若干个值中的一个，否则为非法值；
 * 2.每一个成员(或者称为元素)都是一个该类的实例对象；
 * 定义枚举类时：
 * 1.静态成员列表必须位于第一行；
 * 2.构造方法；默认调用哪个？指定调用哪个？应该在成员列表中怎么指定？
 * =============================================================================
 * 学会：在工程文件夹中查看源文件和class文件，以及在class文件中每个类与子类的关系；
 * 理解：枚举类中成员常量通过大括号定义枚举子类的方式来定义枚举的每一个常量；
 * 		 枚举中的构造方法和抽象方法的修饰；前者必须私有化，后者可以public修饰；
 * 如果只有一个成员，则该枚举类可以作为单例设计模式使用；
 * =============================================================================
 */

public class EnumDemo  {
	
	public static void main(String[] args) {
		
		WeekDays weekday = WeekDays.WED;
		
		System.out.println("============methods=============");
		
		System.out.println(weekday.name());
		System.out.println(weekday.ordinal());
		System.out.println(weekday.getClass());

		System.out.println("========valueOf===========");
		
		WeekDays wd1 = WeekDays.valueOf("WED");	//静态方法valueOf()，将传入的字符串封装为该枚举类实例对象；
		System.out.println(wd1.name());
		System.out.println(WeekDays.valueOf("TUS").name());
		
		System.out.println("=======values=========");
		
		WeekDays[]wds = WeekDays.values();
		for(WeekDays w:wds) {
			System.out.println(w);
		}
		
		System.out.println("=======TrafficLamp Demo=========");
		
		TrafficLamp red = TrafficLamp.RED;
		TrafficLamp yellow = TrafficLamp.YELLOW;
		TrafficLamp green = TrafficLamp.GREEN;
		
		System.out.println(red.time); 
		System.out.println(yellow.time); 
		System.out.println(green.time); 
		
		System.out.println(green.nextLamp()); 
	}

	public enum WeekDays {
		
		SUN,MON,TUS(),WED(1),THU,FRI,SAT;
		
		private WeekDays(){
			System.out.println("first");
		}
		
		private WeekDays(int day){
			System.out.println("second");
		}
	}
	
	/**
	 * 较为复杂的枚举类：TrafficLamp类
	 */
	public enum TrafficLamp	{
		RED(30) {
			public TrafficLamp nextLamp() {
				return GREEN;
			}
		},
		GREEN(25) {
			public TrafficLamp nextLamp() {
				return YELLOW;
			}
		},
		YELLOW(5) {
			public TrafficLamp nextLamp() {
				return RED;
			}
		};
		
		private int time;
		
		/**
		 * 构造方法必须private修饰；
		 * @param time：包含一个私有成员变量time；
		 */
		private TrafficLamp(int time){
			this.time = time;
		}
		
		/**
		 * 抽象方法public修饰；
		 * @return
		 */
		public abstract TrafficLamp nextLamp();	
	}
}
