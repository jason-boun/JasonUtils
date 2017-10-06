package com.jason.jasonutils.db;

/**
 * SIP通话记录对应的Bean类
 * 该类的字段与数据库中表格 t_sipcall_records 的column值对应
 */
public class SipCallRecordsBean {
	
	/**
	 * 字段
	 */
	private String jid;//CamTalk用户JID
	private String name;
	private String number; //号码
	private String date; //通话日期
	private Integer duration;//通话时长
	private Integer type;//通话类型：1 ：打进电话，2：打出电话， 3 ：未接电话
	private Integer voip_type;//电话方式：1 ：VOIP电话；2：直拨电话
	private byte[]photo;//头像
	
	/**
	 * 构造方法
	 */
	public SipCallRecordsBean(){}
	
	/**
	 * setX和getX方法
	 * @return
	 */
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getVoip_type() {
		return voip_type;
	}
	public void setVoip_type(Integer voip_type) {
		this.voip_type = voip_type;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	/**
	 * toString方法，不包含photo字段
	 */
	@Override
	public String toString() {
		return "SipCallRecordsBean [jid=" + jid + ", name=" + name
				+ ", number=" + number + ", date=" + date + ", duration=" + duration + ", type="
				+ type + ", voip_type=" + voip_type + "]";
	}

}
