package com.orange.game.draw.mission.friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orange.common.android.utils.JsonUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.constants.LanguageType;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;
import com.orange.game.draw.network.NetworkRequestPBCallbackInterface;
import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;

public class FriendMission extends CommonMission {

	protected static final String TAG = "FriendMission";
	
	// thread-safe singleton implementation
	private static FriendMission sharedInstance = new FriendMission();
	private FriendMission() {
	}
	public static FriendMission getInstance() {
		return sharedInstance;
	}
	
	final FriendManager myFollowFriendManager = new FriendManager(Friend.FRIEND_FOLLOW);
	final FriendManager myFanFriendManager = new FriendManager(Friend.FRIEND_FAN);
	
	public FriendManager getMyFollowFriendManager(){
		return myFollowFriendManager;
	}

	public FriendManager getMyFanFriendManager(){
		return myFanFriendManager;
	}

	private void getFriendList(final String userId, final FriendManager friendManager, final int offset, final int limit, final MissionCompleteInterface completeHandler) {
		if (userId == null){
			Log.i(TAG, "<getFriendList> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		if (friendManager == null){
			Log.i(TAG, "<getFriendList> but feed manager is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_PARAMETER);
			return;
		}

		// TODO load from user settings
		DrawNetworkRequest.getFriendList(configManager.getUserApiServerURL(),
				userId, friendManager.getFriendType(), offset, limit, 
				new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
						
						
						
						try {
							JSONArray friendList = JsonUtil.getJSONArray(jsonData, ServiceConstant.PARA_USERS);
							if (friendList == null || friendList.length() == 0){
								callCompleteHandler(completeHandler, 0);
								return;
							}
							
							// update model
							int count = friendList.length();
							for (int i=0; i<count; i++){
								JSONObject friendObject = friendList.getJSONObject(i);
								Friend friend = Friend.parseFriend(friendObject);
								friendManager.addFriend(friend);
							}

							// notify UI to update
							callCompleteHandler(completeHandler, 0);
							
						} catch (JSONException e) {
							Log.e(TAG, "<getFriendList> catch JSON exception="+e.toString(), e);
							callCompleteHandler(completeHandler, 0);
						}
						
						
						// TODO Auto-generated method stub
//						Log.i(TAG, "<getFriendList> total "+response.getFeedCount()+ " friends got");

						
						
						
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						// TODO Auto-generated method stub
						callCompleteHandler(completeHandler, errorCode);
						
					}
				});
		
	}
	
	public void getMyFollowFriendList(final int offset, final int limit, final MissionCompleteInterface completeHandler) {
		String userId = userManager.getUserId();				
		userId = userManager.getTestUserId();		// TODO for test
		getFriendList(userId, myFollowFriendManager, offset, limit, completeHandler);		
	}
	
	public void getMyFanFriendList(final int offset, final int limit, final MissionCompleteInterface completeHandler) {
		String userId = userManager.getUserId();				
		userId = userManager.getTestUserId();		// TODO for test
		getFriendList(userId, myFanFriendManager, offset, limit, completeHandler);		
	}

	public void searchUser(final FriendManager friendManager, final String keyword, final int offset, final int limit, final MissionCompleteInterface completeHandler) {
		String userId = userManager.getUser().getUserId();				
				
		DrawNetworkRequest.searchUser(configManager.getUserApiServerURL(),
				userId, keyword, offset, limit, 
				new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
																		
						try {
							JSONArray friendList = JsonUtil.getJSONArray(jsonData, ServiceConstant.PARA_USERS);
							if (friendList == null || friendList.length() == 0){
								callCompleteHandler(completeHandler, 0);
								return;
							}
																				
							// update model
							int count = friendList.length();
							for (int i=0; i<count; i++){
								JSONObject friendObject = friendList.getJSONObject(i);
								Friend friend = Friend.parseFriend(friendObject);
								friendManager.addFriend(friend);
							}

							// notify UI to update
							callCompleteHandler(completeHandler, 0);
							
						} catch (JSONException e) {
							Log.e(TAG, "<getFriendList> catch JSON exception="+e.toString(), e);
							callCompleteHandler(completeHandler, 0);
						}						
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);						
					}
				});
	}

	
	public void getUserRelationCount(final MissionCompleteInterface completeHandler) {
		String userId = userManager.getUser().getUserId();				
				
		DrawNetworkRequest.getUserRelationCount(configManager.getUserApiServerURL(),
				userId, 
				new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
																		
						try {
							int fanCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_RELATION_FAN_COUNT);
							int followCount = JsonUtil.getInt(jsonData, ServiceConstant.PARA_RELATION_FOLLOW_COUNT);
							
							Log.i(TAG, "<getUserRelationCount> fanCount="+fanCount+", followCount="+followCount);

							userManager.setFanCount(fanCount);
							userManager.setFollowCount(followCount);

							// notify UI to update
							callCompleteHandler(completeHandler, 0);
							
						} catch (Exception e) {
							Log.e(TAG, "<getUserRelationCount> catch exception="+e.toString(), e);
							callCompleteHandler(completeHandler, 0);
						}						
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);						
					}
				});
	}
	
	
	public void followUser(String targetUserId,final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();
		DrawNetworkRequest.followUser(configManager.getUserApiServerURL(), userId, targetUserId, new NetworkRequestJSONCallbackInterface()
		{
			
			@Override
			public void handleSuccessResponse(JSONObject jsonData)
			{
				try {
					JSONArray friendList = JsonUtil.getJSONArray(jsonData, ServiceConstant.PARA_USERS);
					if (friendList == null || friendList.length() == 0){
						callCompleteHandler(completeHandler, 0);
						return;
					}									
					// update model
					JSONObject friendObject = friendList.getJSONObject(0);
					Friend friend = Friend.parseFriend(friendObject);
					myFollowFriendManager.addFriendToHead(friend);
					int followCount = userManager.getFollowCount()+1;
					userManager.setFollowCount(followCount);
					// notify UI to update
					callCompleteHandler(completeHandler, 0);
				}catch (Exception e) {
						Log.e(TAG, "<followUser> catch JSON exception="+e.toString(), e);
						callCompleteHandler(completeHandler, 0);
					}
			}
			
			@Override
			public void handleFailureResponse(int errorCode)
			{
				callCompleteHandler(completeHandler, errorCode);
				
			}
		});
	}
	
	
	public  void  unFollowUser(String targetUserId,final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();
		DrawNetworkRequest.unFollowUser(configManager.getUserApiServerURL(), userId, targetUserId, new NetworkRequestJSONCallbackInterface()
		{
			
			@Override
			public void handleSuccessResponse(JSONObject jsonData)
			{
				try {
					JSONArray friendList = JsonUtil.getJSONArray(jsonData, ServiceConstant.PARA_USERS);
					if (friendList == null || friendList.length() == 0){
						callCompleteHandler(completeHandler, 0);
						return;
					}									
					// update model
					JSONObject friendObject = friendList.getJSONObject(0);
					Friend friend = Friend.parseFriend(friendObject);
					myFollowFriendManager.remove(friend);
					int followCount = userManager.getFollowCount()-1;
					userManager.setFollowCount(followCount);
					// notify UI to update
					callCompleteHandler(completeHandler, 0);
				}catch (Exception e) {
						Log.e(TAG, "<followUser> catch JSON exception="+e.toString(), e);
						callCompleteHandler(completeHandler, 0);
					}
				
			}
			
			@Override
			public void handleFailureResponse(int errorCode)
			{
				callCompleteHandler(completeHandler, errorCode);
			}
		});
	}
	
}
