/**  
        * @title TimelineDrawToMeFragment.java  
        * @package com.orange.game.draw.activity.timeline.fragment  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-11 上午11:33:27  
        * @version V1.0  
 */
package com.orange.game.draw.activity.timeline.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonFragment;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.activity.timeline.adapter.TimelineCommentListAdapter;
import com.orange.game.draw.activity.top.adapter.PhotoGalleryGridViewAdapter;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-11 上午11:33:27  
 */

public class TimelineDrawToMeFragment extends CommonFragment
{
	private static final String TAG = "TimelineDrawToMeFragment";

	private static final String BUNDLE_KEY_TAB_ID = "tabId";

	public static TimelineDrawToMeFragment newInstance(FragmentManager fm, FragmentTransaction ft, String tabId){
		TimelineDrawToMeFragment timelineDrawToMeFragment = (TimelineDrawToMeFragment) fm.findFragmentByTag(tabId);		
		if(timelineDrawToMeFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			Bundle bundle = TimelineDrawToMeFragment.createBundle(tabId);
			timelineDrawToMeFragment = new TimelineDrawToMeFragment();
			timelineDrawToMeFragment.setArguments(bundle);
			ft.add(R.id.real_tab_content, timelineDrawToMeFragment, tabId);			
		}else{	
			ft.show(timelineDrawToMeFragment);
		}
		return timelineDrawToMeFragment;
	}
	
	
	
	
	public static Bundle createBundle(String tabId){
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_KEY_TAB_ID, tabId);
		return bundle;
	}
	
	
	
	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId ){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		TimelineDrawToMeFragment.newInstance(fm, ft, tabId);
		ft.commit();
	}
	
	
	private String tabId;
	private FeedManager feedManager ;
	private PullToRefreshGridView pullToRefreshGridView;
	private PhotoGalleryGridViewAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "<onCreate>");
		tabId = getArguments().getString(BUNDLE_KEY_TAB_ID);
		feedManager = new FeedManager(FeedListType.FeedListTypeDrawToMe);
		loadData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{	
		Log.d(TAG, "<onCreateView>");
		View fragmentView = null;
		fragmentView = initGridFragment(inflater, container, getActivity());
		
		return fragmentView;
	}
	
	
	public View initGridFragment(LayoutInflater inflater,ViewGroup container,Context context){
		int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		View fragmetnView = inflater.inflate(R.layout.common_feed_grid_fragment, container, false);
		pullToRefreshGridView =  (PullToRefreshGridView) fragmetnView.findViewById(R.id.common_fragment_grid_view);
		GridView gridView = pullToRefreshGridView.getRefreshableView();
		pullToRefreshGridView.setMode(Mode.BOTH);
		pullToRefreshGridView.setOnRefreshListener(onRefreshListener2);	
		adapter = new PhotoGalleryGridViewAdapter(feedManager.getFeedList(), getActivity(), screenWidth);
		gridView.setAdapter(adapter);			
		pullToRefreshGridView.getRefreshableView().setOnItemClickListener(
				onItemClickListener);
		return fragmetnView;
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			PBFeed feed = feedManager.getFeedList().get(arg2);
			DrawDetailActivity.newInstance(getActivity(), feed.getFeedId());
		}
	};
	
	
	
	private OnRefreshListener2 onRefreshListener2 = new OnRefreshListener2(){

		@Override
		public void onPullDownToRefresh(PullToRefreshBase refreshView)
		{
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			loadData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase refreshView)
		{
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			loadMoreData();
			
		}
		
	};
	
	
	
	
	
	
	private String getLastUpdateTime(){
		String lastUpdateTime = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		return lastUpdateTime;
	}




	@Override
	public void loadData()
	{
		if (((CommonDrawActivity)getActivity()).isProgressDialogShowing())
		    return;
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
        int offset = 0;
        int limit = ConfigManager.getInstance().getFeedListDisplayCount();
        FeedMission.getInstance().getDrawToMe(feedManager,offset, limit, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				pullToRefreshGridView.onRefreshComplete();
				((CommonDrawActivity)getActivity()).hideDialog();
				if (errorCode == ErrorCode.ERROR_SUCCESS){
					adapter.notifyDataSetChanged();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}
				
			}
		});
	}




	@Override
	public void loadMoreData()
	{
		if (((CommonDrawActivity)getActivity()).isProgressDialogShowing())
		    return;
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
        int offset = feedManager.getFeedListCount();
        int limit = ConfigManager.getInstance().getFeedListDisplayCount();
        FeedMission.getInstance().getDrawToMe(feedManager,offset, limit, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				((CommonDrawActivity)getActivity()).hideDialog();
				pullToRefreshGridView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS){
					adapter.notifyDataSetChanged();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}
			}
		});
		
	}




	@Override
	public void clear()
	{
		feedManager.clear();
	}
}
