package com.orange.game.draw.activity.common;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class CommonDummyTabContent implements TabContentFactory{
	private Context mContext;
	
	public CommonDummyTabContent(Context context){
		mContext = context;
	}
			

	@Override
	public View createTabContent(String tag) {
		View v = new View(mContext);
		return v;
	}
	

}
