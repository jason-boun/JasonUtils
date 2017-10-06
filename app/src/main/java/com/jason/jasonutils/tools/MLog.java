package com.jason.jasonutils.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 日志级别: 
 * 0:禁用日志, 
 * 1:启用(所有日志), 
 * 2:verbose, 
 * 3:debug,
 * 4:info, 
 * 5:warn, 
 * 6:error, 
 */
public class MLog {
	
	public static final int DISABLE = 0;
	public static final int ENABLE_ALL = 1;
	public static final int VERBOSE = 2;// Log.VERBOSE;
	public static final int DEBUG = 3;// Log.DEBUG;
	public static final int INFO = 4;// Log.INFO;
	public static final int WARN = 5;// Log.WARN;
	public static final int ERROR = 6;// Log.ERROR;
	// public static final int ASSERT =7;// Log.ASSERT;

	private static final String[] levelText = new String[] { "DISABLE", "ENABLE_ALL", "VERBOSE", "DEBUG", "INFO", "WARN", "ERROR", "ASSERT", };

	private static String TAG = "MyLog";
	private static int logLevel = ENABLE_ALL;
	private static List<XPrinter> printers = new ArrayList<MLog.XPrinter>();
	static {
		addPrinter(new LogcatPrinter());
	}

	public static void setTag(String tag) {
		TAG = tag;
	}

	/**
	 * 设置logcat的log级别, 见android.util.Log.VERBOSE等
	 * @param level:值是android.util.Log.VERBOSE/DEBUG/INFO/WARN等, 0表示全部禁用, 1表示全部启用
	 */
	public static void setEnableLevel(int level) {
		logLevel = level;
	}

	private static boolean isEnable(int level) {
		return logLevel > 0 && level >= logLevel;
	}

	public static void d(Object... args) {
		println(DEBUG, TAG, args);
	}

	public static void dTag(String tag, Object... args) {
		println(DEBUG, tag, args);
	}

	public static void w(Object... args) {
		println(WARN, TAG, args);
	}

	public static void wTag(String tag, Object... args) {
		println(WARN, tag, args);
	}

	public static void e(Object... args) {
		println(ERROR, TAG, args);
	}

	public static void eTag(String tag, Object... args) {
		println(ERROR, tag, args);
	}

	public static void i(Object... args) {
		println(INFO, TAG, args);
	}

	public static void iTag(String tag, Object... args) {
		println(INFO, tag, args);
	}

	public static void v(Object... args) {
		println(VERBOSE, TAG, args);
	}

	public static void vTag(String tag, Object... args) {
		println(VERBOSE, tag, args);
	}

	private static String getString(Object obj) {
		if (obj == null) {
			return "null";
		}
		// else
		if (obj instanceof Throwable) {
			Throwable tr = (Throwable) obj;
			StringWriter sw = new StringWriter(128);
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			return sw.toString();
		}
		// else
		if (obj instanceof String) {
			return (String) obj;
		}
		// else
		return obj.toString();
	}

	/**
	 * @param priority:见Log.WARN/ERROR等
	 * @param tag
	 * @param args
	 */
	public static void println(int priority, String tag, Object... args) {
		if (!isEnable(priority)) {
			return;
		}
		String msg = null;
		if (args.length == 0) {
			msg = "";
		} else {
			StringBuffer sb = new StringBuffer(args.length * 8 + 8);
			boolean first = true;
			for (Object obj : args) {
				if (first) {
					first = false;
				} else {
					sb.append(" ");
				}
				sb.append(getString(obj));
			}
			msg = sb.toString();
		}
		
		for(XPrinter p : printers) {
			p.println(priority, tag, msg);
		}
	}

	public static String getLevelText(int level) {
		if (level >= 0 && level < levelText.length) {
			return levelText[level];
		}
		return "";
	}

	public static interface XPrinter {
		public void println(int priority, String tag, String msg);
	}

	

	public static void addPrinter(XPrinter...ps) {
		for(XPrinter p : ps) {
			printers.add(p);
		}
	}
	public static void clearPrinter() {
		printers.clear();
	}
	public static void removePrinter(XPrinter...ps) {
		for(XPrinter p : ps) {
			if(printers.contains(p)) {
				printers.remove(p);
			}
		}
	}
	public static void removePrinter(Class<? extends XPrinter>...ps) {
		List<Class<? extends XPrinter>> args = Arrays.asList(ps);
		ArrayList<XPrinter> toremove = new ArrayList<MLog.XPrinter>();
		for(XPrinter p : printers) {
			if(args.contains(p.getClass())) {
				toremove.add(p);
			}
		}
		printers.removeAll(toremove);
	}
	
	
	public static class LogcatPrinter implements XPrinter{

		@Override
		public void println(int priority, String tag, String msg) {
			Log.println(priority, tag, msg);
		}
		
	}
	
	public static class FilePrinter implements XPrinter {
		private BufferedWriter writer = null;
		
		public FilePrinter(File f, int limit) {
			try {
				if(f.exists()) {
					FileInputStream is = new FileInputStream(f);
					if(is.available() > limit) {
						f.delete();
					}
					is.close();
					is = null;
				}
				writer = new BufferedWriter(new FileWriter(f, true), 1024*20);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void flush() {
			try {
				if(writer != null) {
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
		@SuppressLint("SimpleDateFormat")
		@Override
		public void println(int priority, String tag, String msg) {
			if (writer == null) {
				return;
			}
			try {
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ").format(new Date());
				writer.write(date);
				writer.write(getLevelText(priority));
				writer.write(" [");
				writer.write(tag);
				writer.write("] ");
				writer.write(msg);
				writer.write("\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
