/**  
        * @title DetailDisplayStrategyInterface.java  
        * @package com.orange.game.draw.activity.adapter.draw.detail  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-22 下午5:34:19  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail.adapter.detail;

import android.content.Context;

import com.orange.game.draw.model.feed.FeedManager;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-22 下午5:34:19  
 */

public interface DetailDisplayStrategyInterface
{
	public boolean isShowActionImage(PBFeed feed);
	public int  actionImageURL();
	public String  getCommentText(Context context,PBFeed feed);
	public FeedManager getFeedManager();
}
