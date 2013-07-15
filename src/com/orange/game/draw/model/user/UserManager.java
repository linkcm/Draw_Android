package com.orange.game.draw.model.user;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.orange.game.constants.ServiceConstant;
import com.orange.game.draw.model.db.DbManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;
import com.orange.network.game.protocol.model.GameBasicProtos.PBSNSUser;

public class UserManager {

	private static final String TAG = UserManager.class.getName();
	PBGameUser user; // user object
	
	private PBGameUser tempGameUser;
	private int tempUserRelationShip;
	int fanCount = 0;
	int followCount = 0;
	String localAvatarPath = null;
	
	// thread-safe singleton implementation
	private static UserManager manager = new UserManager();
	
	private UserManager() {
		loadUser();
	}	
	
	public static UserManager getInstance() {
		return manager;
	}

	public void loadUser(){
		this.user = DbManager.getInstance().getUser();
	}
	
	public PBGameUser getUser(){
		
//		// for test, construct user object
		PBGameUser.Builder builder = PBGameUser.newBuilder();
		builder.setUserId(getTestUserId());
		builder.setGender(true);
		builder.setLocation("世界 火星");
		builder.setNickName("pipi");
		builder.setAvatar("http://img.you100.me:8080/upload/20130108/008dba50-5920-11e2-9dd9-00163e017d22.jpg");
		
		user = builder.build();
		
		return user;
	}
	
	public String getUserId() {
		if (user == null)
			return null;
		
		return user.getUserId();
	}
	
	
	public static String nickNameByEmail(String email) {
		int index = email.indexOf('@');
		String nickNameString = email.substring(0, index);
		return nickNameString;
	}
	
	public void save() {
		// save to share perfenrence
		DbManager.getInstance().saveUser(this.user);
	}
	
	public void save(PBGameUser newUser) {
		Log.i(TAG, "save user="+newUser.toString());
		this.user = newUser;
		
		// save to share perfenrence
		DbManager.getInstance().saveUser(this.user);
	}
	
	public void setLocation(String location) {
		if (this.user == null) {
			return;
		}
		
		if (location == null) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		builder.setLocation(location);
		
		this.user = builder.build();
	}
	
	public void setSinaSNSUser(String userId, String sinaId, String password,
			String nickName, String gender, String avatarURL, 
			String accessToken, String accessTokenSecret) {
		setSNSUser(ServiceConstant.REGISTER_TYPE_SINA, userId, sinaId, 
				password, nickName,
				gender, avatarURL, accessToken, accessTokenSecret);
	}
	
	public void setQQSNSUser(String userId, String qqId, String password,
			String nickName, String gender, String avatarURL, 
			String accessToken, String accessTokenSecret) {
		setSNSUser(ServiceConstant.REGISTER_TYPE_QQ, userId, qqId, 
				password, nickName,
				gender, avatarURL, accessToken, accessTokenSecret);
	}
	
	public void setFacebookSNSUser(String userId, String facebookId, String password,
			String nickName, String gender, String avatarURL, 
			String accessToken, String accessTokenSecret) {
		setSNSUser(ServiceConstant.REGISTER_TYPE_FACEBOOK, userId, facebookId, 
				password, nickName,
				gender, avatarURL, accessToken, accessTokenSecret);
	}
	
	private void setSNSUser(int type, String userId, String snsId, String password, String nickName, 
			String gender, String avatarURL, String accessToken,
			String accessTokenSecret) {

		setUserId(userId);
		setNickName(nickName);
		setPassword(password);
		setAvatar(avatarURL);
		setGender(GenderUtils.boolFromString(gender));

	    setSNSUser(type, snsId, nickName, accessToken, accessTokenSecret);
	    
	    Log.d(TAG, this.user.toString());
	}
	
	public String getTestUserId() {
		//return "50d2bb69e4b0d73d234df6af";
		 // return "4f95717e260967aa715a5af4";
		return "4f87cc95260958163895b95b";
	}
	
	public String getGenderString() {
		if (user == null)
			return GenderUtils.FEMALE;
		
		return (GenderUtils.toString(user.getGender()));
	}
	
