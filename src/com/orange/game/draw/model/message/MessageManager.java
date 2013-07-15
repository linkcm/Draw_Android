package com.orange.game.draw.model.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.orange.network.game.protocol.model.GameBasicProtos.PBMessage;
import com.orange.network.game.protocol.model.GameBasicProtos.PBMessageStat;

public class MessageManager {

	final List<MessageStat> messageStatList = new CopyOnWriteArrayList<MessageStat>();	
	final ConcurrentHashMap<String, CopyOnWriteArrayList<PPMessage>> userMessages = new ConcurrentHashMap<String, CopyOnWriteArrayList<PPMessage>>();
	
	public void addMessageStatList(
			List<PBMessageStat> newMessageStatList,
			int offset, 
			int limit) {

		if (messageStatList == null || offset < 0){
			return;
		}

		int newLen = newMessageStatList.size();
		int existDataLen = messageStatList.size();
		for (int i=0; i<newLen; i++){
			PBMessageStat pbMessageStat = newMessageStatList.get(i);
			MessageStat stat = new MessageStat(pbMessageStat);
			
			if (offset+i < existDataLen){
				// replace exist 
				messageStatList.set(i, stat);
			}
			else{
				// insert new one
				messageStatList.add(stat);
			}
		}			
	}

	public void clearMessageStat(){
		messageStatList.clear();
	}

	public List<MessageStat> getMessageStatList() {
		return messageStatList;
	}

	
	public synchronized void addMessageList(String targetUserId, List<PBMessage> messageList) {
		if (targetUserId == null || messageList == null)
			return;
		
		CopyOnWriteArrayList<PPMessage> list = null;				
		if (!userMessages.containsKey(targetUserId)){
			list = new CopyOnWriteArrayList<PPMessage>();
			userMessages.put(targetUserId, list);
		}
		else{
			list = userMessages.get(targetUserId);
		}
		
		// add message, here we avoid duplicate message
		for (PBMessage pbMessage : messageList){
			PPMessage msg = new PPMessage(pbMessage);
			list.addIfAbsent(msg);
		}						
	}	
	
	public synchronized void deleteMessageList(String targetUserId){
		if (targetUserId == null)
			return;
		
		if (userMessages.containsKey(targetUserId)){
			userMessages.remove(targetUserId);
		}
	}
	
	
	public synchronized List<PPMessage> getMessageList(String targetUserId){
		if (targetUserId == null)
			return Collections.emptyList();
		if (userMessages.containsKey(targetUserId))
		{
			return userMessages.get(targetUserId);
		}else {
			return Collections.emptyList();
		}
	}
	
	
	public synchronized MessageStat getMessageStatByUserId(String userId)
	{
		if (userId == null)
			return null;
		for (MessageStat messageStat:messageStatList)
		{
			if (messageStat.getPBMessageStat().getFriendUserId() == userId)
			{
				return messageStat;
			}
		}
		return null;
	}
	
}
