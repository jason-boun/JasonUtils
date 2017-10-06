package com.jason.jasonutils.gridview.complex;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jason.jasonutils.R;
import com.jason.jasonutils.gridview.singleline.Person;
import com.jason.jasonutils.gridview.singleline.PersonDataUtil;

public class PersonListActivity extends Activity implements OnClickListener {
	
	private TextView tv_gridview_personlist_info;
	private Button bt_gridview_personlist_confirm, bt_gridview_personlist_cancel;
	private String info;
	private List<Person> addList ;
	private int addSize = 0;
	public static final String ADD_PERSON_SESULT_KEY = "add_person_sesult_key";
	public static final String LIST_PRE_SIZE = "list_pre_size";
	private int preListSize = 0;
	private String intentFrom = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridview_person_list_demo);
		init();
		getIntentData();
	}
	
	public void getIntentData(){
		Intent intent = getIntent();
		intentFrom = intent.getStringExtra(PersonDataUtil.INTENT_FROM_TYPE);
		if(intentFrom.equals(PersonDataUtil.INTENT_FROM_SINGLE_GRIDVIEW)){
			List<Person> selectedList = (List<Person>) intent.getSerializableExtra(PersonDataUtil.SELECTED_PERSON_LIST);
			StringBuilder sb = new StringBuilder();
			if(selectedList.size()>0){
				for(Person p:selectedList){
					sb.append(p.getName());
					sb.append("\n");
				}
				sb.substring(0, sb.indexOf("\n"));
			}
			info = "已经选择了"+selectedList.size()+"个Person对象 ，\n"+"名字列表如下：\n"+sb.toString();
			tv_gridview_personlist_info.setText(info);
		}else if(intentFrom.equals(PersonDataUtil.INTENT_FROM_COMPLEX_GRIDVIEW)){
			addSize = new Random().nextInt(5);
			if(addSize!=0){
				info = "已经封装了"+addSize+"个Person对象的数据，你需要添加这"+addSize+"个人吗？";
				tv_gridview_personlist_info.setText(info);
			}
			preListSize = getIntent().getIntExtra(LIST_PRE_SIZE, 0);
		}
	}

	private void init() {
		tv_gridview_personlist_info = (TextView) findViewById(R.id.tv_gridview_personlist_info);
		bt_gridview_personlist_confirm = (Button) findViewById(R.id.bt_gridview_personlist_confirm);
		bt_gridview_personlist_cancel = (Button) findViewById(R.id.bt_gridview_personlist_cancel);
		
		bt_gridview_personlist_confirm.setOnClickListener(this);
		bt_gridview_personlist_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_gridview_personlist_confirm:
			if(intentFrom.equals(PersonDataUtil.INTENT_FROM_COMPLEX_GRIDVIEW)){
				addPerson();
			}
			finish();
			break;
		case R.id.bt_gridview_personlist_cancel:
			finish();
			break;
		}
	}
	
	public void addPerson (){
		addList = PersonDataUtil.dataFactory(addSize ,preListSize);
		Intent intent = new Intent();
		Bundle data = new Bundle();
		data.putSerializable(ADD_PERSON_SESULT_KEY, (Serializable) addList);
		intent.putExtras(data);
		setResult(RESULT_OK, intent);
	}

}
