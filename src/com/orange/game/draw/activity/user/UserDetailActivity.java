/**  
        * @title UserDetail.java  
        * @package com.orange.game.draw.activity.user  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-8 上午10:11:19  
        * @version V1.0  
 */
package com.orange.game.draw.activity.user;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.google.protobuf.InvalidProtocolBufferException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orange.common.android.utils.AlertDialogUtil;
import com.orange.common.android.utils.ImageUtil;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.common.api.service.error.CommonErrorCode;
import com.orange.game.R;
import com.orange.game.application.DrawApplication;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialog;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialogCompleteInterface;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.activity.message.DrawHomeMessageDialogActivity;
import com.orange.game.draw.mission.account.AccountMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.mission.friend.FriendMission;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.user.PBSNSUserUtils;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;
import com.tencent.weibo.constants.ErrorCodeConstants;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-8 上午10:11:19  
 */

public class UserDetailActivity extends CommonDrawActivity
{

	public static final String USER_DATA = "user_data";
	public static final String FRIEND_TYPE = "friend_type";
	
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

	public static void newInstance(Context context,PBGameUser gameUser,int friendType){
		Intent intent = new Intent();
		intent.setClass(context,UserDetailActivity.class);
		intent.putExtra(USER_DATA, gameUser.toByteArray());
		intent.putExtra(FRIEND_TYPE, friendType);
		context.startActivity(intent);
	}
	
