package android.widget;

import java.util.ArrayList;

import android.widget.ListView.FixedViewInfo;

public class HeaderViewListAdapterDemo extends HeaderViewListAdapter {

	public HeaderViewListAdapterDemo(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
		super(headerViewInfos, footerViewInfos, adapter);
	}

}
