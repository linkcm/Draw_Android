/**  
        * @title DrawHomeFriendAdapter.java  
        * @package com.orange.game.draw.activity.adapter.home  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-18 下午3:31:59  
        * @version V1.0  
 */
package com.orange.game.draw.activity.friend.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonBaseAdapter;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.PBSNSUserUtils;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.orange.network.game.protocol.model.GameBasicProtos.PBSNSUser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-18 下午3:31:59  
 */

public class DrawHomeFriendAdapter extends CommonBaseAdapter
{
	private static final String TAG = "DrawHomeFriendAdapter";
	private List<Friend> friendList;
	private Context context;
	private ImageLoader imageLoader;
	
	
	public DrawHomeFriendAdapter(List<Friend> friendList,Context context)
	{
		super();
		this.friendList = friendList;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (friendList == null)
			return 0;
		return friendList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.draw_home_friend_fragment_list_view_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		initView(viewHolder, friendList.get(position));
		return convertView;
	}

	private void initView(ViewHolder viewHolder,Friend friend){
		ImageView userAvatar = viewHolder.getUserAvatar();
		ImageView sinaIcon = viewHolder.getSinaIcon();
		ImageView qqIcon = viewHolder.getQqIcon();
		ImageView facebookIcon = viewHolder.getFacebookIcon();
		TextView userNickName = viewHolder.getUserNickName();
		TextView userLevel = viewHolder.getUserLevel();
		TextView userGender = viewHolder.getUserGender();
		TextView userLocation = viewHolder.getUserLocation();
		imageLoader.displayImage(friend.getUser().getAvatar(), userAvatar);
		userNickName.setText(friend.getUser().getNickName());
		userLevel.setText(context.getString(R.string.level)+friend.getUser().getLevel());
		userGender.setText(GenderUtils.getUserGender(friend.getUser().getGender(),context));
		userLocation.setText(friend.getUser().getLocation());
		Log.d(TAG, "is bind sina weibo "+PBSNSUserUtils.isBindSina(friend.getUser()));
		if (PBSNSUserUtils.isBindSina(friend.getUser()))
		{
			sinaIcon.setVisibility(View.VISIBLE);
		}else {
			sinaIcon.setVisibility(View.GONE);
		}
		
		if (PBSNSUserUtils.isBindQQ(friend.getUser()))
		{
			qqIcon.setVisibility(View.VISIBLE);
		}else{
			qqIcon.setVisibility(View.GONE);
		}
		
		if (PBSNSUserUtils.isBindFacebook(friend.getUser()))
		{
			facebookIcon.setVisibility(View.VISIBLE);
		}else {
			facebookIcon.setVisibility(View.GONE);
		}
		//setUserAvatarOnClick(context, userAvatar, friend.getUser().getUserId());
	}
	
	
	
	
	class ViewHolder{
		private View convertView;
		private ImageView userAvatar;
		private TextView userNickName;
		private TextView userLevel;
		private TextView userGender;
		private TextView userLocation;
		private ImageView qqIcon;
		private ImageView sinaIcon;
		private ImageView facebookIcon;
		
		
		
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
		public TextView getUserLevel()
		{
			if (convertView != null)
			{
				userLevel = (TextView) convertView.findViewById(R.id.user_level);
			}
			return userLevel;
		}
		public TextView getUserGender()
		{
			if (convertView != null)
			{
				userGender = (TextView) convertView.findViewById(R.id.user_gender);
			}
			return userGender;
		}
		public TextView getUserLocation()
		{
			if (convertView != null)
			{
				userLocation = (TextView) convertView.findViewById(R.id.user_location);
			}
			return userLocation;
		}
		public ImageView getQqIcon()
		{
			if (convertView !=null)
			{
				qqIcon = (ImageView) convertView.findViewById(R.id.qq_weibo_icon);
			}
			return qqIcon;
		}
		public ImageView getSinaIcon()
		{
			if (convertView != null)
			{
				sinaIcon = (ImageView) convertView.findViewById(R.id.sina_weibo_icon);
			}
			return sinaIcon;
		}
		public ImageView getFacebookIcon()
		{
			if (convertView !=null)
			{
				facebookIcon = (ImageView) convertView.findViewById(R.id.facebook_icon);
			}
			return facebookIcon;
		}
		
		
	}




	public List<Friend> getFriendList()
	{
		return friendList;
	}

	public void setFriendList(List<Friend> friendList)
	{
		this.friendList = friendList;
	}




	
	
}