	private int friendType = Friend.FRIEND_UNKNOW;
	private PBGameUser user;
	private boolean isLoginUser = false;
	private FeedManager userOpusfeedManager;
	private FeedManager userFavoriteFeedManager;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_user_detail);
		userOpusfeedManager = new FeedManager(FeedListType.FeedListTypeUserOpus);
		userFavoriteFeedManager = new FeedManager(FeedListType.FeedListTypeUserFavorite);
		try
		{
			user = PBGameUser.parseFrom(getIntent().getByteArrayExtra(USER_DATA));
		} catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		friendType = getIntent().getIntExtra(FRIEND_TYPE, Friend.FRIEND_UNKNOW);
		if (user.getUserId().equalsIgnoreCase(UserManager.getInstance().getTestUserId()))
		{
			isLoginUser = true;
		}else {
			isLoginUser = false;
		}
		imageLoader = ImageLoader.getInstance();
		init();
	
	}
	
	
	private ViewGroup btnGroup;
	private ViewGroup userBasicInfoGroup;
	private ViewGroup userOpusGroup;
	private ImageButton sinaWeiboButton;
	private ImageButton qqWeiBoButton;
	private ImageButton facebookButton;
	private ImageLoader imageLoader;
	private Button userEditButton;
	private void init(){
		btnGroup = (ViewGroup) findViewById(R.id.user_detail_btn_group);
		userBasicInfoGroup = (ViewGroup) findViewById(R.id.user_detail_basic_info_group);
		userOpusGroup = (ViewGroup) findViewById(R.id.user_detail_opus_group);
		ImageView pullDownButton = (ImageView) findViewById(R.id.user_detail_pull_dwon);
		pullDownButton.setOnClickListener(pullDownOnClickListener);
		Button backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		ImageView userGender = (ImageView) findViewById(R.id.user_gender);
		TextView userLevel = (TextView) findViewById(R.id.user_level);
		TextView userNickName = (TextView) findViewById(R.id.user_nick_name);
		
		userEditButton = (Button) findViewById(R.id.user_detail_edit);
		
		
		final ImageView userAvatar = (ImageView) findViewById(R.id.user_avatar);
		imageLoader.loadImage(user.getAvatar(), new ImageLoadingListener()
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
					userAvatar.setImageBitmap(							  
							   ImageUtil.getRoundBitmap(loadedImage));	
				}
							
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{
			}
		});
		
		Button yellowButton = (Button) findViewById(R.id.user_detail_yellow_button);
		Button redButton = (Button) findViewById(R.id.user_detail_red_button);
		Button purpleButton = (Button) findViewById(R.id.user_detail_purple_button);
		Button followButton = (Button) findViewById(R.id.user_detail_follow_button);
		Button fansButton = (Button) findViewById(R.id.user_detail_fans_button);
		
		
		sinaWeiboButton = (ImageButton) findViewById(R.id.user_detail_sina_weibo);
		qqWeiBoButton = (ImageButton) findViewById(R.id.user_detail_qq_weibo);
		facebookButton = (ImageButton) findViewById(R.id.user_detail_facebook);
		
		
		
		if (user.getGender())
			userGender.setImageResource(R.drawable.user_detail_gender_male);
		else
			userGender.setImageResource(R.drawable.user_detail_gender_female);
		userNickName.setText(user.getNickName());
		userLevel.setText("LV:"+user.getLevel());
		
		
		followButton.setText(getString(R.string.follow)+"\n"+user.getFollowCount());
		fansButton.setText(getString(R.string.fans)+"\n"+user.getFanCount());
		
		RadioGroup radioBtnGroup = (RadioGroup) findViewById(R.id.user_detail_radio_btn_group);
		TextView opusTextView = (TextView) findViewById(R.id.user_detail_opus_group_title);
				

		if (!isLoginUser)
		{
			switch (friendType)
			{
			case Friend.FRIEND_UNKNOW:
				yellowButton.setBackgroundResource(R.drawable.user_detail_follow_btn);
				yellowButton.setText(R.string.follow);
				yellowButton.setOnClickListener(followOnClickListener);
				break;
			case Friend.FRIEND_FAN:
				yellowButton.setBackgroundResource(R.drawable.user_detail_follow_btn);
				yellowButton.setText(R.string.follow);
				yellowButton.setOnClickListener(followOnClickListener);
				break;
			case Friend.FRIEND_FOLLOW:
				yellowButton.setBackgroundResource(R.drawable.user_detail_unfollow_btn);	
				yellowButton.setText(R.string.un_follow);
				break;
			default:
				yellowButton.setBackgroundResource(R.drawable.user_detail_follow_btn);
				yellowButton.setText(R.string.follow);
				yellowButton.setOnClickListener(followOnClickListener);
				break;
			}
			redButton.setBackgroundResource(R.drawable.user_detail_draw_btn);
			redButton.setText(R.string.draw_to);
			purpleButton.setBackgroundResource(R.drawable.user_detail_message_btn);
			purpleButton.setText(R.string.chat);
			purpleButton.setOnClickListener(chatOnClickListener);
			
			radioBtnGroup.setVisibility(View.GONE);
			opusTextView.setVisibility(View.VISIBLE);
			userEditButton.setVisibility(View.GONE);
			
		}else {
			radioBtnGroup.setOnCheckedChangeListener(onCheckedChangeListener);
			
		}
		
		
		if(!PBSNSUserUtils.isBindFacebook(user)){
			if (isLoginUser)
			{
				facebookButton.setSelected(true);
				facebookButton.setOnClickListener(facebookOnClickListener);
			}else {
				facebookButton.setVisibility(View.GONE);
			}
		}
		
		if (!PBSNSUserUtils.isBindQQ(user)){
			if (isLoginUser)
			{
				qqWeiBoButton.setSelected(true);
				qqWeiBoButton.setOnClickListener(qqOnClickListener);
			}else {
				qqWeiBoButton.setVisibility(View.GONE);
			}
		}
		if (!PBSNSUserUtils.isBindSina(user)){
			if (isLoginUser)
			{
				sinaWeiboButton.setSelected(true);
				sinaWeiboButton.setOnClickListener(sinaOnClickListener);
			}else {
				sinaWeiboButton.setVisibility(View.GONE);
			}			
		}
		
		
		
		
		
		
		initUserOpus();
		initUserBasicInfo();
		initSpecialFunction();
	}

	
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			switch (checkedId)
			{
			case R.id.user_detail_opus:
				initUserOpus();
				break;
			case R.id.user_detail_favorite:
				initUserFavorite();
				break;
			default:
				break;
			}
			
		}
	};
	
	
	
	
	
	private void initUserOpus(){
		if (userOpusfeedManager.getFeedList().size()==0)
		{
			FeedMission.getInstance().getUserOpus(
					userOpusfeedManager,
					0, 
					8, 
					user.getUserId(), 
					new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						initOpusViewGroup(userOpusfeedManager.getFeedList());
					}else {
						DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
					}	
				}
			});
		}else {
			initOpusViewGroup(userOpusfeedManager.getFeedList());
		}
		
		
		
	}
	
	
	
	private void initUserFavorite(){
		if (userFavoriteFeedManager.getFeedList().size()==0)
		{
			FeedMission.getInstance().getUserOpus(
										userFavoriteFeedManager,
										0, 
										8, 
										user.getUserId(), 
										new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						initOpusViewGroup(userFavoriteFeedManager.getFeedList());
					}else {
						DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
					}	
				}
			});
		}else {
			initOpusViewGroup(userFavoriteFeedManager.getFeedList());
		}
		
	}
	
	
	
	
	private void initOpusViewGroup(List<PBFeed> userOpusFeeds){
		userOpusGroup.removeAllViews();
		ImageView imageView = null;
		LayoutParams params = null;
		if (userOpusFeeds.size()<3)
		{
			FrameLayout.LayoutParams layoutParams =  new FrameLayout.LayoutParams(btnGroup.getWidth(), btnGroup.getHeight());
			layoutParams.gravity = Gravity.CENTER;
			userOpusGroup.setLayoutParams(layoutParams);
		}
		for (final PBFeed feed:userOpusFeeds)
		{
			imageView = new ImageView(UserDetailActivity.this);
			params = new LayoutParams((int)(btnGroup.getHeight()*0.8), (int)(btnGroup.getHeight()*0.8));
			imageView.setLayoutParams(params);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageLoader.displayImage(feed.getOpusImage(), imageView);
			userOpusGroup.addView(imageView);
			imageView.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					DrawDetailActivity.newInstance(UserDetailActivity.this, feed.getFeedId());
					
				}
			});
		}
	}
	
	
	private void initUserBasicInfo(){
		TextView birthdayTextView = (TextView) findViewById(R.id.user_detail_birthday);
		TextView constellactionTextView = (TextView) findViewById(R.id.user_detail_constellation);
		TextView bloodTypeTextView = (TextView) findViewById(R.id.user_detail_blood_type);
		TextView locationTextView = (TextView) findViewById(R.id.user_detail_location);
		
		String birthday = getString(R.string.birthday)+"-"+user.getBirthday();
		String constellcaction = getString(R.string.constellation);
		String bloodType = getString(R.string.blood_type);
		String location = getString(R.string.location);
		
		birthdayTextView.setText(birthday);
		constellactionTextView.setText(constellcaction);
		bloodTypeTextView.setText(bloodType);
		locationTextView.setText(location);
	}
	
	
	private void initSpecialFunction(){
		final Button blackUserButton = (Button) findViewById(R.id.black_user_list_btn);
		final Button deleteBlackButton = (Button)findViewById(R.id.delete_black_btn);
		Button userManagerButton = (Button) findViewById(R.id.user_manager_btn);
		userManagerButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				showManagerGroup(v);
			}
		});
		
		if (!DrawApplication.getInstance().getUserPermissionManager().isPermissionSite())
		{
			userManagerButton.setVisibility(View.GONE);
		}
		
		blackUserButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				CommonAlertDialog.makeAlertDialog(
						UserDetailActivity.this, 
						"", 
						getString(R.string.black_user_toast), 
						new CommonAlertDialogCompleteInterface()
				{
					
					@Override
					public void complete(String[] data)
					{
						AccountMission.getInstance().blackFriend(user.getUserId(), new MissionCompleteInterface()
						{
							
							@Override
							public void onComplete(int errorCode)
							{
								if (errorCode==ErrorCode.ERROR_SUCCESS)
								{
									Toast.makeText(UserDetailActivity.this, getString(R.string.add_black_friend_success), Toast.LENGTH_SHORT).show();
									deleteBlackButton.setVisibility(View.VISIBLE);
									blackUserButton.setVisibility(View.GONE);
								}
							}
						});
					}
				});
				
			}
		});
		
		deleteBlackButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				CommonAlertDialog.makeAlertDialog(
						UserDetailActivity.this, 
						"", 
						getString(R.string.delete_black_toast), 
						new CommonAlertDialogCompleteInterface()
				{
					
					@Override
					public void complete(String[] data)
					{
						AccountMission.getInstance().blackFriend(user.getUserId(), new MissionCompleteInterface()
						{
							
							@Override
							public void onComplete(int errorCode)
							{
								if (errorCode==ErrorCode.ERROR_SUCCESS)
								{
									Toast.makeText(UserDetailActivity.this, getString(R.string.delete_black_friend_success), Toast.LENGTH_SHORT).show();
									deleteBlackButton.setVisibility(View.GONE);
									blackUserButton.setVisibility(View.VISIBLE);
								}
							}
						});
					}
				});
			}
		});
		
	}
	
	private PopupWindow managerGroup = null;
	private void showManagerGroup(View parentView){
		if (managerGroup==null)
		{
			View view = getLayoutInflater().inflate(R.layout.user_manager_group, null);
			ColorDrawable backgroundDrawable = new ColorDrawable(getResources().getColor(R.color.half_transparent_black));
			managerGroup = PopupWindowUtil.getMatcParentPopupWindow(view, backgroundDrawable, UserDetailActivity.this);
						
			TextView userInfoTextView = (TextView) view.findViewById(R.id.user_info_tv);
			String userInfo = user.getNickName()
					+"("
					+getString(R.string.user_id)
					+user.getUserId()
					+","
					+getString(R.string.gold)
					+user.getCoinBalance()
					+","
					+getString(R.string.ingot)
					+user.getIngotBalance()
					+")";
			
			userInfoTextView.setText(userInfo);
			
			Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					managerGroup.dismiss();
					
				}
			});
			Button chargeGoldButton = (Button) view.findViewById(R.id.charge_gold_btn);
			Button chargeIngotButton = (Button) view.findViewById(R.id.charge_ingot_btn);
			Button blackUserButton = (Button) view.findViewById(R.id.add_to_black_user_btn);
			Button blackDeviceButton = (Button) view.findViewById(R.id.add_to_black_device_btn);
			Button unBlackUserButton = (Button) view.findViewById(R.id.delete_from_black_user_btn);
			Button unBlackDeviceButton = (Button) view.findViewById(R.id.delete_from_black_device_btn);
			chargeGoldButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					managerGroup.dismiss();
				   CommonAlertDialog.makeInputAlertDialog(
						   					UserDetailActivity.this, 
						   					R.string.charge_gold_hint, 
						   					null, 
						   					new String[]{getString(R.string.charge_gold_hint)}, 
						   					new CommonAlertDialogCompleteInterface()
											{
												
												@Override
												public void complete(String[] data)
												{
													String amount = data[0];
													AccountMission.getInstance().chargeGold(
															user.getUserId(), 
															amount, 
															String.valueOf(user.getCoinBalance()), 
															new MissionCompleteInterface()
															{
																
																@Override
																public void onComplete(int errorCode)
																{
																	if (errorCode == ErrorCode.ERROR_SUCCESS)
																	{
																		Toast.makeText(
																				UserDetailActivity.this, 
																				"charge coin success", 
																				Toast.LENGTH_SHORT).show();
																	}else {
																		DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
																	}
																	
																}
															});
													
												}
											});
				}
			});
			
			chargeIngotButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					   managerGroup.dismiss();
					   CommonAlertDialog.makeInputAlertDialog(
							   					UserDetailActivity.this, 
							   					R.string.charge_ingot_hint, 
							   					null, 
							   					new String[]{getString(R.string.charge_ingot_hint)}, 
							   					new CommonAlertDialogCompleteInterface()
												{
													
													@Override
													public void complete(String[] data)
													{
														String amount = data[0];
														AccountMission.getInstance().chargeIngot(
																user.getUserId(), 
																amount, 
																String.valueOf(user.getCoinBalance()), 
																new MissionCompleteInterface()
																{
																	
																	@Override
																	public void onComplete(int errorCode)
																	{
																		if (errorCode == ErrorCode.ERROR_SUCCESS)
																		{
																			Toast.makeText(
																					UserDetailActivity.this, 
																					"charge coin success", 
																					Toast.LENGTH_SHORT).show();
																		}else {
																			DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
																		}
																		
																	}
																});
														
													}
												});
					
				}
			});
			
			blackUserButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CommonAlertDialog.makeAlertDialog(
							UserDetailActivity.this, 
							"", 
							getString(R.string.add_to_black_user_toast), 
							new CommonAlertDialogCompleteInterface()
					{
						
						@Override
						public void complete(String[] data)
						{
							AccountMission.getInstance().blackUser(user.getUserId(), new MissionCompleteInterface()
							{
								
								@Override
								public void onComplete(int errorCode)
								{
									if (errorCode == ErrorCode.ERROR_SUCCESS)
									{
										Toast.makeText(UserDetailActivity.this, "black user success", Toast.LENGTH_SHORT).show();
									}else {
										DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
									}
								}
							});
						}
					});
					
				}
			});
			
			blackDeviceButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CommonAlertDialog.makeAlertDialog(
							UserDetailActivity.this, 
							"", 
							getString(R.string.add_to_black_device_toast), 
							new CommonAlertDialogCompleteInterface()
					{
						
						@Override
						public void complete(String[] data)
						{
							AccountMission.getInstance().blackDevice(UserDetailActivity.this, new MissionCompleteInterface()
							{
								
								@Override
								public void onComplete(int errorCode)
								{
									if (errorCode == ErrorCode.ERROR_SUCCESS)
									{
										Toast.makeText(UserDetailActivity.this, "black device success", Toast.LENGTH_SHORT).show();
									}else {
										DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
									}
								}
							});
						}
					});
					
				}
			});
			
			
			unBlackUserButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CommonAlertDialog.makeAlertDialog(
							UserDetailActivity.this, 
							"", 
							getString(R.string.delete_from_black_user_toast), 
							new CommonAlertDialogCompleteInterface()
					{
						
						@Override
						public void complete(String[] data)
						{
							AccountMission.getInstance().unBlackUser(user.getUserId(), new MissionCompleteInterface()
							{
								
								@Override
								public void onComplete(int errorCode)
								{
									if (errorCode == ErrorCode.ERROR_SUCCESS)
									{
										Toast.makeText(UserDetailActivity.this, "unblack user success", Toast.LENGTH_SHORT).show();
									}else {
										DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
									}
								}
							});
						}
					});
					
				}
			});
			
			
			unBlackDeviceButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CommonAlertDialog.makeAlertDialog(
							UserDetailActivity.this, 
							"", 
							getString(R.string.delete_from_black_device_toast), 
							new CommonAlertDialogCompleteInterface()
					{
						
						@Override
						public void complete(String[] data)
						{
							AccountMission.getInstance().unBlackDevice(UserDetailActivity.this, new MissionCompleteInterface()
							{
								
								@Override
								public void onComplete(int errorCode)
								{
									if (errorCode == ErrorCode.ERROR_SUCCESS)
									{
										Toast.makeText(UserDetailActivity.this, "unblack device success", Toast.LENGTH_SHORT).show();
									}else {
										DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
									}
								}
							});
						}
					});
					
				}
			});
		}
		
		
		
		if(!managerGroup.isShowing()){
			managerGroup.showAtLocation(parentView, Gravity.CENTER, 0, 0);
		}
	}
	
	private OnClickListener pullDownOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if (btnGroup.getVisibility() == View.VISIBLE)
			{
				btnGroup.setVisibility(View.GONE);
				userBasicInfoGroup.setVisibility(View.VISIBLE);
			}else {
				btnGroup.setVisibility(View.VISIBLE);
				userBasicInfoGroup.setVisibility(View.GONE);
			}
			
		}
	};
	
	
	
	
	
	
	
	private OnClickListener chatOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			DrawHomeMessageDialogActivity.newInstance(UserDetailActivity.this, user);
		}
	};
	
	
	private OnClickListener followOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			FriendMission.getInstance().followUser(user.getUserId(), new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						Toast.makeText(UserDetailActivity.this, R.string.followed, Toast.LENGTH_LONG).show();
					}else {
						DrawNetworkConstants.errorToast(UserDetailActivity.this, errorCode);
					}
					
				}
			});
		}
	};
	
	
	private OnClickListener sinaOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			AbstractWeibo sinaWeibo = AbstractWeibo.getWeibo(UserDetailActivity.this, SinaWeibo.NAME);
			sinaWeibo.setWeiboActionListener(new WeiboActionListener()
			{
				
				@Override
				public void onError(AbstractWeibo arg0, int arg1, Throwable arg2)
				{
					runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
							Toast.makeText(UserDetailActivity.this, "authorize sina weibo fail", Toast.LENGTH_LONG).show();
						}
					});
				}
				
				@Override
				public void onComplete(AbstractWeibo arg0, int arg1,
						HashMap<String, Object> arg2)
				{
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(UserDetailActivity.this, "authorize sina weibo success", Toast.LENGTH_LONG).show();
							sinaWeiboButton.setSelected(false);
						}
					});
					
				}
				
				@Override
				public void onCancel(AbstractWeibo arg0, int arg1)
				{
				}
			});
			sinaWeibo.authorize();
		}
	};
	
	
	private OnClickListener qqOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			AbstractWeibo qqWeibo = AbstractWeibo.getWeibo(UserDetailActivity.this, TencentWeibo.NAME);
			qqWeibo.setWeiboActionListener(new WeiboActionListener()
			{
				
				@Override
				public void onError(AbstractWeibo arg0, int arg1, Throwable arg2)
				{
					runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
							Toast.makeText(UserDetailActivity.this, "authorize qq weibo fail", Toast.LENGTH_LONG).show();
						}
					});
				}
				
				@Override
				public void onComplete(AbstractWeibo arg0, int arg1,
						HashMap<String, Object> arg2)
				{
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(UserDetailActivity.this, "authorize qq weibo success", Toast.LENGTH_LONG).show();
							qqWeiBoButton.setSelected(false);
						}
					});
					
				}
				
				@Override
				public void onCancel(AbstractWeibo arg0, int arg1)
				{
				}
			});
			qqWeibo.authorize();
			
		}
	};
	
	
	private OnClickListener facebookOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			AbstractWeibo facebook = AbstractWeibo.getWeibo(UserDetailActivity.this, Facebook.NAME);
			facebook.setWeiboActionListener(new WeiboActionListener()
			{
				
				@Override
				public void onError(AbstractWeibo arg0, int arg1, Throwable arg2)
				{
					runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
							Toast.makeText(UserDetailActivity.this, "authorize facebook fail", Toast.LENGTH_LONG).show();
						}
					});
				}
				
				@Override
				public void onComplete(AbstractWeibo arg0, int arg1,
						HashMap<String, Object> arg2)
				{
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(UserDetailActivity.this, "authorize facebook success", Toast.LENGTH_LONG).show();
							facebookButton.setSelected(false);
						}
					});
					
				}
				
				@Override
				public void onCancel(AbstractWeibo arg0, int arg1)
				{
				}
			});
			facebook.authorize();
			
		}
	};

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		userOpusfeedManager.clear();
		userOpusfeedManager = null;
		userFavoriteFeedManager.clear();
		userFavoriteFeedManager = null;
	}
	
}
