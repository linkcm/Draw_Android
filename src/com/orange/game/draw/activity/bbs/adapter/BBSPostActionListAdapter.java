/**  
        * @title BBSPostDetailListAdapter.java  
        * @package com.orange.game.draw.activity.bbs.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-26 下午3:23:42  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs.adapter;

import java.util.List;



import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.ImageUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.bbs.BBSPostDetailActivity;
import com.orange.game.draw.activity.bbs.BBSSendCommentActivity;
import com.orange.game.draw.activity.bbs.BBSSendPostActivity;
import com.orange.game.draw.activity.common.CommonImageLoaderActivity;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSAction;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-26 下午3:23:42  
 */

public class BBSPostActionListAdapter extends BaseAdapter
{

	private static final String TAG = "BBSPostActionListAdapter";
	private Context context;
	private PBBBSPost post;
	private List<PBBBSAction> actionList;
	private ImageLoader imageLoader;
	private boolean hasRewardWinner = false;
	
	/**  
	* Constructor Method   
	* @param context
	* @param actionList  
	*/
	public BBSPostActionListAdapter(Context context,
			List<PBBBSAction> actionList,PBBBSPost post)
	{
		super();
		this.context = context;
		this.actionList = actionList;
		this.post = post;
		this.imageLoader = ImageLoader.getInstance();
		if (post.hasReward())
			hasRewardWinner = post.getReward().hasWinner();
	}

