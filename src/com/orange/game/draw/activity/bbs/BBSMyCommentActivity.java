/**  
        * @title BBSMyCommentActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-4 上午11:45:30  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.bbs.adapter.BBSMyCommentAdapter;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.bbs.BBSManager;
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSAction;
import com.tencent.weibo.api.PrivateAPI;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-4 上午11:45:30  
 */

public class BBSMyCommentActivity extends CommonDrawActivity
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

	
	private PullToRefreshListView commentListView;
	private BBSMyCommentAdapter adapter;
	private BBSManager bbsManager;
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_my_commment);
		bbsManager = new BBSManager();
		commentListView = (PullToRefreshListView) findViewById(R.id.bbs_my_comment_lv);
		commentListView.setMode(Mode.BOTH);
		adapter = new BBSMyCommentAdapter(BBSMyCommentActivity.this, bbsManager.getActionList());
		commentListView.getRefreshableView().setAdapter(adapter);
		commentListView.setOnRefreshListener(onRefreshListener2);
		ImageButton refreshButton = (ImageButton) findViewById(R.id.bbs_refresh_button);
		refreshButton.setOnClickListener(refreshOnClickListener);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		commentListView.getRefreshableView().setOnItemClickListener(onItemClickListener);
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
	
	
	private OnClickListener refreshOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			loadData();
			
		}
	};
	
	
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			showMenu(arg1,bbsManager.getActionList().get(arg2-1));
			
		}
	};
	
	private PopupWindow menuPopupWindow ;
	private void showMenu(View parentView,final PBBBSAction action){
		
		View menuView = getLayoutInflater().inflate(R.layout.bbs_my_comment_menu_popup_window, null);
		
		Button replyCommentButton = (Button) menuView.findViewById(R.id.bbs_reply_comment_button);
		Button postDetailButton = (Button) menuView.findViewById(R.id.bbs_post_detail_button);
		Button cancelButton = (Button) menuView.findViewById(R.id.cancel_button);
		
		replyCommentButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				showProgressDialog(R.string.loading);
				BBSMission.getInstance().getBBSPostById(action.getSource().getPostId(), new MissionCompleteInterface()
				{
					
					@Override
					public void onComplete(int errorCode)
					{
						hideDialog();
						if(errorCode == ErrorCode.ERROR_SUCCESS){
							BBSSendCommentActivity.newInstance(
									BBSMyCommentActivity.this, 
									BBSMission.getInstance().getTempPost(), 
									action);
						}else {
							Toast.makeText(
									BBSMyCommentActivity.this, 
									R.string.http_load_data_fail, 
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				
			}
		});
		
		postDetailButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				showProgressDialog(R.string.loading);
				BBSMission.getInstance().getBBSPostById(
									action.getSource().getPostId(), 
									new MissionCompleteInterface()
				{
					
					@Override
					public void onComplete(int errorCode)
					{
						hideDialog();
						if (errorCode == ErrorCode.ERROR_SUCCESS)
						{
							BBSPostDetailActivity.newInstance(
									BBSMyCommentActivity.this, 
									BBSMission.getInstance().getTempPost());
						}else {
							Toast.makeText(
									BBSMyCommentActivity.this, 
									R.string.http_load_data_fail, 
									Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (menuPopupWindow != null&&menuPopupWindow.isShowing())
				{
					menuPopupWindow.dismiss();
				}
				
			}
		});
		
		Drawable background = getResources().getDrawable(R.drawable.bbs_action_sheet_bg);
		menuPopupWindow = PopupWindowUtil.getPopupWindow(
																menuView, 
																background, 
																BBSMyCommentActivity.this, 
																LayoutParams.WRAP_CONTENT, 
																LayoutParams.WRAP_CONTENT);
		menuPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);	
		
	}
	
	private void loadData(){
		showProgressDialog(R.string.loading);
		int offset = 0;
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		bbsManager.actionClear();
		BBSMission.getInstance().getPostAction(
								 UserManager.getInstance().getTestUserId(), 
								 "", 
								 bbsManager,
								 BBSStatus.ActionTypeComment, 
								 offset, 
								 limit, 
								 new MissionCompleteInterface()
								{
									
									@Override
									public void onComplete(int errorCode)
									{
										commentListView.onRefreshComplete();
										hideDialog();
										adapter.notifyDataSetChanged();										
									}
								});
	}
	
	
	private void loadMore(){
		showProgressDialog(R.string.loading);
		int offset = bbsManager.getActionList().size();
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		BBSMission.getInstance().getPostAction(
								 UserManager.getInstance().getTestUserId(), 
								 "", 
								 bbsManager,
								 BBSStatus.ActionTypeComment, 
								 offset, 
								 limit, 
								 new MissionCompleteInterface()
								{
									
									@Override
									public void onComplete(int errorCode)
									{
										hideDialog();
										commentListView.onRefreshComplete();
										adapter.notifyDataSetChanged();										
									}
								});
	}
	
	
	
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		if(menuPopupWindow!= null&&menuPopupWindow.isShowing()){
			menuPopupWindow.dismiss();
		}
	}

	
	
}
