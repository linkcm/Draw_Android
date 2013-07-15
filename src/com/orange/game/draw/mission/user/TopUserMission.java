package com.orange.game.draw.mission.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orange.common.android.utils.JsonUtil;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.TopUserManager;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;
import com.orange.network.game.protocol.model.DiceProtos.PBUserDice;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

public class TopUserMission extends CommonMission {

	
	TopUserManager topLevelUserManager = new TopUserManager();
	
	protected static final String TAG = "TopUserMission";
	
	// thread-safe singleton implementation
	private static TopUserMission sharedInstance = new TopUserMission();
	private TopUserMission() {

	}
	public static TopUserMission getInstance() {
		return sharedInstance;
	}
	
	public void getTopLevelUsers(final int offset, final int limit, final MissionCompleteInterface completeHandler) {
		String userId = userManager.getUser().getUserId();				
				
		DrawNetworkRequest.getTopLevelUsers(configManager.getUserApiServerURL(),
				userId, offset, limit, 
				new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
																		
						try {
							JSONArray userList = JsonUtil.getJSONArray(jsonData, ServiceConstant.RET_DATA);
							if (userList == null || userList.length() == 0){
								callCompleteHandler(completeHandler, 0);
								return;
							}
																				
							// update model
							int count = userList.length();
							for (int i=0; i<count; i++){
								JSONObject userObject = userList.getJSONObject(i);
								PBGameUser pbUser = parseTopUser(userObject);
								topLevelUserManager.addUser(pbUser);
							}
							Log.i(TAG, "<getTopLevelUsers> total "+count+" user returned.");

							// notify UI to update
							callCompleteHandler(completeHandler, 0);
							
						} catch (JSONException e) {
							Log.e(TAG, "<getTopLevelUsers> catch JSON exception="+e.toString(), e);
							callCompleteHandler(completeHandler, 0);
						}						
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);						
					}
				});
	}

	private PBGameUser parseTopUser(JSONObject jsonData) {
		
		PBGameUser.Builder builder = PBGameUser.newBuilder();
		
		builder.setUserId(JsonUtil.getString(jsonData, ServiceConstant.PARA_USERID, ""));
		builder.setNickName(JsonUtil.getString(jsonData, ServiceConstant.PARA_NICKNAME, ""));
		builder.setAvatar(JsonUtil.getString(jsonData, ServiceConstant.PARA_AVATAR, ""));
		builder.setGender(GenderUtils.boolFromString(JsonUtil.getString(jsonData, ServiceConstant.PARA_GENDER)));

		// set level
		int level = JsonUtil.getInt(jsonData, ServiceConstant.PARA_LEVEL);
		if (level <= 0)
			level = 1;
		builder.setLevel(level);
		
		// set opus count, not used yet?
		int opusCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_OPUS_COUNT);	
//		Log.i(TAG, "<getTopLevelUsers> total "+count+" user returned.");
		
		return builder.build();
	}
	public TopUserManager getTopLevelUserManager()
	{
		return topLevelUserManager;
	}
	
	
}
