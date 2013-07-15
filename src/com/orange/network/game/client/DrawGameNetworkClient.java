package com.orange.network.game.client;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.Channel;

import com.orange.game.draw.model.draw.DrawWord;
import com.orange.network.game.protocol.constants.GameConstantsProtos.GameCommandType;
import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;
import com.orange.network.game.protocol.message.GameMessageProtos.JoinGameRequest;
import com.orange.network.game.protocol.message.GameMessageProtos.JoinGameRequestOrBuilder;
import com.orange.network.game.protocol.message.GameMessageProtos.SendDrawDataRequest;
import com.orange.network.game.protocol.message.GameMessageProtos.StartGameRequest;
import com.orange.network.game.protocol.message.GameMessageProtos.JoinGameRequest.Builder;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

import android.content.Context;
import android.util.Log;

public class DrawGameNetworkClient extends CommonNetworkClient {

	private static final String DRAW_GAME_ID = "DRAW_GAME";

	AtomicInteger messageId = new AtomicInteger(0);
	
	public DrawGameNetworkClient(Context context) {
		super(context);
	}
	
	private void send(GameMessage message){
		if (message == null || channel == null || !channel.isConnected() || !channel.isWritable()){
			Log.e(TAG, "<send> channel null, not connected or writable");
			return;
		}
		
		channel.write(message);
		Log.d(TAG, "<send> message="+message.toString());
	}
	
	public void sendSimpleMessage(GameCommandType command, String userId, int sessionId){
		GameMessage message = GameMessage.newBuilder()
			.setMessageId(messageId.getAndIncrement())
			.setCommand(command)
			.setUserId(userId)
			.setSessionId(sessionId)
			.build();
	
		send(message);
	}
	
	public void sendJoinGameRequest(PBGameUser user, int sessionId){
		JoinGameRequest.Builder builder = JoinGameRequest.newBuilder()
			.setUserId(user.getUserId())
			.setNickName(user.getNickName())
			.setGender(user.getGender())
			.setAvatar(user.getAvatar())
			.setLocation(user.getLocation())
//			.setUserLevel(user.getUserLevel())
//			.addAllSnsUsers(user.getSnsUsersList())
			.setGameId(DRAW_GAME_ID);
		
		if (sessionId > 0){
			builder.setSessionToBeChange(sessionId);
		}
		
		JoinGameRequest request = builder.build();

		GameMessage message = GameMessage.newBuilder()
			.setMessageId(messageId.getAndIncrement())
			.setCommand(GameCommandType.JOIN_GAME_REQUEST)
			.setJoinGameRequest(request).build();
		
		send(message);
	}

	public void sendStartGameRequest(PBGameUser user, int sessionId) {
		sendSimpleMessage(GameCommandType.START_GAME_REQUEST, user.getUserId(), sessionId);
	}

	public void sendDrawWord(DrawWord word, int sessionId, String userId) {
		SendDrawDataRequest.Builder builder = SendDrawDataRequest.newBuilder();
		builder.setWord(word.getText());
		builder.setLanguage(word.getLanguage());
		builder.setLevel(word.getLevel());
		
		SendDrawDataRequest request = builder.build();
		
		GameMessage message = GameMessage.newBuilder()
			.setCommand(GameCommandType.SEND_DRAW_DATA_REQUEST)
			.setUserId(userId)
			.setSessionId(sessionId)
			.setMessageId(messageId.getAndIncrement())
			.setSendDrawDataRequest(request)
			.build();
		
		send(message);
	}


	
}
