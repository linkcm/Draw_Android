package com.orange.game.draw.activity.message.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.home.DrawHomeActivity;
import com.orange.game.draw.activity.message.DrawHomeMessageDialogActivity;
import com.orange.game.draw.activity.message.DrawHomeMessageFriendListActivity;
import com.orange.game.draw.activity.message.adapter.DrawHomeMessageAdapter;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.mission.friend.FriendMission;
import com.orange.game.draw.mission.message.MessageMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.model.message.MessageStat;
import com.orange.game.draw.network.DrawNetworkConstants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class DrawHomeMessageFragment extends Fragment
{

	private static final String TAG = "DrawHomeChatFragment";
	private DrawHomeMessageAdapter adapter;
	private PullToRefreshListView listView;

	public static void createOrShowFragment(FragmentManager fm, String tabId,
			String currentTabId)
	{
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if (currentFragment != null)
			ft.hide(currentFragment);
		DrawHomeMessageFragment.newInstance(fm, ft, tabId);
		ft.commit();
		ImageLoader.getInstance().clearMemoryCache();
	}

	public static DrawHomeMessageFragment newInstance(FragmentManager fm,
			FragmentTransaction ft, String tabId)
	{
		DrawHomeMessageFragment drawHomeChatFragment = (DrawHomeMessageFragment) fm
				.findFragmentByTag(tabId);
		if (drawHomeChatFragment == null)
		{
			Log.d(TAG, "add  draw fragment " + tabId);
			drawHomeChatFragment = new DrawHomeMessageFragment();
			ft.add(R.id.real_tab_content, drawHomeChatFragment, tabId);
		} else
		{
			ft.show(drawHomeChatFragment);
		}
		return drawHomeChatFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (container == null)
		{
			return null;
		}
		View view = inflater.inflate(R.layout.draw_home_message_fragment,container, false);
		listView = (PullToRefreshListView) view.findViewById(R.id.draw_home_chat_list_view);
		adapter = new DrawHomeMessageAdapter(null, getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickListener);
		listView.setOnScrollListener(onScrollListener);
		Button createNewChatButton = (Button) view.findViewById(R.id.draw_home_chat_create_new_chat);
		createNewChatButton.setOnClickListener(createNewChatOnClickListener);
		listView.setOnRefreshListener(onRefreshListener);
		return view;
	}

	private OnClickListener createNewChatOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(),
					DrawHomeMessageFriendListActivity.class);
			getActivity().startActivity(intent);

		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			MessageStat messageStat = adapter.getMessageStats().get(arg2 - 1);			
			DrawHomeMessageDialogActivity.newInstance(
					getActivity(), 
					messageStat.getPBMessageStat().getFriendUserId(),
					messageStat.getPBMessageStat().getFriendNickName(), 
					messageStat.getPBMessageStat().getFriendAvatar());
		}
	};

	private OnRefreshListener onRefreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh(PullToRefreshBase refreshView)
		{
			
			listView.onRefreshComplete();
			String lastUpdateTime = ((DrawHomeActivity)getActivity()).getLastUpdateTime();
			listView.getLoadingLayoutProxy().setLastUpdatedLabel(lastUpdateTime);
			loadAllMessageList();
			
		}
	};
	
	private OnScrollListener onScrollListener = new OnScrollListener()
	{
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (scrollState == SCROLL_STATE_FLING)
			{
				listView.setMode(Mode.DISABLED);
			}else {
				listView.setMode(Mode.PULL_FROM_START);
			}
			
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount)
		{
			// TODO Auto-generated method stub
			
		}
	};
	
	
	private void loadAllMessageList()
	{

		((DrawHomeActivity) getActivity()).showProgressDialog(R.string.loading);
		int start = 0;
		int count = ConfigManager.getInstance().getFeedListDisplayCount();
		MessageMission.getInstance().getMessageStatList(true, start, count,
				new MissionCompleteInterface()
				{

					@Override
					public void onComplete(int errorCode)
					{
						if(getActivity() != null)
						((DrawHomeActivity) getActivity()).hideDialog();
						
						if (errorCode == ErrorCode.ERROR_SUCCESS)
						{
							adapter.setMessageStats(MessageMission
									.getInstance().getMyMessageManager()
									.getMessageStatList());
							adapter.notifyDataSetChanged();
						}else {
							DrawNetworkConstants.errorToast(getActivity(), errorCode);
						}
					}
				});

	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (isHidden())
		{
			return;
		}
		loadAllMessageList();
	}

}
