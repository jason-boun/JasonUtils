package com.jason.jasonutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jason.jasonutils.actionbar.ActionBarDemo;
import com.jason.jasonutils.adapterdemo.AdapterDemoActivity;
import com.jason.jasonutils.alertdialog.CustomizedProgressDialogActivity;
import com.jason.jasonutils.applockmanager.AppLockManagerActivity;
import com.jason.jasonutils.appmanager.AppManagerActivity;
import com.jason.jasonutils.blacknumber.BlackNumberFunctionListActivity;
import com.jason.jasonutils.camera.CameraDemoActivity;
import com.jason.jasonutils.camera.CameraDemoEarlier;
import com.jason.jasonutils.cleanappcache.CleanAppCacheActivity;
import com.jason.jasonutils.cmbpay.MyCMBPayActivity;
import com.jason.jasonutils.comingcalladdress.PhoneNumberManager;
import com.jason.jasonutils.componentdemo.McWillReceiverDemo;
import com.jason.jasonutils.componentdemo.SendRebootBroadcastActivity;
import com.jason.jasonutils.contact.OperateSysContactsActivity;
import com.jason.jasonutils.creditcard.CreditCardActivity;
import com.jason.jasonutils.customview.CustomViewDemoShowActivity;
import com.jason.jasonutils.db.DbTestDemo;
import com.jason.jasonutils.dialog.DialogDemo;
import com.jason.jasonutils.dimesionalcode.TestActivity;
import com.jason.jasonutils.filter.FilterDemoActivity;
import com.jason.jasonutils.fixpicture.ChooseCutPic_Activity;
import com.jason.jasonutils.fixpicture.FixPictureActivity;
import com.jason.jasonutils.fixpicture.ImageBorderActivity;
import com.jason.jasonutils.floatlayerfunction.FloatLayer2IntroduceFunction;
import com.jason.jasonutils.fragment.FragmentDmeoList;
import com.jason.jasonutils.gridview.DifferentgetXgetRawX;
import com.jason.jasonutils.gridview.GridViewDemoListActivity;
import com.jason.jasonutils.handler.AsyncQueryHandlerDemo;
import com.jason.jasonutils.intent.IntentDemoActivity;
import com.jason.jasonutils.iostream.LinuxReadWriteModeDemo;
import com.jason.jasonutils.listviewlimited.ListViewLimited;
import com.jason.jasonutils.litehttpdemo.LiteHttpDemo;
import com.jason.jasonutils.location.LocationDemoListActivity;
import com.jason.jasonutils.lrucatche.ShowPcitureActivity;
import com.jason.jasonutils.map.BaiduMapDemoList;
import com.jason.jasonutils.mediaplayer.MediaPlayerDemo;
import com.jason.jasonutils.menu.MenuDemoActivity;
import com.jason.jasonutils.network.NetWorkTestDemo;
import com.jason.jasonutils.notification.NotificationDemo;
import com.jason.jasonutils.pictureoperate.PictureOperationDemoActivity;
import com.jason.jasonutils.popupwindown.PopupWindownDemo;
import com.jason.jasonutils.powermanager.PowerMangerDemo;
import com.jason.jasonutils.processmanager.ProcessManagerActivity;
import com.jason.jasonutils.provider.WorkerTextActivity;
import com.jason.jasonutils.pwdlock.PwdLockManager;
import com.jason.jasonutils.refresh.PulldownRefreshDemo;
import com.jason.jasonutils.service.BindMyServiceDemo;
import com.jason.jasonutils.sharedpreference.SharedPreferenceSetAPIDemo;
import com.jason.jasonutils.slidecutlistview.SlideCutListViewDemoActivity;
import com.jason.jasonutils.smile.SimleDemo;
import com.jason.jasonutils.sms.ReceiveMessageDemo;
import com.jason.jasonutils.sms.SendMessageDemo;
import com.jason.jasonutils.sms.SystemCallRecordSmsChangeddemoActivity;
import com.jason.jasonutils.sms.backup.BackupRebackMsgDemo;
import com.jason.jasonutils.sms.sendmsg.SelectContactToSendMsgActivity;
import com.jason.jasonutils.sortsequence.SortListActivity;
import com.jason.jasonutils.spinner.SpinnerDemoActivity;
import com.jason.jasonutils.tabhost.TabHostDemo;
import com.jason.jasonutils.test.TestDemo;
import com.jason.jasonutils.timer.TimerDemoListActivity;
import com.jason.jasonutils.tools.ToastUtil;
import com.jason.jasonutils.tools.ViewUtil;
import com.jason.jasonutils.touch.TouchActivityOne;
import com.jason.jasonutils.view.listview.MultipleItemsList;
import com.jason.jasonutils.webview.WebViewActivity;
import com.jason.jasonutils.widgetcomponent.EditTextDemo;
import com.jason.jasonutils.xmlparser.XmlSerialParserActivity;
import com.jason.jasonutils.zip.ZipDemoActivity;

