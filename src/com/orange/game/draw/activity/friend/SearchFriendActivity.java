/**  
        * @title SearchFriendActivity.java  
        * @package com.orange.game.draw.activity.home  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-24 下午3:47:53  
        * @version V1.0  
 */
package com.orange.game.draw.activity.friend;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.friend.adapter.DrawHomeFriendAdapter;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.friend.FriendMission;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.PBSNSUserUtils;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;



public class SearchFriendActivity extends CommonDrawActivity
{
	private DrawHomeFriendAdapter adapter;
	private EditText keywordEditText;
	private FriendManager searchListManager;
	private String keyword;
	private PullToRefreshListView pullToRefreshListView;
	//private PopupWindow userDetailPopupWindow;;
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
		setContentView(R.layout.search_friend);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		keywordEditText = (EditText) findViewById(R.id.search_friend_key_word);
		Button searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(searchOnClickListener);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.search_friend_list);
		pullToRefreshListView.setMode(Mode.PULL_FROM_END);
		adapter = new DrawHomeFriendAdapter(null, SearchFriendActivity.this);
		pullToRefreshListView.getRefreshableView().setAdapter(adapter);
		pullToRefreshListView.setOnRefreshListener(onRefreshListener);
		searchListManager = new FriendManager(Friend.FRIEND_FAN);
		pullToRefreshListView.getRefreshableView().setOnItemClickListener(itemClickListener);
		
	}
	
	private OnClickListener searchOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			//KeyboardUtil.hideSoftKeyboard(keywordEditText, SearchFriendActivity.this);
			keyword = keywordEditText.getText().toString();
			searchFriend(keyword);
		}
	};

	
	private void searchFriend(String keyword){
		showProgressDialog(R.string.loading);
		searchListManager.clear();
		int offset = 0;
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		FriendMission.getInstance().searchUser(searchListManager, keyword, offset, limit, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				hideDialog();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					adapter.setFriendList(searchListManager.getFriendList());
					adapter.notifyDataSetChanged();
				}else {
					DrawNetworkConstants.errorToast(SearchFriendActivity.this, errorCode);
				}
				
			}
		});
	}
	
	private void loadMoreSearchResult(String keyword){
		showProgressDialog(R.string.loading);
		int offset = adapter.getCount();
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		FriendMission.getInstance().searchUser(searchListManager, keyword, offset, limit, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				hideDialog();
				pullToRefreshListView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					adapter.notifyDataSetChanged();
				}else {
					DrawNetworkConstants.errorToast(SearchFriendActivity.this, errorCode);
				}
				
			}
		});
	}
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh(PullToRefreshBase refreshView)
		{
			loadMoreSearchResult(keyword);
		}
	};
	
	
	
	
	
	
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			Friend friend = adapter.getFriendList().get(arg2-1);
			UserMission.getInstance().getUserByUserId(friend.getUser().getUserId(), new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						UserDetailActivity.newInstance(
								SearchFriendActivity.this,
								UserManager.getInstance().getTempGameUser(),
								UserManager.getInstance().getTempUserRelationShip());
					}else {
						DrawNetworkConstants.errorToast(SearchFriendActivity.this, errorCode);
					}
				}
			});
			
		}
	};
	
	
	
}
