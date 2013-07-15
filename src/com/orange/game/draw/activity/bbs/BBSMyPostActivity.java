/**  
        * @title BBSMyPostActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-4 上午9:32:24  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.bbs.adapter.BBSPostListAdapter;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.bbs.BBSMission.RangeType;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.bbs.BBSManager;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSUser;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-4 上午9:32:24  
 */

public class BBSMyPostActivity extends CommonDrawActivity
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

	public static final String BOARD_ID = "board_id";
	private PullToRefreshListView listView;
	private BBSPostListAdapter adapter;
	private ImageLoader imageLoader;
	private BBSManager bbsManager = new BBSManager();
	
	private RangeType currentRangeType = RangeType.RangeTypeNew;
	
	public static final String TAG = "BBSMyPostActivity";
	
	public static void newInstance(Context context){
		//Log.d(TAG, "<newInstance> boardId = "+board.getName());
		Intent intent = new Intent();
		intent.setClass(context, BBSMyPostActivity.class);
		//intent.putExtra(BOARD_ID, board.toByteArray());
		context.startActivity(intent);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_my_post);
		listView = (PullToRefreshListView) findViewById(R.id.bbs_board_post_lv);
		adapter = new BBSPostListAdapter(BBSMyPostActivity.this, null);
		listView.setMode(Mode.BOTH);
		listView.setOnRefreshListener(onRefreshListener2);
		listView.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
		
		
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		
		
	}
	
	
	
	
	
	

	
	
	
	
	private OnRefreshListener2 onRefreshListener2 = new OnRefreshListener2()
	{

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
			loadMore();
			
		}
	};
	
	
	
	private void loadData(){
		int offset = 0;
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();		
		adapter.setPostList(bbsManager.getPostList());
		showProgressDialog(R.string.loading);
		BBSMission.getInstance().getPostList(
				UserManager.getInstance().getTestUserId(),
				"", 
				bbsManager,
				currentRangeType, 
				offset,
				limit, 
				new MissionCompleteInterface()
				{
					
					@Override
					public void onComplete(int errorCode)
					{
						listView.onRefreshComplete();
						hideDialog();
						if (errorCode == ErrorCode.ERROR_SUCCESS)
						{	
							adapter.notifyDataSetChanged();
						}
						
					}
				});
	}

	private void loadMore(){
		showProgressDialog(R.string.loading);
		int offset = 0;
		
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		BBSMission.getInstance().getPostList(
				UserManager.getInstance().getTestUserId(), 
				"", 
				bbsManager,
				currentRangeType, 
				offset, 
				limit, 
				new MissionCompleteInterface()
				{
					
					@Override
					public void onComplete(int errorCode)
					{
						listView.onRefreshComplete();
						hideDialog();
						if (errorCode == ErrorCode.ERROR_SUCCESS)
						{
							adapter.notifyDataSetChanged();
						}
						
					}
				});
	}
	
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			PBBBSPost post = null;
			
				post = bbsManager.getPostList().get(arg2-1);
				
			BBSPostDetailActivity.newInstance(BBSMyPostActivity.this, post);
		}
	};
	
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}

	
	
}
