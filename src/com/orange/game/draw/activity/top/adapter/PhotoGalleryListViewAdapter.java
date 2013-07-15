/**  
        * @title PhotoGalleryListViewAdapter.java  
        * @package com.orange.game.draw.activity.adapter.top  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-6 下午5:54:09  
        * @version V1.0  
 */
package com.orange.game.draw.activity.top.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orange.game.R;
import com.orange.game.draw.activity.common.adapter.CommonFeedFragmentAdapter;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-6 下午5:54:09  
 */

public class PhotoGalleryListViewAdapter extends CommonFeedFragmentAdapter
{
	
	

	/**  
	* Constructor Method   
	* @param feeds
	* @param activity
	* @param screenWidth  
	*/
	private int screenWidth;
	public PhotoGalleryListViewAdapter(List<PBFeed> feeds, Activity activity,
			int screenWidth)
	{
		super(feeds, activity);
		this.screenWidth =screenWidth;
		// TODO Auto-generated constructor stub
	}




	private static final String TAG = "PhotoGalleryListViewAdapter";

	@Override
	public int getAdapterCount()
	{
		if(feeds == null)
			return 0;
		if(feeds.size()==0)
			return 0 ;
		if (feeds.size() == 1)
			return 1;
		if(feeds.size()%3!=0)
			return feeds.size()/3+2;
		return feeds.size()/3+1;
	}

