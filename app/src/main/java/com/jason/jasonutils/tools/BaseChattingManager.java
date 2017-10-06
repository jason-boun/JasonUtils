package com.jason.jasonutils.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class BaseChattingManager extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createAudioQueue();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		createAudioQueue();
	}
	@Override
	protected void onPause() {
		super.onPause();
		releasePlayAudio(false);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(autoPlayQueue !=null){
			autoPlayQueue.msgIdList = null;
			autoPlayQueue = null;
		}
	}
	
    public AutoPlayAudioQueue autoPlayQueue ;
    public static final int PLAY_NEW_AUDIO = 60;
    public static boolean startNotify = false;//通知开启播放
    protected ChattingItem item = null;
    protected View view = null;
    private static final int TIMELOSS = 150;//时间损耗，MS
    private Class<BaseChattingManager> lock = BaseChattingManager.class;
    
    @SuppressLint("HandlerLeak")
	public Handler autoPlayHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case PLAY_NEW_AUDIO:
				if(!autoPlayQueue.isEmpty()){
					playNextAudio();
				}else{
					startNotify = false;
				}
				break;
			}
    	};
    };
    
    /**
     * 创建播放列表
     */
    public void createAudioQueue(){
    	if(autoPlayQueue == null){
			autoPlayQueue = new AutoPlayAudioQueue();
		}
    }
	
    /**
     * 弹出封装，播放文件
     */
    protected Boolean canPlayNextAudio(){
    	HashMap<String, Object> palyItem = autoPlayQueue.pop();
    	if(palyItem!=null && palyItem.size()>0){
    		item = (ChattingItem) palyItem.get("item");
    		view = (View) palyItem.get("view");
    		if(item !=null && view !=null){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 停止播放，清空列表
     * @param turnOff：点击条目后，true该界面新来语音消息后停止播放；false来新语音消息自动播放
     */
    public void releasePlayAudio(boolean turnOff){
    	autoPlayQueue.clear();
    	startNotify = turnOff;
    }
    
    /**
     * 播放语音的同时来新的语音消息，记录配置参数
     */
    public void pauseMediaPlayer(MediaPlayer player, ChattingItem pauseItem){
    	if(player!=null && player.isPlaying()){
    		player.stop();
    		pauseItem.audio_msg_pause_position = player.getCurrentPosition()-TIMELOSS;
    		pauseItem.audio_msg_read = 1;
    		item = null;
    		startNotify = false;
    		autoPlayQueue.clear();
    	}
    }
    
    /**
     * 需要子类覆写，调用播放adapter中抽取的方法
     */
    public void playNextAudio(){}
	
	/**
	 * 队列数据结构FIFO
	 */
	public class AutoPlayAudioQueue {
		private LinkedList<HashMap<String, Object>> link;
		private ArrayList<String> msgIdList ;
		public AutoPlayAudioQueue(){
			link = new LinkedList<HashMap<String, Object>>();
			msgIdList = new ArrayList<String>();
		}
		
		public void push(ChattingItem item,View view){
			synchronized(lock){
				if(msgIdList.contains(item.message_id)){
					return;
				}else{
					msgIdList.add(item.message_id);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("item", item);
					map.put("view", view);
					link.addFirst(map);
				}
			}
		}
		public HashMap<String, Object> pop(){
			synchronized(lock){
				return link.removeFirst();//因adapter添加顺序相反，为保证需求一致，不使用link.removeLast();此时已经变为栈结构
			}
		}
		public boolean isEmpty(){
			synchronized(lock){
				return link.isEmpty();
			}
		}
		public int size(){
			synchronized(lock){
				return link.size();
			}
		}
		public void clear(){
			synchronized(lock){
				if(msgIdList!=null && msgIdList.size()>0){
					msgIdList.clear();
				}
				if(link!=null && link.size()>0){
					link.clear();
				}
			}
		}
	}
	
	public class ChattingItem {
		public String message_id; //  消息id
		public String chatting_time;//聊天时间
		public String chatting_user;//用户
		public String chatting_content;//聊天内容
		public int chatting_avatar;//头像
		public int chatting_state;//状态
		public Integer send_status;//发送状态：0发送成功；1发送失败
		public boolean isLocal=false;//是否本地
		public int type;//显示的类型定义：0为短消息　1为图片　2为语音  4为名片
		public String jid;
		public String picpath;//聊天内容图片的路径
		public String content_type;//20121128
		public long progress_date;
		public boolean isover;
		public long audio_length; //语言显示的长度
		public byte[] photo;//头像
		public int id;
		public String sendMessage;
		public String chatting_time_stamp;  //会话时间戳
		public Integer audio_msg_read =0;//语音文件是否已读：1未读，0已读，2正在播放
		public Integer audio_msg_pause_position = 0;//记录语音文件播放进度
	}
	
}
