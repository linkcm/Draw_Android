package com.orange.game.draw.model.session;

import java.util.ArrayList;
import java.util.List;

import com.orange.game.draw.model.draw.DrawWord;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameSession;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

public class DrawGameSession {

	public enum Status{
		SESSION_WAITING,
		SESSION_PICK_WORD,
		SESSION_PLAYING
	};
	
	int 							sessionId;
	PBGameUser 				user;
	String						roomName;
	List<PBGameUser>	userList = new ArrayList<PBGameUser>();
	List<PBGameUser>	deleteUserList;
	Status 						status;
	int							roundNumber;
	
	DrawGameTurn			currentTurn;
	
	public DrawGameSession(PBGameSession pbSession, PBGameUser user) {
		this.user = user;
		this.roomName = pbSession.getName();
		this.sessionId = (int) pbSession.getSessionId();
		this.status = Status.SESSION_WAITING;
		this.userList.addAll(pbSession.getUsersList());
		this.roundNumber = 1;
		this.currentTurn = new DrawGameTurn();
		this.currentTurn.currentPlayUserId = pbSession.getCurrentPlayUserId();
	}

	public int getSessionId() {
		return sessionId;
	}

	public List<PBGameUser> getUserList() {
		return userList;
	}

	public void setCurrentPlayUserId(String currentPlayUserId) {
		if (currentTurn == null)
			return;
		
		this.currentTurn.currentPlayUserId = currentPlayUserId;
	}

	public void addUser(PBGameUser user) {
		userList.add(user);
	}

	public void removeUser(String quitUserId) {
		if (quitUserId == null)
			return;
		
		for (PBGameUser user : userList){
			if (user.getUserId().equals(quitUserId)){
				deleteUserList.add(user);
				userList.remove(user);
				break;
			}
		}
	}

	public boolean isCurrentPlayUser(String userId) {
		if (currentTurn == null || currentTurn.currentPlayUserId == null)
			return false;
		
		return currentTurn.currentPlayUserId.equals(userId);
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setDrawWord(DrawWord word) {
		if (currentTurn == null)
			return;
		
		currentTurn.setWord(word);
	}

	public void resetCurrentTurn() {
		// TODO not implemented yet Benson refer to iOS
		currentTurn.setWord(null);		
	}

	public void increaseRound() {
		roundNumber ++;
	}
}
