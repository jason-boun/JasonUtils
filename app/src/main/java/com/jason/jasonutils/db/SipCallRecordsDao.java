package com.jason.jasonutils.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class SipCallRecordsDao {
	
	/**
	 * 存入一条SIP通话记录
	 * @param jid ：CamTalk用户JID
	 * @param number ：号码
	 * @param date ：通话日期
	 * @param duration ：通话时长
	 * @param type ：通话类型：1 ：打进电话，2：打出电话， 3 ：未接电话
	 * @param voip_type ：电话方式：1 ：VOIP电话；2：直拨电话
	 * @param photo ：头像
	 */
	public static void insertSipCallRecords(SQLiteDatabase dbW, String jid, String name, String number, String date, Integer duration, Integer type, Integer voip_type, byte[] photo) {
		try{
			if(!TextUtils.isEmpty(jid) && !TextUtils.isEmpty(number)){
				String sql = "insert into t_sipcall_records (jid,name,number,date,duration,type,voip_type,photo) values (?,?,?,?,?,?,?,?)";
				dbW.execSQL(sql, new Object[] {jid,name,number,date,duration,type,voip_type,photo});
			}
		}finally{
			if(dbW!=null && dbW.isOpen()){
				dbW.close();
			}
		}
		
	 }
	
	/**
	 * 更新SIP通话记录对应的头像
	 * @param photo
	 * @param number
	 */
	public static void updateSipCallRecords(SQLiteDatabase dbW, byte[] photo, String number){
		try{
			if(photo!=null && photo.length>0){
				String sql  = "update t_sipcall_records set photo = ? where number = ?";
				dbW.execSQL(sql, new Object[]{photo, number});
			}
		}finally{
			if(dbW!=null && dbW.isOpen()){
				dbW.close();
			}
		}
		
	}
	
	/**
	 * 查询所有SIP通话记录
	 * @param selection ：查询条件：0：查询全部记录，1 ：打进电话，2：打出电话， 3 ：未接电话
	 * @return
	*/
	public static List<SipCallRecordsBean> queryAllSipCallRecords(SQLiteDatabase dbR, Integer selection) {
		try{
			String sql = null;
			Cursor cursor = null;
			if(selection == 0){
				sql = "select * from t_sipcall_records order by date desc";
				cursor = dbR.rawQuery(sql,null);
			}else{
				sql = "select * from t_sipcall_records where type = ? order by date desc";
				cursor = dbR.rawQuery(sql, new String[]{String.valueOf(selection)});
			}
			return parseSipCallRecordsListCursor(cursor);
		}finally{
			if(dbR!=null && dbR.isOpen()){
				dbR.close();
			}
		}
	}
	
	/**
	 * 根据电话号码查询该号码的所有SIP通话记录
	 * @param number
	 */
	public static List<SipCallRecordsBean> querySipCallRecordsByPhoneNumber(SQLiteDatabase dbR, String number) {
		try{
			String sql = "select * from t_sipcall_records where number = ?";
			Cursor cursor = dbR.rawQuery(sql, new String[]{number});
			return parseSipCallRecordsListCursor(cursor);
		}finally{
			if(dbR!=null && dbR.isOpen()){
				dbR.close();
			}
		}
		
	}
	/**
	 * 根据电话号码查询该号码的最新一条SIP通话记录
	 * @param number
	 */
	public static SipCallRecordsBean queryLatestSipCallRecordsByPhoneNumber(SQLiteDatabase dbR, String number){
		try{
			String sql = "select * from t_sipcall_records where number = ?";
			Cursor cursor = dbR.rawQuery(sql, new String[]{number});
			return parseLatestSipCallRecordsCursor(cursor);
		}finally{
			if(dbR!=null && dbR.isOpen()){
				dbR.close();
			}
		}
	}
	
	/**
	 * 根据电话号码删除该号码的所有SIP通话记录
	 * @return
	*/
	public static void deleteSipCallRecordsByPhoneNumber(SQLiteDatabase dbW, String number) {
		try{
			String sql = "delete from t_sipcall_records where number = ?";
			dbW.execSQL(sql, new Object[]{number});
			dbW.close();
		}finally{
			if(dbW!=null && dbW.isOpen()){
				dbW.close();
			}
		}
	}
	
	/**
	 * 解析一条SipCallRecordsBean
	 * @param cursor
	 * @return
	 */
	public static SipCallRecordsBean parseLatestSipCallRecordsCursor(Cursor cursor) {
		SipCallRecordsBean sipCallRecordsBean = null;
		if (cursor.moveToFirst()) {
			sipCallRecordsBean = new SipCallRecordsBean();
			String tempJid = cursor.getString(cursor.getColumnIndex("jid"));
			String tempName = cursor.getString(cursor.getColumnIndex("name"));
			String tempNumber = cursor.getString(cursor.getColumnIndex("number"));
			String tempDate = cursor.getString(cursor.getColumnIndex("date"));
			Integer tempDuration = cursor.getInt(cursor.getColumnIndex("duration"));
			Integer tempType = cursor.getInt(cursor.getColumnIndex("type"));
			Integer tempVoipType = cursor.getInt(cursor.getColumnIndex("voip_type"));
			byte[] tempPhoto = cursor.getBlob(cursor.getColumnIndex("photo"));
			
			if(!TextUtils.isEmpty(tempJid)){
				sipCallRecordsBean.setJid(tempJid);
			}
			if(!TextUtils.isEmpty(tempName)){
				sipCallRecordsBean.setJid(tempName);
			}
			if(!TextUtils.isEmpty(tempNumber)){
				sipCallRecordsBean.setNumber(tempNumber);
			}
			if(!TextUtils.isEmpty(tempDate)){
				sipCallRecordsBean.setDate(tempDate);
			}
			if(!TextUtils.isEmpty(tempDuration+"")){
				sipCallRecordsBean.setDuration(tempDuration);
			}
			if(!TextUtils.isEmpty(tempType+"")){
				sipCallRecordsBean.setType(tempType);
			}
			if(!TextUtils.isEmpty(tempVoipType+"")){
				sipCallRecordsBean.setVoip_type(tempVoipType);
			}
			if(tempPhoto !=null && tempPhoto.length>0){
				sipCallRecordsBean.setPhoto(tempPhoto);
			}
			tempJid = null;
			tempName = null;
			tempNumber = null;
			tempDate = null;
			tempDuration = null;
			tempType = null;
			tempVoipType = null;
			tempPhoto = null;
		}
		if(null!=cursor){
			cursor.close();
			cursor =null;
		}
		return sipCallRecordsBean ;
	}
	
	/**
	 * 解析SipCallRecordsBean，并返回存有该Bean的集合
	 * @param cursor
	 * @return
	 */
	public static List<SipCallRecordsBean> parseSipCallRecordsListCursor(Cursor cursor) {
		List<SipCallRecordsBean> list = new ArrayList<SipCallRecordsBean>();
		SipCallRecordsBean sipCallRecordsBean = null;
		String tempJid = null;
		String tempName = null;
		String tempNumber = null;
		String tempDate = null;
		Integer tempDuration = null;
		Integer tempType = null;
		Integer tempVoipType = null;
		byte[] tempPhoto = null;
		while (cursor.moveToNext()) {
			sipCallRecordsBean = new SipCallRecordsBean();
			tempJid = cursor.getString(cursor.getColumnIndex("jid"));
			tempName = cursor.getString(cursor.getColumnIndex("name"));
			tempNumber = cursor.getString(cursor.getColumnIndex("number"));
			tempDate = cursor.getString(cursor.getColumnIndex("date"));
			tempDuration = cursor.getInt(cursor.getColumnIndex("duration"));
			tempType = cursor.getInt(cursor.getColumnIndex("type"));
			tempVoipType = cursor.getInt(cursor.getColumnIndex("voip_type"));
			tempPhoto = cursor.getBlob(cursor.getColumnIndex("photo"));
			
			if(!TextUtils.isEmpty(tempJid)){
				sipCallRecordsBean.setJid(tempJid);
			}
			if(!TextUtils.isEmpty(tempName)){
				sipCallRecordsBean.setName(tempName);
			}
			if(!TextUtils.isEmpty(tempNumber)){
				sipCallRecordsBean.setNumber(tempNumber);
			}
			if(!TextUtils.isEmpty(tempDate)){
				sipCallRecordsBean.setDate(tempDate);
			}
			if(!TextUtils.isEmpty(tempDuration+"")){
				sipCallRecordsBean.setDuration(tempDuration);
			}
			if(!TextUtils.isEmpty(tempType+"")){
				sipCallRecordsBean.setType(tempType);
			}
			if(!TextUtils.isEmpty(tempVoipType+"")){
				sipCallRecordsBean.setVoip_type(tempVoipType);
			}
			if(tempPhoto !=null && tempPhoto.length>0){
				sipCallRecordsBean.setPhoto(tempPhoto);
			}
			list.add(sipCallRecordsBean);
		}
		if(list.size()>0){
			tempJid = null;
			tempName = null;
			tempNumber = null;
			tempDate = null;
			tempDuration = null;
			tempType = null;
			tempVoipType = null;
			tempPhoto = null;
		}
		if(null!=cursor){
			cursor.close();
			cursor =null;
		}
		return list ;
	}

}
