
package com.orange.game.draw.activity.drawDetail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.orange.common.android.sns.common.SNSConstants;
import com.orange.common.android.sns.qqweibo.QQWeiboHandler;
import com.orange.common.android.sns.sina.SinaSNSRequest;
import com.orange.common.android.sns.sina.SinaSendWeiboRequestHandler;
import com.orange.common.android.sns.sina.SinaWeiboHandler;
import com.orange.common.android.sns.wechat.WeChatApi;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.FileUtil;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.common.android.utils.ToastUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.SNSPopupView;
import com.orange.game.draw.activity.drawDetail.adapter.DrawDetailAdapter;
import com.orange.game.draw.activity.drawDetail.adapter.SNSSharePopupAdapter;
import com.orange.game.draw.activity.drawDetail.adapter.detail.CommentDetailDisplay;
import com.orange.game.draw.activity.drawDetail.adapter.detail.DetailDisplayStrategyInterface;
import com.orange.game.draw.activity.drawDetail.adapter.detail.FlowerDetailDisplay;
import com.orange.game.draw.activity.drawDetail.adapter.detail.GuessDetailDisplay;
import com.orange.game.draw.activity.drawDetail.adapter.detail.TomatoDetailDisplay;
import com.orange.game.draw.activity.share.ShareItemData;
import com.orange.game.draw.activity.share.ShareToWeiboActivity;
import com.orange.game.draw.activity.timeline.TimelineActivity;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteGetFeedDetailInterface;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedActionMission;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.db.DbManager;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.feed.FeedConstants;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.feed.FeedUtil;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.umeng.analytics.i;



public class DrawDetailActivity extends CommonDrawActivity
{
	public static final String DRAW_DETAIL_FEED_ID = "draw_detail";
	private PBFeed feed;
	private ImageLoader imageLoader;
	public CommentDetailDisplay commentDisplay = new CommentDetailDisplay();
	public GuessDetailDisplay guessDisplay = new GuessDetailDisplay();
	public FlowerDetailDisplay flowerDisplay = new FlowerDetailDisplay();
	
	
	private String feedId;	
	
	private SNSPopupView snsPopupView;
	private PopupWindow sharePopupWindow;

	private View headerView;
	private PullToRefreshListView listView;
	private ViewGroup animGroup;
	private int screenWidth = 0;
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	
	
