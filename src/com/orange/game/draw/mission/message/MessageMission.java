package com.orange.game.draw.mission.message;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.orange.common.android.utils.FileUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.constants.FileNameConstants;
import com.orange.game.draw.constants.LanguageType;
import com.orange.game.draw.mission.common.CommonMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.friend.FriendManager;
import com.orange.game.draw.model.message.MessageManager;
import com.orange.game.draw.model.message.PPMessage;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.network.NetworkRequestJSONCallbackInterface;
import com.orange.game.draw.network.NetworkRequestPBCallbackInterface;
import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;
import com.orange.network.game.protocol.model.GameBasicProtos.PBMessage;

public class MessageMission extends CommonMission {

	
	public enum ReadDirection {
		FORWARD(1),
		BACKWARD(0);

	    final int value;

	    ReadDirection(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
	};
	
	protected static final String TAG = "MessageMission";
	private static final String MESSAGE = "msg_";
	
	
	final MessageManager myMessageManager = new MessageManager();
	
	// thread-safe singleton implementation
	private static MessageMission sharedInstance = new MessageMission();
	private MessageMission() {

	}
	public static MessageMission getInstance() {
		return sharedInstance;
	}
	
	public MessageManager getMyMessageManager(){
		return myMessageManager;
	}
	
	public void getMessageStatList(				
			final boolean isReload,
			final int offset, 
			final int limit, 
			final MissionCompleteInterface completeHandler) {
		String userId = userManager.getUserId();				
		userId = userManager.getTestUserId();		// TODO for test
		
		if (userId == null){
			Log.i(TAG, "<getMessageStatList> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		DrawNetworkRequest.getMessageStatList(
				configManager.getTrafficApiServerURL(),
				userId, 
				offset, 
				limit, 
				new NetworkRequestPBCallbackInterface() {

					@Override
					public void handleSuccessResponse(DataQueryResponse response) {
						Log.i(TAG, "<getMessageStatList> total "+response.getFeedCount()+ " message stat got");

						if (isReload){
							myMessageManager.clearMessageStat();
						}
												
						myMessageManager.addMessageStatList(response.getMessageStatList(), offset, limit);
						
						// notify UI to update
						callCompleteHandler(completeHandler, 0);
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);
					}
				});		
		
	}
	
	public void getMessageList(final String targetUserId,			
			final String offsetMessageId,
			final ReadDirection readType,
			final int limit, 
			final MissionCompleteInterface completeHandler) {
		
		String userId = userManager.getUserId();				
		userId = userManager.getTestUserId();		// TODO for test
		
		if (userId == null){
			Log.i(TAG, "<getMessageList> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		DrawNetworkRequest.getMessageList(
				configManager.getTrafficApiServerURL(),
				userId,
				targetUserId,
				offsetMessageId,
				readType.intValue(),
				limit, 
				new NetworkRequestPBCallbackInterface() {

					@Override
					public void handleSuccessResponse(DataQueryResponse response) {
						Log.i(TAG, "<getMessageList> total "+response.getFeedCount()+ " messages got");
												
						myMessageManager.addMessageList(targetUserId, response.getMessageList());
						
						// notify UI to update
						callCompleteHandler(completeHandler, 0);
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);
					}
				});				
	}
	
	
	public void sendTextMessage(final String targetUserId, final String text, final MissionCompleteInterface completeHandler){

		String userId = userManager.getUserId();				
		userId = userManager.getTestUserId();		// TODO for test
		
		if (userId == null){
			Log.i(TAG, "<sendTextMessage> but userId is null");
			callCompleteHandler(completeHandler, ErrorCode.ERROR_USERID_NOT_FOUND);
			return;
		}
		
		DrawNetworkRequest.sendTextMessage(
				configManager.getTrafficApiServerURL(),
				userId,
				targetUserId,
				text,
				new NetworkRequestJSONCallbackInterface() {

					@Override
					public void handleSuccessResponse(JSONObject jsonData) {
						Log.i(TAG, "<sendTextMessage> success");
						
						// TODO update model
						
						
						// notify UI to update
						callCompleteHandler(completeHandler, 0);						
					}

					@Override
					public void handleFailureResponse(int errorCode) {
						callCompleteHandler(completeHandler, errorCode);						
					}


				});						
	}
	
	
	
	public void saveMessageInFile(String targetUserId){
		String fileName = MESSAGE+targetUserId+FileNameConstants.PB_FILE;
		String filePath = FileNameConstants.MESSAGE_CACHE_PATH;
		List<PPMessage> list = myMessageManager.getMessageList(targetUserId);
		if(list==null||list.isEmpty())
			return;
		List<PBMessage> pbMessages = new ArrayList<PBMessage>(); 
		DataQueryResponse.Builder builder = DataQueryResponse.newBuilder();
		for (PPMessage ppMessage:list)
		{
			pbMessages.add(ppMessage.getPbMessage());
		}
		builder.setResultCode(0);
		builder.addAllMessage(pbMessages);
		FileUtil.saveFileInSDCard(fileName, filePath, new ByteArrayInputStream(builder.build().toByteArray()));		
	}
	
	
	public String  getLastMessageIdFromFile(String targetUserId){
		String fileName = MESSAGE+targetUserId+FileNameConstants.PB_FILE;
		String filePath = FileNameConstants.MESSAGE_CACHE_PATH+fileName;
		if (FileUtil.checkFileIsExits(filePath))
		{
			return "";
		}
		FileInputStream fileInputStream = FileUtil.readFile(filePath);
		if (fileInputStream == null)
		{
			return "";
		}
		DataQueryResponse response = null;
		try
		{
			response = DataQueryResponse.parseFrom(fileInputStream);
			if (response.getMessageCount()==0)
			{
				return "";
			}else{
				int size = response.getMessageCount();
				PBMessage message = response.getMessage(size-1);
				return message.getMessageId();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			Log.e(TAG, "<getLastMessageIdFromFile> but catch exception = "+e.toString());
			return "";
		}finally{
			try
			{
				if (fileInputStream!=null)
				{
					fileInputStream.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
			
		
		
	}
	
	
	public void readMessage(String messageUserId, final MissionCompleteInterface completeHandler){
		
	}	
	
	public void deleteMessageStat(String messageUserId, final MissionCompleteInterface completeHandler){
		
	}	
	
	public void deleteMessage(String messageId, final MissionCompleteInterface completeHandler){
		
	}	
}
