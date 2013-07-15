/**  
        * @title DrawHomeFriendFragment.java  
        * @package com.orange.game.draw.activity.home.fragment  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-18 上午10:33:29  
        * @version V1.0  
 */
package com.orange.game.draw.activity.friend.fragment;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.ActivityUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.friend.SearchFriendActivity;
import com.orange.game.draw.activity.friend.adapter.DrawHomeFriendAdapter;
import com.orange.game.draw.activity.home.DrawHomeActivity;
import com.orange.game.draw.activity.message.DrawHomeMessageDialogActivity;
import com.orange.game.draw.activity.message.DrawHomeMessageFriendListActivity;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.mission.friend.FriendMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.PBSNSUserUtils;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-18 上午10:33:29  
 */

public class DrawHomeFriendFragment extends Fragment
{

	private static final String TAG = "DrawHomeFriendFragment";
	private DrawHomeFriendAdapter myFollowAdapter;
	



	private DrawHomeFriendAdapter myFansAdapter;
	private PullToRefreshListView myFollowListView;
	private PullToRefreshListView myFansListView;
	//private PopupWindow friendDetailPopupWindow;
	private PopupWindow invitePopupWindow;
	private RadioButton myFollow;
	private RadioButton myFans;
	private View view;
	
	private boolean isDrawHome = true;
	
	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		DrawHomeFriendFragment.newInstance(fm, ft, tabId);
		ft.commit();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	
	
	public static DrawHomeFriendFragment newInstance(FragmentManager fm, FragmentTransaction ft,
			String tabId)
	{
		DrawHomeFriendFragment drawHomeFriendFragment = (DrawHomeFriendFragment) fm.findFragmentByTag(tabId);		
		if(drawHomeFriendFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			drawHomeFriendFragment = new DrawHomeFriendFragment();
			ft.add(R.id.real_tab_content, drawHomeFriendFragment, tabId);			
		}else{	
			ft.show(drawHomeFriendFragment);
		}
		return drawHomeFriendFragment;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (container == null)
			return null;
		view = inflater.inflate(R.layout.draw_home_friend_fragment, container,false);
		init(view);
		return view;
	}

	
	

	
	private void init(View view){
		
		ViewGroup headerBar = (ViewGroup) view.findViewById(R.id.header_bar);
		if (!isDrawHome)
		{
			headerBar.setVisibility(View.GONE);
		}
		
		RadioGroup tabGroup  = (RadioGroup) view.findViewById(R.id.draw_home_friend_header_tab_group);
		myFollow = (RadioButton) view.findViewById(R.id.draw_home_friend_my_follow_button);
		myFans   = (RadioButton) view.findViewById(R.id.draw_home_friend_my_fans_button);
		
		Button searchFriendButton = (Button) view.findViewById(R.id.draw_home_friend_search_friend_button);
		Button inviteFriendButton = (Button) view.findViewById(R.id.draw_home_friend_invite_friend_button);
		
		
		myFollowListView = (PullToRefreshListView) view.findViewById(R.id.draw_home_friend_my_follow_list_view);
		myFansListView = (PullToRefreshListView) view.findViewById(R.id.draw_home_friend_my_fans_list_view);
		myFansListView.setMode(Mode.BOTH);
		myFollowListView.setMode(Mode.BOTH);
		myFollowAdapter = new DrawHomeFriendAdapter(null, getActivity());
		myFansAdapter = new DrawHomeFriendAdapter(null, getActivity());
		myFollowListView.getRefreshableView().setAdapter(myFollowAdapter);
		myFansListView.getRefreshableView().setAdapter(myFansAdapter);
		myFansListView.setOnItemClickListener(itemClickListener);
		myFollowListView.setOnItemClickListener(itemClickListener);
		myFansListView.setOnRefreshListener(myFanOnRefreshListener2);
		myFollowListView.setOnRefreshListener(myFollowOnRefreshListener2);
		myFansListView.setOnScrollListener(onScrollListener);
		myFollowListView.setOnScrollListener(onScrollListener);
		
		tabGroup.setOnCheckedChangeListener(onCheckedChangeListener);
		inviteFriendButton.setOnClickListener(inviteFriendOnClickListener);
		searchFriendButton.setOnClickListener(searchFriendOnClickListener);
		
		
		myFollow.setText(
				getString(R.string.follow)
				+"("+UserManager.getInstance().getFollowCount()
				+")");
		myFans.setText(
				getString(R.string.fans)
				+"("+UserManager.getInstance().getFanCount()
				+")");
		//refreshTab();
	}
	
	
	
	
	
