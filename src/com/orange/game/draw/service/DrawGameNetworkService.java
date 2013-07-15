package com.orange.game.draw.service;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ExceptionEvent;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.inputmethod.InputMethod.SessionCallback;

import com.google.protobuf.InvalidProtocolBufferException;
import com.orange.game.draw.model.draw.DrawWord;
import com.orange.game.draw.model.session.DrawGameSession;
import com.orange.game.draw.model.session.DrawGameSession.Status;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.client.DrawGameNetworkClient;
import com.orange.network.game.protocol.constants.GameConstantsProtos.GameCommandType;
import com.orange.network.game.protocol.constants.GameConstantsProtos.GameResultCode;
import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;
import com.orange.network.game.protocol.message.GameMessageProtos.GeneralNotification;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameSession;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;


public class DrawGameNetworkService extends Service {
	
	private static final String TAG = "DrawGameNetworkService";

	// for internal handler message
	public static final String KEY_GAME_MESSAGE = "KEY_GAME_MESSAGE";
	
	// for intent name
	public static final String ACTION_RECV_GAME_MESSAGE = "RECV_GAME_MESSAGE";
	public static final String ACTION_SERVER_CONNECTED = "SERVER_CONNECTED";
	public static final String ACTION_SERVER_DISCONNECTED = "SERVER_DISCONNECTED";
	
	// for intent bundle key
	public static final String GAME_MESSAGE = "GAME_MESSAGE";

	// session constants
	private static final int NULL_SESSION = -1;
	
	// managers
	private UserManager userManager = UserManager.getInstance();
	
	// network connection
	private DrawGameNetworkClient networkClient = new DrawGameNetworkClient(this);
	
	// data
	DrawGameSession drawSession = null;
	private int onlineUserCount = 0;
	
	Handler handler = new MyHandler();

	public class MyHandler extends Handler{

		@Override
        public void handleMessage(Message msg) {	
			// main UI thread handling
			Bundle bundle = msg.getData();
			if (bundle == null)
				return;
			
			byte[] data = bundle.getByteArray(KEY_GAME_MESSAGE);
			if (data == null)
				return;
			
			GameMessage gameMsg;
			try {
				gameMsg = GameMessage.parseFrom(data);
				DrawGameNetworkService.this.handleMessage(gameMsg, networkClient.getChannel());
			} catch (InvalidProtocolBufferException e) {
				return;
			} 
		}
	}
	
	private static DrawGameNetworkService instance = null;	
	public static DrawGameNetworkService getInstance() {
		return instance;
	}
	
	private void handleMessage(GameMessage message, Channel channel) {
		// TODO Auto-generated method stub
		
		switch (message.getCommand()) {
		case JOIN_GAME_RESPONSE:
			handleJoinGameResponse(message);
			break;
			
		case USER_JOIN_NOTIFICATION_REQUEST:
			handleNewUserJoin(message);
			break;
			
		case USER_QUIT_NOTIFICATION_REQUEST:
			handleUserQuit(message);
			break;
			
		case START_GAME_RESPONSE:
			handleStartGameResponse(message);
			break;
			
		case GAME_START_NOTIFICATION_REQUEST:
			hanldeGameStart(message);
			break;
			
		case NEW_DRAW_DATA_NOTIFICATION_REQUEST:
			handleDrawData(message);
			break;
			
		case CLEAN_DRAW_NOTIFICATION_REQUEST:
			handleCleanDraw(message);
			break;
			
		case GAME_TURN_COMPLETE_NOTIFICATION_REQUEST:
			handleGameTurnComplete(message);
			break;
			
		case CHAT_NOTIFICATION_REQUEST:
			handleChatNotification(message);
			break;
			
		default:
			break;
		}
		
		brocastGameMessage(message);
	}
	
	private void handleChatNotification(GameMessage message) {
		// TODO Auto-generated method stub
		
	}

	private void handleGameTurnComplete(GameMessage message) {
		drawSession.setStatus(Status.SESSION_WAITING);
		updateOnlineUserCount(message);
		updateSessionByGameNotification(message.getNotification());
		drawSession.resetCurrentTurn();
	}

	private void handleCleanDraw(GameMessage message) {
		// TODO save draw action, refer to iOS
		
	}

	private void handleDrawData(GameMessage message) {
		// TODO Auto-generated method stub
		updateCurrentTurn(message.getNotification());
		if (hasWord(message)){
			drawSession.setStatus(Status.SESSION_PLAYING);			
		}		
		else if (hasGuessWord(message)){
			
		}
		
		//TODO save draw data refer to iOS
	}

	public static boolean hasGuessWord(GameMessage message) {
		if (message.getNotification().hasGuessWord() && message.getNotification().getGuessWord().length() > 0)
			return true;
		return false;
	}

	public static boolean hasWord(GameMessage message) {
		if (message.getNotification().hasWord() && message.getNotification().getWord().length() > 0)
			return true;
		return false;
	}

