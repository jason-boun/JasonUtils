package com.jason.jasonutils.dbframeworktool.bean;

import com.jason.jasonutils.dbframeworktool.annotation.Coloumn;
import com.jason.jasonutils.dbframeworktool.annotation.ID;
import com.jason.jasonutils.dbframeworktool.annotation.TableName;
import com.jason.jasonutils.dbframeworktool.base.DBConstants;

/**
 * 需要操作的JavaBean
 * 注意注解属性，该属性数据库操作的关键
 */
@TableName(DBConstants.TABLE_NEWS_NAME)
public class News {
	@Coloumn(DBConstants.TABLE_ID)
	@ID(autoincrement=true)
	private long id;
	@Coloumn(DBConstants.TABLE_NEWS_TITLE)
	private String title;
	@Coloumn(DBConstants.TABLE_NEWS_SUMMARY)
	private String summary;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