	@Override
	protected View initConverView(View convertView, int position)
	{
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
			convertView =   LayoutInflater.from(context).inflate(R.layout.photo_gallery_list_view_item,null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		initViewGroup(screenWidth, position, viewHolder);
		initUserDrawGroup(position, viewHolder);	
		return convertView;
	}
	
	
	private void initViewGroup(int screedWidth,int position,ViewHolder viewHolder){
		ViewGroup userViewGroup1 = viewHolder.getUserViewGroup1();
		ViewGroup userViewGroup2 = viewHolder.getUserViewGroup2();
		ViewGroup userViewGroup3 = viewHolder.getUserViewGroup3();
		
		int width = screedWidth/3;
		int height = screedWidth/3;
		if(position==0){
			width = screedWidth;
			height = screedWidth/2;
			userViewGroup2.setVisibility(View.GONE);
			userViewGroup3.setVisibility(View.GONE);
		}else if (position==1) {
			width = screedWidth/2;
			height = screedWidth/2;
			userViewGroup1.setVisibility(View.VISIBLE);
			userViewGroup2.setVisibility(View.VISIBLE);
			userViewGroup3.setVisibility(View.GONE);
		}else if (feeds.size()%3==1&&position==feeds.size()/3+2) {			
			userViewGroup2.setVisibility(View.GONE);
			userViewGroup3.setVisibility(View.GONE);
		}else if (feeds.size()%3==2&&position==feeds.size()/3+2) {
			userViewGroup3.setVisibility(View.GONE);
		}else {
			userViewGroup1.setVisibility(View.VISIBLE);
			userViewGroup2.setVisibility(View.VISIBLE);
			userViewGroup3.setVisibility(View.VISIBLE);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
		params.setMargins(0, 0, 2, 0);
		userViewGroup1.setLayoutParams(params);
		userViewGroup3.setLayoutParams(params);
		userViewGroup2.setLayoutParams(params);
	}
	
	private void initUserDrawGroup(int position,ViewHolder viewHolder){
		ImageView userDrawPicture1 = viewHolder.getUserDrawPicture1();
		ImageView userDrawPicture2 = viewHolder.getUserDrawPicture2();
		ImageView userDrawPicture3 =  viewHolder.getUserDrawPicture3();
		TextView userNickName1 = viewHolder.getUserNickName1();
		TextView userNickName2 = viewHolder.getUserNickName2();
		TextView userNickName3 = viewHolder.getUserNickName3();
		int startPosition = (position-2)*3+2;
		if(position==0){
			userDrawPicture1.setScaleType(ImageView.ScaleType.CENTER_CROP);
			setData(0,viewHolder.getUserViewGroup1(), userDrawPicture1, userNickName1);
		}else if (position==1) {
			userDrawPicture1.setScaleType(ImageView.ScaleType.FIT_XY);
			setData(1,viewHolder.getUserViewGroup1(), userDrawPicture1, userNickName1);
			setData(2,viewHolder.getUserViewGroup2(),userDrawPicture2, userNickName2);
		}else if (feeds.size()%3==1&&position==feeds.size()/3+1) {
			userDrawPicture1.setScaleType(ImageView.ScaleType.FIT_XY);
			setData(startPosition+1,viewHolder.getUserViewGroup1(), userDrawPicture1, userNickName1);
		}else if (feeds.size()%3==2&&position==feeds.size()/3+1) {
			userDrawPicture1.setScaleType(ImageView.ScaleType.FIT_XY);
			setData(startPosition+1, viewHolder.getUserViewGroup2(),userDrawPicture2, userNickName2);
			setData(startPosition+2,viewHolder.getUserViewGroup3(), userDrawPicture3, userNickName3);
		}else {
			userDrawPicture1.setScaleType(ImageView.ScaleType.FIT_XY);
			setData(startPosition+1,viewHolder.getUserViewGroup1(), userDrawPicture1, userNickName1);		
			setData(startPosition+2,viewHolder.getUserViewGroup2(), userDrawPicture2, userNickName2);
			setData(startPosition+3,viewHolder.getUserViewGroup3(), userDrawPicture3, userNickName3);
		}
	}
	
	
	private void setData(int position,ViewGroup userViewGroup,ImageView imageView,TextView textView){
		PBFeed feed = feeds.get(position);
		Log.d(TAG, "top year image url = "+feed.getOpusImage());
		imageLoader.displayImage(feed.getOpusImage(), imageView);
		textView.setText(feed.getNickName());
		createIntent(feed.getFeedId(), userViewGroup);
	}
	
	

	
	class ViewHolder {
		private View convertView;	
		private ImageView pictureFlag1;
		private ImageView pictureFlag2;
		private ImageView pictureFlag3;
		private ImageView userDrawPicture1;
		private ImageView userDrawPicture2;
		private ImageView userDrawPicture3;
		private ViewGroup userViewGroup1;
		private ViewGroup userViewGroup2;
		private ViewGroup userViewGroup3;
		private TextView  userNickName1;
		private TextView  userNickName2;
		private TextView  userNickName3;
		private TextView  userDrawWord1;
		private TextView  userDrawWord2;
		private TextView  userDrawWord3;
		
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
			userViewGroup1 = (ViewGroup) convertView.findViewById(R.id.photo_grallery_list_item1);
			userViewGroup2 = (ViewGroup) convertView.findViewById(R.id.photo_grallery_list_item2);
			userViewGroup3 = (ViewGroup) convertView.findViewById(R.id.photo_grallery_list_item3);
		}

		public ImageView getUserDrawPicture1()
		{
			if (userViewGroup1 !=null)
			{
				userDrawPicture1 = (ImageView) userViewGroup1.findViewById(R.id.user_draw_picture);
			}
			return userDrawPicture1;
		}

		public ImageView getUserDrawPicture2()
		{
			if (userViewGroup2 !=null)
			{
				userDrawPicture2 = (ImageView) userViewGroup2.findViewById(R.id.user_draw_picture);
			}
			return userDrawPicture2;
		}

		public ImageView getUserDrawPicture3()
		{
			if (userViewGroup3 !=null)
			{
				userDrawPicture3 = (ImageView) userViewGroup3.findViewById(R.id.user_draw_picture);
			}
			return userDrawPicture3;
		}

		public ViewGroup getUserViewGroup1()
		{
			return userViewGroup1;
		}

		public ViewGroup getUserViewGroup2()
		{
			return userViewGroup2;
		}

		public ViewGroup getUserViewGroup3()
		{
			return userViewGroup3;
		}

		public TextView getUserNickName1()
		{
			if (userViewGroup1 !=null)
			{
				userNickName1 = (TextView) userViewGroup1.findViewById(R.id.user_nick_name);
			}
			return userNickName1;
		}

		public TextView getUserNickName2()
		{
			if (userViewGroup2 !=null)
			{
				userNickName2 = (TextView) userViewGroup2.findViewById(R.id.user_nick_name);
			}
			return userNickName2;
		}

		public TextView getUserNickName3()
		{
			if (userViewGroup3 !=null)
			{
				userNickName3 = (TextView) userViewGroup3.findViewById(R.id.user_nick_name);
			}
			return userNickName3;
		}

		public TextView getUserDrawWord1()
		{
			if (userViewGroup1 !=null)
			{
				userDrawWord1 = (TextView) userViewGroup1.findViewById(R.id.user_draw_word);
			}
			return userDrawWord1;
		}

		public TextView getUserDrawWord2()
		{
			if (userViewGroup2 !=null)
			{
				userDrawWord2 = (TextView) userViewGroup2.findViewById(R.id.user_draw_word);
			}
			return userDrawWord2;
		}

		public TextView getUserDrawWord3()
		{
			if (userViewGroup3 !=null)
			{
				userDrawWord3 = (TextView) userViewGroup3.findViewById(R.id.user_draw_word);
			}
			return userDrawWord3;
		}

		public ImageView getPictureFlag()
		{
			if (userViewGroup1 != null)
			{
				pictureFlag1 = (ImageView) userViewGroup1.findViewById(R.id.picture_flag);
			}
			return pictureFlag1;
		}

		public ImageView getPictureFlag2()
		{
			if (userViewGroup1 != null)
			{
				pictureFlag2 = (ImageView) userViewGroup2.findViewById(R.id.picture_flag);
			}
			return pictureFlag2;
		}

		public ImageView getPictureFlag3()
		{
			if (userViewGroup3 != null)
			{
				pictureFlag3 = (ImageView) userViewGroup3.findViewWithTag(R.id.picture_flag);
			}
			return pictureFlag3;
		}

		

		
	}


	
}
