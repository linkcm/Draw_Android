/**  
 * @title DrawHomeChatAdapter.java  
 * @package com.orange.game.draw.activity.home.adapter  
 * @description   
 * @author liuxiaokun  
 * @update 2013-1-31 上午10:45:52  
 * @version V1.0  
 */
package com.orange.game.draw.activity.message.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonBaseAdapter;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.message.MessageStat;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @description
 * @version 1.0
 * @author liuxiaokun
 * @update 2013-1-31 上午10:45:52
 */

public class DrawHomeMessageAdapter extends CommonBaseAdapter
{

	private static final String TAG = "DrawHomeMessageAdapter";
	private List<MessageStat> messageStats;
	private Context context;
	private ImageLoader imageLoader;

	
	public DrawHomeMessageAdapter(List<MessageStat> messageStats, Context context)
	{
		super();
		this.messageStats = messageStats;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (messageStats == null)
		{
			return 0;
		}
		return messageStats.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.draw_home_message_fragment_list_view_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageView userAvatar = viewHolder.getUserAvatar();
		TextView messageContent = viewHolder.getMessageContent();
		TextView messageTime = viewHolder.getMessageTime();
		TextView userNickName = viewHolder.getUserNickName();
		final MessageStat messageStat = messageStats.get(position);
		imageLoader.displayImage(messageStat.getPBMessageStat().getFriendAvatar(), userAvatar);
		userNickName.setText(messageStat.getPBMessageStat().getFriendNickName());
		messageTime.setText(DateUtil.dateFormatToString(messageStat.getPBMessageStat().getModifiedDate(),context));
		messageContent.setText(messageStat.getPBMessageStat().getText());
		setUserAvatarOnClick(context, userAvatar, messageStat.getPBMessageStat().getUserId());
		return convertView;
	}

	

	
	
	
	class ViewHolder
	{
		private View convertView;
		private ImageView userAvatar;
		private TextView messageContent;
		private TextView messageTime;
		private TextView userNickName;
		
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


		public TextView getMessageContent()
		{
			if (convertView != null)
			{
				messageContent = (TextView) convertView.findViewById(R.id.message_content);
			}
			return messageContent;
		}


		public TextView getMessageTime()
		{
			if (convertView != null)
			{
				messageTime = (TextView) convertView.findViewById(R.id.message_time);
			}
			return messageTime;
		}

	}

	public List<MessageStat> getMessageStats()
	{
		return messageStats;
	}

	public void setMessageStats(List<MessageStat> messageStats)
	{
		this.messageStats = messageStats;
	}

	
	

}
