package com.orange.game.draw.network;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.sql.Date;
import java.util.HashMap;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.drawable;
import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;
import com.orange.common.android.network.http.CustomBinaryHttpResponseHandler;
import com.orange.common.android.utils.FileUtil;
import com.orange.common.android.utils.JsonUtil;
import com.orange.common.android.utils.StringUtil;
import com.orange.game.application.DrawApplication;
import com.orange.game.constants.DBConstants;
import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.activity.user.SNSConstants;
import com.orange.game.draw.constants.LanguageType;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.message.PPMessage;
import com.orange.game.draw.model.support.FeedbackType;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;
import com.tencent.weibo.api.UserAPI;

public class DrawNetworkRequest {

	
	protected static final String TAG = "DrawNetworkRequest";
	static AsyncHttpClient client = new AsyncHttpClient();
	static Context context = DrawApplication.getInstance();
	
	protected static void httpGetWithPBResponse(final String url,
			final RequestParams para,
			final NetworkRequestPBCallbackInterface callbackHandler){
		
		//client.setTimeout(5000);
		
		para.put(DrawNetworkConstants.FORMAT, DrawNetworkConstants.FORMAT_PB);
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                		
		
		final String tag = "["+UUID.randomUUID().toString()+"]";
		Log.i(TAG, "[SEND]"+tag+" url="+url+", parameters="+para.toString());
		client.get(context,url, para, new CustomBinaryHttpResponseHandler(null) {
					@Override
					public void onSuccess(byte[] data) {
						if (data == null || data.length == 0){
							Log.i(TAG, "[RECV]"+tag+" url = "+url+", data is null or length = 0");
							callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NO_DATA);
							return;
						}
						
						Log.i(TAG, "[RECV]"+tag+" receive "+data.length+" bytes");						
						try {
							DataQueryResponse response = DataQueryResponse.parseFrom(data);
							if (response == null){
								Log.i(TAG, "[RECV]"+tag+" url = "+url+", data is null or length = 0");
								callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NO_DATA_AFTER_PARSING);
								return;
							}
							
							if (response.getResultCode() == ErrorCode.ERROR_SUCCESS){
								callbackHandler.handleSuccessResponse(response);
							}
							else{
								callbackHandler.handleFailureResponse(response.getResultCode());
							}
							
						} catch (InvalidProtocolBufferException e) {
							Log.e(TAG, "[RECV]"+tag+" url = "+url+", but catch exception="+e.toString(), e);
							callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_INVALID_PROTOCOL_BUFFER);
							return;
						}
						
					}

