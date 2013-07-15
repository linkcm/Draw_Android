package com.orange.game.draw.model;

import com.orange.game.R.string;
import com.orange.game.constants.DBConstants;
import com.orange.game.draw.constants.LanguageType;


public class ConfigManager {

	public static final String SINA_APP_KEY = "176806015";
	public static final String SINA_REDIRECT_URL = "http://www.sina.com";
	public static final String QQ_APP_KEY = "801317066";
	public static final String QQ_APP_SECRET = "baa27f7228983e24e096f34bdbd0338d";
	public static final String QQ_REDIRECT_URL = "http://www.drawlively.com";
	public static final String WECHAT_APP_ID = "wxc765604d04036e0e";
	public static final String SHOP_ITEM_NAME = "shop_item_Draw";
	public static final String IAP_PRODUCT_NAME = "iap_product_Draw";
	
	
	public static final int ANDROID_OS = 2;
	
	public static final long DRAW_LIVELY_SINA_WEIBO_UID = 0;
	public static final String DRAW_LIVELY_SINA_WEIBO = "猜猜画画手机版";
	
	private static ConfigManager manager = new ConfigManager();
	private ConfigManager() {
		
	}	
	public static ConfigManager getInstance() {
		return manager;
	}
	
	public String getAppId() {
		return DBConstants.APPID_DRAW;
	}
	
	public String getSystemUserId() {
		// TODO load from online parameters
		return "888888888888888888888888";
	}
	
	public String getTrafficApiServerURL() {
		// TODO load from online parameters
		return "http://58.215.184.18:8100/api/i";
		//for test
		//return "http://192.168.1.5:8100/api/i";
	}

	public String getUserApiServerURL() {
		// TODO load from online parameters
		return "http://58.215.160.100:8001/api/i";
	}	
	
	public String getGameId() {
		return DBConstants.GAME_ID_DRAW;
	}

	public int getFeedListDisplayCount()
	{
		return 24;
	}
	
	public int getActionFeedListDisplayCount()
	{
		return 12;
	}
	
	public LanguageType getLanguageType() {
		// TODO load from user settings
		return LanguageType.CHINESE;
	}
	public  String getSinaAppKey()
	{
		return SINA_APP_KEY;
	}
	public  String getSinaRedirectUrl()
	{
		return SINA_REDIRECT_URL;
	}
	public  String getQqAppKey()
	{
		return QQ_APP_KEY;
	}
	public  String getQqAppSecret()
	{
		return QQ_APP_SECRET;
	}
	public  String getQqRedirectUrl()
	{
		return QQ_REDIRECT_URL;
	}
	public  String getWechatAppId()
	{
		return WECHAT_APP_ID;
	}
	public  long getDrawLivelySinaWeiboUid()
	{
		return DRAW_LIVELY_SINA_WEIBO_UID;
	}
	public  String getDrawLivelySinaWeibo()
	{
		return DRAW_LIVELY_SINA_WEIBO;
	}
	public  String getShopItemName()
	{
		return SHOP_ITEM_NAME;
	}
	public  String getIapProductName()
	{
		return IAP_PRODUCT_NAME;
	}
	public int getAndroidOs()
	{
		return ANDROID_OS;
	}
	
}
