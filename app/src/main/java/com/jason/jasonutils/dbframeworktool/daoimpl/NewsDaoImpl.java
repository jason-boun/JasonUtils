package com.jason.jasonutils.dbframeworktool.daoimpl;

import android.content.Context;

import com.jason.jasonutils.dbframeworktool.base.DaoSupport;
import com.jason.jasonutils.dbframeworktool.bean.News;
import com.jason.jasonutils.dbframeworktool.dao.NewsDao;

public class NewsDaoImpl extends DaoSupport<News> implements NewsDao {

	public NewsDaoImpl(Context context) {
		super(context);
	}

}