	private DrawDetailAdapter adapter;
	private ImageView drawDetailPicture;
	private RadioGroup actionTabGroup;
	private int commentCount = 0,guessCount =0 ,flowerCount = 0;
	private RadioButton drawComment, drawGuess, drawFlower;
	
	
	@Override
	protected boolean isRegisterServerConnect()
	{
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage()
	{
		return false;
	}

	@Override
	protected void handleServiceConnected()
	{
	}

	
	
	public static void newInstance(Context context,String feedId)
	{
		Log.d(TAG, "<newInstance> feedId = "+feedId);
		Intent intent = new Intent();
		intent.setClass(context, DrawDetailActivity.class);
		intent.putExtra(DRAW_DETAIL_FEED_ID, feedId);
		context.startActivity(intent);
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_detail);
		imageLoader = ImageLoader.getInstance();
		
		feedId = getIntent().getStringExtra(DRAW_DETAIL_FEED_ID);
		Log.d(TAG, "<onCreate> feedId = "+feedId);
		adapter = new DrawDetailAdapter(null,commentDisplay,DrawDetailActivity.this);
		
		
		listView = (PullToRefreshListView) findViewById(R.id.draw_detail_list_view);
		headerView = getLayoutInflater().inflate(R.layout.draw_detail_header, null);
	    screenWidth = getWindowManager().getDefaultDisplay().getWidth();
	    ImageView drawDetailPicture = (ImageView) headerView.findViewById(R.id.draw_detail_picture);
	    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(screenWidth*0.8),(int)( screenWidth*0.8));
	    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);	
	    layoutParams.setMargins(0, 20, 0, 20);
	    drawDetailPicture.setScaleType(ScaleType.FIT_XY);
	    drawDetailPicture.setLayoutParams(layoutParams);
	    
		listView.setMode(Mode.PULL_FROM_END);
		listView.getRefreshableView().addHeaderView(headerView);
		listView.getRefreshableView().setAdapter(adapter);
		listView.setOnRefreshListener(onRefreshListener2);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		backButton.setOnClickListener(backOnClickListener);
		refreshButton.setOnClickListener(refreshOnClickListener);
		ImageButton flowerButton = (ImageButton) findViewById(R.id.draw_detail_flower);
		ImageButton shareButton = (ImageButton) findViewById(R.id.draw_detail_share);
		ImageButton commentButton = (ImageButton) findViewById(R.id.draw_detail_comment);
		animGroup = (ViewGroup) findViewById(R.id.anim_group);
		flowerButton.setOnClickListener(flowerOnClickListener);
		shareButton.setOnClickListener(shareOnClickListener);
		commentButton.setOnClickListener(commentOnClickListener);
		listView.setOnScrollListener(onScrollListener);
		
	}

	
	private OnScrollListener onScrollListener = new OnScrollListener()
	{
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
			{
				listView.setMode(Mode.PULL_FROM_END);			
			}else if (scrollState == SCROLL_STATE_IDLE) {
				listView.setMode(Mode.PULL_FROM_END);
			}else if (scrollState == SCROLL_STATE_FLING)
			{
				listView.setMode(Mode.DISABLED);
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount)
		{			
		}
	};
	
	
	
	private OnClickListener flowerOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if (feed != null)
			{
				int flowerCount = DbManager.getInstance().getUserGiveFlowerCount(feed.getFeedId());
				if (flowerCount<10)
				{
					flowerCount=flowerCount+1;
					DbManager.getInstance().saveUserGiveFlowerHistory(feed.getFeedId(), flowerCount);
					ImageView imageView = new ImageView(DrawDetailActivity.this);
					imageView.setImageResource(R.drawable.flower);
					imageView.setTag(FeedActionType.Flower);
					initActionImage(imageView, v);			
				}else {
					feedActionToast(getString(R.string.too_many_action));
				}
				
			}		
			actionTabGroup.check(R.id.draw_detail_tab_flower);
		}
	};
	

	
	private void initActionImage(ImageView imageView,View button){
		LayoutParams layoutParams = new LayoutParams(60, 60);
		imageView.setLayoutParams(layoutParams);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setClickable(false);
		imageView.setFocusable(false);
		int[] fromLocation = new int[2]; 
		button.getLocationOnScreen(fromLocation);
		initAnim(imageView, fromLocation[0]+button.getWidth()/2,(int)(screenWidth*0.5),fromLocation[1]-button.getHeight()/2,(int)(screenWidth*0.6));
		
		animGroup.addView(imageView);
		imageViewList.add(imageView);
	}
	
	
	
	private void initAnim(ImageView imageView,int fromX,int toX,int fromY,int toY){
		Animation animation_scale,animation_translate,animation_rotate,animation_alpha;  
		AnimationSet animationSet;    
        animation_alpha=new AlphaAnimation(1.0f,0f);  
        animation_alpha.setRepeatCount(0);
        animation_alpha.setDuration(100);
        animation_alpha.setStartOffset(2600);
        // rotate  
        animation_rotate = new RotateAnimation(0, 359,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation_rotate.setRepeatCount(30);  
        animation_rotate.setDuration(50);
          
        //scale  
        animation_scale=new ScaleAnimation(1.0f,5.0f,1.0f,5.0f,
							        		Animation.RELATIVE_TO_SELF,0.5f,
							        		Animation.RELATIVE_TO_SELF,0.5f);  
        animation_scale.setStartOffset(2600);
        animation_scale.setRepeatCount(0);  
        animation_scale.setDuration(300);
          
        //translate  
        animation_translate=new TranslateAnimation(fromX,toX,fromY,toY);  
        animation_translate.setRepeatCount(0);
        animation_translate.setDuration(3000); 
          
        animationSet=new AnimationSet(true);  
          
        animationSet.addAnimation(animation_alpha);  
        animationSet.addAnimation(animation_rotate);  
        animationSet.addAnimation(animation_scale);  
        animationSet.addAnimation(animation_translate);  
        imageView.startAnimation(animationSet);  
        animationSet.setAnimationListener(animationListener);
	}
	
	private AnimationListener animationListener = new AnimationListener()
	{
		
		@Override
		public void onAnimationStart(Animation animation)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation)
		{
			for (ImageView imageView:imageViewList)
			{
				if (imageView.getVisibility() == View.VISIBLE)
				{
					imageView.setVisibility(View.GONE);
					final FeedActionType actionType = (FeedActionType) imageView.getTag();
					MissionCompleteInterface completeInterface = new MissionCompleteInterface()
					{
						@Override
						public void onComplete(int errorCode)
						{
							String toastContent = "";
							if (errorCode == ErrorCode.ERROR_SUCCESS)
							{
								toastContent = getString(R.string.give_flower_toast);
								flowerCount++;							
								refreshRadioGroup();
							} else
							{
								DbManager.getInstance().removeUserFlowerHistory(feed.getFeedId()); 
								toastContent = "action  error";
							}
							refreshFeedActionList(actionType);
							feedActionToast(toastContent);						
						}
					};		
					feedAction(actionType, feed, completeInterface);
				}
			}
			
		}
	};
	
	private void refreshFeedActionList(FeedActionType actionType){
		if (actionType == FeedActionType.Flower)
		{
			if(actionTabGroup.getCheckedRadioButtonId() == R.id.draw_detail_tab_flower)
				initFeedData(flowerDisplay,flowerDisplay.getFeedManager());
			else	
				actionTabGroup.check(R.id.draw_detail_tab_flower);
		}
	}
	
	
	private void initUserInfo(){
		ImageView userAvatar = (ImageView) headerView.findViewById(R.id.user_avatar);
		TextView userNickName = (TextView) headerView.findViewById(R.id.user_nick_name);
		imageLoader.displayImage(feed.getAvatar(), userAvatar);
		userNickName.setText(feed.getNickName());
		userAvatar.setOnClickListener(userAvatarOnClickListener);
	}
	
	
	
	
	
	
	private void initDrawPicture(){
		drawDetailPicture = (ImageView) headerView.findViewById(R.id.draw_detail_picture);
		TextView drawTime = (TextView) headerView.findViewById(R.id.draw_detail_commit_time);
		TextView drawTarget = (TextView) headerView.findViewById(R.id.draw_detail_target_user);
		TextView drawDesc = (TextView) headerView.findViewById(R.id.draw_detail_description);
		
		imageLoader.displayImage(feed.getOpusImage(), drawDetailPicture);
		drawTime.setText(DateUtil.dateFormatToString(feed.getCreateDate(),DrawDetailActivity.this));
		drawDesc.setText(feed.getOpusDesc());
		if (feed.getTargetUserId()!=null&&!feed.getTargetUserId().equalsIgnoreCase(""))
		{
			drawTarget.setText(getString(R.string.draw_to)+feed.getTargetUserNickName());
		}
	}
	
	
	private void initDrawDetailRadioGroup(){
		actionTabGroup = (RadioGroup) headerView.findViewById(R.id.draw_detail_tab_group);
		drawComment = (RadioButton) headerView.findViewById(R.id.draw_detail_tab_comment);
		drawGuess = (RadioButton) headerView.findViewById(R.id.draw_detail_tab_guess);
		drawFlower = (RadioButton) headerView.findViewById(R.id.draw_detail_tab_flower);
		
		
		commentCount = FeedUtil.getFeedTimesByType(feed.getFeedTimesList(), FeedConstants.FeedTimesTypeComment);
		guessCount = FeedUtil.getFeedTimesByType(feed.getFeedTimesList(), FeedConstants.FeedTimesTypeGuess);
		flowerCount = FeedUtil.getFeedTimesByType(feed.getFeedTimesList(), FeedConstants.FeedTimesTypeFlower);
		
		refreshRadioGroup();
		actionTabGroup.setOnCheckedChangeListener(onCheckedChangeListener);
	}
	
	private void refreshRadioGroup(){
		drawComment.setText(getString(R.string.comment)+"("+commentCount+")");
		drawGuess.setText(getString(R.string.guess)+"("+guessCount+")");
		drawFlower.setText(getString(R.string.flower)+"("+flowerCount+")");
	}
	
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			Log.d(TAG, "radio group checked id = "+checkedId);
			switch (checkedId)
			{
			case R.id.draw_detail_tab_comment:
				initFeedData(commentDisplay,commentDisplay.getFeedManager());
				break;
			case R.id.draw_detail_tab_guess:
				initFeedData(guessDisplay,guessDisplay.getFeedManager());
				break;
			case R.id.draw_detail_tab_flower:
				initFeedData(flowerDisplay,flowerDisplay.getFeedManager());
				break;
			default:
				break;
			}
		}
	};
	
	
	
	
	private OnClickListener userAvatarOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			showProgressDialog(R.string.loading);
			UserMission.getInstance().getUserByUserId(feed.getUserId(), new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					hideDialog();
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						UserDetailActivity.newInstance(
								DrawDetailActivity.this,
								UserManager.getInstance().getTempGameUser(),
								UserManager.getInstance().getTempUserRelationShip());
					}
					
				}
			});
			
		}
	};
	
	
	private void loadPbfeed(){
		showProgressDialog(R.string.loading);
		FeedMission.getInstance().getFeedById(feedId, new MissionCompleteGetFeedDetailInterface()
		{
			
			@Override
			public void comlete(PBFeed feed, int errorCode)
			{
				hideDialog();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					DrawDetailActivity.this.feed = feed;
					updateView();
				}else {
					DrawNetworkConstants.errorToast(DrawDetailActivity.this, errorCode);
				}
				listView.onRefreshComplete();
			}
		});
	}
	
	private void loadFeedList(final FeedManager feedManager)
	{
		if (feed != null)
		{
			int start = 0;
			int count = ConfigManager.getInstance().getActionFeedListDisplayCount();
			feedManager.clear();
			FeedMission.getInstance().getFeedActionList(
											feed.getUserId(), 
											feedManager, 
											feed.getFeedId(), 
											start, 
											count, 
											new MissionCompleteInterface()
			{			
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						adapter.setFeeds(feedManager.getFeedList());					
						adapter.notifyDataSetChanged();					
					}else {
						DrawNetworkConstants.errorToast(DrawDetailActivity.this, errorCode);
					}				
				}
			});
		}
		
	}
	
	private void loadMoreFeedList(final FeedManager feedManager)
	{
		if (feed != null)
		{
			int start = 0;
			int count = feedManager.getFeedListCount();
			FeedMission.getInstance().getFeedActionList(feed.getUserId(), feedManager, feed.getFeedId(), start, count, new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						adapter.setFeeds(feedManager.getFeedList());					
						adapter.notifyDataSetChanged();	
					}else {
						DrawNetworkConstants.errorToast(DrawDetailActivity.this, errorCode);
					}
					listView.onRefreshComplete();
				}
			});
		}
		
	}

	
	private OnRefreshListener2<ListView> onRefreshListener2 = new OnRefreshListener2<ListView>()
	{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			imageLoader.clearMemoryCache();
			loadPbfeed();	
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			loadMoreFeedList(adapter.getDisplay().getFeedManager());
		}
	};	
	
	private void initFeedData(DetailDisplayStrategyInterface display,FeedManager feedManager)
	{
		adapter.setDisplay(display);
		if (feedManager.getFeedListCount()==0)
		{
			loadFeedList(feedManager);
		}
	}

	
	
	private OnClickListener refreshOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			imageLoader.clearMemoryCache();
			loadPbfeed();		
		}
	};
	
	
	private OnClickListener shareOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			sharePopupWindow = snsPopupView.getSharePopupWindow(v);	
		}
	};
	
	private OnClickListener commentOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if (feed != null)
			{
				Intent intent = new Intent();
				intent.putExtra(ReplyCommentActivity.PB_FEED, setPBfeedData(feed));
				intent.setClass(DrawDetailActivity.this, ReplyCommentActivity.class);
				startActivityForResult(intent,ReplyCommentActivity.REQUEST_CODE);
			}
			
		}
	};
	
	
	private byte[] setPBfeedData(PBFeed feed){
		PBFeed.Builder builder = PBFeed.newBuilder();
		builder.mergeFrom(feed);
		builder.clearDrawData();
		return builder.build().toByteArray();
	}
	
	
	private void feedAction(FeedActionType actionType,PBFeed feed,MissionCompleteInterface completeInterface){
		FeedActionMission.getInstance().actionOnFeed(feed.getFeedId(), feed.getUserId(), actionType, completeInterface );
	}
	
	private void feedActionToast(String toastContent){
		View view = getLayoutInflater().inflate(R.layout.draw_detail_toast, null);
		((TextView)view.findViewById(R.id.draw_detial_toast_content)).setText(toastContent);
		ToastUtil.makeViewToast(DrawDetailActivity.this,view);
	}
	
	
	private void updateView(){
		initUserInfo();
		initDrawPicture();
		initDrawDetailRadioGroup();
		initFeedData(adapter.getDisplay(),adapter.getDisplay().getFeedManager());
		snsPopupView = new SNSPopupView(DrawDetailActivity.this, feed.getOpusWord(), feed.getOpusImage());
		Log.d(TAG, "draw detail image url = "+feed.getOpusImage());
		//snsPopupView.saveImageCache();
	}
	
	
	private void closeSharePopupWindow(){
		if (sharePopupWindow != null)
		{
			sharePopupWindow.dismiss();
		}
	}
	
	

	@Override
	protected void onStart()
	{
		super.onStart();
		if (feed == null)
		{
			loadPbfeed();
		}
		closeSharePopupWindow();
		
	}

	
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		clear();
		finish();
	}
	

	
	
	private void clear()
	{
		closeSharePopupWindow();
		commentDisplay.getFeedManager().clear();
		flowerDisplay.getFeedManager().clear();
		guessDisplay.getFeedManager().clear();
		if (feed!=null)
		{			
			imageLoader.clearMemoryCache();
		}
		commentDisplay = null;
		flowerDisplay = null;
		guessDisplay = null;
		feed = null;
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ReplyCommentActivity.REQUEST_CODE && data !=null)
		{		
			actionTabGroup.check(R.id.draw_detail_tab_comment);
			if (data.getBooleanExtra(ReplyCommentActivity.RESULT_CODE, false))
			{
				commentCount++;
				refreshRadioGroup();
				initFeedData(commentDisplay, commentDisplay.getFeedManager());
			}		
		}
		
		
	}
	
}
