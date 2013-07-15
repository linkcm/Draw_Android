/**  
        * @title BBSPostDetailActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-26 上午11:38:19  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.ImageUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.game.R;
import com.orange.game.application.DrawApplication;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.bbs.adapter.BBSPostActionListAdapter;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonImageLoaderActivity;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialog;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialogCompleteInterface;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.game.draw.model.db.DbManager;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-26 上午11:38:19  
 */

public class BBSPostDetailActivity extends CommonDrawActivity
{

	public static final String POST = "post";
	private PBBBSPost post;
	private ImageLoader imageLoader;
	private PullToRefreshListView listView;
	private BBSPostActionListAdapter adapter;
	private int currentActionType = BBSStatus.ActionTypeComment;
	private ImageView selectedLine;
	private RadioGroup radioGroup;
	private TextView postTop;
	private ImageButton postTopAction;
	private PopupWindow transferPopupWindow;
	
	
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

	public static void newInstance(Context context,PBBBSPost post){
		if (post == null)
		   return;
		Intent intent = new Intent();
		intent.setClass(context, BBSPostDetailActivity.class);
		intent.putExtra(POST, post.toByteArray());
		context.startActivity(intent);
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_post_detail);
		try
		{
			post = PBBBSPost.parseFrom(getIntent().getByteArrayExtra(POST));
		} catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		imageLoader = ImageLoader.getInstance();
		listView = (PullToRefreshListView) findViewById(R.id.bbs_post_detail_comment_lv);
		listView.setMode(Mode.PULL_FROM_END);
		
	
		
		
		adapter = new BBSPostActionListAdapter(BBSPostDetailActivity.this,null,post);
		listView.getRefreshableView().setAdapter(adapter);
		listView.setOnRefreshListener(onRefreshListener);
		
		
		
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		ImageButton refreshButton = (ImageButton) findViewById(R.id.bbs_refresh_button);
		refreshButton.setOnClickListener(refreshOnClickListener);
		
		
		
		
		
