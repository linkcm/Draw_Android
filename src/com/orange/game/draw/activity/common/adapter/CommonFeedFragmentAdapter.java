/**  
        * @title CommonFragmentAdapter.java  
        * @package com.orange.game.draw.activity.adapter.common  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-10 下午1:33:17  
        * @version V1.0  
 */
package com.orange.game.draw.activity.common.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.model.feed.FeedConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.orange.network.game.protocol.model.DrawProtos.PBFeedTimes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-10 下午1:33:17  
 */

public abstract class CommonFeedFragmentAdapter extends CommonBaseAdapter
{
	private static final String TAG = "PhotoGalleryAdapter";
	protected List<PBFeed> feeds;
	protected Context context;
	protected ImageLoader imageLoader;

	abstract public int getAdapterCount();
	abstract protected View initConverView(View convertView,int position);
	
	public CommonFeedFragmentAdapter(List<PBFeed> feeds, Context context)
	{
		super();
		this.feeds = feeds;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		return getAdapterCount();
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
		return initConverView(convertView, position);
	}
	public void setFeeds(List<PBFeed> feeds)
	{
		this.feeds = feeds;
	}


	
	public void createIntent(final String feedId,View view)
	{
		view.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				DrawDetailActivity.newInstance(context, feedId);
			}
		});
	}
}