	public void setFanCount(int fanCount) {
		this.fanCount = fanCount;
	}
	
	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}
	
	public int getFanCount() {
		return fanCount;
	}
	
	public int getFollowCount() {
		return followCount;
	}
	public PBGameUser getTempGameUser()
	{
		return tempGameUser;
	}
	public void setTempGameUser(PBGameUser tempGameUser)
	{
		this.tempGameUser = tempGameUser;
	}
	public int getTempUserRelationShip()
	{
		return tempUserRelationShip;
	}
	public void setTempUserRelationShip(int tempUserRelationShip)
	{
		this.tempUserRelationShip = tempUserRelationShip;
	}
	
	public void setLocalAvatar(String localAvatarPath){
		if (localAvatarPath == null) {
			return;
		}
		this.localAvatarPath = localAvatarPath;
	}
	
	public void setUserId(String userId){
		if (userId == null) {
			return;
		}
		
		PBGameUser.Builder builder = null;
		if (this.user != null) {
			builder = PBGameUser.newBuilder(this.user);
			builder.setUserId(userId);
		}else{
			builder = PBGameUser.newBuilder();
			builder.setUserId(userId);
			builder.setNickName("");
		}
		
		this.user = builder.build();
	}
	
	
	public void setEmail(String email){
		if (this.user == null) {
			return;
		}
		
		if (email == null) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		builder.setEmail(email);
		
		this.user = builder.build();
	}
	
	public void setNickName(String nickName){
		if (this.user == null) {
			return;
		}
		
		if (nickName == null) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		builder.setNickName(nickName);
		
		this.user = builder.build();
	}
	
	public void setPassword(String password){
		if (this.user == null) {
			return;
		}
		
		if (password == null) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		builder.setPassword(password);
		
		this.user = builder.build();
	}
	
	public void setAvatar(String avatar){
		if (this.user == null) {
			return;
		}
		
		if (avatar == null || avatar.length() <= 0) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		builder.setAvatar(avatar);
		
		this.user = builder.build();
	}
	
	public void setGender(Boolean gender){
		if (this.user == null) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		builder.setGender(gender);
		this.user = builder.build();
	}
	
	private void setSNSUser(int type, String snsId, String nickName,
			String accessToken, String accessTokenSecret){
		setSNSUser(type, snsId, nickName, 
				accessTokenSecret, accessTokenSecret, null, 0, null);
	}

	private void setSNSUser(int type, String snsId, String nickName,
			String accessToken, String accessTokenSecret, String refreshToken,
			int expireTime, String qqOpenId) {
		
		if (snsId == null) {
			return;
		}
		
		if (nickName == null) {
			return;
		}
		
		if (this.user == null) {
			return;
		}
		
		removeSNSUser(type);
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		
		PBSNSUser.Builder snsUserBuilder = PBSNSUser.newBuilder();

		snsUserBuilder.setType(type);
		snsUserBuilder.setUserId(snsId);
		snsUserBuilder.setNickName(nickName);
		
		if (accessToken != null) {
			snsUserBuilder.setAccessToken(accessToken);
		}
		
		if (accessTokenSecret != null) {
			snsUserBuilder.setAccessTokenSecret(accessTokenSecret);
		}
		
		if (refreshToken != null) {
			snsUserBuilder.setRefreshToken(refreshToken);
		}
		
		snsUserBuilder.setExpireTime(expireTime);
		
		if (qqOpenId != null) {
			snsUserBuilder.setQqOpenId(qqOpenId);
		}
		
		PBSNSUser newSnsUser =snsUserBuilder.build();
	
		builder.addSnsUsers(newSnsUser);
		
		this.user = builder.build();
	}
	
	private void removeSNSUser(int type) {
		
		if (this.user == null) {
			return;
		}
		
		PBGameUser.Builder builder = PBGameUser.newBuilder(this.user);
		
		for (int i=0; i<builder.getSnsUsersCount(); i++) {
			PBSNSUser snsUser = builder.getSnsUsers(i);
			if (snsUser.getType() == type) {
				builder.removeSnsUsers(i);
			}
		}
		
		this.user = builder.build();
	}
}