	private void loadAllMyFollowData(){
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
		FriendMission.getInstance().getMyFollowFriendManager().clear();
		int start = 0;
		int count = ConfigManager.getInstance().getActionFeedListDisplayCount();
		FriendMission.getInstance().getMyFollowFriendList(start, count, new MissionCompleteInterface()
		{			
			@Override
			public void onComplete(int errorCode)
			{
				if (getActivity() !=null)
					((CommonDrawActivity)getActivity()).hideDialog();
				
				myFollowListView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					FriendManager myFollowFriendManager = FriendMission.getInstance().getMyFollowFriendManager();
					updateView(myFollowFriendManager.getFriendList());
				}else {
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}		
			}
		});
	}
	
	
	private void loadAllMyFanData(){
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
		FriendMission.getInstance().getMyFanFriendManager().clear();
		int start = 0;
		int count = ConfigManager.getInstance().getActionFeedListDisplayCount();
		FriendMission.getInstance().getMyFanFriendList(start, count, new MissionCompleteInterface()
		{			
			@Override
			public void onComplete(int errorCode)
			{
				if(getActivity() != null)
				((CommonDrawActivity)getActivity()).hideDialog();
				
				myFansListView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					FriendManager myFanFriendManager = FriendMission.getInstance().getMyFanFriendManager();
					updateView(myFanFriendManager.getFriendList());
				}else {
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}		
			}
		});
	}

	
	private void loadMoreMyFollowData(){
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
		int start = myFollowAdapter.getCount();
		int count = ConfigManager.getInstance().getActionFeedListDisplayCount();
		FriendMission.getInstance().getMyFollowFriendList(start, count, new MissionCompleteInterface()
		{			
			@Override
			public void onComplete(int errorCode)
			{
				if(getActivity() != null)
				((CommonDrawActivity)getActivity()).hideDialog();
				
				myFollowListView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					myFollowAdapter.notifyDataSetChanged();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}
			}
		});
	}
	
	
	private void loadMoreMyFanData(){
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
		int start = myFansAdapter.getCount();
		int count = ConfigManager.getInstance().getActionFeedListDisplayCount();
		FriendMission.getInstance().getMyFanFriendList(start, count, new MissionCompleteInterface()
		{			
			@Override
			public void onComplete(int errorCode)
			{
				if(getActivity() != null)
				((CommonDrawActivity)getActivity()).hideDialog();
				
				myFansListView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					myFansAdapter.notifyDataSetChanged();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}		
			}
		});
	}
	
	
	
	private OnScrollListener onScrollListener = new OnScrollListener()
	{
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
			{
				myFansListView.setMode(Mode.PULL_FROM_END);
				myFollowListView.setMode(Mode.PULL_FROM_END);
				
			}else if (scrollState == SCROLL_STATE_IDLE) {
				myFansListView.setMode(Mode.BOTH);
				myFollowListView.setMode(Mode.BOTH);
			}else if (scrollState == SCROLL_STATE_FLING)
			{
				myFansListView.setMode(Mode.DISABLED);
				myFollowListView.setMode(Mode.DISABLED);
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount)
		{
			
			
		}
	};
	

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			if (checkedId == R.id.draw_home_friend_my_follow_button)
			{
				myFollowAdapter.notifyDataSetChanged();
				myFollowListView.getRefreshableView().setVisibility(View.VISIBLE);
				myFollowListView.setVisibility(View.VISIBLE);
				myFansListView.setVisibility(View.GONE);
			}else {
				myFansAdapter.notifyDataSetChanged();
				myFollowListView.setVisibility(View.GONE);
				myFansListView.getRefreshableView().setVisibility(View.VISIBLE);
				myFansListView.setVisibility(View.VISIBLE);
				if (myFansAdapter.getCount() == 0)
				{
					loadAllMyFanData();
				}
				
			}
			//refreshTab();
		}
	};
	
	
	
	
	
	
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			Friend friend = null;
			if (myFollowListView.getVisibility() == View.VISIBLE)		
				friend = myFollowAdapter.getFriendList().get(arg2-1);
			else 
				friend = myFansAdapter.getFriendList().get(arg2-1);	
			if (isDrawHome)
			{	
				UserDetailActivity.newInstance(getActivity(), friend.getUser(),friend.getFriendType());
			}else {
				DrawHomeMessageDialogActivity.newInstance(getActivity(), friend.getUser());
				getActivity().finish();
				
			}
			
		}
	};
	
	
	
	
	private OnClickListener inviteCancelOnClick = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if (invitePopupWindow !=null&&invitePopupWindow.isShowing())
			{
				invitePopupWindow.dismiss();
			}		
		}
	};
	
	private OnClickListener displayClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// nothing need to do 			
		}
	};
	
	
	private OnRefreshListener2 myFollowOnRefreshListener2 = new OnRefreshListener2<View>()
	{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<View> refreshView)
		{
			String label = ((DrawHomeActivity)getActivity()).getLastUpdateTime();
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);			
			loadAllMyFollowData();
			
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<View> refreshView)
		{
			loadMoreMyFollowData();
			
		}
	};
	
	
	private OnRefreshListener2 myFanOnRefreshListener2 = new OnRefreshListener2()
	{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase refreshView)
		{
			String label = ((DrawHomeActivity)getActivity()).getLastUpdateTime();
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			
			loadAllMyFanData();
			
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase refreshView)
		{
			loadMoreMyFanData();
			
		}
	};
	
	private OnClickListener inviteFriendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			inviteFriendDialog();			
		}
	};
	
	
	
	private void inviteFriendDialog(){
		invitePopupWindow = new PopupWindow(getActivity());
		View contentView = getActivity().getLayoutInflater().inflate(R.layout.invite_friend, null);
		View inviteGroup = contentView.findViewById(R.id.invite_group);
		Button inviteByMessageButton = (Button) contentView.findViewById(R.id.invite_by_message_button);
		Button inviteByWeixinButton = (Button) contentView.findViewById(R.id.invite_by_weixin_button);
		Button cancelButton = (Button) contentView.findViewById(R.id.cancel_button);
		invitePopupWindow.setContentView(contentView);		
		ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.half_transparent_black));
		invitePopupWindow.setBackgroundDrawable(background);
		invitePopupWindow.setWidth(300);
		invitePopupWindow.setHeight(300);
		invitePopupWindow.setOutsideTouchable(true);
		invitePopupWindow.setFocusable(true);
		invitePopupWindow.update();
		invitePopupWindow.showAtLocation(view.findViewById(R.id.draw_home_friend_my_follow_button), Gravity.CENTER,0, 0);
		contentView.setOnClickListener(inviteCancelOnClick);
		inviteGroup.setOnClickListener(displayClickListener);
		cancelButton.setOnClickListener(inviteCancelOnClick);
		inviteByMessageButton.setOnClickListener(inviteByMessageOnClickListener);
		inviteByWeixinButton.setOnClickListener(inviteByWeixinOnClickListener);
	}
	
	private OnClickListener inviteByMessageOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			 String messageCont = getString(R.string.invite_message);
			ActivityUtil.sendMessage(getActivity(), messageCont);
		}
	};
	
	private OnClickListener inviteByWeixinOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			
		}
	};
	
	
	private OnClickListener searchFriendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), SearchFriendActivity.class);
			getActivity().startActivity(intent);
			
		}
	};
	
	private void updateView(List<Friend> friendList){
		//initUserFriendCount();
		
		if (myFollowListView.getVisibility() == View.VISIBLE)
		{
			myFollowAdapter.setFriendList(friendList);
			myFollowAdapter.notifyDataSetChanged();
		}else {
			myFansAdapter.setFriendList(friendList);
			myFansAdapter.notifyDataSetChanged();
		}
		
	}
	
	
	/*private void initUserFriendCount(){
		FriendMission.getInstance().getUserRelationCount(new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					refreshTab();
				}
				
			}
		});
	}*//*
	
	private void refreshTab(){
		myFollow.setText(
				getString(R.string.follow)
				+"("+FriendMission.getInstance().userManager.getFollowCount()
				+")");
		myFans.setText(
				getString(R.string.fans)
				+"("+FriendMission.getInstance().userManager.getFanCount()
				+")");
		myFollow.setText(
				getString(R.string.follow)
				+"("+UserManager.getInstance().getFollowCount()
				+")");
		myFans.setText(
				getString(R.string.fans)
				+"("+UserManager.getInstance().getFanCount()
				+")");
	}
	*/
	
	@Override
	public void onStart()
	{
		super.onStart();
		if (isHidden())
		{
			return;
		}
		
		if (myFollowListView.getVisibility() == View.VISIBLE)
		{
			if (myFollowAdapter.isEmpty())
				loadAllMyFollowData();	
		}else {
			if(myFansAdapter.isEmpty())
				loadAllMyFanData();
		}
		
	}



	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (invitePopupWindow != null)
			invitePopupWindow.dismiss();
	}



	public void setDrawHome(boolean isDrawHome)
	{
		this.isDrawHome = isDrawHome;
	}
	
}
