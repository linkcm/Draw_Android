/**  
        * @title FeedUtil.java  
        * @package com.orange.game.draw.model.feed  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-22 下午4:09:06  
        * @version V1.0  
 */
package com.orange.game.draw.model.feed;

import java.util.List;

import com.orange.network.game.protocol.model.DrawProtos.PBFeedTimes;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-22 下午4:09:06  
 */

public class FeedUtil
{
	public static int getFeedTimesByType(List<PBFeedTimes>  feedTimesList, FeedConstants feedConstants){
		for (PBFeedTimes times : feedTimesList)
		{
			if (times.getType() == feedConstants.intValue())
			{
				return times.getValue();
			}
		}
		return 0;
	}
}
