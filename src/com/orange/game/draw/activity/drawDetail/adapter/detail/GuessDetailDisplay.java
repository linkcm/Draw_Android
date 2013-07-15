/**  
        * @title GuessDetailDisplay.java  
        * @package com.orange.game.draw.activity.adapter.draw.detail  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-22 下午5:51:59  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail.adapter.detail;

import android.R.integer;
import android.content.Context;
import android.util.Log;

import com.orange.game.R;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-22 下午5:51:59  
 */

public class GuessDetailDisplay implements DetailDisplayStrategyInterface
{

	@Override
	public boolean isShowActionImage(PBFeed feed)
	{
		return false;
	}

	@Override
	public int actionImageURL()
	{
		return 0;
	}

	@Override
	public String getCommentText(Context context, PBFeed feed)
	{
		StringBuffer comment = new StringBuffer();
		if (feed.getIsCorrect())
		{
			comment.append(context.getString(R.string.guess_correct));
		}else {
			comment.append(context.getString(R.string.guessed));
			int i = 0;
			for (String word:feed.getGuessWordsList())
			{
				if (i>0)
				{
					comment.append("、"+word);
				}
				else{
					comment.append(word);
				}
				i++;
				if (i == 3){
					comment.append("...");
					break;
				}
					
			}	
		}
		return comment.toString();
	}

	@Override
	public FeedManager getFeedManager()
	{
		return new FeedManager(FeedActionType.Guess);
	}

}
