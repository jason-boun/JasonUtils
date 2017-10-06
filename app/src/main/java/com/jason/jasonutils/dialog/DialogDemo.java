package com.jason.jasonutils.dialog;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.tools.ViewUtil;

public class DialogDemo extends Activity implements OnClickListener {
	
	private String[] provinces = new String []{"辽宁省", "山东省", "河北省", "福建省", "广东省", "黑龙江省"};
	private int choiceIndex=-1;
	private Context mContext = DialogDemo.this;
	private ListView listView ;
	private boolean[] defaultSelect = new boolean[]{true,false,true,false,false,true};
	
	private boolean progressDialogFlag = false;
	private Handler progressHandler = null;
	private int Max_Progress = 100;
	private int progress = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_demo_activity);
		ViewUtil.setChildsOnClickLisener(this, R.id.ll_dialogdemo_container, 0, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_Button_simpleList_dialog:
			createSimpleListDilalog();
			break;
		case R.id.bt_Button_singlechoice_dialog:
			createSingleChoiceDilalog();
			break;
		case R.id.bt_Button_multichoice_dialog:
			createMultiChoiceDilalog();
			break;
		case R.id.bt_Button_threebutton_dialog:
			createThreeButtonDilalog();
			break;
		case R.id.bt_customview_dialog:
			createCustomViewDilalog();
			break;
		case R.id.bt_progress_dialog:
			showProgressDialog(ProgressDialog.STYLE_HORIZONTAL);
//			showProgressDialog(ProgressDialog.STYLE_SPINNER);
			break;
		case R.id.bt_activity_dialog:
			createActivityDilalog();
			break;
		}
	}

	/**
	 * 列表对话框
	 */
	private void createSimpleListDilalog() {
		new AlertDialog.Builder(this).setTitle("省份选择").setItems(provinces, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String tip = "您已经选择第"+which+"个选项："+provinces[which];
				final AlertDialog innerDialog = new AlertDialog.Builder(mContext).setMessage(tip).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						innerDialog.dismiss();
					}
				}, 2000);
			}
		}).show();
	}
	
	/**
	 * 单选对话框
	 */
	private void createSingleChoiceDilalog() {
		new AlertDialog.Builder(mContext).setTitle("单选省份列表")
		.setSingleChoiceItems(provinces, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				choiceIndex = which;
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(choiceIndex!=-1){
					Toast.makeText(mContext, "您选择了第"+choiceIndex+"个选项："+ provinces[choiceIndex], 0).show();
				}else{
					Toast.makeText(mContext, "你什么也没选择", 0).show();
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(mContext, "什么也没选择", 0).show();
			}
		}).show();
	}

	/**
	 * 复选对话框
	 */
	private void createMultiChoiceDilalog() {
		AlertDialog multiDialog = new AlertDialog.Builder(mContext).setTitle("省份复选列表")
		.setMultiChoiceItems(provinces, defaultSelect, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int count = listView.getCount();
				String tip = "";
				for(int i=0; i<count; i++){
					if(listView.getCheckedItemPositions().get(i)){
						tip = tip+"第"+i+"个，"+ listView.getAdapter().getItem(i)+"; ";
					}
				}
				if(listView.getCheckedItemPositions().size()>0){
					Toast.makeText(mContext,"您总共选择了"+listView.getCheckedItemPositions().size()+"个，分别为："+ tip, 0).show();
				}else{
					Toast.makeText(mContext, "您什么也没选", 0).show();
				}
			}
		}).setNegativeButton("取消", null)
		.create();
		listView = multiDialog.getListView();
		multiDialog.show();
	}

	
	/**
	 * 三个按钮对话框
	 */
	private void createThreeButtonDilalog() {
		new AlertDialog.Builder(mContext)
		.setTitle("温馨提示")
		.setMessage("确定要覆盖原来的数据吗？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(mContext, "文件已经覆盖", 0).show();
			}
		}).setNeutralButton("忽略", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(mContext, "忽略了此操作", 0).show();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(mContext, "取消了覆盖操作", 0).show();
			}
		}).show();
	}
	
	/**
	 * 对话框挂载自定义的View视图
	 */
	@SuppressWarnings("static-access")
	private void createCustomViewDilalog() {
		LinearLayout login = (LinearLayout) getLayoutInflater().from(mContext).inflate(R.layout.custom_view_dialog_login, null);
		final EditText et_user_name = (EditText) login.findViewById(R.id.et_user_name);
		final EditText et_user_password = (EditText) login.findViewById(R.id.et_user_password);
		new AlertDialog.Builder(mContext).setTitle("请输入您的信息")
		.setView(login)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String tempName = et_user_name.getText().toString().trim();
				String tempPassword = et_user_password.getText().toString().trim();
				String info="";
				if(!TextUtils.isEmpty(tempName)){
					info = "name："+tempName;
				}if(!TextUtils.isEmpty(tempName)){
					info = info+"\n"+"password："+tempPassword;
				}if(!TextUtils.isEmpty(info)){
					Toast.makeText(mContext, "你输入的信息是：\n"+info, 0).show();
				}else{
					Toast.makeText(mContext, "您输入的信息为空", 0).show();
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(mContext, "您取消了输入信息", 0).show();
			}
		}).show();
	}
	
	/**
	 * 进度条对话框
	 * @param style
	 */
	private void showProgressDialog(int style){
		final ProgressDialog progressDialog = new ProgressDialog(mContext);
		
		progressDialog.setIcon(R.drawable.wait);
		progressDialog.setTitle("温馨提示");
		progressDialog.setMessage("正在加载，请您稍候···");
		progressDialog.setProgressStyle(style);
		progressDialog.setMax(Max_Progress);
		
		progressDialog.setButton(DialogInterface.BUTTON_POSITIVE,"暂停", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressHandler.removeMessages(1);//移除消息栈信息，handler不再处理消息码1对应的信息，即handler不再执行循环操作
			}
		});
		
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressHandler.removeMessages(1);//移除消息栈信息，同时进度恢复为0
				progress = 0;
				progressDialog.setProgress(0);
			}
		});
		
		progressDialog.show();
		
		progressHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					if(progress > Max_Progress){
						progress=0;
						progressDialog.dismiss();
					}else{
						progress++;
						progressDialog.incrementProgressBy(5);
						progressHandler.sendEmptyMessageDelayed(1, 500 + new Random().nextInt(500));
					}
					break;
				}
			}
		};
		
		progress = progress>0 ? progress:0;
		progressDialog.setProgress(progress);
		progressHandler.sendEmptyMessage(1);
	}

	/**
	 * 饼状、条状进度条切换
	 */
	private void createProgressDilalog() {
		if(progressDialogFlag){
			showProgressDialog(ProgressDialog.STYLE_HORIZONTAL);
			progressDialogFlag = false;
		}else{
			showProgressDialog(ProgressDialog.STYLE_SPINNER);
			progressDialogFlag = true;
		}
	}
	
	private void createActivityDilalog() {
		Toast.makeText(this, "请查阅popupwindowDemo", Toast.LENGTH_SHORT).show();
	}
}
