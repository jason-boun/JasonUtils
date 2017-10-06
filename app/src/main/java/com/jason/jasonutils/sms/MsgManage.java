package com.jason.jasonutils.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 11-11-9 Time: 下午8:51 To
 * change this template use File | Settings | File Templates.
 */
public class MsgManage {
    private final static String Log_Tag = "MsgManage";
    private final static String SMS_URI_ALL = "content://sms/";   //所有    //table :sms
    private final static String SMS_URI_INBOX = "content://sms/inbox";    //收件箱
    private final static String SMS_URI_SEND = "content://sms/sent";      //发件箱
    private final static String SMS_URI_DRAFT = "content://sms/draft";     //草稿箱
    private final static String SMS_URI_CONVERSATION = "content://sms/conversations";   //table: threads
    private final static String SMS_URI_CANONICAL_ADDRESSES = "content://mms-sms/canonical-addresses";  // table :canonical_addresses

    public static String ORDER_DATE_ASC = " date asc";
    public static String ORDER_DATE_DESC = " date desc";

    public static List<Map> getMsgByTel(Context context, String tel,String order) {
        List<Map> list = new ArrayList<Map>();
        Map map = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), null, "address = ? ", new String[]{tel}, order);
        while (cursor.moveToNext()) {
            map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("thread_id", cursor.getInt(cursor.getColumnIndex("thread_id")));
            map.put("tel", cursor.getString(cursor.getColumnIndex("address")));
            map.put("person", cursor.getInt(cursor.getColumnIndex("person")));
            map.put("date", cursor.getLong(cursor.getColumnIndex("date")) + "");
            map.put("protocol", cursor.getInt(cursor.getColumnIndex("protocol")));
            map.put("read", cursor.getInt(cursor.getColumnIndex("read")));
            map.put("status", cursor.getInt(cursor.getColumnIndex("status")));
//            map.put("status",-1);
            map.put("type", cursor.getInt(cursor.getColumnIndex("type")));//类型 1是接收到的，2是发出的
            map.put("reply_path_present", cursor.getInt(cursor.getColumnIndex("reply_path_present")));
            map.put("subject", cursor.getString(cursor.getColumnIndex("subject")));
            map.put("content", cursor.getString(cursor.getColumnIndex("body")));
            map.put("service_center", cursor.getString(cursor.getColumnIndex("service_center")));
            map.put("locked", cursor.getInt(cursor.getColumnIndex("locked")));
            list.add(map);
        }
        if (cursor != null)
            cursor.close();
        return list;
    }
	   /**
	    * 获取收件箱中最新一条信息
	    * @param context
	    * @param tel
	    * @param getLatestMsg
	    * @return
	    */
 public static Map getMsgByTel(Context context, String tel, boolean getLatestMsg) {
 	if(getLatestMsg){
         Map map = null;
         Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_INBOX), null, "address = ? ", new String[]{tel}, null);
         if (cursor.moveToFirst()) {
             map = new HashMap();
             map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
             map.put("thread_id", cursor.getInt(cursor.getColumnIndex("thread_id")));
             map.put("tel", cursor.getString(cursor.getColumnIndex("address")));
             map.put("person", cursor.getInt(cursor.getColumnIndex("person")));
             map.put("date", cursor.getLong(cursor.getColumnIndex("date")) + "");
             map.put("protocol", cursor.getInt(cursor.getColumnIndex("protocol")));
             map.put("read", cursor.getInt(cursor.getColumnIndex("read")));
             map.put("status", cursor.getInt(cursor.getColumnIndex("status")));
             map.put("type", cursor.getInt(cursor.getColumnIndex("type")));//类型 1是接收到的，2是发出的
             map.put("reply_path_present", cursor.getInt(cursor.getColumnIndex("reply_path_present")));
             map.put("subject", cursor.getString(cursor.getColumnIndex("subject")));
             map.put("content", cursor.getString(cursor.getColumnIndex("body")));
             map.put("service_center", cursor.getString(cursor.getColumnIndex("service_center")));
             map.put("locked", cursor.getInt(cursor.getColumnIndex("locked")));
        
             System.out.println("------id--:"+map.get("id"));
         
         }
         if (cursor != null)
             cursor.close();
         return map;
 	}
     return new HashMap();
 }
    
    
    public static int getMsgThreadIDByTel(Context context, String tel) {
        int threadID  = 0;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse(SMS_URI_ALL), null, "address = ? ",
                new String[]{tel}, null);
        if (cursor.moveToFirst()) {
        	threadID = cursor.getInt(cursor.getColumnIndex("thread_id"));
        }
        if (cursor != null)
            cursor.close();
        return threadID;
    }

    //获取手机所有短息信息
    public static List<Map> getAllMsg(Context context) {
        List<Map> list = new ArrayList<Map>();
        Map map = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), null, null, null, "date desc");
        while (cursor.moveToNext()) {
        	map = new HashMap();
        	String tel = cursor.getString(cursor.getColumnIndex("address"));
        	if(TextUtils.isEmpty(tel))
        	{
        		continue;
        	}
        	map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("thread_id", cursor.getInt(cursor.getColumnIndex("thread_id")));
            map.put("tel",tel);
            map.put("person", cursor.getInt(cursor.getColumnIndex("person")));
            map.put("date", cursor.getLong(cursor.getColumnIndex("date")) + "");
            map.put("protocol", cursor.getInt(cursor.getColumnIndex("protocol")));
            map.put("read", cursor.getInt(cursor.getColumnIndex("read")));
            map.put("status", cursor.getInt(cursor.getColumnIndex("status")));
//            map.put("status",-1);
            map.put("type", cursor.getInt(cursor.getColumnIndex("type")));//类型 1是接收到的，2是发出的
            map.put("reply_path_present", cursor.getInt(cursor.getColumnIndex("reply_path_present")));
            map.put("subject", cursor.getString(cursor.getColumnIndex("subject")));
            map.put("content", cursor.getString(cursor.getColumnIndex("body")));
            map.put("service_center", cursor.getString(cursor.getColumnIndex("service_center")));
            map.put("locked", cursor.getInt(cursor.getColumnIndex("locked")));
            list.add(map);
        }
        if (cursor != null)
            cursor.close();
        return list;
    }

  //获取手机所有短息信息
    public static List<Map> getMainMsg(Context context) {
        List<Map> list = new ArrayList<Map>();
        Map map = null;
        String where = " date in ( select max(date) date_new from sms  group by address ) ";
        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), null,where, null, " date desc");
        while (cursor.moveToNext()) {
        	map = new HashMap();
        	String tel = cursor.getString(cursor.getColumnIndex("address"));
        	if(TextUtils.isEmpty(tel))
        	{
        		continue;
        	}
        	map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("thread_id", cursor.getInt(cursor.getColumnIndex("thread_id")));
            map.put("tel",tel);
            map.put("person", cursor.getInt(cursor.getColumnIndex("person")));
            map.put("date", cursor.getLong(cursor.getColumnIndex("date")) + "");
            map.put("protocol", cursor.getInt(cursor.getColumnIndex("protocol")));
            map.put("read", cursor.getInt(cursor.getColumnIndex("read")));
            map.put("status", cursor.getInt(cursor.getColumnIndex("status")));
//            map.put("status",-1);
            map.put("type", cursor.getInt(cursor.getColumnIndex("type")));//类型 1是接收到的，2是发出的
            map.put("reply_path_present", cursor.getInt(cursor.getColumnIndex("reply_path_present")));
            map.put("subject", cursor.getString(cursor.getColumnIndex("subject")));
            map.put("content", cursor.getString(cursor.getColumnIndex("body")));
            map.put("service_center", cursor.getString(cursor.getColumnIndex("service_center")));
            map.put("locked", cursor.getInt(cursor.getColumnIndex("locked")));
            list.add(map);
        }
        if (cursor != null)
            cursor.close();
        return list;
    }
    
    
    public static  int getConUnReadMsgCount(Context context,String tel) {
    	int count = 0;
        String where = " read = 0 and type = 1 and  address = ?";
        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), null,where, new String[]{tel},null);
        count = cursor.getCount();
        if (cursor != null)
            cursor.close();
        return count;
    }
    
    public static  int getConUnReadMsgCount(Context context) {
    	int count = 0;
        String where = " read = 0 and type = 1 ";
        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), null,where, null,null);
        count = cursor.getCount();
        if (cursor != null)
            cursor.close();
        return count;
    }
    public static int getConTotalMsgCount(Context context,String tel)
    {
    	int count = 0;
        String where = " address = ? ";
        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL), null,where, new String[]{tel},null);
        count = cursor.getCount();
        if (cursor != null)
            cursor.close();
        return count;
    }
    
    
    public static boolean deleteMsgById(Context context, String id) {
        boolean flag = true;
        try {
            context.getContentResolver().delete(Uri.parse(SMS_URI_ALL), " _id = ? ", new String[]{id});
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    
    public static boolean deleteMsgByTel(Context context, String tel) {
        boolean flag = true;
        try {
            context.getContentResolver().delete(Uri.parse(SMS_URI_ALL), " address = ? ", new String[]{tel});
        } catch (Exception e) {
            flag = false;
//            MLog.i(Log_Tag, "deleteMsg--:" + e.getMessage());
        }
        return flag;
    }

    public static boolean deleteMsg(Context context)
    {
        boolean flag = true;
        try {
            context.getContentResolver().delete(Uri.parse(SMS_URI_ALL),null, null);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean insertMsg(Context context, Map map) {
        boolean flag = true;
        try {
            ContentValues contentValues = new ContentValues();
//            contentValues.put("thread_id", (Integer) map.get("thread_id"));   //写短信时，此字段一定不要写入。否则不会生成会话记录，不会在短信箱内看到。
            contentValues.put("address", (String) map.get("tel"));
            contentValues.put("person", (Integer) map.get("person"));
//          int date = Utils.isEmpty((String)map.get("date")) ? 0: Integer.parseInt((String)map.get("date"));
            contentValues.put("date", (String) map.get("date"));
            contentValues.put("protocol", (Integer) map.get("protocol"));
            contentValues.put("read", (Integer) map.get("read"));
            contentValues.put("status", (Integer) map.get("status"));
            contentValues.put("type", (Integer) map.get("type"));//类型 1是接收到的，2是发出的
            contentValues.put("reply_path_present", (Integer) map.get("reply_path_present"));
            contentValues.put("subject", (String) map.get("subject"));
            contentValues.put("body", (String) map.get("content"));
            contentValues.put("service_center", (String) map.get("service_center"));
            contentValues.put("locked", (Integer) map.get("locked"));
            context.getContentResolver().insert(Uri.parse(SMS_URI_ALL), contentValues);
        } catch (Exception e) {
            e.printStackTrace();
//            MLog.i(Log_Tag, "insertMsg--:" + e.getMessage());
            flag = false;
        }
        return flag;
    }
    
    public static boolean updateMsgByTel(Context context,Map map,String tel)
    {
    	  boolean flag = true;
          try {
              ContentValues contentValues = new ContentValues();
              if(map.get("person")!=null)
              {
            	  contentValues.put("person", (Integer) map.get("person"));
              }
              if(map.get("date")!=null)
              {
            	  contentValues.put("date", (String) map.get("date"));
              }
              if(map.get("protocol")!=null)
              {
            	  contentValues.put("protocol", (Integer) map.get("protocol"));
              }
              if(map.get("read")!=null)
              {
            	  contentValues.put("read", (Integer) map.get("read"));
              }
              if(map.get("status")!=null)
              {
            	  contentValues.put("status", (Integer) map.get("status"));
              }
              if(map.get("type")!=null)
              {
            	  contentValues.put("type", (Integer) map.get("type"));//类型 1是接收到的，2是发出的
              }
              if(map.get("reply_path_present")!=null)
              {
            	  contentValues.put("reply_path_present", (Integer) map.get("reply_path_present"));
              }
              if(map.get("subject")!=null)
              {
            	  contentValues.put("subject", (String) map.get("subject"));
              }
              if(map.get("body")!=null)
              {
            	  contentValues.put("body", (String) map.get("content"));
              }
              if(map.get("service_center")!=null)
              {
            	  contentValues.put("service_center", (String) map.get("service_center"));
              }
              if(map.get("locked")!=null)
              {
            	  contentValues.put("locked", (Integer) map.get("locked"));
              }
              context.getContentResolver().update(Uri.parse(SMS_URI_ALL), contentValues," address = ?",new String[]{tel});
          } catch (Exception e) {
              e.printStackTrace();
              flag = false;
          }
          return flag;
    }
}
