package com.jason.jasonutils.pwdlock;

public class Constant {
	
	public static final String APP_LOCK_TURN_ON = "app_lock_turn_on";//已经设置锁屏密码
	public static final String APP_LOCK_SKIP_SET = "app_lock_skip_set";//已经关闭密码锁
	
	public static final String MYSP_CONFIG_FILENAME = "mysp_config_filename";
	public static final String APP_LOCK_KEY = "app_Lock_pwd_key";
	
	public static final String PWD_MODE = "pwd_mode";
	public static final String NEED_BACK = "need_back";
	public static final int MODE_VERIFY = 15;
	public static final int MODE_LOCK = 20;
	public static final int REENTER_PWD = 25;
	public static final int TURN_ON_APPLOCK = 30;
	public static final int TURN_OFF_APPLOCK = 35;
	
	public static final String ERROR_PWD_COUNT = "error_pwd_count";//保存密码输入错误次数
	public static final String LOCK_PWD_TIME = "lock_pwd_time";//软件锁定时间戳
	public static final int LOCK_REVERIFY_PEROID = 5*60*1000;//解锁间隔时间S
	
	public static final int LIMITED_LOCK_TIME = 10*60*1000;//程序锁定时长M
	public static final int LIMITED_ERROR_TIME = 5;//输入密码错误次数限制
	
	public static final String PASSWORD_ENC_SECRET = "PASSWORD_ENC_SECRET";
	public static final String APP_LOCK_OPERATE_FLAG = "app_Lock_operate_flag";
}
