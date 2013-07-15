package com.orange.game.draw.mission.feed;

import java.util.List;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.constants.LanguageType;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteGetFeedDetailInterface;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestPBCallbackInterface;
import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

public class FeedMission extends CommonMission {

	protected static final String TAG = "FeedMission";
	
	// thread-safe singleton implementation
	private static FeedMission sharedInstance = new FeedMission();
	private FeedMission() {
	}
	public static FeedMission getInstance() {
		return sharedInstance;
	}
	
	
	
	
	public void getFeedActionList(
							final String userId, 
							final FeedManager feedManager, 
							final String opusId, 
							final int offset, 
							final int limit, 
							final MissionCompleteInterface completeHandler) {

		if (userId == null){
			Log.i(TAG, "<getFeedActionList> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		if (opusId == null||opusId.equals("")){
			Log.i(TAG, "<getFeedActionList> but opusId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_PARAMETER);
			return;
		}
		
		if (feedManager == null){
			Log.i(TAG, "<getFeedActionList> but feed manager is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_PARAMETER);
			return;
		}

		DrawNetworkRequest.getFeedActionList(
				configManager.getTrafficApiServerURL(),
				userId,
				opusId,
				feedManager.getFeedActionType(), 
				offset, 
				limit, 
				new NetworkRequestPBCallbackInterface() {

					@Override
					public void handleSuccessResponse(DataQueryResponse response) {
						Log.i(TAG, "<getFeedActionList> total "+response.getFeedCount()+ " feed got");

						// update model
						feedManager.addFeedList(response.getFeedList(), offset, limit);
						
						// notify UI to update
						callCompleteHandler(completeHandler, 0);
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);
					}
				});
	}
	
	public void getMyCommentFeedList(
						final FeedManager feedManager,
						final int offset, 
						final int limit, 
						final MissionCompleteInterface completeHandler) {

		String userId = userManager.getUserId();
		userId = userManager.getTestUserId();
		
		if (userId == null){
			Log.i(TAG, "<getMyCommentFeedList> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		DrawNetworkRequest.getMyCommentFeedList(configManager.getTrafficApiServerURL(),
				userId, offset, limit,
				new NetworkRequestPBCallbackInterface() {

					@Override
					public void handleSuccessResponse(DataQueryResponse response) {
						Log.i(TAG, "<getMyCommentFeedList> total "+response.getFeedCount()+ " comments got");

						// update model
						feedManager.addFeedList(response.getFeedList(), offset, limit);
						
						// notify UI to update
						callCompleteHandler(completeHandler, 0);
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);
					}
				});
	}

	
	private void getFeedList(
					final String userId, 
					final FeedManager feedManager, 
					final int offset, 
					final int limit, 
					final MissionCompleteInterface completeHandler) {

		if (userId == null){
			Log.i(TAG, "<getFeedList> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		if (feedManager == null){
			Log.i(TAG, "<getFeedList> but feed manager is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_PARAMETER);
			return;
		}

		// TODO load from user settings
		LanguageType languageType = LanguageType.CHINESE; 
		DrawNetworkRequest.getFeedList(configManager.getTrafficApiServerURL(),
				userId, feedManager.getFeedListType(), offset, limit, languageType,
				new NetworkRequestPBCallbackInterface() {

					@Override
					public void handleSuccessResponse(DataQueryResponse response) {
						Log.i(TAG, "<getFeedList> total "+response.getFeedCount()+ " feed got");

						// update model
						feedManager.addFeedList(response.getFeedList(), offset, limit);
						
						// notify UI to update
						callCompleteHandler(completeHandler, 0);
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);
					}
				});
	}
	
	public void getUserFeedList(
			 String userId, 
			 FeedManager feedManager, 
			 int offset, 
			 int limit, 
			 MissionCompleteInterface completeHandler) {
		getFeedList(userId, feedManager, offset, limit, completeHandler);		
	}
	
	public void getMyFeedList(
			FeedManager feedManager,
			int offset, 
			int limit, 
			MissionCompleteInterface completeHandler) {
		String userId = userManager.getUserId();		
		
		// just for test
		userId = userManager.getTestUserId();
		getFeedList(userId, feedManager, offset, limit, completeHandler);
	}
	
	public void getMyFeedComment(FeedManager feedManager,final int offset, final int limit, final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();		
		
		// just for test
		userId = userManager.getTestUserId();
		getFeedList(userId, feedManager, offset, limit, completeHandler);
	}
	
	
	public void getMyOpus(FeedManager feedManager,final int offset, final int limit, final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();		
		
		// just for test
		userId = userManager.getTestUserId();
		getFeedList(userId, feedManager, offset, limit, completeHandler);
	}
	
	public void getUserOpus(FeedManager feedManager,final int offset,final int limit,final String userId,final MissionCompleteInterface completeHandler){
		getFeedList(userId, feedManager, offset, limit, completeHandler);
	}
	
	
	public void getUserFavorite(FeedManager feedManager,final int offset,final int limit,final String userId,final MissionCompleteInterface completeHandler){
		getFeedList(userId, feedManager, offset, limit, completeHandler);
	}
	
	public void getDrawToMe(FeedManager feedManager,final int offset, final int limit, final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();		
		
		// just for test
		userId = userManager.getTestUserId();
		getFeedList(userId, feedManager, offset, limit, completeHandler);
	}
	
	
	public void getTopFeedList(final FeedManager feedManager, final int offset, final int limit, final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();	
		if (userId == null){
			userId = configManager.getSystemUserId();
		}
		
		getFeedList(userId, feedManager, offset, limit, completeHandler);				
	}
	
	
	public void getFeedById(final String feedId, final MissionCompleteGetFeedDetailInterface completeHandler){
		
		if (feedId == null){
			return;
		}
		
		String userId = userManager.getUserId();	
		//for tese 
		userId = userManager.getTestUserId();

		DrawNetworkRequest.getFeedById(
				configManager.getTrafficApiServerURL(),
				userId,
				configManager.getAppId(),
				feedId,
				new NetworkRequestPBCallbackInterface() {

					private void callCompleteHandler(final MissionCompleteGetFeedDetailInterface completeHandler,
							final PBFeed feed,
							final int errorCode){
						if (completeHandler != null){
							completeHandler.comlete(feed, errorCode);
						}
					}
			
					@Override
					public void handleSuccessResponse(DataQueryResponse response) {
						Log.i(TAG, "<getFeedById> success");

						// update model
						PBFeed feed = null;
						List<PBFeed> list = response.getFeedList();
						if (list == null || list.size() == 0){
							Log.i(TAG, "<getFeedById> but not feed data");
							callCompleteHandler(completeHandler, null, DrawNetworkConstants.ERROR_NO_DATA);
							return;
						}
						
						feed = list.get(0);
						
						// TODO cache feed here
						
						// notify UI to update
						callCompleteHandler(completeHandler, feed, 0);
						
						//remove other data
						int length = list.size();
						for (int i = 1; i < length; i++)
						{
							list.remove(i);
						}
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						Log.e(TAG, "<getFeedById> failure, errorCode="+errorCode);
						callCompleteHandler(completeHandler, null, errorCode);
					}
				});
	}
	
	

}
