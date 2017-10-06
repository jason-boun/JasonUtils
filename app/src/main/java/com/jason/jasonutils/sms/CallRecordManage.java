package com.jason.jasonutils.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import com.jason.jasonutils.tools.MLog;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 11-11-9 Time: 下午8:51 To
 * change this template use File | Settings | File Templates.
 */
public class CallRecordManage {
    private final static String Log_Tag = "CallRecordManage";

    public static List<Map> getAllCallRecord(Context context, String tel) {
        List<Map> list = new ArrayList<Map>();
        Map map = null;
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, " number= ? ", new String[]{tel}, null);
        while (cursor.moveToNext()) {
        	map = new HashMap();
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("tel", cursor.getString(cursor.getColumnIndex("number")));
            map.put("phone_date", cursor.getLong(cursor.getColumnIndex("date")) + "");
            map.put("duration", cursor.getInt(cursor.getColumnIndex("duration")) + "");
            map.put("type", cursor.getInt(cursor.getColumnIndex("type")) + "");
            map.put("new", cursor.getInt(cursor.getColumnIndex("new")) + "");
            map.put("numbertype", cursor.getString(cursor.getColumnIndex("numbertype")));
            map.put("numberlabel", cursor.getString(cursor.getColumnIndex("numberlabel")));
            list.add(map);
        }
        if (cursor != null)
            cursor.close();
        return list;
    }

    public static List<Map> getAllCallRecord(Context context) {
        List<Map> list = new ArrayList<Map>();
        Map map = null;
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null,
//                CallLog.Calls.CONTENT_URI, null, " type = 1 or type = 3 or type = 2 ",
                null, null);
        while (cursor.moveToNext()) {
        	map = new HashMap();
            map.put("tel", cursor.getString(cursor.getColumnIndex("number")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone_date", cursor.getInt(cursor.getColumnIndex("date"))
                    + "");
            map.put("duration", cursor
                    .getInt(cursor.getColumnIndex("duration"))
                    + "");
            map.put("type", cursor.getInt(cursor.getColumnIndex("type")) + "");
            map.put("new", cursor.getInt(cursor.getColumnIndex("new")) + "");
            map.put("numbertype", cursor.getString(cursor
                    .getColumnIndex("numbertype")));
            map.put("numberlabel", cursor.getString(cursor
                    .getColumnIndex("numberlabel")));
            list.add(map);
        }
        if (cursor != null)
            cursor.close();
        return list;
    }


    public static boolean insertCallRecord(Context context, Map map) {
        boolean flag = true;
        ContentValues values = new ContentValues();
        values.put("number", (String) map.get("tel"));
        values.put("name", (String) map.get("name"));
        if (map.get("phone_date") != null) {
            try {
                values.put("date", Long.valueOf((String) map.get("phone_date")));
            } catch (Exception e) {
                MLog.i(Log_Tag, "insertCallRecord-phone_date:" + (String) map.get("phone_date") + "---" + e.getMessage());
                values.put("date", 0);
            }
        }
        if (map.get("duration") != null) {
            try {
                values.put("duration", Integer.valueOf((String) map.get("duration")));
            } catch (Exception e) {
                MLog.i(Log_Tag, "insertCallRecord-duration:" + (String) map.get("duration") + "---" + e.getMessage());
                values.put("duration", 0);
            }
        }
        if (map.get("type") != null) {
            try {
                values.put("type", Integer.valueOf((String) map.get("type")));
            } catch (Exception e) {
                MLog.i(Log_Tag, "insertCallRecord-type:" + (String) map.get("type") + "---" + e.getMessage());
                values.put("type", 0);
            }
        }
        if (map.get("new") != null) {
            try {
                values.put("new", Integer.valueOf((String) map.get("new")));
            } catch (Exception e) {
                MLog.i(Log_Tag, "insertCallRecord-new:" + (String) map.get("new") + "---" + e.getMessage());
                values.put("new", 0);
            }
        }
        values.put("numbertype", (String) map.get("numbertype"));
        values.put("numberlabel", (String) map.get("numberlabel"));
        try {
            context.getContentResolver().insert(CallLog.Calls.CONTENT_URI,
                    values);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static boolean deleteCallRecord(Context context, String tel) {
        boolean flag = true;
        try {
            context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                    " number = ? ", new String[]{tel});
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
    
    
    public static boolean deleteCallRecord(Context context)
    {
    	   boolean flag = true;
           try {
               context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                       null, null);
           } catch (Exception e) {
               e.printStackTrace();
               flag = false;
           }
           return flag;
    }

    public static boolean deleteCallRecordById(Context context, String id) {
        boolean flag = true;
        try {
            context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                    " _id = ? ", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static boolean deleteLastCallRecord(Context context, String tel) {
        boolean flag = true;
        try {
            Cursor cursor = context.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, new String[]{"_id"},
                    "number=? and (type=1 or type=3)", new String[]{tel},
                    "_id desc limit 1");
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                        "_id=?", new String[]{id + ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

}
