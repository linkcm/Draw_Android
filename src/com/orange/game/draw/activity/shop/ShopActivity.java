/**  
        * @title ShopActivity.java  
        * @package com.orange.game.draw.activity.shop  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-20 下午5:00:09  
        * @version V1.0  
 */
package com.orange.game.draw.activity.shop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.orange.game.R;
import com.orange.game.draw.activity.charge.ChargeActivity;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonDummyTabContent;
import com.orange.game.draw.activity.shop.fragment.ShopFragment;
import com.orange.game.draw.model.shop.ShopManager;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.GameBasicProtos;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItemList;



public class ShopActivity extends CommonDrawActivity
{

	public static final String PROPS = "props";
	public static final String TOOLS = "tools";
	public static final String PROMOTIONS = "promotions";
	
	private String currentTabId = "";
	
	private TabHost tabHost;
	@Override
	protected boolean isRegisterServerConnect()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void handleServiceConnected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		currentTabId = PROPS;

		TextView coinTextView = (TextView) findViewById(R.id.coin_balance);
		TextView ignotTextView = (TextView) findViewById(R.id.ingot_balance);
		
		coinTextView.setText(""+UserManager.getInstance().getUser().getCoinBalance());
		ignotTextView.setText(""+UserManager.getInstance().getUser().getIngotBalance());
		Button chargeButton = (Button) findViewById(R.id.charge_button);
		chargeButton.setOnClickListener(chargeOnClickListener);
		initTap();
	}

	
	
	private void initTap()
	{
	   tabHost = (TabHost) findViewById(android.R.id.tabhost);
	   tabHost.setup();

       createOneTab(PROPS, R.drawable.top_draw_left_button, R.drawable.props_icon,R.string.props);
       createOneTab(TOOLS, R.drawable.top_draw_middle_button, R.drawable.tools_icon,R.string.tools);
       createOneTab(PROMOTIONS, R.drawable.top_draw_right_button,R.drawable.promot_icon, R.string.promotions);
       
       tabHost.setCurrentTab(0);
       ShopFragment.createOrShowFragment(getSupportFragmentManager(), currentTabId, currentTabId);
       tabHost.setOnTabChangedListener(tabChangeListener);
		
	}
	

	
	
	
	private void createOneTab(String tabId,int backgroundRes,int icon,int titleRes)
	{
		 View tabView = getLayoutInflater().inflate(R.layout.shop_top_tab_item, null);
	     tabView.findViewById(R.id.tab_item_button).setBackgroundResource(backgroundRes);
	     tabView.findViewById(R.id.tab_item_icon).setBackgroundResource(icon);
	     ((TextView) tabView.findViewById(R.id.tap_title)).setText(titleRes);
		 TabHost.TabSpec spec = tabHost.newTabSpec(tabId);
		 spec.setIndicator(tabView);         
		 spec.setContent(new CommonDummyTabContent(getBaseContext()));
	     tabHost.addTab(spec);
	}
	
	private OnTabChangeListener tabChangeListener = new OnTabChangeListener()
	{
		
		@Override
		public void onTabChanged(String tabId)
		{
			ShopFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTabId);
			currentTabId = tabId;
			
		}
	};
	
	
	private OnClickListener chargeOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(ShopActivity.this, ChargeActivity.class);
			startActivity(intent);
		}
	};
}
