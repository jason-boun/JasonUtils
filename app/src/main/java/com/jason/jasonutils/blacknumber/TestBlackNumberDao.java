package com.jason.jasonutils.blacknumber;

import java.util.List;
import java.util.Random;

import android.test.AndroidTestCase;
import com.jason.jasonutils.tools.MLog;

public class TestBlackNumberDao extends AndroidTestCase {
	BlackNumberDao dao;

	@Override
	protected void setUp() throws Exception {
		dao = new BlackNumberDao(getContext());
		super.setUp();
	}

	public void testAdd() throws Exception {
		//13500000001
		Random random = new Random();
		
		for (int i = 1; i < 200; i++) {
			long number = 13500000000l + i;
			int mode = random.nextInt(3)+1;
			dao.add(new BlackNumberBean(String.valueOf(number),String.valueOf(mode)));
		}
	}
	
	public void addOne(){
		dao.add(new BlackNumberBean(String.valueOf(123),String.valueOf(1)));
	}

	public void testFind() throws Exception {
		boolean result = dao.exist("123");
		assertEquals(true, result);
	}

	public void testUpdate() throws Exception {
		dao.update("123", 2);
	}

	public void testDelete() throws Exception {
		dao.delete("123");
	}
	
	public void testFindAll() throws Exception{
		List<BlackNumberBean>  infos = dao.queryAll();
		for(BlackNumberBean info: infos){
			MLog.i("哈哈", info.toString());
		}
	}
}
