package com.jason.jasonutils.dbframeworktool.daoimpl;

import android.content.Context;

import com.jason.jasonutils.dbframeworktool.base.DaoSupport;
import com.jason.jasonutils.dbframeworktool.bean.Book;
import com.jason.jasonutils.dbframeworktool.dao.BookDao;

public class BookDaoImpl extends DaoSupport<Book> implements BookDao {

	public BookDaoImpl(Context context) {
		super(context);
		
	}

	@Override
	public void aaaa() {
		db.beginTransaction();
	}

	

}
