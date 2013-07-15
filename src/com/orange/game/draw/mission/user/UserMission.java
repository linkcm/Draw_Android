package com.orange.game.draw.mission.user;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.orange.common.android.smart.model.ConfigManager;
import com.orange.common.android.utils.JsonUtil;
import com.orange.common.android.utils.StringUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.activity.user.SNSConstants;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.user.AccountManager;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.UserConstants;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;
import com.orange.game.draw.network.NetworkRequestPBCallbackInterface;
import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameCurrency;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;
import com.orange.network.game.protocol.model.GameBasicProtos.PBUserItem;

public class UserMission extends CommonMission {

	protected static final String TAG = "UserMission";
	
	// thread-safe singleton implementation
	private static UserMission sharedInstance = new UserMission();
	private UserMission() {

	}
	public static UserMission getInstance() {
		return sharedInstance;
	}
	
	
	//private PBGameUser targetUser ;
	
	public void deviceLogin(final MissionCompleteInterface completeHandler){
		
		String appId = configManager.getAppId();
		String gameId = configManager.getGameId();
		String deviceId = "pipi_android_emulator"; // TODO change to android device Id
		
		DrawNetworkRequest.deviceLogin(configManager.getUserApiServerURL(), 
				appId, gameId, deviceId, new NetworkRequestJSONCallbackInterface(){

			@Override
			public void handleSuccessResponse(JSONObject jsonData) {
				
				if (jsonData == null){
					Log.e(TAG, "<deviceLogin> but no data return");
					completeHandler.onComplete(ErrorCode.ERROR_SYSTEM);
					return;
				}
				
				// read user data and save locally
				PBGameUser.Builder userBuilder = PBGameUser.newBuilder();
				userBuilder.setUserId(JsonUtil.getString(jsonData, ServiceConstant.PARA_USERID));
				userBuilder.setNickName(JsonUtil.getString(jsonData, ServiceConstant.PARA_NICKNAME));
				userBuilder.setAvatar(JsonUtil.getString(jsonData, ServiceConstant.PARA_AVATAR));
				userBuilder.setEmail(JsonUtil.getString(jsonData, ServiceConstant.PARA_EMAIL));
				userBuilder.setPassword(JsonUtil.getString(jsonData, ServiceConstant.PARA_PASSWORD));
				userBuilder.setLocation(JsonUtil.getString(jsonData, ServiceConstant.PARA_LOCATION));
				userBuilder.setGender(JsonUtil.getInt(jsonData, ServiceConstant.PARA_GENDER) == UserConstants.MALE);
				userBuilder.setLevel(JsonUtil.getInt(jsonData, ServiceConstant.PARA_LEVEL));
				userBuilder.setExperience(JsonUtil.getLong(jsonData, ServiceConstant.PARA_EXP));
				
				// TODO QQ weibo info 
				String qqAccessToken = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_ACCESS_TOKEN);
				String qqOpenId = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_OPEN_ID);
				String qqRefreshToken = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_REFRESH_TOKEN);
				String qqNickName = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_NICKNAME);
				String qqUserId = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_ID);
				int qqExpireDate = JsonUtil.getInt(jsonData, ServiceConstant.PARA_QQ_EXPIRE_DATE);
				
				// TODO SINA weibo info
				String sinaAccessToken = JsonUtil.getString(jsonData, ServiceConstant.PARA_SINA_ACCESS_TOKEN);
				String sinaRefreshToken = JsonUtil.getString(jsonData, ServiceConstant.PARA_SINA_REFRESH_TOKEN);
				String sinaNickName = JsonUtil.getString(jsonData, ServiceConstant.PARA_SINA_NICKNAME);
				String sinaUserId = JsonUtil.getString(jsonData, ServiceConstant.PARA_SINA_ID);
				int sinaExpireDate = JsonUtil.getInt(jsonData, ServiceConstant.PARA_SINA_EXPIRE_DATE);

				// TODO Facebook info
				String facebookAccessToken = JsonUtil.getString(jsonData, ServiceConstant.PARA_FACEBOOK_ACCESS_TOKEN);
