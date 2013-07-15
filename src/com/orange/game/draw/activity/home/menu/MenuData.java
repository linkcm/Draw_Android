package com.orange.game.draw.activity.home.menu;

import android.view.View.OnClickListener;

public class MenuData {

	final public int textId;
	final public int imageId;
	final public OnClickListener onClickListener;
	final public int badgeNumberData;
	final public boolean isShowBadgeNumber;
	
	public MenuData( int imageId, int textId,OnClickListener onClickListener,
			int badgeNumberData, boolean isShowBadgeNumber)
	{
		super();
		this.textId = textId;
		this.imageId = imageId;
		this.onClickListener = onClickListener;
		this.badgeNumberData = badgeNumberData;
		this.isShowBadgeNumber = isShowBadgeNumber;
	}
	
	
	/*public MenuData(int imageId, int textId, OnClickListener onClickListener){
		this.imageId = imageId;
		this.textId = textId;
		this.onClickListener = onClickListener;
	}*/
}
