/**  
        * @title TopPictureAdapter.java  
        * @package com.orange.game.draw.activity.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2012-12-25 下午2:44:14  
        * @version V1.0  
 */
package com.orange.game.draw.activity.home.adapter;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2012-12-25 下午2:44:14  
 */



public class CommonViewPagerAdapter extends PagerAdapter
{
  private ArrayList<View> topPictures;
	
	

public CommonViewPagerAdapter(ArrayList<View> topPictures)
{
	super();
	this.topPictures = topPictures;
}

	@Override
	public int getCount()
	{
		if (topPictures == null)
		{
			return 0;
		}
		return topPictures.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		// TODO Auto-generated method stub
		((ViewPager) container).removeView(topPictures.get(position));
	}

	@Override
	public int getItemPosition(Object object)
	{
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		// TODO Auto-generated method stub
		((ViewPager) container).addView(topPictures.get(position));
		return topPictures.get(position);
	}
	
	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<View> getTopPictures()
	{
		return topPictures;
	}

	public void setTopPictures(ArrayList<View> topPictures)
	{
		this.topPictures = topPictures;
	}
}