	private void updateCurrentTurn(GeneralNotification notification) {
		if (notification.hasWord() && notification.getWord().length() > 0){
			DrawWord word = new DrawWord(notification.getWord(), 
					notification.getLanguage(), notification.getLevel());
			
			drawSession.setDrawWord(word);
		}
	}

	private void hanldeGameStart(GameMessage message) {
		drawSession.setStatus(Status.SESSION_PICK_WORD);
		updateSessionByGameNotification(message.getNotification());
		drawSession.increaseRound();		
	}

	private void handleUserQuit(GameMessage message) {
		updateSessionByGameNotification(message.getNotification());
		updateOnlineUserCount(message);		
	}

	private void handleStartGameResponse(GameMessage message) {
		if (message.getResultCode() == GameResultCode.SUCCESS){
			drawSession.setStatus(DrawGameSession.Status.SESSION_PICK_WORD);
			drawSession.setCurrentPlayUserId(message.getStartGameResponse().getCurrentPlayUserId());
			
			// TODO
			// remove all draw actions, refer to iOS implementation
		}
	}

	private void handleNewUserJoin(GameMessage message) {
		updateSessionByGameNotification(message.getNotification());
		updateOnlineUserCount(message);
	}

	private void handleJoinGameResponse(GameMessage message) {
		if (message.getResultCode() == GameResultCode.SUCCESS){
			PBGameSession pbSession = message.getJoinGameResponse().getGameSession();
			drawSession = new DrawGameSession(pbSession, userManager.getUser());
			updateOnlineUserCount(message);
		}
	}

	private void updateOnlineUserCount(GameMessage message) {
		if (message.hasOnlineUserCount()){
			this.onlineUserCount = message.getOnlineUserCount();
		}
	}
	
	private void updateSessionByGameNotification(GeneralNotification notification){
		if (drawSession == null){
			Log.d(TAG, "<updateSessionByGameNotification> but session null");
			return;
		}
		
		if (notification.hasCurrentPlayUserId() && notification.getCurrentPlayUserId().length() > 0){
			drawSession.setCurrentPlayUserId(notification.getCurrentPlayUserId());
		}
		
		if (notification.hasNewUserId() && notification.getNewUserId().length() > 0){
			PBGameUser.Builder builder = PBGameUser.newBuilder();
			builder.setUserId(notification.getNewUserId());
			builder.setAvatar(notification.getUserAvatar());
			builder.setNickName(notification.getNickName());
			builder.setGender(notification.getUserGender());
			builder.setLocation(notification.getLocation());
			builder.setUserLevel(notification.getUserLevel());
			builder.addAllSnsUsers(notification.getSnsUsersList());
			
			PBGameUser user = builder.build();
			drawSession.addUser(user);
		}
		
		if (notification.hasQuitUserId() && notification.getQuitUserId().length() > 0){
			drawSession.removeUser(notification.getQuitUserId());
		}
		
	}

	public void handleException(ExceptionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleChannelDisconnected(Channel channel) {
		brocastGameMessage(ACTION_SERVER_DISCONNECTED);
	}
	
	public void handleChannelConnected(Channel channel) {
		brocastGameMessage(ACTION_SERVER_CONNECTED);
	}

	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(TAG, "<onCreate>");
		instance = this;		
	}
	
	@Override
	public void onStart(Intent intent, int startId){				
		Log.d(TAG, "<onStart>");
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	private final IBinder binder = new MyBinder();
	public class MyBinder extends Binder {  
		public DrawGameNetworkService getService() {  
            return DrawGameNetworkService.this;  
        }  
    }
	
	private void brocastGameMessage(GameMessage message){
		Log.d(TAG, "<brocastGameMessage>");
		Intent intent = new Intent(ACTION_RECV_GAME_MESSAGE);
		intent.putExtra(GAME_MESSAGE, message.toByteArray());
		sendBroadcast(intent);				
	}

	private void brocastGameMessage(String info){
		Log.d(TAG, "<brocastGameMessage>, info="+info);
		Intent intent = new Intent(info);
		sendBroadcast(intent);				
	}
	
	public void connect(String host, int port){
		networkClient.connect(host, port);
	}
	
	public void disconnect(){
		networkClient.disconnect();
	}
	
	public void joinGame(){
		PBGameUser user = userManager.getUser();
		networkClient.sendJoinGameRequest(user, NULL_SESSION);
	}

	public void startGame() {
		if (drawSession == null)
			return;
		
		PBGameUser user = userManager.getUser();
		networkClient.sendStartGameRequest(user, drawSession.getSessionId());
	}

	public Handler getHandler() {
		return handler;
	}

	public DrawGameSession getSession() {
		return drawSession;
	}

	public boolean isMyTurn() {
		String userId = userManager.getUser().getUserId();				
		return drawSession.isCurrentPlayUser(userId);
	}

	public void startDraw(DrawWord word) {
		drawSession.setDrawWord(word);
		
		// send request to server
		networkClient.sendDrawWord(word, drawSession.getSessionId(), userManager.getUser().getUserId());
	}
}
