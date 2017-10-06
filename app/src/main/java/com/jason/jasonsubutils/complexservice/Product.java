package com.jason.jasonsubutils.complexservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{
	
	private int id;
	private String name;
	private float price;
	
	public Product(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price
				+ "]";
	}
	
	/**********************序列化该Bean类*************************/


	/**
	 * 内部类使用的构造函数
	 * @param in
	 */
	private Product(Parcel source){
		readFromParcel(source);
	}
	
	/**
	 * 必须定义的CREATOR
	 */
	public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel source) {
			return new Product(source);
		}
		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 序列化数据
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeFloat(price);
	}
	/**
	 * 读取数据
	 * @param source
	 */
	private void readFromParcel(Parcel source) {
		id = source.readInt();
		name = source.readString();
		price = source.readFloat();
	}
	
}