public class JasonUtilsMainActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jason_utils_main_activity);
		ViewUtil.setChildsOnClickLisener(this, R.id.jasonutils_main_container, 0, this);
	}

	/**
	 * 处理点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_toast:
			ToastUtil.showCustomToast(this);
			break;
		case R.id.bt_dimen:
			startActivity(new Intent().setClass(this, TestActivity.class));
			break;
		case R.id.bt_webView:
			WebViewActivity.openWebView(this);
			break;
		case R.id.bt_jump_popupwindown:
			startActivity(new Intent(this, PopupWindownDemo.class));
			break;
		case R.id.bt_fix_picture:
			startActivity(new Intent(this, FixPictureActivity.class));
			break;
		case R.id.bt_border_picture:
			startActivity(new Intent(this, ImageBorderActivity.class));
			break;
		case R.id.bt_progress_onkeydown:
			startActivity(new Intent(this, CustomizedProgressDialogActivity.class));
			break;
		case R.id.bt_choosecut_pic:
			startActivity(new Intent(this, ChooseCutPic_Activity.class));
			break;
		case R.id.bt_gridview_demo:
			startActivity(new Intent(this, GridViewDemoListActivity.class));
			break;
		case R.id.bt_getX_getRawX:
			startActivity(new Intent(this, DifferentgetXgetRawX.class));
			break;
		case R.id.bt_mcwill_status:
			startActivity(new Intent(this, McWillReceiverDemo.class));
			break;
		case R.id.bt_send_message:
			startActivity(new Intent(this, SendMessageDemo.class));
			break;
		case R.id.bt_send_mcwill_msg:
			startActivity(new Intent(this, TestDemo.class));
			break;
		case R.id.bt_read_latest_msg:
			startActivity(new Intent(this, ReceiveMessageDemo.class));
			break;
		case R.id.bt_send_notification:
			startActivity(new Intent(this, NotificationDemo.class));
			break;
		case R.id.bt_operate_database:
			startActivity(new Intent(this, DbTestDemo.class));
			break;
		case R.id.bt_timer_use:
			startActivity(new Intent(this, TimerDemoListActivity.class));
			break;
		case R.id.bt_check_network:
			startActivity(new Intent(this, NetWorkTestDemo.class));
			break;
		case R.id.bt_send_reboot_broadcast:
			startActivity(new Intent(this, SendRebootBroadcastActivity.class));
			break;
		case R.id.bt_pwoermanager_demo:
			startActivity(new Intent(this, PowerMangerDemo.class));
			break;
		case R.id.bt_service_demo:
			startActivity(new Intent(this, BindMyServiceDemo.class));
			break;
		case R.id.bt_dialog_demo:
			startActivity(new Intent(this, DialogDemo.class));
			break;
		case R.id.bt_filter_demo:
			startActivity(new Intent(this, FilterDemoActivity.class));
			break;
		case R.id.bt_sort_demo:
			startActivity(new Intent(this, SortListActivity.class));
			break;
		case R.id.bt_floatlayer_demo:
			startActivity(new Intent(this, FloatLayer2IntroduceFunction.class));
			break;
		case R.id.bt_asyncqueryhandler_demo:
			startActivity(new Intent(this, AsyncQueryHandlerDemo.class));
			break;
		case R.id.bt_adapter_demo:
			startActivity(new Intent(this, AdapterDemoActivity.class));
			break;
		case R.id.bt_search_framework_demo:
			onSearchRequested();
			break;
		case R.id.bt_actionbar_demo:
			startActivity(new Intent(this, ActionBarDemo.class));
			break;
		case R.id.bt_sharedpreference_setapi_demo:
			startActivity(new Intent(this, SharedPreferenceSetAPIDemo.class));
			break;
		case R.id.bt_mediaplayer_demo:
			startActivity(new Intent(this, MediaPlayerDemo.class));
			break;
		case R.id.bt_surfaceview_demo:
			//startActivity(new Intent(this, SurfaceViewDemo.class));
			break;
		case R.id.bt_camera_demo:
			startActivity(new Intent(this, CameraDemoEarlier.class));
			break;
		case R.id.bt_xmlserialparser_demo:
			startActivity(new Intent(this, XmlSerialParserActivity.class));
			break;
		case R.id.bt_linux_read_write_mode_demo:
			startActivity(new Intent(this, LinuxReadWriteModeDemo.class));
			break;
		case R.id.bt_system_sms_callrecord_changed_demo:
			startActivity(new Intent(this, SystemCallRecordSmsChangeddemoActivity.class));
			break;
		case R.id.bt_operate_syscontacts_demo:
			startActivity(new Intent(this, OperateSysContactsActivity.class));
			break;
		case R.id.bt_location_demo:
			startActivity(new Intent(this, LocationDemoListActivity.class));
			break;
		case R.id.bt_worker_provider_demo:
			startActivity(new Intent(this, WorkerTextActivity.class));
			break;
		case R.id.bt_select_contact_sendmsg_demo:
			startActivity(new Intent(this, SelectContactToSendMsgActivity.class));
			break;
		case R.id.bt_intent_demo:
			startActivity(new Intent(this, IntentDemoActivity.class));
			break;
		case R.id.bt_picture_operate_demo:
			startActivity(new Intent(this, PictureOperationDemoActivity.class));
			break;
		case R.id.bt_slide_cut_listview_demo:
			startActivity(new Intent(this, SlideCutListViewDemoActivity.class));
			break;
		case R.id.bt_camera_operate_demo:
			startActivity(new Intent(this, CameraDemoActivity.class));
			break;
		case R.id.bt_custom_view_demo:
			startActivity(new Intent(this, CustomViewDemoShowActivity.class));
			break;
		case R.id.bt_edittext_view_demo:
			startActivity(new Intent(this, EditTextDemo.class));
			break;
		case R.id.bt_touch_demo:
			startActivity(new Intent(this, TouchActivityOne.class));
			break;
		case R.id.bt_baidu_map_demo:
			startActivity(new Intent(this, BaiduMapDemoList.class));
			break;
		case R.id.bt_menu_demo:
			startActivity(new Intent(this, MenuDemoActivity.class));
			break;
		case R.id.bt_applock_manager:
			startActivity(new Intent(this, PwdLockManager.class));
			break;
		case R.id.bt_comingcall_address_manager:
			startActivity(new Intent(this, PhoneNumberManager.class));
			break;
		case R.id.bt_black_number_demo:
			startActivity(new Intent(this, BlackNumberFunctionListActivity.class));
			break;
		case R.id.bt_lrucache_demo:
			startActivity(new Intent(this, ShowPcitureActivity.class));
			break;
		case R.id.bt_spinner_demo:
			startActivity(new Intent(this, SpinnerDemoActivity.class));
			break;
		case R.id.bt_sms_operate_demo:
			startActivity(new Intent(this, BackupRebackMsgDemo.class));
			break;
		case R.id.bt_app_manager_demo:
			startActivity(new Intent(this, AppManagerActivity.class));
			break;
		case R.id.bt_process_manager_demo:
			startActivity(new Intent(this, ProcessManagerActivity.class));
			break;
		case R.id.bt_app_lock_demo:
			startActivity(new Intent(this, AppLockManagerActivity.class));
			break;
		case R.id.bt_clean_app_cache_demo:
			startActivity(new Intent(this, CleanAppCacheActivity.class));
			break;
		case R.id.bt_tabhost_demo:
			startActivity(new Intent(this, TabHostDemo.class));
			break;
		case R.id.bt_fragment_demo:
			startActivity(new Intent(this, FragmentDmeoList.class));
			break;
		case R.id.bt_zip_demo:
			startActivity(new Intent(this, ZipDemoActivity.class));
			break;
		case R.id.bt_simle_demo:
			startActivity(new Intent(this, SimleDemo.class));
			break;
		case R.id.bt_scrowViewListView_demo:
			startActivity(new Intent(this, CreditCardActivity.class));
			break;
		case R.id.bt_litehttp_demo:
			startActivity(new Intent(this, LiteHttpDemo.class));
			break;
		case R.id.bt_listview_adapter_type_demo:
			startActivity(new Intent(this, MultipleItemsList.class));
			break;
		case R.id.bt_listview_limited_demo:
			startActivity(new Intent(this, ListViewLimited.class));
			break;
		case R.id.bt_pull_down_refresh_demo:
			startActivity(new Intent(this, PulldownRefreshDemo.class));
			break;
		case R.id.cmb_pay_demo:
			startActivity(new Intent(this, MyCMBPayActivity.class));
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("main onPause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("main onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("main onDestroy");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("main haha", true);
		super.onSaveInstanceState(outState);
		System.out.println("main onSaveInstanceState");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("main onRestoreInstanceState");
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		System.out.println("main onNewIntent");
	}
	
}