					@Override
					public void onFailure(Throwable e, byte[] data) {
						Log.e(TAG, "[RECV]"+tag+" url = "+url+", failure, e="+e.toString(), e);
						callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NETWORK);
					}
				});		
				
	}
	
	protected static void httpGetWithJSONResponse(final String url,
			final RequestParams para,
			final NetworkRequestJSONCallbackInterface callbackHandler){
		
		//client.setTimeout(5000);
		
		para.put(DrawNetworkConstants.FORMAT, DrawNetworkConstants.FORMAT_JSON);
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                		
        para.put(ServiceConstant.PARA_GAME_ID, ConfigManager.getInstance().getGameId());
		final String tag = "["+UUID.randomUUID().toString()+"]";
		Log.i(TAG, "[SEND]"+tag+" url="+url+", parameters="+para.toString());
		client.get(context,url, para, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {

				Log.i(TAG, "[RECV]"+tag+" success, response="+response);
				
				int resultCode = JsonUtil.getInt(response, ServiceConstant.RET_CODE);
				JSONObject obj = JsonUtil.get(response, ServiceConstant.RET_DATA);
				JSONArray array = null;
				if (obj == null){
					array = JsonUtil.getJSONArray(response, ServiceConstant.RET_DATA);
					if (array != null){
						obj = new JSONObject();
						try {
							obj.put(ServiceConstant.RET_DATA, array);
						} catch (JSONException e) {
							Log.w(TAG, "<httpGetWithJSONResponse> catch JSON exception="+e.toString(), e);
						}
					}
				}

				if (resultCode == 0){
					callbackHandler.handleSuccessResponse(obj);
				}
				else{
					callbackHandler.handleFailureResponse(resultCode);
				}
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				// TODO check error code in more details
				Log.e(TAG, "[RECV]"+tag+" failure, error="+e.toString(), e);
				callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NETWORK);
			}		    
		});		
				
	}	
	
	
	protected static void httpPostWithPBResponse(final String url,
			final RequestParams para,
			final NetworkRequestPBCallbackInterface callbackHandler){
		
		//client.setTimeout(5000);
		
		para.put(DrawNetworkConstants.FORMAT, DrawNetworkConstants.FORMAT_PB);
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                		
		
		final String tag = "["+UUID.randomUUID().toString()+"]";
		Log.i(TAG, "[SEND]"+tag+" url="+url+", parameters="+para.toString());
		String requestUrl = "";
		try
		{
			requestUrl = url+"?"+URLEncoder.encode(para.toString(),"UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		client.post(context,requestUrl, para, new CustomBinaryHttpResponseHandler(null) {
					@Override
					public void onSuccess(byte[] data) {
						if (data == null || data.length == 0){
							Log.i(TAG, "[RECV]"+tag+" url = "+url+", data is null or length = 0");
							callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NO_DATA);
							return;
						}
						
						Log.i(TAG, "[RECV]"+tag+" receive "+data.length+" bytes");						
						try {
							DataQueryResponse response = DataQueryResponse.parseFrom(data);
							if (response == null){
								Log.i(TAG, "[RECV]"+tag+" url = "+url+", data is null or length = 0");
								callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NO_DATA_AFTER_PARSING);
								return;
							}
							
							if (response.getResultCode() == ErrorCode.ERROR_SUCCESS){
								callbackHandler.handleSuccessResponse(response);
							}
							else{
								callbackHandler.handleFailureResponse(response.getResultCode());
							}
							
						} catch (InvalidProtocolBufferException e) {
							Log.e(TAG, "[RECV]"+tag+" url = "+url+", but catch exception="+e.toString(), e);
							callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_INVALID_PROTOCOL_BUFFER);
							return;
						}
						
					}

					@Override
					public void onFailure(Throwable e, byte[] data) {
						Log.e(TAG, "[RECV]"+tag+" url = "+url+", failure, e="+e.toString(), e);
						callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NETWORK);
					}
				});		
				
	}
	
	protected static void httpPostWithJSONResponse(final String url,
			final RequestParams para,
			final NetworkRequestJSONCallbackInterface callbackHandler){
		
		//client.setTimeout(5000);
		
		para.put(DrawNetworkConstants.FORMAT, DrawNetworkConstants.FORMAT_JSON);
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                		
        para.put(ServiceConstant.PARA_GAME_ID, ConfigManager.getInstance().getGameId());
		final String tag = "["+UUID.randomUUID().toString()+"]";
		String requestUrl = null;	
		requestUrl = url+"?"+para.toString();
		Log.i(TAG, "[SEND]"+tag+" url="+requestUrl);
		
		client.post(context,requestUrl, para, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {

				Log.i(TAG, "[RECV]"+tag+" success, response="+response);
				
				int resultCode = JsonUtil.getInt(response, ServiceConstant.RET_CODE);
				JSONObject obj = JsonUtil.get(response, ServiceConstant.RET_DATA);
				JSONArray array = null;
				if (obj == null){
					array = JsonUtil.getJSONArray(response, ServiceConstant.RET_DATA);
					if (array != null){
						obj = new JSONObject();
						try {
							obj.put(ServiceConstant.RET_DATA, array);
						} catch (JSONException e) {
							Log.w(TAG, "<httpGetWithJSONResponse> catch JSON exception="+e.toString(), e);
						}
					}
				}

				if (resultCode == 0){
					callbackHandler.handleSuccessResponse(obj);
				}
				else{
					callbackHandler.handleFailureResponse(resultCode);
				}
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				// TODO check error code in more details
				Log.e(TAG, "[RECV]"+tag+" failure, error="+e.toString(), e);
				callbackHandler.handleFailureResponse(DrawNetworkConstants.ERROR_NETWORK);
			}		    
		});		
				
	}	
	
	public static void getFeedList(final String baseURL, 
			final String userId,
			final FeedListType listType, 
			final  int offset, final int limit, 
			final LanguageType lang,
			final NetworkRequestPBCallbackInterface callbackHandler){
		
		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_FEED_LIST);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
		para.put(ServiceConstant.PARA_TYPE, String.valueOf(listType.intValue()));
        para.put(ServiceConstant.PARA_LANGUAGE, String.valueOf(lang.intValue()));        
        para.put(ServiceConstant.PARA_IMAGE, "1");
		
        httpGetWithPBResponse(url, para, callbackHandler);
	}

	public static void deviceLogin(
			final String baseURL, 
			final String appId,
			final String gameId,
			final String deviceId,
			final NetworkRequestJSONCallbackInterface callbackHandler) {

		String url = baseURL;
		
		RequestParams para = new RequestParams();
		
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_DEVICE_LOGIN);
		para.put(ServiceConstant.PARA_GAME_ID, gameId);
		para.put(ServiceConstant.PARA_DEVICEID, deviceId);
        para.put(ServiceConstant.PARA_APPID, appId);                
        
        httpGetWithJSONResponse(url, para, callbackHandler);
	}

	public static void registerUserByEmail(
			final String baseURL,
			final String appId,
			final String gameId,
			final String deviceId,
			final String email,
			final String password,
			final NetworkRequestJSONCallbackInterface callbackHandler) {

		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_REGISTER_USER);
		para.put(ServiceConstant.PARA_GAME_ID, gameId);
		para.put(ServiceConstant.PARA_DEVICEID, deviceId);
        para.put(ServiceConstant.PARA_APPID, appId);                

        para.put(ServiceConstant.PARA_EMAIL, email);                
        para.put(ServiceConstant.PARA_PASSWORD, password);                
        para.put(ServiceConstant.PARA_REGISTER_TYPE, String.valueOf(ServiceConstant.REGISTER_TYPE_EMAIL));                
        
        // TODO: add detail to below
        String countryCode = "";
        String languageCode = "";
        String deviceModel = "";
        String deviceOS = "";
                
        para.put(ServiceConstant.PARA_COUNTRYCODE, countryCode);                
        para.put(ServiceConstant.PARA_LANGUAGE, languageCode);                
        para.put(ServiceConstant.PARA_DEVICEMODEL, deviceModel);                
        para.put(ServiceConstant.PARA_DEVICEOS, deviceOS);                
        para.put(ServiceConstant.PARA_DEVICETYPE, DrawNetworkConstants.DEVICE_TYPE_ANDROID);                

        
        
        httpGetWithJSONResponse(url, para, callbackHandler);		
	}
	
	public static void registerUserBySNSUserInfo(
			final String baseURL,
			final String appId,
			final String gameId,
			final String deviceId,
			
			final String snsId,
			final int registerType,
			final String nickName,
			final String avatarUrl,
			final String accessToken,
			final String accessTokenSecret,
			final String refreshToken,
			final int expireDate,
			final String qqOpenId,
			final int province,
			final int city,
			final String location,
			final String gender,
			final String birthday,
			final String domain,
			final NetworkRequestJSONCallbackInterface callbackHandler) {

		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_REGISTER_USER);
		para.put(ServiceConstant.PARA_GAME_ID, gameId);
		para.put(ServiceConstant.PARA_DEVICEID, deviceId);
        para.put(ServiceConstant.PARA_APPID, appId);                
        
        para.put(ServiceConstant.PARA_SNSID, snsId);
        
        
        String registerTypeString = String.valueOf(registerType);
        para.put(ServiceConstant.PARA_REGISTER_TYPE, registerTypeString);   
        
        para.put(ServiceConstant.PARA_NICKNAME, nickName);
        para.put(ServiceConstant.PARA_NICKNAME, avatarUrl);
        para.put(ServiceConstant.PARA_ACCESS_TOKEN, accessToken);
        para.put(ServiceConstant.PARA_ACCESS_TOKEN_SECRET, accessTokenSecret);
        para.put(ServiceConstant.PARA_PROVINCE, String.valueOf(province));
        para.put(ServiceConstant.PARA_CITY,  String.valueOf(city));
        para.put(ServiceConstant.PARA_LOCATION, location);
        para.put(ServiceConstant.PARA_GENDER, gender);
        para.put(ServiceConstant.PARA_BIRTHDAY, birthday);
        para.put(ServiceConstant.PARA_DOMAIN, domain);

        
        if (refreshToken != null) {
            para.put(ServiceConstant.PARA_REFRESH_TOKEN, refreshToken);
		}
        
        if (qqOpenId != null) {
            para.put(ServiceConstant.PARA_QQ_OPEN_ID, qqOpenId);
		}
        
        if (expireDate != 0) {
            para.put(ServiceConstant.PARA_QQ_OPEN_ID,  String.valueOf(expireDate));
		}
        
        // TODO: add detail to below
        String countryCode = "";
        String languageCode = "";
        String deviceModel = "";
        String deviceOS = "";
                
        para.put(ServiceConstant.PARA_COUNTRYCODE, countryCode);                
        para.put(ServiceConstant.PARA_LANGUAGE, languageCode);                
        para.put(ServiceConstant.PARA_DEVICEMODEL, deviceModel);                
        para.put(ServiceConstant.PARA_DEVICEOS, deviceOS);                
        para.put(ServiceConstant.PARA_DEVICETYPE, DrawNetworkConstants.DEVICE_TYPE_ANDROID);                
        
        httpGetWithJSONResponse(url, para, callbackHandler);		
	}
	
	public static void updateUser(final String baseURL, 
			final String appId, 
			final String deviceId, 
			final String deviceToken, 
			final String userId, 
			final String password, 
			final String nickName, 
			final String gender,
			final String email, 
			final String localAvatarPath,
			final NetworkRequestJSONCallbackInterface callbackHandler) {
		
		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_UPDATE_USER);
        para.put(ServiceConstant.PARA_APPID, appId);      
		para.put(ServiceConstant.PARA_DEVICEID, deviceId);
		para.put(ServiceConstant.PARA_DEVICETOKEN, deviceToken);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_PASSWORD, password);
		para.put(ServiceConstant.PARA_NICKNAME, nickName);
		para.put(ServiceConstant.PARA_GENDER, gender);
		para.put(ServiceConstant.PARA_EMAIL, email);
		
		if (localAvatarPath != null && localAvatarPath.length() > 0) {
			para.put(ServiceConstant.PARA_AVATAR, "1");
			File file = new File(localAvatarPath);
			try {
				para.put(ServiceConstant.PARA_AVATAR, file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
        httpGetWithJSONResponse(url, para, callbackHandler);		
	}

	public static void getFeedById(String baseURL, 
			String userId,
			String appId,
			String feedId,
			NetworkRequestPBCallbackInterface callbackHandler) {
		
		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_OPUS_BY_ID);
        para.put(ServiceConstant.PARA_APPID, appId);                
        para.put(ServiceConstant.PARA_USERID, userId);                
        para.put(ServiceConstant.PARA_FEED_ID, feedId);                
        
        httpGetWithPBResponse(url, para, callbackHandler);		
	}
	

	public static void getFeedActionList(String baseURL,
			String userId, 
			String opusId, 
			FeedActionType feedActionType, 
			int offset,
			int limit,
			NetworkRequestPBCallbackInterface callbackHandler) {

		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_FEED_COMMENT_LIST);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_OPUS_ID, opusId);
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                
		para.put(ServiceConstant.PARA_TYPE, String.valueOf(feedActionType.intValue()));
        para.put(ServiceConstant.PARA_RETURN_ITEM, "1");
		
        httpGetWithPBResponse(url, para, callbackHandler);		
	}

	public static void getFriendList(
			String baseURL,
			String userId,
			int friendType,
			int offset,
			int limit,
			NetworkRequestJSONCallbackInterface callbackHandler) {

		
		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_FRIEND_LIST);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                
        para.put(ServiceConstant.PARA_GAME_ID, ConfigManager.getInstance().getGameId());                
		para.put(ServiceConstant.PARA_FRIEND_TYPE, String.valueOf(friendType));
		
        httpGetWithJSONResponse(url, para, callbackHandler);		
		
	}

	public static void getMyCommentFeedList(String baseURL,
			String userId, int offset, int limit,
			NetworkRequestPBCallbackInterface callbackHandler) {

		String url = baseURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_MY_COMMENT_LIST);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                
		
        httpGetWithPBResponse(url, para, callbackHandler);		
		
	}

	public static void getMessageStatList(String trafficApiServerURL,
			String userId, int offset, int limit,
			NetworkRequestPBCallbackInterface networkRequestPBCallbackInterface) {

		String url = trafficApiServerURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_MESSAGE_STAT_LIST);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                
		
        httpGetWithPBResponse(url, para, networkRequestPBCallbackInterface);		
		
	}

	public static void getMessageList(String trafficApiServerURL,
			String userId, 
			String targetUserId, 
			String offsetMessageId,
			int readDirection, int limit,
			NetworkRequestPBCallbackInterface networkRequestPBCallbackInterface) {
		
		String url = trafficApiServerURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_MESSAGE_LIST);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TO_USERID, targetUserId);
		para.put(ServiceConstant.PARA_MESSAGE_ID, offsetMessageId);
		para.put(ServiceConstant.PARA_FORWARD, String.valueOf(readDirection));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                
		
        httpGetWithPBResponse(url, para, networkRequestPBCallbackInterface);		
	}

	public static void sendTextMessage(String trafficApiServerURL,
			String userId, String targetUserId, String text,
			NetworkRequestJSONCallbackInterface callback) {

		String url = trafficApiServerURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_SEND_MESSAGE);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TO_USERID, targetUserId);
		para.put(ServiceConstant.PARA_MESSAGETEXT, text);
		para.put(ServiceConstant.PARA_TYPE, String.valueOf(PPMessage.MessageTypeText));
        para.put(ServiceConstant.PARA_APPID, ConfigManager.getInstance().getAppId());                
		
        httpGetWithJSONResponse(url, para, callback);						
	}

	public static void commentOnFeed(
			String trafficApiServerURL,
			String userId,
			String nickName,
			String gender,
			String avatar,
			String opusId,
			String opusCreatorUserId,
			String comment,
			int commentType, 			
			String commentId,
			String commentSummary,
			String commentUserId,
			String commentNickName,
			NetworkRequestJSONCallbackInterface callback) {
		
		String url = trafficApiServerURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_ACTION_ON_OPUS);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_NICKNAME, nickName);
		para.put(ServiceConstant.PARA_AVATAR, avatar);
		para.put(ServiceConstant.PARA_GENDER, gender);
		
		para.put(ServiceConstant.PARA_OPUS_ID, opusId);
		para.put(ServiceConstant.PARA_OPUS_CREATOR, opusCreatorUserId);
		para.put(ServiceConstant.PARA_COMMENT_CONTENT, comment);
		
		para.put(ServiceConstant.PARA_COMMENT_TYPE, String.valueOf(commentType));
		para.put(ServiceConstant.PARA_COMMENT_ID, commentId);
		para.put(ServiceConstant.PARA_COMMENT_SUMMARY, commentSummary);
		para.put(ServiceConstant.PARA_COMMENT_USERID, commentUserId);
		para.put(ServiceConstant.PARA_COMMENT_NICKNAME, commentNickName);
		
		para.put(ServiceConstant.PARA_ACTION_TYPE, String.valueOf(FeedActionType.Comment.intValue()));
        
        httpGetWithJSONResponse(url, para, callback);		
		
	}

	public static void searchUser(
			String userApiServerURL,
			String userId,
			String keyword,
			int offset,
			int limit,
			NetworkRequestJSONCallbackInterface callback) {

		String url = userApiServerURL;

        // set input parameters		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_SEARCH_USER);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_SEARCHSTRING, keyword);
		para.put(ServiceConstant.PARA_START_INDEX, String.valueOf(offset));
		para.put(ServiceConstant.PARA_END_INDEX, String.valueOf(limit));
		
        httpGetWithJSONResponse(url, para, callback);								
	}

	private static void simpleJSONRequest(String url, String method, String userId, 
			NetworkRequestJSONCallbackInterface callback){

        // set input parameters		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, method);
		para.put(ServiceConstant.PARA_USERID, userId);
		
        httpGetWithJSONResponse(url, para, callback);										
	}
	
	public static void getUserRelationCount(
			String userApiServerURL,
			String userId,
			NetworkRequestJSONCallbackInterface callback) {
		simpleJSONRequest(userApiServerURL, ServiceConstant.METHOD_GET_RELATION_COUNT, userId, callback);
	}

	public static void actionOnFeed(
			String trafficApiServerURL,
			String userId,
			String nickName,
			String gender,
			String avatar,
			String opusId,
			String opusCreatorUserId,
			FeedActionType actionType,
			NetworkRequestJSONCallbackInterface callback) {

		String url = trafficApiServerURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_ACTION_ON_OPUS);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_NICKNAME, nickName);
		para.put(ServiceConstant.PARA_AVATAR, avatar);
		para.put(ServiceConstant.PARA_GENDER, gender);
		
		para.put(ServiceConstant.PARA_OPUS_ID, opusId);
		para.put(ServiceConstant.PARA_OPUS_CREATOR, opusCreatorUserId);
		
		para.put(ServiceConstant.PARA_ACTION_TYPE, String.valueOf(actionType.intValue()));
        
        httpGetWithJSONResponse(url, para, callback);		
		
	}

	public static void getTopLevelUsers(
			String userApiServerURL,
			String userId,
			int offset,
			int limit,
			NetworkRequestJSONCallbackInterface callback) {

		String url = userApiServerURL;
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_TOP_PLAYERS);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_COUNT, String.valueOf(limit));
        
        httpGetWithJSONResponse(url, para, callback);				
		
	}
	
	public static void followUser(
			String userApiServerURL,
			String userId,
			String targetUserId,
			NetworkRequestJSONCallbackInterface callback){
		
		String url = userApiServerURL;
		String gameId = ConfigManager.getInstance().getGameId();
		String appId = ConfigManager.getInstance().getAppId();
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,ServiceConstant.METHOD_FOLLOW_USER);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_GAME_ID, gameId);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void unFollowUser(
			String userApiServerURL,
			String userId,
			String targetUserId,
			NetworkRequestJSONCallbackInterface callback){
		
		String url = userApiServerURL;
		String gameId = ConfigManager.getInstance().getGameId();
		String appId = ConfigManager.getInstance().getAppId();
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_UNFOLLOW_USER);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_GAME_ID, gameId);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void getUserByUserId(
						String userApiServerURL,
						String appId,
						String userId,
						String targetUserId,
						NetworkRequestPBCallbackInterface callback){
		String url = userApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_TARGET_USER_INFO);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		
		httpGetWithPBResponse(url, para, callback);
	}
	
	public static void feedback(
						String userApiServerURL,
						String userId,
						String feedback,
						String contact,
						FeedbackType type,
						NetworkRequestJSONCallbackInterface callback){
		String url = userApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_FEEDBACK);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_FEEDBACK, feedback);
		para.put(ServiceConstant.PARA_CONTACT, contact);
		para.put(ServiceConstant.PARA_TYPE, String.valueOf(type.intValue()));
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void submitNewWord(
						String userApiServerURL,
						String appId,
						String userId,
						String wordString,
						NetworkRequestJSONCallbackInterface callback){
		String url = userApiServerURL;
		RequestParams para =  new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_COMMIT_WORDS);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_NEW_WORDS, wordString);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	public static void getBBSBoardList(
						String trafficApiServerURL,
						String userId,
						String appId,
						String gameId,
						int deviceType,
						NetworkRequestPBCallbackInterface callback){
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_BBS_BOARD_LIST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_GAME_ID, gameId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		
		httpGetWithPBResponse(url, para, callback);
	}
	
	
	public static void getBBSPostList(
						String trafficApiServerURL,
						String appId, 
						int deviceType,
						String userId,
						String targetUserId,
						String boardId,
						int rangeType,
						int offset,
						int limit,
						NetworkRequestPBCallbackInterface callback)
	{
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_GET_BBS_POST_LIST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_BOARDID, boardId);
		para.put(ServiceConstant.PARA_RANGE_TYPE, String.valueOf(rangeType));
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_LIMIT, String.valueOf(limit));
		httpGetWithPBResponse(url, para, callback);
	}
	
	
	public static void getBBSPostActionList(
				String trafficApiServerURL,
				String appId, 
				int deviceType,
				String userId,
				String targetUserId,
				String postId,
				int actionType,
				int offset,
				int limit,
				NetworkRequestPBCallbackInterface callback){
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_GET_BBS_ACTION_LIST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_POSTID, postId);
		para.put(ServiceConstant.PARA_ACTION_TYPE, String.valueOf(actionType));
		para.put(ServiceConstant.PARA_OFFSET, String.valueOf(offset));
		para.put(ServiceConstant.PARA_LIMIT, String.valueOf(limit));
		httpGetWithPBResponse(url, para, callback);
		
	}
	
	
	public static void createBBSPost(
									String trafficApiServerURL,
									String appId,
									String userId,
									int deviceType,
									String userNickName,
									boolean gender,
									String avatar,
									String boardId,
									int contentType,
									String textContent,
									int bonus,
									String imageURL,
									byte[] drawData,
									String drawImage,
									NetworkRequestJSONCallbackInterface callback
									){
		String url = trafficApiServerURL;
		
		try
		{
			textContent = URLEncoder.encode(textContent, "utf-8");
		} catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_CREATE_BBS_POST);
		
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_NICKNAME, userNickName);
		para.put(ServiceConstant.PARA_GENDER, GenderUtils.toString(gender));
		para.put(ServiceConstant.PARA_AVATAR, avatar);
		para.put(ServiceConstant.PARA_BOARDID, boardId);
		para.put(ServiceConstant.PARA_CONTENT_TYPE, String.valueOf(contentType));
		para.put(ServiceConstant.PARA_TEXT_CONTENT, textContent);
		para.put(ServiceConstant.PARA_BONUS, String.valueOf(bonus));
		if (!StringUtil.isEmpty(imageURL))
		{
			File image = new File(imageURL);
			try
			{
				para.put(ServiceConstant.PARA_IMAGE, image);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		if (drawData!=null&&drawData.length>0)
		{
			para.put(ServiceConstant.PARA_DRAW_DATA, new ByteArrayInputStream(drawData));
			para.put(ServiceConstant.PARA_DRAW_IMAGE, drawImage);
		}
		
		httpPostWithJSONResponse(url, para, callback);
		
	}
	
	
	public static void createBBSPostAction(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						String userNickName,
						boolean gender,
						String avatar,
						String postId,
						String actionId,
						String postUid,
						String actionUid,
						String actionNickName,
						String briefText,
						int sourceActionType,
						int contentType,
						int actionType,
						String textContent,
						String imageURL,
						byte[] drawData,
						String drawImage,
						NetworkRequestJSONCallbackInterface callback
						)
	{
		String url = trafficApiServerURL;
		
		
		try
		{
			textContent = URLEncoder.encode(textContent, "utf-8");
			userNickName = URLEncoder.encode(userNickName, "utf-8");
			actionNickName = URLEncoder.encode(actionNickName, "utf-8");
			briefText = URLEncoder.encode(briefText, "utf-8");
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		
		
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_CREATE_BBS_ACTION);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_NICKNAME, userNickName);
		para.put(ServiceConstant.PARA_GENDER, GenderUtils.toString(gender));
		para.put(ServiceConstant.PARA_AVATAR, avatar);
		para.put(ServiceConstant.PARA_POSTID, postId);
		para.put(ServiceConstant.PARA_ACTIONID, actionId);
		para.put(ServiceConstant.PARA_POST_UID, postUid);
		para.put(ServiceConstant.PARA_ACTION_UID, actionUid);
		para.put(ServiceConstant.PARA_ACTION_NICKNAME, actionNickName);
		para.put(ServiceConstant.PARA_BRIEF_TEXT, briefText);
		para.put(ServiceConstant.PARA_SOURCE_ACTION_TYPE, String.valueOf(sourceActionType));
		para.put(ServiceConstant.PARA_CONTENT_TYPE, String.valueOf(contentType));
		para.put(ServiceConstant.PARA_ACTION_TYPE, String.valueOf(actionType));
		para.put(ServiceConstant.PARA_TEXT_CONTENT, textContent);
		if (!StringUtil.isEmpty(imageURL))
		{
			File image = new File(imageURL);
			try
			{
				para.put(ServiceConstant.PARA_IMAGE,image);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		if (drawData!=null&&drawData.length>0)
		{
			para.put(ServiceConstant.PARA_DRAW_DATA, new ByteArrayInputStream(drawData));
			para.put(ServiceConstant.PARA_DRAW_IMAGE, drawImage);
		}
		
		
		httpPostWithJSONResponse(url, para, callback);
	}
	
	
	public static void deleteBBSPost(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						String postId,
						String boardId,
						NetworkRequestJSONCallbackInterface callback){
		
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_DELETE_BBS_POST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_DEVICEID, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_POSTID, postId);
		para.put(ServiceConstant.PARA_BOARDID, boardId);
		
		httpGetWithJSONResponse(url, para, callback);
		
	}
	
	
	
	public static void deleteBBSPostAction(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						String actionId,
						NetworkRequestJSONCallbackInterface callback){
		
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_DELETE_BBS_ACTION);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_DEVICETYPE,String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_ACTIONID, actionId);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void editBBSPost(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						String boardId,
						String postId,
						int status,
						NetworkRequestJSONCallbackInterface callback){
		
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_EDIT_BBS_POST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_BOARDID, boardId);
		para.put(ServiceConstant.PARA_POSTID, postId);
		para.put(ServiceConstant.PARA_STATUS, String.valueOf(status));
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void changeUserRole(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						String targetUserId,
						String boardId,
						int roleType,
						int date,
						NetworkRequestJSONCallbackInterface callback
						){
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_CHANGE_BBS_ROLE);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID,userId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_BOARDID, boardId);
		para.put(ServiceConstant.PARA_TYPE, String.valueOf(roleType));
		para.put(ServiceConstant.PARA_EXPIRE_DATE, String.valueOf(date));
		
		
		httpGetWithJSONResponse(url, para, callback);
		
		
		
	}
	
	

	public static void getBBSUserPrivilegeList(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						NetworkRequestPBCallbackInterface callback){
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_GET_BBS_PRIVILEGE_LIST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID,userId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		
		httpGetWithPBResponse(url, para, callback);
	}
	
	public static void getBBSPostById(
						String trafficApiServerURL,
						String appId,
						String userId,
						int deviceType,
						String postId,
						NetworkRequestPBCallbackInterface callback
						){
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD,
				ServiceConstant.METHOD_GET_BBS_POST);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_USERID,userId);
		para.put(ServiceConstant.PARA_DEVICETYPE, String.valueOf(deviceType));
		para.put(ServiceConstant.PARA_POSTID, postId);
		
		httpGetWithPBResponse(url, para, callback);
	}
	
	
	public static void getStatistics(String trafficApiServerURL,
									 String userId,
									 NetworkRequestJSONCallbackInterface callback){
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_GET_STATISTICS);
		para.put(ServiceConstant.PARA_USERID, userId);
		
		httpGetWithJSONResponse(url, para, callback); 
	}
	
	
	public static void chargeGold(
			String gameApiServerURL,
			String userId,
			String adminUserId,
			String amount,
			String sourceAmount,
			NetworkRequestJSONCallbackInterface callback
			){
		String url = gameApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_CHARGE_ACCOUNT);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_ADMIN_USER_ID, adminUserId);
		para.put(ServiceConstant.PARA_AMOUNT, amount);
		para.put(ServiceConstant.PARA_SOURCE, sourceAmount);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void chargeIngot(
			String gameApiServerURL,
			String userId,
			String adminUserId,
			String amount,
			String sourceAmount,
			NetworkRequestJSONCallbackInterface callback
			){
		String url = gameApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_CHARGE_INGOT_ACCOUNT);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_ADMIN_USER_ID, adminUserId);
		para.put(ServiceConstant.PARA_AMOUNT, amount);
		para.put(ServiceConstant.PARA_SOURCE, sourceAmount);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void blackUser(
			String gameApiServerURL,
			String userId,
			String targetUserId,
			String appId,
			String deviceId,
			int blackType,
			int actionType,
			NetworkRequestJSONCallbackInterface callback){
		
		String url = gameApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_BLACK_USER);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_DEVICEID, deviceId);
		para.put(ServiceConstant.PARA_TYPE, String.valueOf(blackType));
		para.put(ServiceConstant.PARA_ACTION_TYPE, String.valueOf(actionType));
		
		httpGetWithJSONResponse(url, para, callback);
		
	}
	
	
	
	public static void blackkFriend(
			String gameApiServerURL,
			String appId,
			String userId,
			String targetUserId,
			String blackType,
			NetworkRequestJSONCallbackInterface callback){
		String url = gameApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_BLACK_FRIEND);
		para.put(ServiceConstant.PARA_APPID,appId);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_TARGETUSERID, targetUserId);
		para.put(ServiceConstant.PARA_TYPE, blackType);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public  static void bbsPayReward(
			String trafficApiServerURL,
			String appId,
			String deviceType,
			String userId,
			String postId,
			String actionId,
			String actionUid,
			String actionNick,
			String userAvatar,
			String gender,
			NetworkRequestJSONCallbackInterface callback
			)
	{
		String url = trafficApiServerURL;
		RequestParams para = new RequestParams();
		para.put(ServiceConstant.METHOD, ServiceConstant.METHOD_PAY_BBS_REWARD);
		para.put(ServiceConstant.PARA_APPID, appId);
		para.put(ServiceConstant.PARA_DEVICETYPE, deviceType);
		para.put(ServiceConstant.PARA_USERID, userId);
		para.put(ServiceConstant.PARA_POSTID, postId);
		para.put(ServiceConstant.PARA_ACTIONID, actionId);
		para.put(ServiceConstant.PARA_ACTION_UID, actionUid);
		para.put(ServiceConstant.PARA_ACTION_NICKNAME, actionNick);
		para.put(ServiceConstant.PARA_AVATAR, userAvatar);
		para.put(ServiceConstant.PARA_GENDER, gender);
		
		httpGetWithJSONResponse(url, para, callback);
	}
	
	
	public static void cancelRequest(){
		client.cancelRequests(context, true);
		
	}
}
