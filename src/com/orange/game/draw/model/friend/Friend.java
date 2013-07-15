package com.orange.game.draw.model.friend;

import java.util.List;

import org.json.JSONObject;

import com.orange.common.android.utils.JsonUtil;
import com.orange.common.android.utils.StringUtil;
import com.orange.game.constants.DBConstants;
import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;
import com.orange.network.game.protocol.model.GameBasicProtos.PBSNSUser;

public class Friend {
    
	public static final int FRIEND_UNKNOW = 0;
	public static final int FRIEND_FOLLOW = 1;
	public static final int FRIEND_FAN = 2;
	public static final int FRIEND_FOLLOW_FAN = 3;
	public static final int FRIEND_BLACK = 4;
	
	PBGameUser pbUser;
	int friendType;	

	public PBGameUser getUser(){
		return pbUser;
	}	
	
	public int getFriendType(){
		return friendType;
	}
	
	public static Friend parseFriend(JSONObject jsonData){
		Friend friend = new Friend();
		
		// TODO check what does friedn type mean here
//		friend.friendType = JsonUtil.getInt(jsonData, ServiceConstant.PARA_FRIEND_TYPE);
		friend.friendType = JsonUtil.getInt(jsonData, ServiceConstant.PARA_RELATION);
		
		PBGameUser.Builder builder = PBGameUser.newBuilder();
		
		builder.setUserId(JsonUtil.getString(jsonData, ServiceConstant.PARA_USERID, ""));
		builder.setNickName(JsonUtil.getString(jsonData, ServiceConstant.PARA_NICKNAME, ""));
		builder.setAvatar(JsonUtil.getString(jsonData, ServiceConstant.PARA_AVATAR, ""));
		builder.setGender(GenderUtils.boolFromString(JsonUtil.getString(jsonData, ServiceConstant.PARA_GENDER)));
		builder.setLocation(JsonUtil.getString(jsonData, ServiceConstant.PARA_LOCATION, ""));
		
		// set SINA
		String sinaId = JsonUtil.getString(jsonData, ServiceConstant.PARA_SINA_ID, "");
		String sinaNick = JsonUtil.getString(jsonData, ServiceConstant.PARA_SINA_NICKNAME, "");		
		if (!StringUtil.isEmpty(sinaNick)){
			PBSNSUser.Builder snsBuilder = PBSNSUser.newBuilder();
			snsBuilder.setUserId(sinaId);
			snsBuilder.setNickName(sinaNick);
			snsBuilder.setType(ServiceConstant.REGISTER_TYPE_SINA);
			builder.addSnsUsers(snsBuilder.build());
		}
		
		// set QQ
		String qqId = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_ID, "");
		String qqNick = JsonUtil.getString(jsonData, ServiceConstant.PARA_QQ_NICKNAME, "");		
		if (!StringUtil.isEmpty(qqNick)){
			PBSNSUser.Builder snsBuilder = PBSNSUser.newBuilder();
			snsBuilder.setUserId(qqId);
			snsBuilder.setNickName(qqNick);
			snsBuilder.setType(ServiceConstant.REGISTER_TYPE_QQ);
			builder.addSnsUsers(snsBuilder.build());
		}
				
		// set Facebook
		String facebookId = JsonUtil.getString(jsonData, ServiceConstant.PARA_FACEBOOKID);
		if (!StringUtil.isEmpty(facebookId)){
			PBSNSUser.Builder snsBuilder = PBSNSUser.newBuilder();
			snsBuilder.setUserId(facebookId);
			snsBuilder.setNickName("");
			snsBuilder.setType(ServiceConstant.REGISTER_TYPE_FACEBOOK);
			builder.addSnsUsers(snsBuilder.build());
		}		
		
		// set balance
		int balance = JsonUtil.getInt(jsonData, ServiceConstant.PARA_USER_COINS);
		builder.setCoinBalance(balance);
		
		// set level
		int level = JsonUtil.getInt(jsonData, ServiceConstant.PARA_LEVEL);
		if (level <= 0)
			level = 1;
		builder.setLevel(level);
		
		friend.pbUser = builder.build();				
		return friend;
	}
}
