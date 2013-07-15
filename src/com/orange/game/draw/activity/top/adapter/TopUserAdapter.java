/**  
        * @title TopUserAdapter.java  
        * @package com.orange.game.draw.activity.top.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-27 上午10:45:26  
        * @version V1.0  
 */
package com.orange.game.draw.activity.top.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.draw.activity.top.adapter.PhotoGalleryGridViewAdapter.ViewHolder;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;



public class TopUserAdapter extends BaseAdapter
{

	private static final String TAG = "PhotoGalleryAdapter";
	private List<PBGameUser> gameUsers;
	private Context context;
	private ImageLoader imageLoader;
	private int screenWidth;
	
	
	
	
	
	
	public TopUserAdapter(List<PBGameUser> gameUsers, Context context,
			int screenWidth)
	{
		super();
		this.gameUsers = gameUsers;
		this.context = context;
		this.screenWidth = screenWidth;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (gameUsers == null)
		return 0;
		return gameUsers.size();
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
		ViewHolder viewHolder = null;
		ImageView userDrawPicture = null;
		ImageView pictureFlag = null;
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
		userDrawPicture = viewHolder.getUserDrawPicture();
		userNickName = viewHolder.getUserNickName();
		drawWord = viewHolder.getDrawWord();
		pictureFlag = viewHolder.getPictureFlag();
		switch (position)
		{
		case 0:
			pictureFlag.setImageResource(R.drawable.gold_cup);
			pictureFlag.setVisibility(View.VISIBLE);
			break;
		case 1:
			pictureFlag.setImageResource(R.drawable.silver_cup);
			pictureFlag.setVisibility(View.VISIBLE);
			break;
		case 2:
			pictureFlag.setImageResource(R.drawable.copper_cup);
			pictureFlag.setVisibility(View.VISIBLE);
			break;
		default:
			pictureFlag.setVisibility(View.GONE);
			break;
		}
		userDrawPicture.setScaleType(ImageView.ScaleType.FIT_XY);
		PBGameUser gameUser = gameUsers.get(position);
		imageLoader.displayImage(gameUser.getAvatar(), userDrawPicture);
		userNickName.setText(gameUser.getNickName());
		drawWord.setText(GenderUtils.getUserGender(gameUser.getGender(), context)+"LV"+gameUser.getLevel());
		return convertView;
	}

	class ViewHolder
	{
		private View convertView;
		private ImageView userDrawPicture;
		private ImageView pictureFlag;
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

		public ImageView getPictureFlag()
		{
			if (convertView != null)
			pictureFlag = (ImageView) convertView.findViewById(R.id.picture_flag);
			return pictureFlag;
		}
		
		

	}

	

	public void setGameUsers(List<PBGameUser> gameUsers)
	{
		this.gameUsers = gameUsers;
	}
}
