/**  
        * @title DbManager.java  
        * @package com.orange.game.draw.model.db  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-1 下午4:21:13  
        * @version V1.0  
 */
package com.orange.game.draw.model.db;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.util.Log;



import com.google.protobuf.InvalidProtocolBufferException;
import com.orange.common.android.utils.FileUtil;
import com.orange.game.application.DrawApplication;
import com.orange.game.constants.FileNameConstants;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;
import com.orange.network.game.protocol.model.GameBasicProtos.PBSNSUser;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-2-1 下午4:21:13  
 */

public class DbManager
{
	// thread-safe singleton implementation
		private static DbManager sharedInstance = new DbManager();
		
		private static final String TAG = "DbManager";
		private Context context;
		
		
		private DbManager() {
			context = DrawApplication.getInstance();
		}		
		public static DbManager getInstance() {
			
			return sharedInstance;
		}
		
		private static final String USER_ACTION_FLOWER_DB_NAME = "user_flower_action";
		private static final String USER_ACTION_TOMATO_DB_NAME = "user_tomato_action";
		private static final String USER_GUESS_DB_NAME  = "user_guess";
		private static final String BBS_POST_ACTION = "bbs_post_action";
		private static final String BBS_POST_ACTION_SUPPORT = "bbs_action_support";
		
		private static final String USER_BALANCE  = "user_balance";

		private static final String USER_COIN_BALANCE = "USER_COIN_BALANCE";
		private static final String USER_INGOT_BALANCE = "USER_INGOT_BALANCE";

		public void saveUserGiveFlowerHistory(String opusId,int count)
		{
			//SharedPreferences userActionDB = context.getSharedPreferences(USER_ACTION_FLOWER_DB_NAME, Activity.MODE_PRIVATE);
			SharedPreferences userActionDB = getSharePre(USER_ACTION_FLOWER_DB_NAME);
			Editor userActionDBEditor = userActionDB.edit();  
			userActionDBEditor.putInt(opusId,count);  
			userActionDBEditor.commit();  
		}
		
		
		public void removeUserFlowerHistory(String opusId)
		{
			int count = getUserGiveFlowerCount(opusId);
			count = count-1;
			saveUserGiveFlowerHistory( opusId, count);
		}
		
		public int getUserGiveFlowerCount (String opusId)
		{
			//SharedPreferences userActionDB = context.getSharedPreferences(USER_ACTION_FLOWER_DB_NAME, Activity.MODE_PRIVATE);  
			
			SharedPreferences userActionDB = getSharePre(USER_ACTION_FLOWER_DB_NAME);
			int count = userActionDB.getInt(opusId, 0);
			return count;  
		}
		
		public void saveUserGiveTomatoHistory(String opusId,int count)
		{
			//SharedPreferences userActionDB = context.getSharedPreferences(USER_ACTION_TOMATO_DB_NAME, Activity.MODE_PRIVATE);
			
			SharedPreferences userActionDB = getSharePre(USER_ACTION_TOMATO_DB_NAME);
			Editor userActionDBEditor = userActionDB.edit();  
			userActionDBEditor.putInt(opusId,count);  
			userActionDBEditor.commit();  
		}
		
		public void  removeUserTomatoHistory(String opusId)
		{
			int count = getUserGiveTomatoCount(opusId);
			count = count-1;
			saveUserGiveTomatoHistory(opusId, count);
		}
		
		public int getUserGiveTomatoCount (String opusId)
		{
			//SharedPreferences userActionDB = context.getSharedPreferences(USER_ACTION_TOMATO_DB_NAME, Activity.MODE_PRIVATE);  
			SharedPreferences userActionDB = getSharePre(USER_ACTION_TOMATO_DB_NAME);
			int count = userActionDB.getInt(opusId, 0);
	        return count;  
		}
		
		
		public void  saveUserGuessWord(String opusId,String guessWord)
		{
			//SharedPreferences userGuessDB = context.getSharedPreferences(USER_GUESS_DB_NAME, Activity.MODE_PRIVATE);
			SharedPreferences userGuessDB = getSharePre(USER_GUESS_DB_NAME);
			Editor userGuessDBEditor = userGuessDB.edit();  
			userGuessDBEditor.putString(opusId,guessWord);  
			userGuessDBEditor.commit(); 
		}
		
		public String  getUserGuessWord(String opusId)
		{
			//SharedPreferences userGuessDB = context.getSharedPreferences(USER_GUESS_DB_NAME, Activity.MODE_PRIVATE);  
			SharedPreferences userGuessDB = getSharePre(USER_GUESS_DB_NAME);
			String guessWord = userGuessDB.getString(opusId,"");
	        return guessWord; 
		}
		
		
		
		public void saveUserSupportPost(String postId){
			SharedPreferences userBBSAction = getSharePre(BBS_POST_ACTION);
			Editor userBBSActionEditor = userBBSAction.edit();  
			userBBSActionEditor.putBoolean(postId,true);  
			userBBSActionEditor.commit(); 
			
		}
		
		
		public boolean checkPostIsSupported(String postId){
			SharedPreferences userBBSAction = getSharePre(BBS_POST_ACTION);
			return userBBSAction.getBoolean(postId, false);
		}
		
		public void saveUser(PBGameUser user) {
		    Context context = DrawApplication.getInstance();
			FileUtil.saveFileAsByte(context, FileNameConstants.USER_BASIC_INFO, user.toByteArray());
		}

		public PBGameUser getUser() {
		    PBGameUser user = null;
		    Context context = DrawApplication.getInstance();
		    byte[] bytes = FileUtil.readFileAsByte(context, FileNameConstants.USER_BASIC_INFO);
		    try {
				user = PBGameUser.parseFrom(bytes);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		    
		    return user;
		}
		
		private SharedPreferences getSharePre(String dbName){
			return context.getSharedPreferences(dbName, Activity.MODE_PRIVATE); 
		}
		
		public void saveCoinBalance(int coinBalance) {
			Editor shareEditor = DrawApplication.getInstance().getSharedPreferences(USER_BALANCE, Service.MODE_PRIVATE).edit();
			shareEditor.putInt(USER_COIN_BALANCE, coinBalance);
			shareEditor.commit();
		}
		
		public void saveIngotBalance(int ingotBalance) {
			Editor shareEditor = DrawApplication.getInstance().getSharedPreferences(USER_BALANCE, Service.MODE_PRIVATE).edit();
			shareEditor.putInt(USER_INGOT_BALANCE, ingotBalance);
			shareEditor.commit();
		}
		
		public int getCoinBalance() {
			SharedPreferences sharedPreferences = DrawApplication.getInstance().getSharedPreferences(USER_BALANCE, Service.MODE_PRIVATE);
			int coinBalance = sharedPreferences.getInt(USER_COIN_BALANCE, 0);
			return coinBalance;
		}
		
		public int getIngotBalance() {
			SharedPreferences sharedPreferences = DrawApplication.getInstance().getSharedPreferences(USER_BALANCE, Service.MODE_PRIVATE);
			int ingotBalance = sharedPreferences.getInt(USER_INGOT_BALANCE, 0);
			return ingotBalance;
		}
}
