/**  
        * @title BBSMission.java  
        * @package com.orange.game.draw.mission.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-24 上午10:06:11  
        * @version V1.0  
 */
package com.orange.game.draw.mission.bbs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.orange.game.application.DrawApplication;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.model.bbs.BBSManager;
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.UserPermissionManager;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;
import com.orange.game.draw.network.NetworkRequestPBCallbackInterface;
import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPrivilege;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-24 上午10:06:11  
 */

public class BBSMission extends CommonMission
{
	protected static final String TAG = "BBSMission";
	
	// thread-safe singleton implementation
	private static BBSMission sharedInstance = new BBSMission();
	private BBSMission() {
	}
	public static BBSMission getInstance() {
		return sharedInstance;
	}
	
	
	
	public enum RangeType{
		RangeTypeNew(0),
		RangeTypeHot(1);

	    final int value;

	    RangeType(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
	}
	
	
	//BBSManager bbsManager = new BBSManager();
	
	BBSManager boardManager = new BBSManager();
	BBSManager hotPostManager = new BBSManager();
	BBSManager newPostManager = new BBSManager();
	BBSManager postCommentManager = new BBSManager();
	BBSManager postSupportManager = new BBSManager();
	//UserPermissionManager bbsUserPermissionManager = new UserPermissionManager();
	
	private PBBBSPost tempPost;
	
