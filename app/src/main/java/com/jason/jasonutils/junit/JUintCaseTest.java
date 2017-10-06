package com.jason.jasonutils.junit;

import android.test.AndroidTestCase;

public class JUintCaseTest extends AndroidTestCase {

	public void testAddMethod(){
		assertEquals(12, new MethodTools().addMethod(4, 8));
	}
}
