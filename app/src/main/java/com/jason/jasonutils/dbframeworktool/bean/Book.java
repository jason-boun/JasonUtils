package com.jason.jasonutils.dbframeworktool.bean;

import com.jason.jasonutils.dbframeworktool.annotation.Coloumn;
import com.jason.jasonutils.dbframeworktool.annotation.ID;
import com.jason.jasonutils.dbframeworktool.annotation.TableName;

@TableName("book")
public class Book {
	@Coloumn("_id")
	@ID(autoincrement=true)
	private long id;
	
	@Coloumn("name")
	private String name;
	
}
