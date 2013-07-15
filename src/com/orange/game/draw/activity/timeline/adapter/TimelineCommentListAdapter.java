/**  
        * @title TimelineCommentListAdapter.java  
        * @package com.orange.game.draw.context.adapter.timeline  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-12 下午3:31:35  
        * @version V1.0  
 */
package com.orange.game.draw.activity.timeline.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonFeedFragmentAdapter;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.feed.FeedConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;



public class TimelineCommentListAdapter extends CommonFeedFragmentAdapter
{

	
	/**  
	* Constructor Method   
	* @param feeds
	* @param context
	* @param screenWidth  
	*/
	public TimelineCommentListAdapter(List<PBFeed> feeds, Activity context)
	{
		super(feeds, context);
		// TODO Auto-generated constructor stub
	}



	private static final String TAG = "TimelineCommentListAdapter";



	

	@Override
	public int getAdapterCount()
	{
		if (feeds == null)
			return 0;

		return feeds.size();
	}

	@Override
	protected View initConverView(View convertView, int position)
	{
		ViewHolder viewHolder ;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.timeline_comment_list_view_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		initTimelineComment(viewHolder, feeds.get(position));
		
		return convertView;
	}
	
	
	private void initTimelineComment(ViewHolder viewHolder,PBFeed feed){
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageView userAvatar = viewHolder.getUserAvatar();
		ImageView timelineActionImage = viewHolder.getTimelineActionImage();
		TextView timelineCommentTime = viewHolder.getTimelineCommentTime();
		TextView timelineCommentContent = viewHolder.getTimelineCommentContent();
		TextView timelineCommentReply = viewHolder.getTimelineReply();
		TextView userNickName = viewHolder.getUserNickName();
		
		imageLoader.displayImage(feed.getAvatar(), userAvatar);
		userNickName.setText(feed.getNickName());
		timelineCommentTime.setText(DateUtil.dateFormatToString(feed.getCreateDate(),context));
		if (isShowActionImage(feed))
		{
			timelineActionImage.setImageResource(getActionImageURL(feed));
			timelineActionImage.setVisibility(View.VISIBLE);
		}else {
			timelineActionImage.setVisibility(View.GONE);
		}
		timelineCommentContent.setText(getTimelineComment(feed));
		timelineCommentReply.setText(getTimelineReply(feed));
		
		setUserAvatarOnClick(context, userAvatar, feed.getUserId());
		
	}
	
	
	private boolean isShowActionImage(PBFeed feed)
	{
		boolean isShow = false;
		if (feed.getActionType() == FeedConstants.FeedTypeFlower.intValue()||feed.getActionType() == FeedConstants.FeedTypeTomato.intValue())
		{
			isShow = true;
		}
		return isShow;
	}
	
	private int getActionImageURL(PBFeed feed)
	{
		int imageURL = 0;
		if (feed.getActionType() == FeedConstants.FeedTypeFlower.intValue())
		{
			imageURL = R.drawable.flower_result;
		}else if(feed.getActionType() == FeedConstants.FeedTypeTomato.intValue()){
			imageURL = R.drawable.tomato_result;
		}else{
			
		}
		return imageURL;	
	}
	
	
	
	private String getTimelineComment(PBFeed feed){
		StringBuffer comment = new StringBuffer();
		if (feed.getActionType() == FeedConstants.FeedTypeFlower.intValue())
		{
			comment.append(context.getString(R.string.give_flower));
		}else if(feed.getActionType() == FeedConstants.FeedTypeTomato.intValue()){
			
			comment.append(context.getString(R.string.give_tomato));
		}else {
			comment.append(context.getString(R.string.comment_reply));
			comment.append(feed.getCommentInfo().getActionNickName());
			comment.append(":"+feed.getComment());
		}
		return comment.toString();
	}
	
	private String getTimelineReply(PBFeed feed){
		StringBuffer reply = new StringBuffer();
		if (feed.hasCommentInfo())
		{
			if (feed.getCommentInfo().getType()==FeedConstants.FeedTypeComment.intValue())
			{
				reply.append(context.getString(R.string.reply_my_comment));
				reply.append(feed.getCommentInfo().getActionSummary());
			}else if(feed.getCommentInfo().getType()==FeedConstants.FeedTypeFlower.intValue()){
				reply.append(context.getString(R.string.reply_my_action));
				reply.append(context.getString(R.string.give_flower));
			}else if (feed.getCommentInfo().getType()==FeedConstants.FeedTypeTomato.intValue())
			{
				reply.append(context.getString(R.string.reply_my_action));
				reply.append(context.getString(R.string.give_tomato));
			}else if (feed.getCommentInfo().getType() == FeedConstants.FeedTypeDrawToContest.intValue()) {
				reply.append(feed.getCommentInfo().getActionSummary());
			}
			else {
				reply.append(context.getString(R.string.comment_my_draw));
				reply.append("["+feed.getCommentInfo().getActionSummary()+"]");
			}
		}
		return reply.toString();
	}
	
	

	
	
	
	
	class ViewHolder{
		private View convertView;
		private ImageView userAvatar;
		private ImageView timelineActionImage;
		private TextView userNickName;
		private TextView timelineCommentContent;
		private TextView timelineCommentTime;
		private TextView timelineReply;
		
		
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}
		
		
		
		public ImageView getUserAvatar()
		{
			if (convertView!=null)
			{
				userAvatar = (ImageView) convertView.findViewById(R.id.user_image);
			}
			return userAvatar;
		}
		public ImageView getTimelineActionImage()
		{
			if (convertView !=null)
			{
				timelineActionImage = (ImageView) convertView.findViewById(R.id.timeline_comment_action_image);
			}
			return timelineActionImage;
		}
		public TextView getUserNickName()
		{
			if (convertView != null)
			{
				userNickName = (TextView) convertView.findViewById(R.id.user_nick_name);
			}
			return userNickName;
		}
		public TextView getTimelineCommentContent()
		{
			if (convertView !=null)
			{
				timelineCommentContent = (TextView) convertView.findViewById(R.id.timeline_comment_content);
			}
			return timelineCommentContent;
		}
		public TextView getTimelineCommentTime()
		{
			if (convertView !=null)
			{
				timelineCommentTime = (TextView) convertView.findViewById(R.id.timeline_comment_time);
			}
			return timelineCommentTime;
		}
		public TextView getTimelineReply()
		{
			if (convertView!=null)
			{
				timelineReply = (TextView) convertView.findViewById(R.id.timeline_comment_reply);
			}
			return timelineReply;
		}
		
		
		
		
	}

}
