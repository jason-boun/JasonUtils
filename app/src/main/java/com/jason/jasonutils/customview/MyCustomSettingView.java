package com.jason.jasonutils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jason.jasonutils.R;

/**
 * 自定义控件
 */
public class MyCustomSettingView extends RelativeLayout {
	
	private TextView tvTitle;
	private TextView tvContent;
	private CheckBox cbStatus;
	
	private String title;
	private String content_on;
	private String content_off;

	/**
	 * 通过XML布局文件来构建该自定义控件
	 * @param context
	 * @param attrs
	 */
	public MyCustomSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView(context);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.my_setting_view);
		title = array.getString(R.styleable.my_setting_view_title);
		content_on = array.getString(R.styleable.my_setting_view_content_on);
		content_off = array.getString(R.styleable.my_setting_view_content_off);
		
		setTitle(title);
		if(isChecked()){
			setContent(content_on);
		}else{
			setContent(content_off);
		}
		
		array.recycle();//回收数据
	}

	/**
	 * 通过代码传递上下文来new该自定义控件
	 * @param context
	 */
	public MyCustomSettingView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 初始化控件
	 * @param context
	 */
	public void initView(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.custome_my_setting_view, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_setting_view_title);
		tvContent = (TextView) view.findViewById(R.id.tv_setting_view_content);
		cbStatus = (CheckBox) view.findViewById(R.id.cb_setting_view_status);
	}

	/** ==================== 通过代码设置控件属性值 =================*/
	
	/**
	 * 属性title设置
	 */
	public void setTitle(String title) {
		this.tvTitle.setText(title);
	}

	/**
	 * 属性content设置
	 */
	public void setContent(String content) {
		this.tvContent.setText(content);
	}
	
	/**
	 * 属性checked设置
	 */
	public void setChecked(boolean checked) {
		this.cbStatus.setChecked(checked);
		if(checked){
			setContent(content_on);
		}else{
			setContent(content_off);
		}
	}
	
	
	/** ==================== 通过代码获取控件属性值 =================*/
	
	public boolean isChecked(){
		return cbStatus.isChecked();
	}
	
	public String getTitle(){
		return tvTitle.getText().toString();
	}
	
	public String getContent(){
		return tvContent.getText().toString();
	}
	
}
