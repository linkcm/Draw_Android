/**  
        * @title TimelineGridAdapter.java  
        * @package com.orange.game.draw.activity.adapter.timeline  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-10 上午9:55:31  
        * @version V1.0  
 */
package com.orange.game.draw.activity.timeline.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonFeedFragmentAdapter;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;



public class TimelineGridAdapter extends CommonFeedFragmentAdapter
{

	
	private int screenWidth;

	/**  
	* Constructor Method   
	* @param feeds
	* @param activity
	* @param screenWidth  
	*/
	public TimelineGridAdapter(List<PBFeed> feeds, Activity activity,
			int screenWidth)
	{
		super(feeds, activity);
		this.screenWidth = screenWidth;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getAdapterCount()
	{
		if (feeds == null)
		{
			return 0;
		}
		return feeds.size();
	}

	@Override
	protected View initConverView(View convertView, int position)
	{
		ViewHolder viewHolder = null;
		ImageView userDrawPicture = null;
		TextView userNickName = null;
		TextView drawWord = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.photo_gallery_view_item, null);
			GridView.LayoutParams params = new GridView.LayoutParams(screenWidth / 3, screenWidth / 3);
			convertView.findViewById(R.id.top_3_image_group).setLayoutParams(params);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PBFeed feed = null;
		userDrawPicture = viewHolder.getUserDrawPicture();
		userNickName = viewHolder.getUserNickName();
		drawWord = viewHolder.getDrawWord();
		userDrawPicture.setScaleType(ImageView.ScaleType.FIT_XY);
		feed = feeds.get(position);
		imageLoader.displayImage(feed.getOpusImage(), userDrawPicture);
		userNickName.setText(feed.getNickName());
		drawWord.setVisibility(View.GONE);
		//drawWord.setText(feed.getOpusWord());
		return convertView;
	}
	

	class ViewHolder
	{
		private View convertView;
		private ImageView userDrawPicture;
		private TextView userNickName;
		private TextView drawWord;
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}

		

		public TextView getUserNickName()
		{
			if(convertView != null)
			userNickName = (TextView) convertView.findViewById(R.id.user_nick_name);
			return userNickName;
		}



		public ImageView getUserDrawPicture()
		{
			if(convertView != null)
			userDrawPicture = (ImageView) convertView.findViewById(R.id.user_draw_picture);
			return userDrawPicture;
		}



		public TextView getDrawWord()
		{
			if(convertView != null)
			drawWord = (TextView) convertView.findViewById(R.id.user_draw_word);
			return drawWord;
		}

	}
}
