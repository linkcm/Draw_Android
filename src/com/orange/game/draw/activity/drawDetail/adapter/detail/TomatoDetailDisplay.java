/**  
        * @title TomatoDisplay.java  
        * @package com.orange.game.draw.activity.adapter.draw.detail  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-22 下午6:31:20  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail.adapter.detail;

import android.content.Context;

import com.orange.game.R;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-22 下午6:31:20  
 */

public class TomatoDetailDisplay implements DetailDisplayStrategyInterface
{

	@Override
	public boolean isShowActionImage(PBFeed feed)
	{
		return true;
	}

	@Override
	public int actionImageURL()
	{
		return R.drawable.tomato_result;
	}

	@Override
	public String getCommentText(Context context, PBFeed feed)
	{
		return context.getString(R.string.give_tomato);
	}

	@Override
	public FeedManager getFeedManager()
	{
		return new FeedManager(FeedActionType.Tomato);
	}

}
