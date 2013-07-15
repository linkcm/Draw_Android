/**  
        * @title StatisticsMission.java  
        * @package com.orange.game.draw.mission.statistics  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-10 下午4:01:49  
        * @version V1.0  
 */
package com.orange.game.draw.mission.statistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orange.common.android.utils.JsonUtil;
import com.orange.game.application.DrawApplication;
import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-10 下午4:01:49  
 */

public class StatisticsMission extends CommonMission
{
	protected static final String TAG = "StatisticsMission";
	
	// thread-safe singleton implementation
	private static StatisticsMission sharedInstance = new StatisticsMission();
	private StatisticsMission() {
	}
	public static StatisticsMission getInstance() {
		return sharedInstance;
	}
	
	private int feedCount = 0;
	private int messageCount = 0;
	private int fanCount = 0;
	private int roomCount = 0;
	private int commentCount = 0;
	private int drawToMeCount = 0;
	private int bbsCommentCount = 0;
	
	
	
	public void getStatistics(final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test 
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.getStatistics(
								configManager.getTrafficApiServerURL(), 
								userId, 
								new NetworkRequestJSONCallbackInterface()
								{
									
									@Override
									public void handleSuccessResponse(JSONObject jsonData)
									{
										feedCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_FEED_COUNT);
										messageCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_MESSAGE_COUNT);
										fanCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_FAN_COUNT);
										roomCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_ROOM_COUNT);
										commentCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_COMMENT_COUNT);
										drawToMeCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_DRAWTOME_COUNT);
										bbsCommentCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_BBS_ACTION_COUNT);
										
										Log.d(TAG, "<getStatistics> feed count = "+feedCount);
										completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
									}
									
									@Override
									public void handleFailureResponse(int errorCode)
									{
										completeHandler.onComplete(errorCode);
									}
								});
		
		
	}
	public int getFeedCount()
	{
		return feedCount;
	}
	public int getMessageCount()
	{
		return messageCount;
	}
	public int getFanCount()
	{
		return fanCount;
	}
	public int getRoomCount()
	{
		return roomCount;
	}
	public int getCommentCount()
	{
		return commentCount;
	}
	public int getDrawToMeCount()
	{
		return drawToMeCount;
	}
	public int getBbsCommentCount()
	{
		return bbsCommentCount;
	}
	
	
	
}
