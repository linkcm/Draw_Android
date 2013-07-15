/**  
        * @title TopDrawActivity.java  
        * @package com.orange.game.draw.activity.home  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-3 上午10:35:56  
        * @version V1.0  
 */
package com.orange.game.draw.activity.top;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;



import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonDummyTabContent;
import com.orange.game.draw.activity.common.CommonFragment;
import com.orange.game.draw.activity.top.fragment.TopNewestFragment;
import com.orange.game.draw.activity.top.fragment.TopUserFragment;
import com.orange.game.draw.activity.top.fragment.TopWeekFragment;
import com.orange.game.draw.activity.top.fragment.TopYearFragment;



public class TopDrawActivity extends CommonDrawActivity
{
	public static final String TOP_USER = "topUser";
	public static final String TOP_YEAR = "topYear";
	public static final String TOP_WEEK = "topWeek";
	public static final String TOP_NEWEST = "topNewest";
	private String currentTab = TOP_WEEK;	
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_draw);
		initTap(savedInstanceState);
		initTitleBar();
	}

	
	private void initTitleBar()
	{
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		backButton.setOnClickListener(backOnClickListener);
		refreshButton.setOnClickListener(refreshOncClickListener);
	}

	private TabHost tabHost;
	private void initTap(Bundle savedInstanceState)
	{
	   tabHost = (TabHost) findViewById(android.R.id.tabhost);
	   tabHost.setup();

       createOneTab(TOP_USER, R.drawable.top_draw_left_button, R.string.top_player);
       createOneTab(TOP_YEAR, R.drawable.top_draw_middle_button, R.string.year);
       createOneTab(TOP_WEEK, R.drawable.top_draw_middle_button, R.string.week);
       createOneTab(TOP_NEWEST, R.drawable.top_draw_right_button, R.string.newest);
       
       tabHost.setCurrentTab(2);
       TopWeekFragment.createOrShowFragment(getSupportFragmentManager(), currentTab, currentTab);
       tabHost.setOnTabChangedListener(tabChangeListener);
		
	}
	

	
	
	
	private void createOneTab(String tabId,int backgroundRes,int titleRes)
	{
		 View tabView = getLayoutInflater().inflate(R.layout.top_tab_item, null);
	     tabView.findViewById(R.id.tab_item_button).setBackgroundResource(backgroundRes);
	     ((TextView) tabView.findViewById(R.id.tap_title)).setText(titleRes);
		 TabHost.TabSpec spec = tabHost.newTabSpec(tabId);
		 spec.setIndicator(tabView);         
		 spec.setContent(new CommonDummyTabContent(getBaseContext()));
	     tabHost.addTab(spec);
	}
	
	
    TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {		
		@Override
		public void onTabChanged(String tabId) {
			if (tabId.equalsIgnoreCase(TOP_USER))
			{
				TopUserFragment.createOrShowFragment(getSupportFragmentManager(), TOP_USER, currentTab);
			}else if(tabId.equalsIgnoreCase(TOP_WEEK)){
				TopWeekFragment.createOrShowFragment(getSupportFragmentManager(), TOP_WEEK, currentTab);
			}else if (tabId.equalsIgnoreCase(TOP_YEAR)) {
				TopYearFragment.createOrShowFragment(getSupportFragmentManager(), TOP_YEAR, currentTab);
			}else if (tabId.equalsIgnoreCase(TOP_NEWEST)) {
				TopNewestFragment.createOrShowFragment(getSupportFragmentManager(), TOP_NEWEST, currentTab);
			}
			
			currentTab = tabId;
		}				
		
	};
		
		
	
		private OnClickListener refreshOncClickListener = new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				FragmentManager fm =   getSupportFragmentManager();			
				if (currentTab == TopDrawActivity.TOP_USER)
				{
					TopUserFragment topUserFragment = (TopUserFragment) fm.findFragmentByTag(currentTab);
					topUserFragment.loadAllFeedList(true);
				}else {
					CommonFragment commonFragment = (CommonFragment) fm.findFragmentByTag(currentTab);
					commonFragment.loadData();
				}
				
			}
		};

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			finish();
		}

		
        
	

}
