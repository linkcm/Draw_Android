/**  
        * @title DrawHomeMessageDialogAdapter.java  
        * @package com.orange.game.draw.activity.message.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-4 上午10:45:26  
        * @version V1.0  
 */
package com.orange.game.draw.activity.message.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orange.common.android.utils.DateUtil;
import com.orange.common.android.utils.ImageUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonBaseAdapter;
import com.orange.game.draw.model.message.MessageStat;
import com.orange.game.draw.model.message.PPMessage;
import com.orange.game.draw.model.user.UserManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;



public class DrawHomeMessageDialogAdapter extends CommonBaseAdapter
{

	private static final String TAG = "DrawHomeMessageDialogAdapter";
	private List<PPMessage> messageList;
	private Context context;
	private ImageLoader imageLoader;
	private String targetUserId;
	private String targetUserAvatar;
	private int lastUpdateTime;
	@Override
	public int getCount()
	{
		if (messageList == null)
		{
			return 0;
		}
		return messageList.size();
	}

	
	public DrawHomeMessageDialogAdapter(List<PPMessage> messageList,String targetUserId,String targetUserAvatar,
			Context context)
	{
		super();
		this.messageList = messageList;
		this.targetUserId = targetUserId;
		this.targetUserAvatar = targetUserAvatar;
		this.context = context;
		this.imageLoader = ImageLoader.getInstance();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.draw_home_message_dialog_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ImageView userAvatar = viewHolder.getUserAvatar();
		TextView messageContent = viewHolder.getMessageContent();
		TextView messageCreateTime = viewHolder.getMessageCreateTime();
		PPMessage message = messageList.get(getCount()-(position+1));
		ViewGroup messageGroup = viewHolder.getMessageGroup();
		ImageView messageImage = viewHolder.getMessageImage();
		//userAvatar.setScaleType(ScaleType.FIT_XY);
		int timeLimit = message.getPbMessage().getCreateDate()-lastUpdateTime;
		if (position==0||timeLimit>180)
		{
			messageCreateTime.setVisibility(View.VISIBLE);
			messageCreateTime.setText(DateUtil.dateFormatToString(message.getPbMessage().getCreateDate(),context));
			lastUpdateTime = message.getPbMessage().getCreateDate();
		}else {
			messageCreateTime.setVisibility(View.GONE);
		}
		
		if (message.getPbMessage().getFrom().equalsIgnoreCase(targetUserId))
		{
			RelativeLayout.LayoutParams layoutParams = new LayoutParams(
					context.getResources().getDimensionPixelSize(R.dimen.space_40), 
					context.getResources().getDimensionPixelSize(R.dimen.space_40));
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			userAvatar.setLayoutParams(layoutParams);
			RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF,R.id.user_avatar);
			params.setMargins(
					context.getResources().getDimensionPixelSize(R.dimen.space_10), 
					0,
					context.getResources().getDimensionPixelSize(R.dimen.space_10) , 
					0);
			messageGroup.setLayoutParams(params);
			imageLoader.displayImage(targetUserAvatar, userAvatar);
			messageGroup.setBackgroundResource(R.drawable.receive_message);		
			
			
		}else {
			RelativeLayout.LayoutParams layoutParams = new LayoutParams(
					context.getResources().getDimensionPixelSize(R.dimen.space_40), 
					context.getResources().getDimensionPixelSize(R.dimen.space_40));
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			userAvatar.setLayoutParams(layoutParams);
			RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.LEFT_OF,R.id.user_avatar);
			params.setMargins(
					context.getResources().getDimensionPixelSize(R.dimen.space_10), 
					0, 
					context.getResources().getDimensionPixelSize(R.dimen.space_10),
					0);
			messageGroup.setLayoutParams(params);
			imageLoader.displayImage(UserManager.getInstance().getUser().getAvatar(), userAvatar);
			messageGroup.setBackgroundResource(R.drawable.sent_message);
		}
		setUserAvatarOnClick(context, userAvatar, targetUserId);
		if (message.getPbMessage().hasDrawDataVersion())
		{
			imageLoader.displayImage(targetUserAvatar, messageImage);
		}else {
			messageContent.setText(message.getPbMessage().getText());
		}
		
		return convertView;
	}

	
	class ViewHolder {
		private View convertView;
		private ImageView userAvatar;
		private ViewGroup messageGroup;
		private ImageView messageImage;
		private TextView messageContent;
		private TextView messageCreateTime;
		
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


		public TextView getMessageContent()
		{
			if (convertView != null)
			{
				messageContent = (TextView) convertView.findViewById(R.id.message_content);
			}
			return messageContent;
		}


		public TextView getMessageCreateTime()
		{
			if (convertView != null)
			{
				messageCreateTime = (TextView) convertView.findViewById(R.id.message_create_time);
			}
			return messageCreateTime;
		}


		public ViewGroup getMessageGroup()
		{
			if (convertView != null)
			{
				messageGroup = (ViewGroup) convertView.findViewById(R.id.message_content_group);
			}
			return messageGroup;
		}


		public ImageView getMessageImage()
		{
			if (convertView!= null)
			{
				messageImage = (ImageView) convertView.findViewById(R.id.message_image);
			}
			return messageImage;
		}
		
	}


	public List<PPMessage> getMessageList()
	{
		return messageList;
	}


	public void setMessageList(List<PPMessage> messageList)
	{
		this.messageList = messageList;
	}


	
	
}
