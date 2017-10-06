package com.jason.jasonutils.blacknumber;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.MyAsyncTask;
import com.jason.jasonutils.tools.Utils;

public class ShowPageLoadActivity extends Activity {
	
	private LinearLayout ll_data_loading;
	private ListView lv_black_number_list;
	private List<BlackNumberBean> dataList;
	private BlackNumberAdapter adapter;
	private BlackNumberDao dao;
	private Context ctx;
	
	private boolean isLoadingData = false;
	private int blockSize = 20;
	private int currentPage = 1;
	private int totalPage = 1;
	
	private TextView tv_black_number_page_status;
	private EditText et_black_number_page_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black_number_show_page_data_activity);
		Utils.keyBoardHindden2(this);
		ctx = ShowPageLoadActivity.this;
		dao = new BlackNumberDao(ShowPageLoadActivity.this);
		
		Intent intent = getIntent();//将来电一声响号码添加到黑名单中
		if (intent != null) {
			String number = intent.getStringExtra("number");
			if(!TextUtils.isEmpty(number)){
				if(dao.exist(number)){
					Toast.makeText(this, "来电一声响号码:"+number, 1).show();
				}else{
					Toast.makeText(this, "号码:"+number+"已经存在于黑名单中", 1).show();
					insert2DB(number,dao);
				}
			}
		}
		
		totalPage = dao.getDbTotalSize()%blockSize == 0?dao.getDbTotalSize()/blockSize:dao.getDbTotalSize()/blockSize+1;
		
		ll_data_loading = (LinearLayout) findViewById(R.id.ll_data_loading);
		lv_black_number_list = (ListView) findViewById(R.id.lv_black_number_list);
		
		tv_black_number_page_status = (TextView) findViewById(R.id.tv_black_number_page_status);
		et_black_number_page_number = (EditText) findViewById(R.id.et_black_number_page_number);
		
		tv_black_number_page_status.setText("当前/总页码:" + currentPage + "/" + totalPage);
		et_black_number_page_number.setText(String.valueOf(currentPage));
		fillData();
	}
	
	/**
	 * 将该Activity页面的启动模式设置为singleTop，就会调用该方法
	 * 将来电一声响号码添加到黑名单中
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			String number = intent.getStringExtra("number");
			if(!TextUtils.isEmpty(number)){
				if(dao.exist(number)){
					Toast.makeText(this, "来电一声响号码:"+number, 1).show();
				}else{
					Toast.makeText(this, "号码:"+number+"已经存在于黑名单中", 1).show();
					insert2DB(number,dao);
				}
			}
		}
	}
	
	/**
	 * 将来电一声响号码添加到黑名单中
	 * @param number
	 * @param dao2
	 */
	private void insert2DB(final String number, final BlackNumberDao dao) {
		AlertDialog.Builder builder = new Builder(ctx);
		builder.setTitle("提示");
		builder.setMessage("将"+number+"添加到黑名单中？");
		builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int result = dao.add(new BlackNumberBean(number, "1"));
				if(result>0){
					Toast.makeText(ShowPageLoadActivity.this, "添加成功", 0).show();
					fillData();
				}
			}
		});
		builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}

	private void fillData() {
		new MyAsyncTask() {
			@Override
			protected void onPreExecute() {
				ll_data_loading.setVisibility(View.VISIBLE);
				isLoadingData = true;
			}
			@Override
			protected void onPostExecute() {
				adapter = new BlackNumberAdapter(ctx,dataList,dao);
				lv_black_number_list.setAdapter(adapter);
				tv_black_number_page_status.setText("当前/总页码:" + currentPage + "/" + totalPage);
				ll_data_loading.setVisibility(View.INVISIBLE);
				isLoadingData = false;
			}
			@Override
			protected void doInBackgroud() {
				dataList = dao.queryInfoByPage(currentPage, blockSize);
			}
		}.execute();
	}
	
	/**
	 * 跳转到指定页
	 * @param view
	 */
	public void jump(View view){
		String page = et_black_number_page_number.getText().toString().trim();
		int pageNumber = Integer.parseInt(page);
		if(TextUtils.isEmpty(page) || pageNumber ==0 || pageNumber >totalPage){
			Toast.makeText(getApplicationContext(), "请输入合法的页码", 0).show();
			return;
		}
		if(isLoadingData || currentPage == pageNumber){
			return;
		}
		currentPage = Integer.parseInt(page);
		fillData();
	}
	
	/**
	 * 添加号码到黑名单中
	 * @param view
	 */
	private EditText et_black_number_enter_phone;
	private RadioGroup rg_black_number_save_mode;
	private Button bt_black_number_enter_cancle, bt_black_number_enter_ok;
	private AlertDialog dialog ;
	
	public void addBlackNumber(View view){
		AlertDialog.Builder builder = new Builder(this);
		View dialogView = LayoutInflater.from(ctx).inflate(R.layout.dialog_add_blacknumber,null);
		et_black_number_enter_phone = (EditText) dialogView.findViewById(R.id.et_black_number_enter_phone);
		rg_black_number_save_mode = (RadioGroup) dialogView.findViewById(R.id.rg_black_number_save_mode);
		bt_black_number_enter_ok = (Button) dialogView.findViewById(R.id.bt_black_number_enter_ok);
		bt_black_number_enter_cancle = (Button) dialogView.findViewById(R.id.bt_black_number_enter_cancle);
		
		bt_black_number_enter_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog.isShowing())
					dialog.dismiss();
			}
		});
		bt_black_number_enter_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = et_black_number_enter_phone.getText().toString().trim();
				if(TextUtils.isEmpty(number)){
					Toast.makeText(ctx,"号码不能为空",0).show();
					return;
				}
				if(dao.exist(number)){
					Toast.makeText(ctx,"该号码已经在黑名单中了",0).show();
					return;
				}
				if(dialog.isShowing()){
					dialog.dismiss();
				}
				String mode = "1";
				switch (rg_black_number_save_mode.getCheckedRadioButtonId()) {
				case R.id.rb_all:
					mode = "1";
					break;
				case R.id.rb_phone:
					mode = "2";
					break;
				case R.id.rb_sms:
					mode = "3";
					break;
				};
				BlackNumberBean bean = new BlackNumberBean(number, mode);
				dao.add(bean);
				fillData();
				//dataList.add(bean);
				//adapter.notifyDataSetChanged();
			}
		});
		builder.setView(dialogView);
		dialog = builder.show();
	}
	
}
