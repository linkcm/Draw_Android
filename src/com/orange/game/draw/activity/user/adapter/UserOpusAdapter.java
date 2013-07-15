/**  
        * @title UserOpusAdapter.java  
        * @package com.orange.game.draw.activity.user.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-17 下午3:45:41  
        * @version V1.0  
 */
package com.orange.game.draw.activity.user.adapter;

import java.util.List;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orange.common.android.gallery.CoverFlowAdapter;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-17 下午3:45:41  
 */

public class UserOpusAdapter extends CoverFlowAdapter
{

	private List<PBFeed> feeds;
	//private ImageLoader imageLoader;
	
	
	/**  
	* Constructor Method   
	* @param feeds  
	*/
	public UserOpusAdapter(List<PBFeed> feeds)
	{
		super();
		this.feeds = feeds;
		//this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (feeds == null)
		return 0;
		return feeds.size();
	}

	//private Bitmap bitmap;
	
	/*@Override
	public String getBitmap(int position)
	{
		PBFeed feed= feeds.get(position);
		String imageUrl = feed.getOpusImage();
		imageLoader.loadImage(imageUrl, new ImageLoadingListener()
		{
			
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{
				// TODO Auto-generated method stub
				
			}
		});
		return null;
	}
*/
	public void setFeeds(List<PBFeed> feeds)
	{
		this.feeds = feeds;
	}

	@Override
	public String getImageURL(int position)
	{
		// TODO Auto-generated method stub
		return feeds.get(position).getOpusImage();
	}

}
