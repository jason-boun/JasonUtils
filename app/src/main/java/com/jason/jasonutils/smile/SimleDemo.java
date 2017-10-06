package com.jason.jasonutils.smile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jason.jasonutils.R;

public class SimleDemo extends Activity implements OnClickListener {
	
	private Button bt_send_emotion,bt_cancel_emotion;
	private EditText et_enter_content;
	private LinearLayout ll_face_container;
	private ViewPager vp_pager;
	private List<String> reslist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smile_demo);
		
		init();
		initData();
	}

	private void initData() {
		reslist = getExpressionRes(35);
		List<View> views = new ArrayList<View>();
		View gv1 = getGridView(1);
		View gv2 = getGridView(2);
		views.add(gv1);
		views.add(gv2);
		vp_pager.setAdapter(new SmilePagerAdapter(views));
	}

	private void init() {
		bt_send_emotion = (Button) findViewById(R.id.bt_send_emotion);
		bt_cancel_emotion = (Button) findViewById(R.id.bt_cancel_emotion);
		et_enter_content = (EditText) findViewById(R.id.et_enter_content);
		ll_face_container = (LinearLayout) findViewById(R.id.ll_face_container);
		vp_pager = (ViewPager) findViewById(R.id.vp_pager);
		
		bt_send_emotion.setOnClickListener(this);
		bt_cancel_emotion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send_emotion:
			//显示表情框
			showContaniner(true);
			break;
		case R.id.bt_cancel_emotion:
			//隐藏表情框
			showContaniner(false);
			break;
		}
	}
	
	public void showContaniner(boolean show){
		if(show){
			ll_face_container.setVisibility(View.VISIBLE);
		}else{
			ll_face_container.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 初始化表情资源名称
	 */
	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;
			reslist.add(filename);
		}
		return reslist;
	}
	
	/**
	 * 初始化表情的容器和点击事件
	 */
	private View getGridView(int i) {
		View view = getLayoutInflater().inflate(R.layout.expression_gridview, null);
		SmileGridView gv = (SmileGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final GridViewAdapter expressionAdapter = new GridViewAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					if (filename != "delete_expression") { // 显示表情
						Class<?> clz = Class.forName("com.jason.jasonutils.smile.SmileUtils");
						Field field = clz.getField(filename);
						et_enter_content.append(SmileUtils.getSmiledText(SimleDemo.this, (String) field.get(null)));
					} else { // 删除文字或者表情
						if (!TextUtils.isEmpty(et_enter_content.getText())) {

							int selectionStart = et_enter_content.getSelectionStart();// 获取光标的位置
							if (selectionStart > 0) {
								String body = et_enter_content.getText().toString();
								String tempStr = body.substring(0, selectionStart);
								int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
								if (i != -1) {
									CharSequence cs = tempStr.substring(i, selectionStart);
									if (SmileUtils.containsKey(cs.toString()))
										et_enter_content.getEditableText().delete(i, selectionStart);
									else
										et_enter_content.getEditableText().delete(selectionStart - 1, selectionStart);
								} else {
									et_enter_content.getEditableText().delete(selectionStart - 1, selectionStart);
								}
							}
						}

					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return view;
	}

}