//				String facebookRefreshToken = JsonUtil.getString(jsonData, ServiceConstant.PARA_FACEBOOK_REFRESH_TOKEN);
//				String facebookNickName = JsonUtil.getString(jsonData, ServiceConstant.PARA_FACEBOOK_NICKNAME);
				String facebookUserId = JsonUtil.getString(jsonData, ServiceConstant.PARA_FACEBOOKID);
				int facebookExpireDate = JsonUtil.getInt(jsonData, ServiceConstant.PARA_FACEBOOK_EXPIRE_DATE);
				
				// balance info
				int balance = JsonUtil.getInt(jsonData, ServiceConstant.PARA_ACCOUNT_BALANCE);
				userBuilder.setCoinBalance(balance);
				
				// items info
				JSONArray itemArray = JsonUtil.getJSONArray(jsonData, ServiceConstant.PARA_ITEMS);
				if (itemArray != null){
					for (int i=0; i<itemArray.length(); i++){						
						JSONObject obj;
						try {
							obj = itemArray.getJSONObject(i);
						} catch (JSONException e) {
							continue;
						}
						
						if (obj == null){
							continue;
						}
						
						// add this item
						PBUserItem.Builder itemBuilder = PBUserItem.newBuilder();
						itemBuilder.setCount(JsonUtil.getInt(obj, ServiceConstant.PARA_ITEM_AMOUNT));
						itemBuilder.setItemId(JsonUtil.getInt(obj, ServiceConstant.PARA_ITEM_TYPE));
						userBuilder.addItems(itemBuilder.build());
					}
				}
				
				PBGameUser user = userBuilder.build();
				
				// save user data
				userManager.save(user);
				
				// notify UI
				completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
			}

			@Override
			public void handleFailureResponse(int errorCode) {
				if (errorCode == ErrorCode.ERROR_DEVICE_NOT_BIND){
					Log.i(TAG, "user not bind to any device");
				}
				
				completeHandler.onComplete(errorCode);
			}
			
		});
	}
	
	public void registerUser(final String email, final String password, final MissionCompleteInterface completeHandler){
		
		String appId = configManager.getAppId();
		String gameId = configManager.getGameId();
		String deviceId = "pipi_android_emulator"; // TODO change to android device Id
		
		DrawNetworkRequest.registerUserByEmail(configManager.getUserApiServerURL(), 
				appId, gameId, deviceId, email, password, new NetworkRequestJSONCallbackInterface(){

			@Override
			public void handleSuccessResponse(JSONObject jsonData) {
				String userId = JsonUtil.getString(jsonData, ServiceConstant.PARA_USERID);
				String nickName = UserManager.nickNameByEmail(email);
				userManager.setUserId(userId);
				userManager.setEmail(email);
				userManager.setNickName(nickName);
				userManager.setPassword(password);
				userManager.save();
				
				int coinBalance = JsonUtil.getInt(jsonData, ServiceConstant.PARA_ACCOUNT_BALANCE);
				AccountManager.getInstance().setBalance(PBGameCurrency.Coin, coinBalance);
				
				completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
			}

			@Override
			public void handleFailureResponse(int errorCode) {
				completeHandler.onComplete(errorCode);
			}
		});
	}
	
	public void registerUserWithUserInfo(HashMap<String, Object> userInfo, final MissionCompleteInterface completeHandler){
		
		final String appId = configManager.getAppId();
		final String gameId = configManager.getGameId();
		final String deviceId = "pipi_android_emulator"; // TODO change to android device Id
		final String snsId = (String)userInfo.get(SNSConstants.SNS_USER_ID);
		final int registerType = getRegisterType(userInfo);
		final String nickName = (String)userInfo.get(SNSConstants.SNS_NICK_NAME);
		final String avatarUrl = (String)userInfo.get(SNSConstants.SNS_USER_IMAGE_URL);
		final String accessToken = (String)userInfo.get(SNSConstants.SNS_OAUTH_TOKEN);
		final String accessTokenSecret = (String)userInfo.get(SNSConstants.SNS_OAUTH_TOKEN_SECRET);
		final String refreshToken = (String)userInfo.get(SNSConstants.SNS_REFRESH_TOKEN);
		final Integer expireDate = (Integer)userInfo.get(SNSConstants.SNS_EXPIRATION_DATE);
		final String qqOpenId = (String)userInfo.get(SNSConstants.SNS_QQ_OPEN_ID);
		final Integer province = new Integer((String)userInfo.get(SNSConstants.SNS_PROVINCE));
		final Integer city = new Integer((String)userInfo.get(SNSConstants.SNS_CITY));
		final String location = (String)userInfo.get(SNSConstants.SNS_LOCATION);
		final String gender = (String)userInfo.get(SNSConstants.SNS_GENDER);
		final String birthday = (String)userInfo.get(SNSConstants.SNS_BIRTHDAY);
		final String domain = (String)userInfo.get(SNSConstants.SNS_DOMAIN);
		
		DrawNetworkRequest.registerUserBySNSUserInfo(configManager.getUserApiServerURL(), 
				appId, gameId, deviceId, snsId, registerType, 
				nickName, avatarUrl, accessToken, accessTokenSecret, refreshToken, 
				expireDate.intValue(), qqOpenId, province.intValue(), city.intValue(), location,
				gender, birthday, domain, new NetworkRequestJSONCallbackInterface(){
			
			@Override
			public void handleSuccessResponse(JSONObject jsonData) {
                // save user data locally
				String userId = JsonUtil.getString(jsonData, ServiceConstant.PARA_USERID);
				
				if (registerType == ServiceConstant.REGISTER_TYPE_SINA) {
					userManager.setSinaSNSUser(userId, snsId, null, nickName, gender,
							avatarUrl, accessToken, accessTokenSecret);
				}else if (registerType == ServiceConstant.REGISTER_TYPE_QQ) {
					userManager.setQQSNSUser(userId, snsId, null, nickName, gender,
							avatarUrl, accessToken, accessTokenSecret);
				}else if (registerType == ServiceConstant.REGISTER_TYPE_FACEBOOK) {
					userManager.setFacebookSNSUser(userId, snsId, null, nickName, gender,
							avatarUrl, null, null);
				}
				
				userManager.setLocation(location);
				userManager.save();
				
				int coinBalance = JsonUtil.getInt(jsonData, ServiceConstant.PARA_ACCOUNT_BALANCE);
				AccountManager.getInstance().setBalance(PBGameCurrency.Coin, coinBalance); 
				
				completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
			}
			
			@Override
			public void handleFailureResponse(int errorCode) {		
				completeHandler.onComplete(errorCode);
			}
		});
	}
	
	public void updateUser(String nickName, String localAvatarPath, Boolean gender, String password, 
			String email, final MissionCompleteInterface completeHandler) {

		if (userManager.getUser() == null) {
			return;
		}
		
		userManager.setNickName(nickName);
		userManager.setGender(gender);
		userManager.setPassword(password);
		userManager.setLocalAvatar(localAvatarPath);
		userManager.setEmail(email);
		userManager.save();
		
		final String appId = configManager.getAppId();
		final String userId = userManager.getUserId();
		final String deviceId = "pipi_android_emulator"; // TODO change to android device Id
		
		DrawNetworkRequest.updateUser(configManager.getUserApiServerURL(), appId, 
				deviceId, null, userId, password, nickName, GenderUtils.toString(gender),
				email, localAvatarPath, new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
						String avatarUrl = JsonUtil.getString(jsonData, ServiceConstant.PARA_AVATAR);
						userManager.setAvatar(avatarUrl);
						userManager.save();
						
						completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						completeHandler.onComplete(errorCode);
					}
				});
	}

	
	public void getUserByUserId(String targetUserId,final MissionCompleteInterface completeHandler){
		String appId = configManager.getAppId();
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		DrawNetworkRequest.getUserByUserId(configManager.getUserApiServerURL(), appId, userId, targetUserId, new NetworkRequestPBCallbackInterface()
		{
			
			@Override
			public void handleSuccessResponse(DataQueryResponse response)
			{
				// TODO Auto-generated method stub
				//targetUser = response.getUser();
				
				userManager.setTempGameUser(response.getUser());
				userManager.setTempUserRelationShip(response.getUserRelation());
				completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
			}
			
			@Override
			public void handleFailureResponse(int errorCode)
			{
				completeHandler.onComplete(errorCode);
			}
		});
	}

	private int getRegisterType(HashMap<String, Object> userInfo) {
		String networkName = (String) userInfo.get(SNSConstants.SNS_NETWORK);
		
		if (networkName.equals(SNSConstants.SNS_SINA_WEIBO)) {
			return ServiceConstant.REGISTER_TYPE_SINA;
		}else if (networkName.equals(SNSConstants.SNS_QQ_WEIBO)) {
			return ServiceConstant.REGISTER_TYPE_QQ;
		}else if (networkName.equals(SNSConstants.SNS_FACEBOOK)) {
			return ServiceConstant.REGISTER_TYPE_FACEBOOK;
		}else if (networkName.equals(SNSConstants.SNS_TWITTER)){
			return ServiceConstant.REGISTER_TYPE_TWITTER;
		}else {
		    Log.e(TAG, "<getRegisterType> cannot find SNS type for network name = "+networkName);
			return -1;
		}		
	}
}
