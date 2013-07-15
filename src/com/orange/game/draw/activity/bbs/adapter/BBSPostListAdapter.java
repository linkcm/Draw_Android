/**  
        * @title BBSPostListAdapter.java  
        * @package com.orange.game.draw.activity.bbs.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-25 下午2:35:01  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.ImageUtil;
import com.orange.game.R;
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-25 下午2:35:01  
 */

public class BBSPostListAdapter extends BaseAdapter
{

	private Context context;
	private List<PBBBSPost> postList;
	private ImageLoader imageLoader;
	
	
	
	
	/**  
	* Constructor Method   
	* @param context
	* @param postList  
	*/
	public BBSPostListAdapter(Context context, List<PBBBSPost> postList)
	{
		super();
		this.context = context;
		this.postList = postList;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (postList == null)
			return 0;
		return postList.size();
	}

	@Override
	public Object getItem(int arg0)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.bbs_post_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ImageView userAvatar = viewHolder.getUserAvatar();
		TextView userNickName = viewHolder.getUserNickName();
		TextView postCreateTime = viewHolder.getPostCreateTime();
		TextView postContent = viewHolder.getPostContent();
		ImageView postImage = viewHolder.getPostImage();
		TextView postTop = viewHolder.getPostTop();
		ImageView postRewardImage = viewHolder.getPostRewardImage();
		TextView postReward = viewHolder.getPostReward();
		TextView postSupportCount = viewHolder.getPostSupportCount();
		TextView postCommentCount = viewHolder.getPostCommentCount();
		
		PBBBSPost post = postList.get(position);
		

		imageLoader.loadImage(post.getCreateUser().getAvatar(), new ImageLoadingListener()
		{
			
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason)
			{
				// TODO Auto-generated method stub
				
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
				// TODO Auto-generated method stub
				
			}
		});
		
		userNickName.setText(post.getCreateUser().getNickName());
		postCreateTime.setText(DateUtil.dateFormatToString(post.getCreateDate(),context));
		postContent.setText(post.getContent().getText());
		postImage.setVisibility(View.VISIBLE);
		if (post.getContent().hasImageUrl())
		{
			imageLoader.displayImage(post.getContent().getThumbImageUrl(), postImage);
		}else if (post.getContent().hasDrawImageUrl()) {
			imageLoader.displayImage(post.getContent().getDrawThumbUrl(), postImage);
		}else {
			postImage.setVisibility(View.GONE);
		}
		
		if (post.getStatus()==BBSStatus.StatusTop)
		{
			postTop.setVisibility(View.VISIBLE);
		}else {
			postTop.setVisibility(View.GONE);
		}
		
		if (post.hasReward())
		{
			postReward.setText(""+post.getReward().getBonus());
			postRewardImage.setVisibility(View.VISIBLE);
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
		
		postSupportCount.setText(""+post.getSupportCount());
		postCommentCount.setText(""+post.getReplyCount());
		
		return convertView;
	}

	
	class ViewHolder{
		private View convertView;
		private ImageView userAvatar;
		private TextView userNickName;
		private TextView postCreateTime;
		private TextView postContent;
		private ImageView postImage;
		private TextView postTop;
		private ImageView postRewardImage;
		private TextView postReward;
		private TextView postSupportCount;
		private TextView postCommentCount;
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
		public TextView getPostCreateTime()
		{
			if (convertView != null)
			{
				postCreateTime = (TextView) convertView.findViewById(R.id.bbs_post_create_time);
			}
			return postCreateTime;
		}
		public TextView getPostContent()
		{
			if (convertView != null)
			{
				postContent = (TextView) convertView.findViewById(R.id.bbs_post_content);
			}
			return postContent;
		}
		public ImageView getPostImage()
		{
			if (convertView != null)
			{
				postImage = (ImageView) convertView.findViewById(R.id.bbs_post_image);
			}
			return postImage;
		}
		public TextView getPostTop()
		{
			if (convertView != null)
			{
				postTop = (TextView) convertView.findViewById(R.id.bbs_post_top);
			}
			return postTop;
		}
		public ImageView getPostRewardImage()
		{
			if (convertView != null)
			{
				postRewardImage = (ImageView) convertView.findViewById(R.id.bbs_post_reward_image);
			}
			return postRewardImage;
		}
		public TextView getPostReward()
		{
			if (convertView != null)
			{
				postReward = (TextView) convertView.findViewById(R.id.bbs_post_reward);
			}
			return postReward;
		}
		public TextView getPostSupportCount()
		{
			if (convertView != null)
			{
				postSupportCount = (TextView) convertView.findViewById(R.id.bbs_post_support_count);
			}
			return postSupportCount;
		}
		public TextView getPostCommentCount()
		{
			if (convertView != null)
			{
				postCommentCount = (TextView) convertView.findViewById(R.id.bbs_post_comment_count);
			}
			return postCommentCount;
		}
		
		
		
	}


	public void setPostList(List<PBBBSPost> postList)
	{
		this.postList = postList;
	}


	
}
