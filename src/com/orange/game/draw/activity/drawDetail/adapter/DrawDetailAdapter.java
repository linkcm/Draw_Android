/**  
        * @title DrawDetailAdapter.java  
        * @package com.orange.game.draw.activity.drawDetail.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-28 上午9:39:42  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.DateUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.adapter.CommonBaseAdapter;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.activity.drawDetail.ReplyCommentActivity;
import com.orange.game.draw.activity.drawDetail.adapter.detail.DetailDisplayStrategyInterface;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;



public class DrawDetailAdapter extends CommonBaseAdapter
{

	private DetailDisplayStrategyInterface display;	
	protected static final String TAG = "DrawDetailAdapter";
	private List<PBFeed> feeds;
	private Context context;
	private ImageLoader imageLoader;
	
	
	public DrawDetailAdapter(List<PBFeed> feeds,DetailDisplayStrategyInterface display,Context context)
	{
		super();
		this.feeds = feeds;
		this.context = context;
		this.display = display;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if(feeds == null)
			return 0;
		return feeds.size();
	}

	@Override
	public Object getItem(int position)
	{
		return feeds.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder ;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.draw_detail_list_view_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PBFeed feed = feeds.get(position);
		initDrawDetailComment(viewHolder, feed);
		return convertView;
	}

	
	
	private void initDrawDetailComment(ViewHolder viewHolder,final PBFeed feed){
		ImageView userAvatar = viewHolder.getUserAvatar();
		ImageView actionImage = viewHolder.getDrawDetailActionImage();
		ImageView replyComment = viewHolder.getDrawDetailCommentReply();
		TextView userNickName = viewHolder.getUserNickName();
		TextView drawDetailComment = viewHolder.getDrawDetailComment();
		TextView drawDetailCommentTime = viewHolder.getDrawDetailCommentTime();
		imageLoader.displayImage(feed.getAvatar(),userAvatar);
		userNickName.setText(feed.getNickName());
		drawDetailCommentTime.setText(DateUtil.dateFormatToString(feed.getCreateDate(),context));
		replyComment.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{

				if (feed != null)
				{
					Intent intent = new Intent();
					intent.putExtra(ReplyCommentActivity.PB_FEED,feed.toByteArray());
					intent.setClass(context,ReplyCommentActivity.class);
					context.startActivity(intent);
				}

			}
		});
		
		setUserAvatarOnClick(context, userAvatar, feed.getUserId());
		
		
		
		boolean isShowActionImage = display.isShowActionImage(feed);
		String  commentText = display.getCommentText(context,feed);
		int  imageURL = display.actionImageURL();
		if (isShowActionImage)
		{
			actionImage.setVisibility(View.VISIBLE);
			actionImage.setImageResource(imageURL);
		}else {
			actionImage.setVisibility(View.GONE);
		}
		drawDetailComment.setText(commentText);
			
	}
	
	

	
	


	class ViewHolder {
		private View convertView;
		private ImageView userAvatar;
		private ImageView drawDetailCommentReply;
		private ImageView drawDetailActionImage;
		private TextView userNickName;
		private TextView drawDetailComment;
		private TextView drawDetailCommentTime;
		
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}
		public ImageView getUserAvatar()
		{
			if (convertView !=null)
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
		public TextView getDrawDetailComment()
		{
			if (convertView!=null)
			{
				drawDetailComment = (TextView) convertView.findViewById(R.id.draw_detail_comment);
			}
			return drawDetailComment;
		}
		public TextView getDrawDetailCommentTime()
		{
			if (convertView !=null)
			{
				drawDetailCommentTime = (TextView) convertView.findViewById(R.id.draw_detail_comment_time);
			}
			return drawDetailCommentTime;
		}
		public ImageView getDrawDetailCommentReply()
		{
			if (convertView !=null)
			{
				drawDetailCommentReply = (ImageView) convertView.findViewById(R.id.draw_detail_comment_reply);
			}
			return drawDetailCommentReply;
		}
		public ImageView getDrawDetailActionImage()
		{
			if (convertView != null)
			{
				drawDetailActionImage = (ImageView) convertView.findViewById(R.id.draw_detail_comment_image);
			}
			return drawDetailActionImage;
		}
		
		
	}



	
	




	public List<PBFeed> getFeeds()
	{
		return feeds;
	}

	public void setFeeds(List<PBFeed> feeds)
	{
		this.feeds = feeds;
	}

	public DetailDisplayStrategyInterface getDisplay()
	{
		return display;
	}

	public void setDisplay(DetailDisplayStrategyInterface display)
	{
		this.display = display;
	}



	




}
