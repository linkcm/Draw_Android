package com.orange.game.draw.mission.feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.orange.common.android.utils.JsonUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.feed.FeedActionType;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

public class FeedActionMission extends CommonMission {
	protected static final String TAG = "FeedActionMission";

	private static final int ACTION_SUMMARY_MAX_LENGTH = 60;
	
	// thread-safe singleton implementation
	private static FeedActionMission sharedInstance = new FeedActionMission();
	private FeedActionMission() {
	}
	public static FeedActionMission getInstance() {
		return sharedInstance;
	}
	
	public void actionOnFeed(final String opusId, 
			final String opusCreatorUserId,
			final FeedActionType actionType,
			final MissionCompleteInterface completeHandler){
		
		if (opusId == null || opusCreatorUserId == null){
			Log.i(TAG, "<actionOnFeed> but opusId or  opusCreatorUserId is null");
			callCompleteHandler(completeHandler, DrawNetworkConstants.ERROR_FEED_NULL);
			return;
		}
        String userId = userManager.getUser().getUserId();
        String nickName = userManager.getUser().getNickName();
        String gender = userManager.getGenderString();
        String avatar = userManager.getUser().getAvatar();        
        
		DrawNetworkRequest.actionOnFeed(configManager.getTrafficApiServerURL(),
				userId, 
				nickName,
				gender,
				avatar,
				opusId,
				opusCreatorUserId,
				actionType,
				new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {												
						
						try {
							Log.i(TAG, "<actionOnFeed> success");

							// notify UI to update
							callCompleteHandler(completeHandler, 0);
							
						} catch (Exception e) {
							Log.e(TAG, "<actionOnFeed> catch exception="+e.toString(), e);
							callCompleteHandler(completeHandler, 0);
						}
												
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						// TODO Auto-generated method stub
						callCompleteHandler(completeHandler, errorCode);
						
					}
				});						
	}
	
	public void commentOnFeed( 
			final PBFeed feed,
			final String commentMsg,
			final MissionCompleteInterface completeHandler) {
		
		if (feed == null){
			Log.i(TAG, "<commentOpus> but feed is null");
			callCompleteHandler(completeHandler, DrawNetworkConstants.ERROR_FEED_NULL);
			return;
		}
		
		String opusId = null;
		String opusCreatorUserId = null;
		String comment = commentMsg;
		
        int commentType = 0;
        String commentId = "";
        String commentSummary = "";
        String commentUserId = "";
        String commentNickName = "";		
		
        if (FeedActionType.isFeedAction(feed.getActionType())){   
        	
        	opusId = feed.getFeedId();
        	opusCreatorUserId = feed.getUserId();
        	
            commentType = feed.getActionType();
            commentId = feed.getFeedId();
            commentSummary = feed.getComment();
            commentUserId =  feed.getUserId();
            commentNickName = feed.getNickName();        	
        }
        else{
        	
        	opusId = feed.getFeedId();
        	opusCreatorUserId = feed.getUserId();
        	
            commentType = feed.getActionType();
            commentId = opusId;
            commentSummary = feed.getOpusWord();
            commentUserId = opusCreatorUserId;
            commentNickName = feed.getNickName();        	
        }
		
        if (commentSummary.length() > ACTION_SUMMARY_MAX_LENGTH) {
            commentSummary = commentSummary.substring(0, ACTION_SUMMARY_MAX_LENGTH); //  substringToIndex:ACTION_SUMMARY_MAX_LENGTH];
        }
		
        String userId = userManager.getUser().getUserId();
        String nickName = userManager.getUser().getNickName();
        String gender = userManager.getGenderString();
        String avatar = userManager.getUser().getAvatar();        
        
		DrawNetworkRequest.commentOnFeed(configManager.getTrafficApiServerURL(),
				userId, 
				nickName,
				gender,
				avatar,
				opusId,
				opusCreatorUserId,
				comment,
				commentType,
				commentId,
				commentSummary,
				commentUserId,
				commentNickName,
				new NetworkRequestJSONCallbackInterface() {
					
					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
						
						
						
						try {
							String commentId = JsonUtil.getString(jsonData, ServiceConstant.PARA_FEED_ID);
							Log.i(TAG, "<commentOnFeed> commentId="+commentId);

							// notify UI to update
							callCompleteHandler(completeHandler, 0);
							
						} catch (Exception e) {
							Log.e(TAG, "<commentOnFeed> catch exception="+e.toString(), e);
							callCompleteHandler(completeHandler, 0);
						}
												
					}
					
					@Override
					public void handleFailureResponse(int errorCode) {
						// TODO Auto-generated method stub
						callCompleteHandler(completeHandler, errorCode);
						
					}
				});
		
	}
}
