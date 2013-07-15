/**  
        * @title TopUserFragment.java  
        * @package com.orange.game.draw.activity.top.fragment  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-27 上午10:58:46  
        * @version V1.0  
 */
package com.orange.game.draw.activity.top.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.adapter.CommonFeedFragmentAdapter;
import com.orange.game.draw.activity.timeline.TimelineActivity;
import com.orange.game.draw.activity.top.TopDrawActivity;
import com.orange.game.draw.activity.top.adapter.TopUserAdapter;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.TopUserMission;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.user.TopUserManager;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;



public class TopUserFragment extends Fragment
{
	 private static final String TAG = "TopUserFragment";
	    private PullToRefreshGridView pullToRefreshGridView;
	    private TopUserAdapter adapter;
	    public static final int VIEW_TYPE_GRID = 0;
	    public static final int VIEW_TYPE_LIST = 1;
		protected int screenWidth = 0;
		private TopUserManager topUserManager;
		
		private static final String BUNDLE_KEY_TAB_ID = "tabId";
		
		
		
		
		public static TopUserFragment newInstance(FragmentManager fm, FragmentTransaction ft, String tabId){

			TopUserFragment topUserFragment = (TopUserFragment) fm.findFragmentByTag(tabId);		
			if(topUserFragment==null){		
				Log.d(TAG, "add  draw fragment "+tabId);
				Bundle bundle = TopUserFragment.createBundle(tabId);
				topUserFragment = new TopUserFragment();
				topUserFragment.setArguments(bundle);
				ft.add(R.id.real_tab_content, topUserFragment, tabId);			
			}else{	
				ft.show(topUserFragment);
			}
			return topUserFragment;
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
			TopUserFragment.newInstance(fm, ft, tabId);
			ft.commit();
		}
		
		
		
		
		
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			topUserManager = TopUserMission.getInstance().getTopLevelUserManager();
			loadAllFeedList(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
		{	
			screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
			Log.d(TAG, "screen width = "+screenWidth);
			View fragmentView = fragmentView = initGridFragment(inflater, container, getActivity(), screenWidth);	
		
			return fragmentView;
		}

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			topUserManager.clear();
			topUserManager = null;
		}

		@Override
		public void onDestroyView()
		{
			super.onDestroyView();
		}

		@Override
		public void onResume(){
			super.onResume();
		}
		
		

		@Override
		public void onStart()
		{
			super.onStart();		
		}
		
		
		
		
		
		public View initGridFragment(LayoutInflater inflater,ViewGroup container,Context context,int screenWidth){
			View fragmetnView = inflater.inflate(R.layout.common_feed_grid_fragment, container, false);
			pullToRefreshGridView =  (PullToRefreshGridView) fragmetnView.findViewById(R.id.common_fragment_grid_view);
			GridView gridView = pullToRefreshGridView.getRefreshableView();
			pullToRefreshGridView.setMode(Mode.BOTH);
			pullToRefreshGridView.setOnRefreshListener(onRefreshListener2);		
			adapter = new TopUserAdapter(null, getActivity(), screenWidth);
			gridView.setAdapter(adapter);			
			pullToRefreshGridView.setOnScrollListener(gridviewOnScrollListener);
			pullToRefreshGridView.getRefreshableView().setOnItemClickListener(itemClickListener);
			return fragmetnView;
		}
		
		
		private OnRefreshListener2 onRefreshListener2 = new OnRefreshListener2()
		{
			
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView)
			{
				

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
				loadAllFeedList(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView)
			{
				loadMoreFeedList();	
				
			}
		};
		
		public void refreshComplete(){
		
			
			if (pullToRefreshGridView != null){
				pullToRefreshGridView.onRefreshComplete();
			}
		}
		
		
		public void loadAllFeedList(boolean showLoadingDialog){
			if (showLoadingDialog)
				((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
	        int start = 0;
	        int count = ConfigManager.getInstance().getFeedListDisplayCount();
	        MissionCompleteInterface completeHandler = new MissionCompleteInterface(){			
				@Override
				public void onComplete(int errorCode) {
					((CommonDrawActivity)getActivity()).hideDialog();
					if (errorCode == ErrorCode.ERROR_SUCCESS){
						adapter.setGameUsers(topUserManager.getTopUserList());
						adapter.notifyDataSetChanged();
					}else {
						DrawNetworkConstants.errorToast(getActivity(), errorCode);
					}				
					refreshComplete();
				}
			};
			TopUserMission.getInstance().getTopLevelUsers( start, count, completeHandler);
		}
		
		
		public void loadMoreFeedList(){		
	        final int start = adapter.getCount();
	        final int count = ConfigManager.getInstance().getFeedListDisplayCount();
	        
	        MissionCompleteInterface completeHandler = new MissionCompleteInterface() {			
				@Override
				public void onComplete(int errorCode) {
					if (errorCode == ErrorCode.ERROR_SUCCESS){
						adapter.notifyDataSetChanged();
					}else {
						DrawNetworkConstants.errorToast(getActivity(), errorCode);
					}				
					refreshComplete();
				}
			};
			TopUserMission.getInstance().getTopLevelUsers(start, count, completeHandler);
			
		}
		
		
		
		private OnItemClickListener itemClickListener = new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				PBGameUser gameUser = topUserManager.getTopUserList().get(arg2);
				showUserDetail(getActivity(), gameUser, arg1);
			}
		};
		
		
		
		private void showUserDetail(final Context context,PBGameUser gameUser,View view){
			//CommonPopupView.getUserDetailPopupWindow(context, gameUser, view);
			//UserDetailActivity.newInstance(context, gameUser,Friend.FRIEND_UNKNOW);
			UserMission.getInstance().getUserByUserId(gameUser.getUserId(), new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						UserDetailActivity.newInstance(context, 
								UserManager.getInstance().getTempGameUser(), 
								UserManager.getInstance().getTempUserRelationShip());
					}
				}
			});
		}

		
		
		
		
		private OnScrollListener gridviewOnScrollListener = new OnScrollListener()
		{
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
				{
					pullToRefreshGridView.setMode(Mode.PULL_FROM_END);
					
				}else if (scrollState == SCROLL_STATE_IDLE) {
					pullToRefreshGridView.setMode(Mode.BOTH);
				}else if (scrollState == SCROLL_STATE_FLING)
				{
					pullToRefreshGridView.setMode(Mode.DISABLED);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{			
			}
		};

		private String getLastUpdateTime(){
			String lastUpdateTime = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			return lastUpdateTime;
		}

		
	

		public void clear(){
			topUserManager.clear();
			topUserManager = null;
		}
		
		@Override
		public void onHiddenChanged(boolean hidden)
		{
			super.onHiddenChanged(hidden);
			if (hidden)
			{
				ImageLoader.getInstance().clearMemoryCache();
			}
		}
}