	@Override
	public int getCount()
	{
		if (actionList == null)
			return 0;
		return actionList.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.bbs_post_action_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final PBBBSAction action = actionList.get(position);
		
		final ImageView userAvatar = viewHolder.getUserAvatar();
		TextView userNickName = viewHolder.getUserNickName();
		TextView postActtionCreateTime = viewHolder.getPostActionCreateTime();
		TextView postComment = viewHolder.getPostComment();
		ImageView postSupport = viewHolder.getPostSupport();
		ImageView postCommentReply = viewHolder.getPostCommentReply();
		ImageView postImage = viewHolder.getPostImage();
		ImageView postReward = viewHolder.getPostReward(); 
		
		imageLoader.loadImage(action.getCreateUser().getAvatar(), new ImageLoadingListener()
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
		
		
		if (action.getType() == BBSStatus.ActionTypeComment)
		{
			if (action.getSource().hasActionId())
			{
				postComment.setText(
						context.getString(R.string.comment_reply)
						+action.getSource().getActionNick()
						+":"
						+action.getContent().getText());
			}else {
				postComment.setText(action.getContent().getText());
			}
			
			postSupport.setVisibility(View.GONE);
			postComment.setVisibility(View.VISIBLE);
			postImage.setVisibility(View.VISIBLE);
			if (action.getContent().hasImageUrl())
			{
				Log.d(TAG, "action image url = "+action.getContent().getImageUrl());
				imageLoader.displayImage(action.getContent().getThumbImageUrl(), postImage);
				postImage.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						CommonImageLoaderActivity.newInstance(
										context,
										action.getContent().getImageUrl() );
						
					}
				});
			}else if (action.getContent().hasDrawImageUrl()) {
				imageLoader.displayImage(action.getContent().getDrawThumbUrl(), postImage);
				postImage.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						CommonImageLoaderActivity.newInstance(
										context,
										action.getContent().getDrawImageUrl() );
						
					}
				});
			}else {
				postImage.setVisibility(View.GONE);
			}
			if (!hasRewardWinner
					&& !action.getCreateUser()
							  .getUserId()
							  .equalsIgnoreCase(post.getCreateUser().getUserId())
					&&post.getCreateUser()
							.getUserId()
							.equalsIgnoreCase(UserManager.getInstance()
							.getTestUserId())		  )
			{
				postReward.setVisibility(View.VISIBLE);
				postCommentReply.setVisibility(View.GONE);
				postReward.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						showRewardOption(v, action);
					}
				});

			} else
			{
				postReward.setVisibility(View.GONE);
				postCommentReply.setVisibility(View.VISIBLE);
			}
			
		}else {
				postImage.setVisibility(View.GONE);
				postComment.setVisibility(View.GONE);
				postSupport.setVisibility(View.VISIBLE);
		}
		
		userNickName.setText(action.getCreateUser().getNickName());
		postActtionCreateTime.setText(DateUtil.dateFormatToString(action.getCreateDate(), context));
		postCommentReply.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				BBSSendCommentActivity.newInstance(context, post, action);			
			}
		});
		
		
		return convertView;
	}
	
	
	private PopupWindow rewardPopupWindow;
	private void showRewardOption(View parentView,final PBBBSAction action){

		if (rewardPopupWindow ==null)
		{
			View rewardOptionView = LayoutInflater.from(context).inflate(R.layout.bbs_post_detail_reward, null);
			Button rewardButton = (Button) rewardOptionView.findViewById(R.id.bbs_reward);
			Button commentReplyButton = (Button) rewardOptionView.findViewById(R.id.bbs_comment_reply);
			
			
			rewardButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					
					rewardPopupWindow.dismiss();
					BBSMission.getInstance().payReward(
							post.getPostId(), 
							action.getActionId(), 
							action.getCreateUser().getUserId(), 
							action.getCreateUser().getNickName(), 
							new MissionCompleteInterface()
							{
								
								@Override
								public void onComplete(int errorCode)
								{
									if (errorCode == ErrorCode.ERROR_SUCCESS)
									{
										hasRewardWinner = true;
										notifyDataSetChanged();
									}else {
										DrawNetworkConstants.errorToast(context, errorCode);
									}
								}
							});
				}
			});
			
			commentReplyButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					BBSSendCommentActivity.newInstance(context, post, action);	
					rewardPopupWindow.dismiss();
				}
			});
			
			
			
			
			ColorDrawable background = new ColorDrawable(context.getResources().getColor(android.R.color.transparent)); 
			rewardPopupWindow = PopupWindowUtil.getPopupWindow(
																rewardOptionView, 
																background,
																context,
																LayoutParams.WRAP_CONTENT, 
																LayoutParams.WRAP_CONTENT);
		}
		if (!rewardPopupWindow.isShowing())
		{
			rewardPopupWindow.showAsDropDown(parentView, 
					 parentView.getScrollX(), 
					 parentView.getScrollY()-3*parentView.getHeight());
		}
		
		
		
	
	}
	
	
	
	class ViewHolder{
		private View convertView;
		private ImageView userAvatar;
		private TextView userNickName;
		private TextView postActionCreateTime;
		private TextView postComment;
		private ImageView postSupport;
		private ImageView postCommentReply;
		private ImageView postImage;
		private ImageView postReward;
		
		/**  
		* Constructor Method   
		* @param convertView  
		*/
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}
		
		
		
		public ImageView getUserAvatar()
		{
			if (convertView != null)
			{
				userAvatar = (ImageView) convertView.findViewById(R.id.user_avatar);
			}
			return userAvatar;
		}
		public TextView getUserNickName()
		{
			if (convertView != null)
			{
				userNickName = (TextView) convertView.findViewById(R.id.user_nick_name);
			}
			return userNickName;
		}
		public TextView getPostActionCreateTime()
		{
			if (convertView != null)
			{
				postActionCreateTime = (TextView) convertView.findViewById(R.id.bbs_post_detail_action_create_time);
			}
			return postActionCreateTime;
		}
		public TextView getPostComment()
		{
			if (convertView != null)
			{
				postComment = (TextView) convertView.findViewById(R.id.bbs_post_detail_comment);
			}
			return postComment;
		}
		public ImageView getPostSupport()
		{
			if (convertView != null)
			{
				postSupport = (ImageView) convertView.findViewById(R.id.bbs_post_detail_support);
			}
			return postSupport;
		}
		public ImageView getPostCommentReply()
		{
			if (convertView != null)
			{
				postCommentReply = (ImageView) convertView.findViewById(R.id.bbs_post_detail_comment_reply);
			}
			return postCommentReply;
		}
		public ImageView getPostImage()
		{
			if (convertView != null)
			{
				postImage = (ImageView) convertView.findViewById(R.id.bbs_post_detail_image);
			}
			return postImage;
		}



		public ImageView getPostReward()
		{
			if (convertView != null)
			{
				postReward = (ImageView) convertView.findViewById(R.id.bbs_post_detail_reward);
			}
			return postReward;
		}

	}


	public void setActionList(List<PBBBSAction> actionList)
	{
		this.actionList = actionList;
	}

}
