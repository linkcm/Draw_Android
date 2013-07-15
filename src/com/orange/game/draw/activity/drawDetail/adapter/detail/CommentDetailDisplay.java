/**  
        * @title CommentDetailDisplay.java  
        * @package com.orange.game.draw.activity.adapter.draw.detail  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-22 下午5:36:32  
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
 * @update 2013-1-22 下午5:36:32  
 */

public class CommentDetailDisplay implements DetailDisplayStrategyInterface
{

	@Override
	public boolean isShowActionImage(PBFeed feed)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int actionImageURL()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommentText(Context context, PBFeed feed)
	{
		String comment = "";
		if (feed.hasCommentInfo())
		{
			 comment = context.getText(R.string.comment_reply)+feed.getCommentInfo().getActionNickName()+":"+feed.getComment();
		}else {
			 comment = feed.getComment();
		}
		return comment;
	}

	@Override
	public FeedManager getFeedManager()
	{
		return new FeedManager(FeedActionType.Comment);
	}

}
