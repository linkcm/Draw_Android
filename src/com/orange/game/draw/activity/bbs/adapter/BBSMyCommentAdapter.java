/**  
        * @title BBSMyCommentAdapter.java  
        * @package com.orange.game.draw.activity.bbs.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-4 下午2:41:18  
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
import com.orange.network.game.protocol.model.BBSProtos.PBBBSAction;
import com.tencent.weibo.api.PrivateAPI;

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
 * @update 2013-5-4 下午2:41:18  
 */

public class BBSMyCommentAdapter extends BaseAdapter
{

	private Context context;
	private List<PBBBSAction> actionList;
	private ImageLoader imageLoader;
	
	
	/**  
	* Constructor Method   
	* @param context
	* @param actionList  
	*/
	public BBSMyCommentAdapter(Context context, List<PBBBSAction> actionList)
	{
		super();
		this.context = context;
		this.actionList = actionList;
		this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (actionList == null)
			return 0;
		return actionList.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2)
	{
		ViewHolder viewHolder;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.bbs_my_comment_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ImageView userAvatar = viewHolder.getUserAvatar();
		TextView userNickName = viewHolder.getUserNickName();
		TextView commentCreateTime = viewHolder.getCommentCreateTime();
		TextView commentContent = viewHolder.getCommentContent();
		ImageView commentImage = viewHolder.getCommentImage();
		TextView sourceComment = viewHolder.getSourceComment();
		
		PBBBSAction action = actionList.get(arg0);
		
		imageLoader.loadImage(action.getCreateUser().getAvatar(), new ImageLoadingListener()
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
		
		commentCreateTime.setText(DateUtil.dateFormatToString(action.getCreateDate(), context));
		userNickName.setText(action.getCreateUser().getNickName());
		commentContent.setText(action.getContent().getText());
		if (action.getContent().hasImageUrl())
		{
			commentImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(action.getContent().getThumbImageUrl(), commentImage);			
		}else if (action.getContent().hasDrawImageUrl()) {
			commentImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(action.getContent().getDrawThumbUrl(), commentImage);
		}else {
			commentImage.setVisibility(View.GONE);
		}
		
		sourceComment.setText(context.getString(R.string.feedback_me)+action.getSource().getBriefText());
		
		return convertView;
	}
	
	class ViewHolder{
		private View convertView;
		private ImageView userAvatar;
		private TextView userNickName;
		private TextView commentCreateTime;
		private TextView commentContent;
		private ImageView commentImage;
		private TextView sourceComment;
		
		
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
		public TextView getCommentCreateTime()
		{
			if (convertView != null)
			{
				commentCreateTime = (TextView) convertView.findViewById(R.id.bbs_comment_create_time);
			}
			return commentCreateTime;
		}
		public TextView getCommentContent()
		{
			if (convertView != null)
			{
				commentContent = (TextView) convertView.findViewById(R.id.bbs_comment_content);
			}
			return commentContent;
		}
		public ImageView getCommentImage()
		{
			if (convertView != null)
			{
				commentImage = (ImageView) convertView.findViewById(R.id.bbs_comment_image);
			}
			return commentImage;
		}
		public TextView getSourceComment()
		{
			if(convertView != null){
				sourceComment = (TextView) convertView.findViewById(R.id.bbs_source_comment);
			}
			return sourceComment;
		}
		
		
		
	}

	public void setActionList(List<PBBBSAction> actionList)
	{
		this.actionList = actionList;
	}

}