	public void getBBSBoardList(final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		String appId = configManager.getAppId();
		String gameId = configManager.getGameId();
		int deviceType = configManager.getAndroidOs();
		
		DrawNetworkRequest.getBBSBoardList(
							configManager.getTrafficApiServerURL(), 
							userId, 
							appId, 
							gameId, 
							deviceType, 
							new NetworkRequestPBCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(DataQueryResponse response)
								{
									Log.i(TAG, "<getBBSBoardList> total "+response.getBbsBoardCount()+ " board got");
									boardManager.addBoardList(response.getBbsBoardList());
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);									
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);								
								}
							});
	}
	
	
	private void getBBSPostList(
				String targetUserId,
				String boardId,
				final BBSManager bbsManager,
				final RangeType rangeType,
				final int offset,
				final int limit,
				final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test 
		userId = userManager.getTestUserId();
		String appId = configManager.getAppId();
		DrawNetworkRequest.getBBSPostList(
						configManager.getTrafficApiServerURL(), 
						appId, 
						configManager.getAndroidOs(), 
						userId, 
						targetUserId, 
						boardId, 
						rangeType.intValue(), 
						offset, 
						limit, new NetworkRequestPBCallbackInterface()
						{
							
							@Override
							public void handleSuccessResponse(DataQueryResponse response)
							{
								Log.i(TAG, "<getBBSPostList> total "+response.getBbsPostCount()+ " post got");	
								bbsManager.addPostList(response.getBbsPostList(), offset, limit);
								completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
							}
							
							@Override
							public void handleFailureResponse(int errorCode)
							{
								completeHandler.onComplete(errorCode);
								
							}
						});
		
	}
	
	
	public void getPostList(
					String targetUserId,
					String boardId,
					BBSManager bbsManager,
					RangeType rangeType,
					int offset,
					int limit,
					MissionCompleteInterface completeHandler){
		getBBSPostList(targetUserId, boardId, bbsManager, rangeType, offset, limit, completeHandler);
	}
	
	public void getPostList(String targetUserId,
			String boardId,
			RangeType rangeType,
			int offset,
			int limit,
			MissionCompleteInterface completeHandler){
		switch (rangeType)
		{
		case RangeTypeHot:
			getBBSPostList(
					targetUserId, 
					boardId, 
					hotPostManager, 
					RangeType.RangeTypeHot, 
					offset, 
					limit, 
					completeHandler);
			break;
		case RangeTypeNew:
			getBBSPostList(
					targetUserId, 
					boardId, 
					newPostManager, 
					RangeType.RangeTypeNew, 
					offset, 
					limit, 
					completeHandler);
			break;
		default:
			break;
		}
	}
	
	
	
	
	private void getPostActionList(
			String targetUserId,
			String postId,
			final BBSManager bbsManager,
			final int actionType,
			final int offset,
			final int limit,
			final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.getBBSPostActionList(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							configManager.getAndroidOs(), 
							userId, 
							targetUserId, 
							postId, 
							actionType, 
							offset, 
							limit, 
							new NetworkRequestPBCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(DataQueryResponse response)
								{
									Log.i(TAG, "<getPostActionList> total "+response.getBbsActionCount()+" action got");
									
									bbsManager.addActionList(response.getBbsActionList(),offset,limit);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);
									
								}
							});
	}
	
	
	
	public void getBBSUserPrivilegeList(final MissionCompleteInterface completehandler){
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		DrawNetworkRequest.getBBSUserPrivilegeList(
									configManager.getTrafficApiServerURL(), 
									configManager.getAppId(), 
									userId, 
									configManager.getAndroidOs(), 
									new NetworkRequestPBCallbackInterface()
									{
										
										@Override
										public void handleSuccessResponse(DataQueryResponse response)
										{
											DrawApplication
											.getInstance()
											.getUserPermissionManager()
											.setPrivilegeList(
															response.getBbsPrivilegeListList());
											completehandler.onComplete(ErrorCode.ERROR_SUCCESS);
											
										}
										
										@Override
										public void handleFailureResponse(int errorCode)
										{
											completehandler.onComplete(errorCode);
										}
									});
	}
	
	
	
	public void getPostAction(
				String targetUserId,
				String postId,
				BBSManager bbsManager,
				int actionType,
				int offset,
				int limit,
				MissionCompleteInterface completeHandler)
	{
		getPostActionList(
				targetUserId, 
				postId, 
				bbsManager, 
				actionType, 
				offset, 
				limit, 
				completeHandler);
	}
	
	public void getPostAction(
			String targetUserId,
			String postId,
			int actionType,
			int offset,
			int limit,
			MissionCompleteInterface completeHandler)
{
		switch (actionType)
		{
		case BBSStatus.ActionTypeComment:
			getPostActionList(
					targetUserId, 
					postId, 
					postCommentManager, 
					BBSStatus.ActionTypeComment, 
					offset, 
					limit, 
					completeHandler);
			break;
		case BBSStatus.ActionTypeSupport:
			getPostActionList(
					targetUserId, 
					postId, 
					postSupportManager, 
					BBSStatus.ActionTypeSupport, 
					offset, 
					limit, 
					completeHandler);
			break;
		default:
			break;
		}
}
	
	
	
	public void getPostComment(
				String targetUserId,
				String postId,
				int offset,
				int limit,
				MissionCompleteInterface completeHandler){
		getPostActionList(
					targetUserId, 
					postId, 
					postCommentManager, 
					BBSStatus.ActionTypeComment, 
					offset, 
					limit, 
					completeHandler);
	}
	
	public void getPostSupport(
				String targetUserId,
				String postId,
				int offset,
				int limit,
				MissionCompleteInterface completeHandler){
		getPostActionList(
				targetUserId, 
				postId, 
				postSupportManager, 
				BBSStatus.ActionTypeSupport, 
				offset, 
				limit, 
				completeHandler);
	}
	
	
	public void createBBSPost(
						String boardId,
						int contentType,
						String textContent,
						int bonus,
						String imageURL,
						byte[] drawData,
						String drawImage,
						final MissionCompleteInterface completeHandler
						){
		String userId = userManager.getUserId();
		//for test 
		userId = userManager.getTestUserId();
		
		
		DrawNetworkRequest.createBBSPost(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							userId, 
							configManager.getAndroidOs(), 
							userManager.getUser().getNickName(), 
							userManager.getUser().getGender(), 
							userManager.getUser().getAvatar(), 
							boardId, 
							contentType, 
							textContent, 
							bonus, 
							imageURL, 
							drawData, 
							drawImage, 
							new NetworkRequestJSONCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(JSONObject jsonData)
								{
									// TODO Auto-generated method stub
									Log.d(TAG, "<createBBSPost> return  json result = "+jsonData);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									// TODO Auto-generated method stub
									completeHandler.onComplete(errorCode);
								}
							});
	}
	
	
	
	public void createBBSPostAction(
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
				final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test 
		userId = userManager.getTestUserId();
		DrawNetworkRequest.createBBSPostAction(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							userId, 
							configManager.getAndroidOs(), 
							userManager.getUser().getNickName(), 
							userManager.getUser().getGender(), 
							userManager.getUser().getAvatar(), 
							postId, 
							actionId, 
							postUid, 
							actionUid, 
							actionNickName, 
							briefText, 
							sourceActionType, 
							contentType, 
							actionType, 
							textContent, 
							imageURL, 
							drawData, 
							drawImage, 
							new NetworkRequestJSONCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(JSONObject jsonData)
								{
									Log.i(TAG, "<createBBSPostAction> return result json = "+jsonData);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);									
								}
							});
	}
	
	
	public void deletePost(String postId,String boardId,final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test 
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.deleteBBSPost(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							userId, 
							configManager.getAndroidOs(), 
							postId, 
							boardId, 
							new NetworkRequestJSONCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(JSONObject jsonData)
								{
									Log.i(TAG,"<deletePost> return result json = "+jsonData);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);
								}
							});
	}
	
	
	
	public void deleteAction(String actionId,final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		DrawNetworkRequest.deleteBBSPostAction(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							userId, 
							configManager.getAndroidOs(), 
							actionId, 
							new NetworkRequestJSONCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(JSONObject jsonData)
								{
									Log.i(TAG, "<deleteAction> return result json = "+jsonData);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);
								}
							});
	}
	
	
	public void editBBSPost(
					String boardId,
					String postId,
					int status,
					final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		DrawNetworkRequest.editBBSPost(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							userId, 
							configManager.getAndroidOs(), 
							boardId, 
							postId, 
							status, 
							new NetworkRequestJSONCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(JSONObject jsonData)
								{
									Log.i(TAG, "<editBBSPost> return result json = "+jsonData);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);
								}
							});
	}
	
	
	public void changeUserRole(
					String targetUserId,
					String boardId,
					int roleType,
					int date,
					final MissionCompleteInterface completeHandler){
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		DrawNetworkRequest.changeUserRole(
							configManager.getTrafficApiServerURL(), 
							configManager.getAppId(), 
							userId, 
							configManager.getAndroidOs(), 
							targetUserId, 
							boardId, 
							roleType, 
							date, 
							new NetworkRequestJSONCallbackInterface()
							{
								
								@Override
								public void handleSuccessResponse(JSONObject jsonData)
								{
									Log.i(TAG, "<changeUserRole> return result json = "+jsonData);
									completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
								}
								
								@Override
								public void handleFailureResponse(int errorCode)
								{
									completeHandler.onComplete(errorCode);
								}
							});
		
	}
	
	
	
	public void getBBSPostById(String postId,final MissionCompleteInterface completeHandler)
	{
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.getBBSPostById(
						configManager.getTrafficApiServerURL(),
						configManager.getAppId(),
						userId,
						configManager.getAndroidOs(),
						postId,
						new NetworkRequestPBCallbackInterface()
						{
							
							@Override
							public void handleSuccessResponse(DataQueryResponse response)
							{
								tempPost = response.getBbsPost(0);
								completeHandler.onComplete(ErrorCode.ERROR_SUCCESS);
							}
							
							@Override
							public void handleFailureResponse(int errorCode)
							{
								completeHandler.onComplete(errorCode);
							}
						});
	}
	
	

	
	public void CreateBBSPostComment(
						String postId,
						String postUid,
						String actionId,
						String actionUid,
						String actionNickName,
						String briefText,
						int sourceActionType,
						int contentType,
						String textContent,
						String imageURL,
						byte[] drawData,
						String drawImage,
						MissionCompleteInterface completeHandler
						){
		int actionType = BBSStatus.ActionTypeComment;
		createBBSPostAction(
						postId, 
						actionId, 
						postUid, 
						actionUid, 
						actionNickName, 
						briefText, 
						sourceActionType, 
						contentType, 
						actionType, 
						textContent, 
						imageURL, 
						drawData,
						drawImage, 
						completeHandler);
	}
	
	
	
	public void createBBSPostSupport(String postId,String postUid,String briefText,MissionCompleteInterface completeHandler){
		createBBSPostAction(
					postId, 
					"", 
					postUid, 
					"", 
					"", 
					briefText, 
					BBSStatus.ActionTypeNO, 
					BBSStatus.ContentTypeNo, 
					BBSStatus.ActionTypeSupport, 
					"", 
					"", 
					null,
					"", 
					completeHandler);
	}
	
	
	
	public void createBBSPostComment(
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
							byte[]drawData,
							String drawImage,
							MissionCompleteInterface completeHandler
							)
	{
		createBBSPostAction(
					postId, 
					actionId, 
					postUid, 
					actionUid, 
					actionNickName, 
					briefText, 
					sourceActionType, 
					contentType, 
					actionType, 
					textContent, 
					imageURL, 
					drawData, 
					drawImage, 
					completeHandler);
	}
	
	
	
	public void payReward(
			String postId,
			String actionId,
			String actionUid,
			String actionNick,
			final MissionCompleteInterface completeHandler
			)
	{
		String userId = userManager.getUserId();
		//for test
		userId = userManager.getTestUserId();
		
		DrawNetworkRequest.bbsPayReward(
				configManager.getTrafficApiServerURL(), 
				configManager.getAppId(), 
				String.valueOf(configManager.getAndroidOs()), 
				userId, 
				postId, 
				actionId, 
				actionUid, 
				actionNick, 
				userManager.getUser().getAvatar(), 
				GenderUtils.toString(userManager.getUser().getGender()), 
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
	
	
	
	public BBSManager getBoardManager()
	{
		return boardManager;
	}
	public BBSManager getHotPostManager()
	{
		return hotPostManager;
	}
	public BBSManager getNewPostManager()
	{
		return newPostManager;
	}
	public BBSManager getPostCommentManager()
	{
		return postCommentManager;
	}
	public BBSManager getPostSupportManager()
	{
		return postSupportManager;
	}
	public PBBBSPost getTempPost()
	{
		return tempPost;
	}
	
	
	
}
