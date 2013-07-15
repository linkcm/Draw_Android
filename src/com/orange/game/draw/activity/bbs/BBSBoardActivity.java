/**  
        * @title BBSBoardActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-25 上午10:11:56  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import junit.framework.Test;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.bbs.adapter.BBSPostListAdapter;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.bbs.BBSMission.RangeType;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSUser;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-25 上午10:11:56  
 */

public class BBSBoardActivity extends CommonDrawActivity
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
	private PBBBSBoard board ;
	private ImageButton postNew;
	private ImageButton postHot;
	private ImageLoader imageLoader;
	
	private RangeType currentRangeType = RangeType.RangeTypeNew;
	
	public static final String TAG = "BBSBoardActivity";
	
	public static void newInstance(Context context,PBBBSBoard board){
		Log.d(TAG, "<newInstance> boardId = "+board.getName());
		Intent intent = new Intent();
		intent.setClass(context, BBSBoardActivity.class);
		intent.putExtra(BOARD_ID, board.toByteArray());
		context.startActivity(intent);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_board);
		listView = (PullToRefreshListView) findViewById(R.id.bbs_board_post_lv);
		adapter = new BBSPostListAdapter(BBSBoardActivity.this, null);
		listView.setMode(Mode.BOTH);
		listView.setOnRefreshListener(onRefreshListener2);
		listView.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
		try
		{
			board = PBBBSBoard.parseFrom(getIntent().getByteArrayExtra(BOARD_ID));
		} catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		TextView titleTextView = (TextView) findViewById(R.id.title_text);
		titleTextView.setText(board.getName());
		
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		ImageButton bbsPostCreate = (ImageButton) findViewById(R.id.bbs_post_create_button);
		
		postNew = (ImageButton) findViewById(R.id.bbs_post_new);
		postHot = (ImageButton) findViewById(R.id.bbs_post_hot);
		
		bbsPostCreate.setOnClickListener(createPostOnClickListener);
		postNew.setOnClickListener(postNewOnClickListener);
		postHot.setOnClickListener(postHotOnClickListener);
		initBoradManagerGroup();
	}
	
	
	
	private void initBoradManagerGroup(){
		imageLoader = ImageLoader.getInstance();
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.bbs_board_manager_group);
		TextView boardManager = new TextView(BBSBoardActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
										  (LayoutParams.WRAP_CONTENT, 
										   LayoutParams.WRAP_CONTENT);
		params.setMargins
					(0, 
					 0, 
					 getResources().getDimensionPixelSize(R.dimen.space_20),
					 0);
		boardManager.setLayoutParams(params);
		boardManager.setText(R.string.board_manager);
		boardManager.setTextColor(getResources().getColor(R.color.white));
		viewGroup.addView(boardManager);
		TextView userNickName;
		ImageView userAvatar;
		LinearLayout.LayoutParams userNickNameParams = new LinearLayout
														  .LayoutParams
														  (getResources()
														  .getDimensionPixelSize(R.dimen.space_60), 
														   LayoutParams.WRAP_CONTENT);
		userNickNameParams.setMargins
						(getResources().getDimensionPixelSize(R.dimen.space_5),
						 0,
						 getResources().getDimensionPixelSize(R.dimen.space_10), 
						 0);
		for (PBBBSUser user:board.getAdminListList())
		{
			userNickName = new TextView(BBSBoardActivity.this);
			userAvatar = new ImageView(BBSBoardActivity.this);
			userNickName.setTextColor(getResources().getColor(R.color.white));
			userNickName.setSingleLine(true);
			userNickName.setEllipsize(TruncateAt.END);
			userNickName.setLayoutParams(userNickNameParams);
			userNickName.setText(user.getNickName());
			userAvatar.setLayoutParams(new LayoutParams(
											getResources().getDimensionPixelSize(R.dimen.space_15), 
											getResources().getDimensionPixelSize(R.dimen.space_15)));
			imageLoader.displayImage(user.getAvatar(), userAvatar);
			viewGroup.addView(userAvatar);
			viewGroup.addView(userNickName);
			
		}
		
	}
	
	
	
	private OnClickListener createPostOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			BBSSendPostActivity.newInstance(BBSBoardActivity.this, board);
			
		}
	};
	
	
	private OnClickListener postNewOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			v.setVisibility(View.GONE);
			currentRangeType = RangeType.RangeTypeHot;
			postHot.setVisibility(View.VISIBLE);
			loadData();
		}
	};
	
	
	private OnClickListener postHotOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			v.setVisibility(View.GONE);
			currentRangeType = RangeType.RangeTypeNew;
			postNew.setVisibility(View.VISIBLE);
			loadData();
		}
	};
	
	
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
		showProgressDialog(R.string.loading);
		switch (currentRangeType)
		{
		case RangeTypeHot:
			adapter.setPostList(BBSMission.getInstance().getHotPostManager().getPostList());
			break;
		case RangeTypeNew:
			adapter.setPostList(BBSMission.getInstance().getNewPostManager().getPostList());
			break;
		default:
			break;
		}
		BBSMission.getInstance().getPostList(
				"",
				board.getBoardId(), 
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
		
		switch (currentRangeType)
		{
		case RangeTypeHot:
			offset = BBSMission.getInstance().getHotPostManager().getPostList().size();
			break;
		case RangeTypeNew:
			offset = BBSMission.getInstance().getNewPostManager().getPostList().size();
			break;
		default:
			break;
		}
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		BBSMission.getInstance().getPostList(
				"", 
				board.getBoardId(), 
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
			switch (currentRangeType)
			{
			case RangeTypeHot:
				post = BBSMission.getInstance().getHotPostManager().getPostList().get(arg2-1);
				break;
			case RangeTypeNew:
				post = BBSMission.getInstance().getNewPostManager().getPostList().get(arg2-1);
				break;
			default:
				break;
			}
			BBSPostDetailActivity.newInstance(BBSBoardActivity.this, post);
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
