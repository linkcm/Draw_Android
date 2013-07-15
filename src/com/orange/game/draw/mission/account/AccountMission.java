/**  
        * @title AccountMission.java  
        * @package com.orange.game.draw.mission  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-15 下午3:44:20  
        * @version V1.0  
 */
package com.orange.game.draw.mission.account;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.orange.common.android.utils.SystemUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-15 下午3:44:20  
 */

public class AccountMission extends CommonMission
{
	protected static final String TAG = "AccountMission";
	
	// thread-safe singleton implementation
	private static AccountMission sharedInstance = new AccountMission();
	private AccountMission() {

	}
	public static AccountMission getInstance() {
		return sharedInstance;
	}
	
	
	
	enum BlackType{
		
		BLACK_USER(0),
		BLACK_DEVICE(1);
		
		final int value;
		BlackType(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
		
	}
	
	enum BlackActionType{
		
		BLACK(0),
		UN_BLACK(1);
		
		final int value;
		BlackActionType(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
		
	}
	
	
	public void chargeGold(
			String userId,
			String amount,
			String sourceAmount,
			final MissionCompleteInterface completeHandler){
		String adminUserId = userManager.getUserId();
		//for test 
		adminUserId = userManager.getTestUserId();
		DrawNetworkRequest.chargeGold(
				configManager.getUserApiServerURL(), 
				userId, 
				adminUserId, 
				amount, 
				sourceAmount, 
				new NetworkRequestJSONCallbackInterface()
				{
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData)
					{
						completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
						
					}
					
					@Override
					public void handleFailureResponse(int errorCode)
					{
						completeHandler.onComplete(errorCode);
						
					}
				});
	}
	
	
	
	public void chargeIngot(
			String userId,
			String amount,
			String sourceAmount,
			final MissionCompleteInterface completeHandler){
		    String adminUserId = userManager.getUserId();
		//for test 
		adminUserId = userManager.getTestUserId();
		DrawNetworkRequest.chargeIngot(
				configManager.getUserApiServerURL(), 
				userId, 
				adminUserId, 
				amount, 
				sourceAmount, 
				new NetworkRequestJSONCallbackInterface()
				{
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData)
					{
						completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
						
					}
					
					@Override
					public void handleFailureResponse(int errorCode)
					{
						completeHandler.onComplete(errorCode);
						
					}
				});
	}
	
	private void blackUser(
			String targetUserId,
			String deviceId,
			BlackType blackType,
			BlackActionType actionType,
			final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test 
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.blackUser(
				configManager.getUserApiServerURL(), 
				userId, 
				targetUserId, 
				configManager.getAppId(), 
				deviceId, 
				blackType.value, 
				actionType.value, 
				new NetworkRequestJSONCallbackInterface()
				{
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData)
					{
						completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
						
					}
					
					@Override
					public void handleFailureResponse(int errorCode)
					{
						completeHandler.onComplete(errorCode);
					}
				}); 
	}
	
	
	
	private void blackFriend(String targetUserId,BlackActionType blackType,final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.blackkFriend(
				configManager.getUserApiServerURL(), 
				configManager.getAppId(), 
				userId, 
				targetUserId, 
				String.valueOf(blackType.value),
				new NetworkRequestJSONCallbackInterface()
				{
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData)
					{
						completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
					}
					
					@Override
					public void handleFailureResponse(int errorCode)
					{
						completeHandler.onComplete(errorCode);
					}
				});
	}
	
	
	
	public void blackUser(String targetUserId,MissionCompleteInterface completeHandler){
		blackUser(targetUserId, "",BlackType.BLACK_USER, BlackActionType.BLACK, completeHandler);
	}
	
	
	public void blackDevice(Context context,MissionCompleteInterface completeHandler){
		String deviceId = SystemUtil.getDeviceId(context);
		blackUser("", deviceId, BlackType.BLACK_DEVICE, BlackActionType.BLACK, completeHandler);
	}
	
	
	public void unBlackUser(String targetUserId,MissionCompleteInterface completeHandler){
		blackUser(targetUserId, "",BlackType.BLACK_USER, BlackActionType.UN_BLACK, completeHandler);
	}
	
	
	public void unBlackDevice(Context context,MissionCompleteInterface completeHandler){
		String deviceId = SystemUtil.getDeviceId(context);
		blackUser("", deviceId, BlackType.BLACK_DEVICE, BlackActionType.UN_BLACK, completeHandler);
	}
	
	public void blackFriend(String targetUserId,MissionCompleteInterface completeHandler){
		blackFriend(targetUserId, BlackActionType.BLACK,completeHandler);
	}
	
	public void unBlackFriend(String targetUserId,MissionCompleteInterface completeHandler){
		blackFriend(targetUserId, BlackActionType.UN_BLACK,completeHandler);
	}
}


