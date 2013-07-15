/**  
        * @title SupportMission.java  
        * @package com.orange.game.draw.mission.support  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-22 上午10:23:14  
        * @version V1.0  
 */
package com.orange.game.draw.mission.support;

import org.json.JSONObject;

import android.util.Log;

import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.support.FeedbackType;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-22 上午10:23:14  
 */

public class SupportMission extends CommonMission
{
	protected static final String TAG = null;
	// thread-safe singleton implementation
		private static SupportMission sharedInstance = new SupportMission();
		private SupportMission() {

		}
		public static SupportMission getInstance() {
			return sharedInstance;
		}
		
		
		public void feedBack(String feedback,String contact,FeedbackType type,final MissionCompleteInterface completeHandler){
			String userId = UserManager.getInstance().getUserId();
			//for test
			userId = userManager.getTestUserId();
			DrawNetworkRequest.feedback(configManager.getUserApiServerURL(), userId, feedback, contact, type, new NetworkRequestJSONCallbackInterface()
			{
				
				@Override
				public void handleSuccessResponse(JSONObject jsonData)
				{
					
					/*if (jsonData == null)
					    return ;
					Log.d(TAG, "feedback  json result = " +jsonData);*/
					completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
				}
				
				@Override
				public void handleFailureResponse(int errorCode)
				{
					completeHandler.onComplete(errorCode);
				}
			});
			
		}
		
		
		public void submitNewWord(String wordString,final MissionCompleteInterface completeHandler){
			String appId = configManager.getAppId();
			String userId = userManager.getUserId();
			//for test
			userId = userManager.getTestUserId();
			
			DrawNetworkRequest.submitNewWord(configManager.getUserApiServerURL(), appId, userId, wordString, new NetworkRequestJSONCallbackInterface()
			{
				
				@Override
				public void handleSuccessResponse(JSONObject jsonData)
				{
					// TODO Auto-generated method stub
					//Log.d(TAG, "<submitNewWord> result code = "+jsonData);
					completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
				}
				
				@Override
				public void handleFailureResponse(int errorCode)
				{
					completeHandler.onComplete(errorCode);
					
				}
			});
			
		}
		
		
}
