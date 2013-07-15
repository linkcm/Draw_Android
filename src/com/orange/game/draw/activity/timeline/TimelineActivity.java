/**  
        * @title TimelineActivity.java  
        * @package com.orange.game.draw.activity.timeline  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-9 上午11:07:28  
        * @version V1.0  
 */
package com.orange.game.draw.activity.timeline;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.R.string;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonDummyTabContent;
import com.orange.game.draw.activity.common.CommonFragment;
import com.orange.game.draw.activity.timeline.fragment.TimelineCommentFragment;
import com.orange.game.draw.activity.timeline.fragment.TimelineDrawToMeFragment;
import com.orange.game.draw.activity.timeline.fragment.TimelineMyFeedFragment;
import com.orange.game.draw.activity.top.fragment.TopUserFragment;
import com.orange.game.draw.mission.statistics.StatisticsMission;
import com.readystatesoftware.viewbadger.BadgeView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-9 上午11:07:28  
 */

public class TimelineActivity extends CommonDrawActivity
{

	public static final String TAB_TIMELINE = "timeline";
	public static final String TAB_OPUS = "opus";
	public static final String TAB_COMMENT = "comment";
	public static final String TAB_DRAW_TO_ME = "drawToMe";
	
	private String currentTab = TAB_TIMELINE;
	private Map<String, BadgeView> tabViewMap;
	
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
		setContentView(R.layout.timeline);
		initHeaderBar();
		initTap();
	}

	private void initHeaderBar()
	{
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		backButton.setOnClickListener(backOnClickListener);
		refreshButton.setOnClickListener(refreshOncClickListener);
	}

	private TabHost tabHost;
	private void initTap()
	{
	   tabHost = (TabHost) findViewById(android.R.id.tabhost);
	   tabHost.setup();

	   int 	feedCount = StatisticsMission.getInstance().getFeedCount();
	   int drawToMeCount = StatisticsMission.getInstance().getDrawToMeCount();
	   int commentCount = StatisticsMission.getInstance().getCommentCount();
	   tabViewMap = new HashMap<String, BadgeView>();
       createOneTab(TAB_TIMELINE, R.drawable.top_draw_left_button, R.string.timeline,0);
      // createOneTab(TAB_OPUS, R.drawable.top_draw_middle_button, R.string.opus,0);
       createOneTab(TAB_COMMENT, R.drawable.top_draw_middle_button, R.string.comment,commentCount);
       createOneTab(TAB_DRAW_TO_ME, R.drawable.top_draw_right_button, R.string.draw_to_me,drawToMeCount);
       
       tabHost.setCurrentTab(0);
       TimelineMyFeedFragment.createOrShowFragment(getSupportFragmentManager(), currentTab, currentTab);
       tabHost.setOnTabChangedListener(tabChangeListener);
		
	}
	
	
	
	
	
	TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {		
		@Override
		public void onTabChanged(String tabId) {	
			if (tabId.equalsIgnoreCase(TAB_TIMELINE))
			{
				 TimelineMyFeedFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTab);
			}else if (tabId.equalsIgnoreCase(TAB_COMMENT))
			{
				TimelineCommentFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTab);
			}
			else if (tabId.equalsIgnoreCase(TAB_DRAW_TO_ME))
			{
				TimelineDrawToMeFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTab);
			}
			currentTab = tabId;
			BadgeView badgeView = tabViewMap.get(tabId);
			if (badgeView != null)
			{
				badgeView.hide();
			}
		}				
		
	};
	
	private void createOneTab(String tabId,int backgroundRes,int titleRes,int badgeNumber)
	{
		 View tabView = getLayoutInflater().inflate(R.layout.top_tab_item, null);
	     tabView.findViewById(R.id.tab_item_button).setBackgroundResource(backgroundRes);
	     ((TextView) tabView.findViewById(R.id.tap_title)).setText(titleRes);
	     if (badgeNumber>0)
		{
	    	 BadgeView badgeView = new BadgeView(this, tabView.findViewById(R.id.tab_item_button));
	    	 if (badgeNumber>10)
			{
				badgeView.setText("N");
			}else {
				badgeView.setText(""+badgeNumber);
			}
		     badgeView.show();
		     tabViewMap.put(tabId, badgeView);
		}
		 TabHost.TabSpec spec = tabHost.newTabSpec(tabId);
		 spec.setIndicator(tabView);         
		 spec.setContent(new CommonDummyTabContent(getBaseContext()));
	     tabHost.addTab(spec);
	}
	
	

	private OnClickListener refreshOncClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			FragmentManager fm =   getSupportFragmentManager();			
			CommonFragment currentFragment = (CommonFragment) fm.findFragmentByTag(currentTab);
			currentFragment.loadData();
		}
	};

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	
	
}
