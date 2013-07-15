/**  
 * @title TimelineListAdapter.java  
 * @package com.orange.game.draw.activity.adapter.timeline  
 * @description   
 * @author liuxiaokun  
 * @update 2013-1-9 下午3:26:43  
 * @version V1.0  
 */
package com.orange.game.draw.activity.timeline.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.DateUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonFeedFragmentAdapter;
import com.orange.game.draw.activity.timeline.TimelineActivity;
import com.orange.game.draw.model.feed.FeedConstants;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.feed.FeedUtil;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.orange.network.game.protocol.model.DrawProtos.PBFeedTimes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimelineListAdapter extends CommonFeedFragmentAdapter
{

	

	/**  
	* Constructor Method   
	* @param feeds
	* @param activity
	* @param screenWidth  
	*/
	public TimelineListAdapter(List<PBFeed> feeds, Context context)
	{
		super(feeds, context);
		// TODO Auto-generated constructor stub
	}

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
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.timeline_list_view_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PBFeed feed = feeds.get(position);
		initUserInfo(viewHolder, feed);

		initTimeline(viewHolder, feed);
		//createIntent(feed.getOpusId(), convertView);
		return convertView;
	}

	private void initUserInfo(ViewHolder viewHolder, PBFeed feed)
	{
		ImageView userAvatar = viewHolder.getUserAvatar();
		TextView userNickName = viewHolder.getUserNickName();
		imageLoader.displayImage(feed.getAvatar(), userAvatar);
		userNickName.setText(feed.getNickName());
		setUserAvatarOnClick(context, userAvatar, feed.getUserId());
	}

	
	
	
	
	
	private void initTimeline(ViewHolder viewHolder, PBFeed feed)
	{
		TextView timelineContent = viewHolder.getTimelineContent();
		TextView timelineFeedTime = viewHolder.getTimelineFeedTime();
		TextView userGuessed = viewHolder.getUserGuessed();
		TextView userGuessedCorrect = viewHolder.getUserGuessedCorrect();
		ImageView userDrawPicture = viewHolder.getUserDrawPicture();
		imageLoader.displayImage(feed.getOpusImage(), userDrawPicture);
		List<PBFeedTimes>  feedTimesList =feed.getFeedTimesList();
		String userGuessedString = context.getString(R.string.guessed)
				+FeedUtil.getFeedTimesByType(feedTimesList,FeedConstants.FeedTimesTypeGuess);
		String userGuessCorrectString = context.getString(R.string.guess_bingo)
				+ FeedUtil.getFeedTimesByType(feedTimesList,FeedConstants.FeedTimesTypeCorrect);
		userGuessed.setText(userGuessedString);
		userGuessedCorrect.setText(userGuessCorrectString);
		timelineFeedTime.setText(DateUtil.dateFormatToString(feed.getCreateDate(),context));
		String timelineContentDesc = getTimelineContentByType(feed, feed.getActionType());
		timelineContent.setText(timelineContentDesc);

	}

	
	
	private String getTimelineContentByType(PBFeed feed,int actionType){
		String timelineContent = "";
		if (actionType == FeedConstants.FeedTypeDraw.intValue())
		{
			timelineContent = context.getString(R.string.draw_desc_no_word);
		}else if (actionType == FeedConstants.FeedTypeGuess.intValue())
		{
			timelineContent = String.format(context.getString(R.string.guess_right_desc_no_word),feed.getOpusCreatorNickName());
		}else if (actionType == FeedConstants.FeedTypeDrawToUser.intValue()) {
			timelineContent = String.format(context.getString(R.string.draw_to_somebody_no_word),feed.getTargetUserNickName());
		}else {
			timelineContent = ""+actionType;
		}
		return timelineContent;
	}
	
	class ViewHolder
	{
		private View convertView;
		private ImageView userAvatar;
		private ImageView userDrawPicture;
		private TextView userNickName;
		private TextView timelineContent;
		private TextView timelineFeedTime;
		private TextView userGuessed;
		private TextView userGuessedCorrect;

		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}

		public ImageView getUserAvatar()
		{
			if (convertView != null)
			{
				userAvatar = (ImageView) convertView.findViewById(R.id.user_image);
			}
			return userAvatar;
		}

		public ImageView getUserDrawPicture()
		{
			if (convertView != null)
			{
				userDrawPicture = (ImageView) convertView
						.findViewById(R.id.user_draw_picture);
			}
			return userDrawPicture;
		}

		public TextView getUserNickName()
		{
			if (convertView != null)
			{
				userNickName = (TextView) convertView
						.findViewById(R.id.user_nick_name);
			}
			return userNickName;
		}

		public TextView getTimelineContent()
		{
			if (convertView != null)
			{
				timelineContent = (TextView) convertView
						.findViewById(R.id.timeline_content);
			}
			return timelineContent;
		}

		public TextView getTimelineFeedTime()
		{
			if (convertView != null)
			{
				timelineFeedTime = (TextView) convertView
						.findViewById(R.id.timeline_feed_time);
			}
			return timelineFeedTime;
		}

		public TextView getUserGuessed()
		{
			if (convertView != null)
			{
				userGuessed = (TextView) convertView
						.findViewById(R.id.user_guessed);
			}
			return userGuessed;
		}

		public TextView getUserGuessedCorrect()
		{
			if (convertView != null)
			{
				userGuessedCorrect = (TextView) convertView.findViewById(R.id.user_guess_correct);
			}
			return userGuessedCorrect;
		}
	}

}
