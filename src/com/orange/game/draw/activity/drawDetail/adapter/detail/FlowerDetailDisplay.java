/**  
        * @title FlowerDetailDisplay.java  
        * @package com.orange.game.draw.activity.adapter.draw.detail  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-22 下午6:21:04  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail.adapter.detail;

import android.R.raw;
import android.content.Context;

import com.orange.game.R;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-22 下午6:21:04  
 */

public class FlowerDetailDisplay implements DetailDisplayStrategyInterface
{

	@Override
	public boolean isShowActionImage(PBFeed feed)
	{
		return true;
	}

	@Override
	public int actionImageURL()
	{
		return R.drawable.flower_result;
	}

	@Override
	public String getCommentText(Context context, PBFeed feed)
	{
		return context.getString(R.string.give_flower);
	}

	@Override
	public FeedManager getFeedManager()
	{
		return new FeedManager(FeedActionType.Flower);
	}

}
