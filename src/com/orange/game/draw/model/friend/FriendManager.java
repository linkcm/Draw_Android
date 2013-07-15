package com.orange.game.draw.model.friend;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

	final int friendType;
	List<Friend> friendList = new ArrayList<Friend>();

	public FriendManager(int friendType){
		this.friendType = friendType;
	}
	
	public int getFriendType() {
		return friendType;
	}

	public void addFriend(Friend friend) {
		friendList.add(friend);
	}

	public void addFriendToHead(Friend friend)
	{
		friendList.add(0, friend);
	}
	
	public void clear(){
		friendList.clear();
	}
	
	public int getCurrentOffset(){
		return friendList.size();
	}
	
	public List<Friend> getFriendList(){
		return friendList;
	}

	
	public void remove(Friend friend)
	{
		String userId = friend.getUser().getUserId();
		int i = 0;
		for (Friend arg:friendList)
		{
			if (arg.getUser().getUserId().equalsIgnoreCase(userId))
			{
				friendList.remove(i);
				return;
			}
			i++;
		}
		
	}
		
}
