/**  
 * @title DrawHomeChatCreateActivity.java  
 * @package com.orange.game.draw.activity.chat  
 * @description   
 * @author liuxiaokun  
 * @update 2013-1-31 下午2:41:58  
 * @version V1.0  
 */
package com.orange.game.draw.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.friend.adapter.DrawHomeFriendAdapter;
import com.orange.game.draw.activity.friend.fragment.DrawHomeFriendFragment;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.friend.FriendMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.network.DrawNetworkConstants;

public class DrawHomeMessageFriendListActivity extends CommonDrawActivity
{

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
		setContentView(R.layout.draw_home_message_friend_list);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		FragmentManager fm = getSupportFragmentManager();
		DrawHomeFriendFragment friendListFragment = new DrawHomeFriendFragment();
		friendListFragment.setDrawHome(false);
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.add(R.id.fragment_group, friendListFragment);
		fragmentTransaction.commit();
		
	}
}
