package com.orange.game.draw.model.user;

import java.util.ArrayList;
import java.util.List;

import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

public class TopUserManager {

	List<PBGameUser> userList = new ArrayList<PBGameUser>();
	
	public TopUserManager(){
		
	}
	
	public void addUser(PBGameUser user) {
		userList.add(user);
	}

	public void clear(){
		userList.clear();
	}
	
	public int getCurrentOffset(){
		return userList.size();
	}
	
	public List<PBGameUser> getTopUserList(){
		return userList;
	}
		
	
}
