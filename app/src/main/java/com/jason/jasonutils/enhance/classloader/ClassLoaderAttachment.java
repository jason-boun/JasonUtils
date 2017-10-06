package com.jason.jasonutils.enhance.classloader;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassLoaderAttachment extends Date {

	private static final long serialVersionUID = 1L;

	@SuppressLint("SimpleDateFormat")
	public String toString() {
		return "hello, JasonUtils: " + new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss").format(this);
	}
}
