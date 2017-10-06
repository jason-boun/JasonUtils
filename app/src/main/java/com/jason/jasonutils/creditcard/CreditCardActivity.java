package com.jason.jasonutils.creditcard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.jasonutils.R;

public class CreditCardActivity extends Activity {

	private ListView lv_card_available, lv_card_overdue, lv_card_unavailable;

	private final List<CreditCardInfo> cardInfoListv = new ArrayList<CreditCardInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_quickpay_cardhistory);
		initData();
		init();
	}

	private void init() {
		lv_card_available = (ListView) findViewById(R.id.lv_quickpay_card_available);
		lv_card_overdue = (ListView) findViewById(R.id.lv_quickpay_card_overdue);
		lv_card_unavailable = (ListView) findViewById(R.id.lv_quickpay_card_unavailable);

		lv_card_available.setAdapter(new QuickPayHistoryAdapter(this, 0, cardInfoListv));

		lv_card_overdue.setAdapter(new QuickPayHistoryAdapter(this, 1, cardInfoListv));

		lv_card_unavailable.setAdapter(new QuickPayHistoryAdapter(this, 2, cardInfoListv));
		setListViewHeightBasedOnChildren(lv_card_available);
		setListViewHeightBasedOnChildren(lv_card_overdue);
		setListViewHeightBasedOnChildren(lv_card_unavailable);
	}

	void initData() {
		cardInfoListv.clear();
		CreditCardInfo cardInfo;
		for (int i = 0; i < 20; i++) {
			cardInfo = new CreditCardInfo();
			cardInfo.bankName = "招商银行";
			cardInfo.cardNumber = "**** **** 1234 ****";
			cardInfo.CardType = "信用卡";
			cardInfoListv.add(cardInfo);
		}
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param lv
	 */
	public static void setListViewHeightBasedOnChildren(ListView lv) {
		if (lv == null) {
			return;
		}
		ListAdapter adapter = lv.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, lv);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = lv.getLayoutParams();
		params.height = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
		lv.setLayoutParams(params);
	}
	
	class QuickPayHistoryAdapter extends BaseAdapter {
		private final Context ctx;
		private final int availableType;
		private final List<CreditCardInfo> cardInfoList;

		/**
		 * 
		 * @param ctx
		 * @param availableType
		 *            银行卡可用标识：0-可用；1-过期；2-本单不可用
		 * @param cardInfoList
		 * @param unAvailableCardNoList
		 *            不可用的信用卡号列表
		 */
		public QuickPayHistoryAdapter(Context ctx, int availableType, List<CreditCardInfo> cardInfoList) {
			this.ctx = ctx;
			this.availableType = availableType;
			this.cardInfoList = cardInfoList;
		}

		@Override
		public int getCount() {
			return cardInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return cardInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(ctx, R.layout.payment_quickpay_cardhistory_item, null);
				holder.bankIcon = (ImageView) convertView.findViewById(R.id.iv_bank_icon);
				holder.bankName = (TextView) convertView.findViewById(R.id.quickpay_tv_bankname);
				holder.cardNumber = (TextView) convertView.findViewById(R.id.quickpay_tv_cardnumber);
				holder.cardType = (TextView) convertView.findViewById(R.id.quickpay_tv_cardtype);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CreditCardInfo cardInfo = cardInfoList.get(position);
			holder.bankName.setText(cardInfo.bankName);
			holder.cardNumber.setText(cardInfo.cardNumber);
			holder.cardType.setText(cardInfo.CardType);
			if (availableType != 0) {
				convertView.setEnabled(false);
				convertView.setBackgroundColor(Color.GRAY);
			}
			return convertView;
		}

		public class ViewHolder {
			ImageView bankIcon;
			TextView bankName;
			TextView cardNumber;
			TextView cardType;
		}

	}
}
