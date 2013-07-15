/**  
        * @title BbsActivity.java  
        * @package com.orange.game.draw.activity.bbs.activity  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-23 上午11:50:28  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.bbs.adapter.BBSBoardListAdapter;
import com.orange.game.draw.activity.bbs.adapter.BBSPostListAdapter;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.bbs.BBSManager;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPrivilege;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-23 上午11:50:28  
 */

public class BBSActivity extends CommonDrawActivity
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

	private PullToRefreshExpandableListView expandableListView;

	
	
	private BBSBoardListAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		expandableListView = (PullToRefreshExpandableListView) findViewById(R.id.bbs_lv);
		adapter = new BBSBoardListAdapter(BBSActivity.this,null);
		adapter.setBoardList(BBSMission.getInstance().getBoardManager().getBoardList());
		expandableListView.getRefreshableView().setAdapter(adapter);
		expandableListView.getRefreshableView().setOnChildClickListener(onChildClickListener);
		expandableListView.setOnRefreshListener(onRefreshListener);
		expandableListView.getRefreshableView().setOnGroupClickListener(groupClickListener);
		
		ImageButton myCommentButton = (ImageButton) findViewById(R.id.bbs_my_commont);
		ImageButton myPostButton = (ImageButton) findViewById(R.id.bbs_my_post);
		
		myPostButton.setOnClickListener(myPostOnClickListener);
		myCommentButton.setOnClickListener(myCommentOnClickListener);
		/*
		BBSMission.getInstance().getBBSUserPrivilegeList(new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
			}
		});*/
	}

	
	private OnClickListener myPostOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			BBSMyPostActivity.newInstance(BBSActivity.this);
			
		}
	};
	
	
	private OnClickListener myCommentOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(BBSActivity.this, BBSMyCommentActivity.class);
			startActivity(intent);
		}
	};
	
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh(PullToRefreshBase refreshView)
		{
			// TODO Auto-generated method stub
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			loadData();
			
		}
	};
	
	
	private void loadData(){
		showProgressDialog(R.string.loading);
		BBSMission.getInstance().getBoardManager().boardClear();
		BBSMission.getInstance().getBBSBoardList(new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				expandableListView.onRefreshComplete();
				hideDialog();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					
					adapter.notifyDataSetChanged();
					if (BBSMission.getInstance().getBoardManager().getBoardList().size()>0)
					{
						expandableListView.getRefreshableView().expandGroup(0);
					}				
				}
				
			}
		});
	}

	private OnChildClickListener onChildClickListener = new OnChildClickListener()
	{
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id)
		{
			if (adapter.getBoardList().size()>1)
			{
				PBBBSBoard board = adapter.getBoardList().get(childPosition+1);
				BBSBoardActivity.newInstance(BBSActivity.this, board);	
			}
			return false;
		}
	};
	
	private OnGroupClickListener groupClickListener = new OnGroupClickListener()
	{
		
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id)
		{
			// TODO Auto-generated method stub
			ImageView imageView = (ImageView) v.findViewById(R.id.bbs_board_swith);
			String tag = (String) imageView.getTag();
			if (tag.equalsIgnoreCase("0"))
			{
				imageView.setImageResource(R.drawable.bbs_switch_down);
				imageView.setTag("1");
			}else {
				imageView.setImageResource(R.drawable.bbs_switch_right);
				imageView.setTag("0");
			}
			
			return false;
		}
	};
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		loadData();
	}
	
}