		initHeaderView();
		initPostActionGroup();
	}
	
	
	
	
	private void initHeaderView(){
		View headerView = LayoutInflater.
					from(BBSPostDetailActivity.this).
					inflate(R.layout.bbs_post_detail_list_header, null);
		final ImageView userAvatar = (ImageView) headerView.findViewById(R.id.user_avatar);
		TextView userNickName = (TextView) headerView.findViewById(R.id.user_nick_name);
		TextView postCreateTime = (TextView) headerView.findViewById(R.id.bbs_post_create_time);
		TextView postContent = (TextView) headerView.findViewById(R.id.bbs_post_content);
		ImageView postImage = (ImageView) headerView.findViewById(R.id.bbs_post_image);
		postTop = (TextView) headerView.findViewById(R.id.bbs_post_top);
		ImageView postRewardImage = (ImageView) headerView.findViewById(R.id.bbs_post_reward_image);
		TextView postReward = (TextView) headerView.findViewById(R.id.bbs_post_reward);

		
		selectedLine = (ImageView) headerView.findViewById(R.id.bbs_post_detail_selected_line);
		
		
		radioGroup = (RadioGroup) headerView.findViewById(R.id.bbs_post_detail_radio_group);
		radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
		
		
		imageLoader.loadImage(post.getCreateUser().getAvatar(), new ImageLoadingListener()
		{
			
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason)
			{
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				if (loadedImage != null)
				{
					userAvatar.setImageBitmap(ImageUtil.getRoundBitmap(loadedImage));
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{
			}
		});
		
		
		
		userNickName.setText(post.getCreateUser().getNickName());
		postCreateTime.setText(DateUtil.dateFormatToString(post.getCreateDate(),BBSPostDetailActivity.this));
		postContent.setText(post.getContent().getText());
		if (post.getContent().hasImageUrl())
		{
			imageLoader.displayImage(post.getContent().getThumbImageUrl(), postImage);
			postImage.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CommonImageLoaderActivity.newInstance(
									BBSPostDetailActivity.this,
									post.getContent().getImageUrl() );
					
				}
			});
		}else if (post.getContent().hasDrawImageUrl()) {
			imageLoader.displayImage(post.getContent().getDrawThumbUrl(), postImage);
			postImage.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CommonImageLoaderActivity.newInstance(
									BBSPostDetailActivity.this,
									post.getContent().getDrawImageUrl() );
					
				}
			});
		}else {
			postImage.setVisibility(View.GONE);
		}
		
		
		
		if (post.hasReward())
		{
			postReward.setText(""+post.getReward().getBonus());
			postRewardImage.setVisibility(View.VISIBLE);
			Log.d(TAG, "bbs reward status = "+post.getReward().getStatus());
			if (post.getReward().hasWinner())
			{
				postRewardImage.setImageResource(R.drawable.bbs_post_rewarded);				
			}else
			{
				postRewardImage.setImageResource(R.drawable.bbs_post_reward);
			}
			
		}else{
			postRewardImage.setVisibility(View.GONE);
			postReward.setText("");
		}
		
		
		listView.getRefreshableView().addHeaderView(headerView);
	}

	
	
	

	private void showTransferPopupqWindow(View parent){
		if (transferPopupWindow == null)
		{
			View transferMenuView = getLayoutInflater().inflate(R.layout.bbs_option_menu, null);
			ViewGroup menuGroup = (ViewGroup) transferMenuView.findViewById(R.id.bbs_option_menu_group);
			Button button = null;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
										getResources().getDimensionPixelSize(R.dimen.space_120),
										getResources().getDimensionPixelSize(R.dimen.space_38));
			layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.space_4);
			layoutParams.bottomMargin =getResources().getDimensionPixelSize(R.dimen.space_4);
			for (final PBBBSBoard board:BBSMission.getInstance().getBoardManager().getBoardList())
			{
				button = new Button(BBSPostDetailActivity.this);
				button.setLayoutParams(layoutParams);
				button.setBackgroundResource(R.drawable.bbs_option_bg);
				button.setText(board.getName());
				button.setTextColor(getResources().getColor(android.R.color.white));
				button.setGravity(Gravity.CENTER);
				button.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						BBSMission.getInstance().editBBSPost(
											board.getBoardId(), 
											post.getPostId(), 
											post.getStatus(), 
											new MissionCompleteInterface()
											{
												
												@Override
												public void onComplete(int errorCode)
												{
													if (errorCode == ErrorCode.ERROR_SUCCESS)
													{
														Toast.makeText(
																BBSPostDetailActivity.this, 
																R.string.transfer_post_success, 
																Toast.LENGTH_SHORT).show();
													}else {
														Toast.makeText(
																BBSPostDetailActivity.this, 
																R.string.transfer_post_fail, 
																Toast.LENGTH_SHORT).show();
													}
												}
											});
						finish();
						
					}
				});
				menuGroup.addView(button);
			}
			Drawable backgroundDrawable = getResources().getDrawable(R.drawable.bbs_action_sheet_bg);
			transferPopupWindow = PopupWindowUtil.getPopupWindow(
					transferMenuView, 
					backgroundDrawable, 
					BBSPostDetailActivity.this, 
					LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
		}
		
		
		if (transferPopupWindow!=null&&!transferPopupWindow.isShowing())
		{
			transferPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		}
		
		
	}
	
	
	
	
	
	private void initPostActionGroup(){
		ImageButton postCommentAction = (ImageButton) findViewById(R.id.bbs_post_comment_action_button);
		ImageButton postSupportAction = (ImageButton) findViewById(R.id.bbs_post_support_action_button);
		ImageButton postDeleteAction = (ImageButton) findViewById(R.id.bbs_post_delete_action_button);
		ImageButton postTransferAction = (ImageButton) findViewById(R.id.bbs_post_transfer_action_button);
		postTopAction = (ImageButton) findViewById(R.id.bbs_post_top_action_button);
		
		if (DrawApplication
				.getInstance()
				.getUserPermissionManager()
				.canWriteOnBBBoard(post.getBoardId()))
		{
			postSupportAction.setVisibility(View.VISIBLE);
			postCommentAction.setVisibility(View.VISIBLE);
			postSupportAction.setOnClickListener(postSupportOnClickListener);
			postCommentAction.setOnClickListener(commentOnClickListener);	
		}
		
		
		if (DrawApplication
				.getInstance()
				.getUserPermissionManager()
				.canDeletePost(post.getBoardId()))
		{
			postDeleteAction.setVisibility(View.VISIBLE);
			postDeleteAction.setOnClickListener(postDeleteOnClickListener);
		}
		
		if (DrawApplication
				.getInstance()
				.getUserPermissionManager()
				.canTransferPost(post.getBoardId()))
		{
			postTransferAction.setVisibility(View.VISIBLE);
			postTransferAction.setOnClickListener(postTransferOnClickListener);
		}
		
		if (DrawApplication
				.getInstance()
				.getUserPermissionManager()
				.canTopPost(post.getBoardId()))
		{
			postTopAction.setVisibility(View.VISIBLE);
			if (post.getStatus()==BBSStatus.StatusTop)
			{
				initPostUnTop();
			}else {
				initPostTop();
			}
		}
		
		
		
		
		
	}
	
	
	
	private void initPostUnTop(){
		postTopAction.setImageResource(R.drawable.bbs_post_detail_untop);
		postTopAction.setOnClickListener(postUnTopOnClickListener);
		postTop.setVisibility(View.VISIBLE);
	}
	
	private void initPostTop(){
		postTopAction.setImageResource(R.drawable.bbs_post_detail_totop);
		postTopAction.setOnClickListener(postToTopOnClickListener);
		postTop.setVisibility(View.GONE);
	}
	
	private OnClickListener postTransferOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			showTransferPopupqWindow(v);
			
		}
	};
	
	
	
	private OnClickListener postDeleteOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			CommonAlertDialog.makeAlertDialog(
					BBSPostDetailActivity.this,
					"", 
					getString(R.string.delete_post_alert_title),
					new CommonAlertDialogCompleteInterface()
					{

						@Override
						public void complete(String[] data)
						{
							BBSMission.getInstance().deletePost(
									post.getPostId(), 
									post.getBoardId(),
									new MissionCompleteInterface()
									{

										@Override
										public void onComplete(int errorCode)
										{
											if (errorCode == ErrorCode.ERROR_SUCCESS)
											{
												Toast.makeText(
														BBSPostDetailActivity.this,
														R.string.delete_post_success,
														Toast.LENGTH_SHORT)
														.show();
											}else{
												Toast.makeText(
														BBSPostDetailActivity.this,
														R.string.delete_post_fail,
														Toast.LENGTH_SHORT)
														.show();
											}

										}
									});
							finish();
						}
					});

		}
	};
	
	
	private OnClickListener postToTopOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			BBSMission.getInstance().editBBSPost(
									post.getBoardId(), 
									post.getPostId(), 
									BBSStatus.StatusTop, 
									new MissionCompleteInterface()
									{
										
										@Override
										public void onComplete(int errorCode)
										{
											if (errorCode == ErrorCode.ERROR_SUCCESS)
											{
												Toast.makeText(
														BBSPostDetailActivity.this, 
														R.string.post_to_top_success, 
														Toast.LENGTH_SHORT).show();
												initPostUnTop();
											}else{
												Toast.makeText(
														BBSPostDetailActivity.this, 
														R.string.post_to_top_fail, 
														Toast.LENGTH_SHORT).show();
											}
											
										}
									});
			
		}
	};
	
	
	private OnClickListener postUnTopOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			BBSMission.getInstance().editBBSPost(
					post.getBoardId(), 
					post.getPostId(), 
					BBSStatus.StatusNormal, 
					new MissionCompleteInterface()
					{
						
						@Override
						public void onComplete(int errorCode)
						{
							if (errorCode == ErrorCode.ERROR_SUCCESS)
							{
								Toast.makeText(
										BBSPostDetailActivity.this, 
										R.string.post_un_top_success, 
										Toast.LENGTH_SHORT).show();
								initPostTop();
							}else {
								Toast.makeText(
										BBSPostDetailActivity.this, 
										R.string.post_un_top_fail, 
										Toast.LENGTH_SHORT).show();
							}
							
						}
					});
		}
	};
	
	
	private OnClickListener postSupportOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			
			boolean isSupported = DbManager.getInstance().checkPostIsSupported(post.getPostId());
			if (isSupported)
			{
				Toast.makeText(
							BBSPostDetailActivity.this, 
							R.string.bbs_support_too_many, 
							Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			String briefText ;
			if (post.getContent().getText().length()>15)
			
				briefText = post.getContent().getText().substring(0, 14);
			else
				briefText = post.getContent().getText();
			BBSMission.getInstance()
					  .createBBSPostSupport(
					post.getPostId(), 
					post.getCreateUser().getUserId(),
					briefText,
					new MissionCompleteInterface()
					{
						
						@Override
						public void onComplete(int errorCode)
						{
							currentActionType = BBSStatus.ActionTypeSupport;
							if (errorCode == ErrorCode.ERROR_SUCCESS)
							{
								DbManager.getInstance().saveUserSupportPost(post.getPostId());														
								loadData();
							}
							radioGroup.check(R.id.bbs_post_detail_support);
						}
					});
		}
	};
	
	
	
	private OnClickListener commentOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			BBSSendCommentActivity.newInstance(BBSPostDetailActivity.this,post,null);
			radioGroup.check(R.id.bbs_post_detail_comment);
		}
	};
	
	
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh(PullToRefreshBase refreshView)
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
	
	
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			
				Animation animation = null;
				switch (checkedId)
				{
				case R.id.bbs_post_detail_comment:
					animation = AnimationUtils.loadAnimation(
										BBSPostDetailActivity.this, 
										R.anim.bbs_detail_selected_line_move_left);
					currentActionType = BBSStatus.ActionTypeComment;
					refreshPostComment();
					break;
				case R.id.bbs_post_detail_support:
					animation = AnimationUtils.loadAnimation(
										BBSPostDetailActivity.this, 
										R.anim.bbs_detail_selected_line_move_right);
					currentActionType = BBSStatus.ActionTypeSupport;
					refreshPostSupport();
					break;
				default:
					break;
				}
				
				selectedLine.startAnimation(animation);
			
			
		}
	};
	
	
	private void refreshPostComment(){
		if(BBSMission
				.getInstance()
				.getPostCommentManager()
				.getActionList()
				.size() == 0){
				loadData();
			}
		else {
			refreshData();
		}
	}
	
	
	private void refreshPostSupport(){
		if(BBSMission
				.getInstance()
				.getPostSupportManager()
				.getActionList()
				.size() == 0){
				loadData();
			}
		else {
			refreshData();
		}
	}
	
	
	private void refreshData(){
		if (currentActionType == BBSStatus.ActionTypeComment)
		{			
			adapter.setActionList(
					BBSMission.getInstance()
					.getPostCommentManager()
					.getActionList());
		}else {		
			adapter.setActionList(
					BBSMission.getInstance()
					.getPostSupportManager()
					.getActionList());
		}
		adapter.notifyDataSetChanged();
	}
	
	private void loadData(){
		if (isProgressDialogShowing())
			return;
		refreshData();
		listView.getRefreshableView().scrollTo(0, 0);
		showProgressDialog(R.string.loading);
		int offset = 0;
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		BBSMission.getInstance().getPostAction(
									"", 
									post.getPostId(), 
									currentActionType, 
									offset, 
									limit, 
									new MissionCompleteInterface()
									{
										
										@Override
										public void onComplete(int errorCode)
										{
											hideDialog();
											if (errorCode == ErrorCode.ERROR_SUCCESS)
											{					
												adapter.notifyDataSetChanged();
											}
											
										}
									});
	}

	
	public void loadMore(){
		if(isProgressDialogShowing())
			return;
		showProgressDialog(R.string.loading);
		int offset = 0;
		int limit = ConfigManager.getInstance().getFeedListDisplayCount();
		if (currentActionType == BBSStatus.ActionTypeComment)
		{
			adapter.setActionList(
					BBSMission.getInstance()
					.getPostCommentManager()
					.getActionList());
			offset = BBSMission.getInstance()
					.getPostCommentManager()
					.getActionList().size();
		}else {
			adapter.setActionList(
					BBSMission.getInstance()
					.getPostSupportManager()
					.getActionList());
			offset = BBSMission.getInstance()
					.getPostSupportManager()
					.getActionList().size();
		}
		BBSMission.getInstance().getPostAction(
									"", 
									post.getPostId(), 
									currentActionType, 
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
	
	
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		loadData();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		BBSMission.getInstance().getPostCommentManager().actionClear();
		BBSMission.getInstance().getPostSupportManager().actionClear();
	}
	
	
	
	
}
