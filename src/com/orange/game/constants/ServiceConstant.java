package com.orange.game.constants;

public class ServiceConstant  {

	// server name//////////////////////////////////////////////////
//	public static final String FILE_SERVER_NAME = "http://192.168.1.150:80/upload/";
//	public static final String FILE_LOCAL_PATH = "F:/dipan/upload/";
	public static final String FILE_LOCAL_PATH = "/Library/WebServer/Documents/upload/";
	public static final String FILE_SERVER_NAME = "http://192.168.1.188:80/upload/";
	public static final String SNS_LOG_FILE = "F:/sns.log";
//	public static final String FILE_SERVER_NAME = "http://192.168.1.151:80/upload/";
//	public static final String FILE_LOCAL_PATH = "C:/xampp/htdocs/upload/";
	public static final String VERIFY_URL = "http://127.0.0.1:8000/service";
	
	// method name
	public static final String METHOD = "m";
	public static final String METHOD_TEST = "test";
	public static final String METHOD_ONLINESTATUS = "srpt";
	public static final String METHOD_REGISTRATION = "reg";
	public static final String METHOD_CREATE_BBS_POST = "cp";
	public static final String METHOD_CREATEPLACE = "cpl";
	public static final String METHOD_GETUSERPLACES = "gup";
	public static final String METHOD_GETPLACEPOST = "gpp";
	public static final String METHOD_GETNEARBYPLACE = "gnp";
	public static final String METHOD_USERFOLLOWPLACE = "ufp";
	public static final String METHOD_USERUNFOLLOWPLACE = "unfp";
	public static final String METHOD_GETUSERFOLLOWPOSTS = "guf";
	public static final String METHOD_GETUSERFOLLOWPLACE = "gufp";
	public static final String METHOD_GETNEARBYPOSTS = "gne";
	public static final String METHOD_GETMYPOSTS = "gmyp";
	public static final String METHOD_DEVICE_LOGIN = "dl";
	public static final String METHOD_GETPOSTRELATEDPOST = "gpr";
	public static final String METHOD_BINDUSER = "bu";
	public static final String METHOD_SEND_MESSAGE = "sm";
	public static final String METHOD_GET_MY_MESSAGE = "gmm";
	public static final String METHOD_DELETE_MESSAGE = "dmm";
	public static final String METHOD_GETMEPOST = "gmep";
	public static final String METHOD_USER_READ_MESSAGE = "urm";
	public static final String METHOD_UPDATE_USER = "uu";
	public static final String METHOD_UPDATEPLACE = "up";
	public static final String METHOD_GETAPPS = "ga";
	public static final String METHOD_GET_APP_UPDATE = "gau";
	public static final String METHOD_GETPLACE = "gtp";
	public static final String METHOD_GETPUBLICTIMELINE = "gpt";
	public static final String METHOD_ACTIONONPOST = "aop";	
	public static final String METHOD_SEARCHPRODUCT = "fpk";
	public static final String METHOD_UPDATEKEYWORD = "uk";
	public static final String METHOD_ACTIONONPRODUCT = "ap";
	public static final String METHOD_REGISTER_USER = "ru";
	public static final String METHOD_LOGIN = "lg";
	public static final String METHOD_ADDSHOPPINGITEM = "asi";
	public static final String METHOD_UPDATESHOPPINGITEM = "usi";
	public static final String METHOD_DELETESHOPPINGITEM = "dsi";
	public static final String METHOD_GETSHOPPINGITEM = "gsi";	
    public static final String METHOD_WRITEPRODUCTCOMMENT = "wpc";
    public static final String METHOD_GETPRODUCTCOMMENTS = "gpc";
    public static final String METHOD_GETALLCATEGORY = "gac";
    public static final String METHOD_GETSHOPPINGCATEGORY = "gsc";
    public static final String METHOD_GETUSERSHOPPINGITEMLIST = "gusil";
    public static final String METHOD_COUNTSHOPPINGITEMPRODUCTS = "csip";
    public static final String METHOD_SEGMENTTEXT = "st";
    public static final String METHOD_COMPAREPRODUCT = "comp";
	
	// wall service
	public static final String METHOD_CREATE_USER_WALL = "createWall";    
	public static final String METHOD_GET_USER_WALL = "getWall";
	public static final String METHOD_GET_USER_WALL_LIST = "getWallList";
	public static final String METHOD_UPDATE_USER_WALL = "updateWall";
	
    
	
	//learn draw service
	public static final String METHOD_GET_LEARNDRAW_LIST = "gldl";
	public static final String METHOD_GET_USER_LEARNDRAW_LIST = "guldl";
	public static final String METHOD_GET_USER_LEARDRAWID_LIST = "guldil";
	public static final String METHOD_BUY_LEARN_DRAW = "bld";
	public static final String METHOD_ADD_LEARN_DRAW = "ald";

	
	
	// internal usage
	public static final String METHOD_DELETESOLRINDEX = "deletesolr";
	
	// for groupbuy
	public static final String METHOD_REGISTER_DEVICE = "rd";
	public static final String METHOD_GROUPBUY_DEVICELOGIN = "gdl";
	public static final String METHOD_UPDATE_SUBSCRIPTION = "us";
	public static final String METHOD_BIND_USER_SERVICE = "bu";
	
	// for download
    public static final String METHOD_FINDTOPSITES = "fts";
    public static final String METHOD_FINDTOPDOWNLOADS = "ftdl";
    public static final String METHOD_REPORTDOWNLOAD = "rdl";
	
    
    //for game service
    public static final String METHOD_GET_PRICE = "gpri";
    public static final String METHOD_GET_ACCOUNT_BALANCE = "gab";
    
	public static final String METHOD_CHARGE_ACCOUNT = "ca";
	public static final String METHOD_CHARGE_INGOT_ACCOUNT = "cia";	
	public static final String METHOD_DEDUCT_ACCOUNT = "da";
	public static final String METHOD_BUY_ITEM = "buyItem";
	public static final String METHOD_USE_ITEM = "useItem";
	public static final String METHOD_CONSUME_ITEM = "consumeItem";
	
	public static final String METHOD_NEW_SYNC_ACCOUNT = "syncAccount";
	public static final String METHOD_UPLOAD_USER_IMAGE = "uploadUserImage";
	public static final String METHOD_NEW_UPDATE_USER = "updateUser";
	public static final String METHOD_INCREASE_EXPERIENCE = "increaseExp";
	
	

	
	//for game traffic service
    public static final String METHOD_FIND_ROOM_BY_USER = "fru";
    public static final String METHOD_INVITE_USER = "ivu";    
	public static final String METHOD_SEARCH_ROOM = "scr";
	public static final String METHOD_UPDATE_ROOM = "udr";
	public static final String METHOD_CREATE_ROOM = "cr";
	public static final String METHOD_JOIN_ROOM = "jr";
	public static final String METHOD_REOMOVE_ROOM = "rr";
	
	//friends method
	public static final String METHOD_FIND_FRIENDS = "ff";
	public static final String METHOD_SEARCH_USER = "su";
	public static final String METHOD_FOLLOW_USER = "fu";
	public static final String METHOD_UNFOLLOW_USER = "ufu";
	public static final String METHOD_FIND_DRAW = "fd";
	
	//commit words
	public static final String METHOD_COMMIT_WORDS = "cw";
	
	//level service
	public static final String METHOD_SYNC_LEVEL_EXP = "sle";

	// draw service
	public static final String METHOD_CREATE_OPUS = "cop";
	public static final String METHOD_MATCH_OPUS = "mop";
	public static final String METHOD_GUESS_OPUS = "gop";
	public static final String METHOD_GET_OPUS_BY_ID = "goi";
	// request parameters

   // for bulletin
	public static final String METHOD_GET_BULLETINS = "gblt";
	
    
	public static final String PARA_USERID = "uid";
	public static final String PARA_ADMIN_USER_ID = "auid";
	public static final String PARA_LOGINID = "lid";
	public static final String PARA_LOGINIDTYPE = "lty";
	public static final String PARA_USERTYPE = "uty";
	public static final String PARA_PASSWORD = "pwd";
	public static final String PARA_VERIFICATION = "vri";
	public static final String PARA_NEW_PASSWORD = "npwd";
	public static final String PARA_VERIFYCODE = "code";
	
	public static final String PARA_SNSID = "sid";
    public static final String PARA_SINA_ID = "siid";
    public static final String PARA_QQ_ID = "qid";
    
	public static final String PARA_FACEBOOKID = "fid";
	public static final String PARA_RENRENID = "rid";
	public static final String PARA_TWITTERID = "tid";
	public static final String PARA_QUERY = "q";
	public static final String PAPA_STARTDATE = "sd";
	public static final String PAPA_ENDDATE = "ed";

	public static final String PARA_MOBILE = "mb";
	public static final String PARA_EMAIL = "em";
	public static final String PARA_NEW_EMAIL = "nem";
	
	public static final String PARA_DEVICEID = "did";
	public static final String PARA_DEVICETYPE = "dty";
	public static final String PARA_DEVICEMODEL = "dm";
	public static final String PARA_DEVICEOS = "dos";
	public static final String PARA_DEVICETOKEN = "dto";
	public static final String PARA_NICKNAME = "nn";
	public static final String PARA_SIGNATURE = "sig";
	public static final String PARA_AUTO_REG = "are";
	

	public static final String PARA_NEED_RETURN_USER = "r";
	public static final String PARA_ACCESS_TOKEN = "at";
	public static final String PARA_ACCESS_TOKEN_SECRET = "ats";
	public static final String PARA_AVATAR = "av";
	public static final String PARA_BACKGROUND = "bg";
	public static final String PARA_URL = "url";
	
	
	public static final String PARA_COUNTRYCODE = "cc";
	public static final String PARA_LANGUAGE = "lang";
	public static final String PARA_APPID = "app";
	public static final String PARA_NEW_APPID = "napp";


	public static final String PARA_RADIUS = "ra";
	public static final String PARA_POSTTYPE = "pt";
	public static final String PARA_NAME = "na";
	public static final String PARA_DESC = "de";
	public static final String PARA_AFTER_TIMESTAMP = "at";
	public static final String PARA_BEFORE_TIMESTAMP = "bt";
	public static final String PARA_MAX_COUNT = "mc";
	public static final String PARA_RETURN_COUNT = "rc";
	public static final String PARA_LIST = "lt";

	public static final String PARA_TOTAL_VIEW = "tv";
	public static final String PARA_TOTAL_FORWARD = "tf";
	public static final String PARA_TOTAL_QUOTE = "tq";
	public static final String PARA_TOTAL_REPLY = "tr";
	public static final String PARA_TOTAL_RELATED = "trl";	
	public static final String PARA_CREATE_DATE = "cd";
	public static final String PARA_MODIFY_DATE = "cd";
	public static final String PARA_SEQ = "sq";

	public static final String PARA_POSTID = "pi";
	public static final String PARA_IMAGE_URL = "iu";
	public static final String PARA_CONTENT_TYPE = "ct";
	public static final String PARA_TEXT_CONTENT = "t";
	public static final String PARA_USER_LATITUDE = "ula";
	public static final String PARA_USER_LONGITUDE = "ulo";
	public static final String PARA_SYNC_SNS = "ss";
	public static final String PARA_SRC_POSTID = "spi";
	public static final String PARA_EXCLUDE_POSTID = "epi";

	public static final String PARA_PLACEID = "pid";

	public static final String PARA_REPLY_POSTID = "rpi";

	public static final String PARA_SINA_ACCESS_TOKEN = "sat";
	public static final String PARA_SINA_ACCESS_TOKEN_SECRET = "sats";
	public static final String PARA_QQ_ACCESS_TOKEN = "qat";
	public static final String PARA_QQ_ACCESS_TOKEN_SECRET = "qats";

	public static final String PARA_PROVINCE = "pro";
	public static final String PARA_CITY = "ci";
	public static final String PARA_LOCATION = "lo";
	public static final String PARA_GENDER = "ge";
	public static final String PARA_BIRTHDAY = "bi";
	public static final String PARA_SINA_NICKNAME = "sn";
	public static final String PARA_SINA_DOMAIN = "sd";
	public static final String PARA_SINA_REFRESH_TOKEN = "srt";
	public static final String PARA_SINA_EXPIRE_DATE = "sed";	
	
	public static final String PARA_QQ_NICKNAME = "qn";
	public static final String PARA_QQ_DOMAIN = "qd";
	public static final String PARA_QQ_REFRESH_TOKEN = "qrt";
	public static final String PARA_QQ_EXPIRE_DATE = "qed";	
	
	public static final String PARA_FACEBOOK_ACCESS_TOKEN = "fat";
	public static final String PARA_FACEBOOK_EXPIRE_DATE = "fed";	
	

	public static final String PARA_DOMAIN = "d";

	public static final String PARA_CREATE_USERID = "cuid";

	public static final String PARA_STATUS = "s";

	public static final String PARA_TIMESTAMP = "ts";
	public static final String PARA_MAC = "mac";

	public static final String PARA_DATA = "dat";

	public static final String PARA_LONGITUDE = "lo";
	public static final String PARA_LATITUDE = "lat";
	public static final String PAPA_RADIUS = "r";
	public static final String PARA_MESSAGETEXT = "t";

	public static final String PARA_VERSION = "v";

	public static final String PARA_TO_USERID = "tuid";
	public static final String PARA_MESSAGE_ID = "mid";
	
	public static final String PARA_POST_ACTION_TYPE = "pat";
	
	public static final String PARA_COMPAREWORD = "cw";
	
	public static final String PARA_NEW_FACETIME = "ft";
	public static final String PARA_NEW_SIGNATURE = "sig";
	
	//for game service
	public static final String PARA_PRICE_TYPE = "pt";
	public static final String PARA_FEEDBACK = "fb";
	public static final String PARA_CONTACT = "ca";
	

	//for game traffic service
	public static final String PARA_ROOM_ID = "frid";
	public static final String PARA_ROOM_NAME = "rn";
	public static final String PARA_USERID_LIST = "uids";
	public static final String PARA_SERVER_ADDRESS = "sa";
	public static final String PARA_SERVER_PORT = "sp";
	public static final String PARA_LAST_PLAY_DATE = "lpd";
	public static final String PARA_PLAY_TIMES = "pt";
	public static final String PAPA_ROOM_USERS = "rus";
	public static final String PARA_OFFSET = "os";
	public static final String PARA_COUNT = "ct";
	
	// response parameters

	public static final String RET_MESSAGE = "msg";
	public static final String RET_CODE = "ret";
	public static final String RET_DATA = "dat";
	
	//app service response parameters

	public static final String PARA_APPURL = "au";
	public static final String PARA_ICON = "ai";
	public static final String PARA_SINA_APPKEY = "sak";
	public static final String PARA_SINA_APPSECRET = "sas";
	public static final String PARA_QQ_APPKEY = "qak";
	public static final String PARA_QQ_APPSECRET = "qas";
	public static final String PARA_RENREN_APPKEY = "rak";
	public static final String PARA_RENREN_APPSECRET = "ras";
	public static final String PARA_MESSAGE_TYPE = "mt";

	public static final int DEFAULT_MAX_COUNT = 30;
	
	public static final String PARA_GPS = "gps";
	public static final String PARA_KEYWORDS = "kw";
	public static final String PARA_CATEGORIES = "ctg";
	
	public static final String PRAR_START_OFFSET = "so";
	public static final String PARA_MAX_DISTANCE = "md";
	public static final String PARA_TODAY_ONLY = "to";
	public static final String PARA_SORT_BY = "sb";
	public static final String PARA_KEYWORD = "kw";
	public static final String PARA_START_PRICE = "sp";
	public static final String PARA_END_PRICE = "ep";
	public static final String PARA_SEGMENT = "sg";
	
	public static final String METHOD_FINDPRODUCTSWITHPRICE = "fpp";
	public static final String METHOD_FINDPRODUCTSWITHREBATE = "fpd";
	public static final String METHOD_FINDPRODUCTSWITHBOUGHT = "fpb";
	public static final String METHOD_FINDPRODUCTSWITHLOCATION = "fpl";
	public static final String METHOD_FINDPRODUCTSWITHCATEGORY = "fpc";
	public static final String METHOD_FINDPRODUCTSGROUPBYCATEGORY = "fgc";	
	public static final String METHOD_FINDPRODUCTS = "fp";
	public static final String METHOD_FINDPRODUCTBYSCORE = "fps";
	public static final String METHOD_FINDPRODUCTBYSHOPPINGITEM = "fpsi";
	
	public static final String METHOD_ACTION_ON_OPUS = "aoo";

	
	//response parameters
	public static final String PARA_LOC = "loc";
	public static final String PARA_IMAGE= "img";
	public static final String PARA_TITLE = "tt";
	public static final String PARA_START_DATE = "sd";
	public static final String PARA_END_DATE = "ed";
	public static final String PARA_PRICE = "pr";
	public static final String PARA_CURRENCY = "crr";
	public static final String PARA_FORCE_BUY = "fby";
	public static final String PARA_VALUE = "val";
	public static final String PARA_BOUGHT = "bo";
	public static final String PARA_SITE_ID = "si";
	public static final String PARA_SITE_NAME = "sn";
	public static final String PARA_SITE_URL = "su";
	public static final String PARA_ID = "_id";
	public static final String PARA_ADDRESS = "add";
	public static final String PARA_DETAIL = "dt";
	public static final String PARA_REBATE = "rb";
	public static final String PARA_WAP_URL = "wu";
	public static final String PARA_TEL = "te";
	public static final String PARA_SHOP = "sh";
	public static final String PARA_UP = "up";
	public static final String PARA_DOWN = "down";
	public static final String PARA_TOP_SCORE = "ts";
	
	public static final String PARA_ITEMID = "ii";
	public static final String PARA_CATEGORY_NAME = "na";
	public static final String PARA_SUB_CATEGORY = "sc";
	public static final String PARA_CATEGORY_ID = "ci";
	public static final String PARA_PRODUCT = "p";
	public static final String PARA_EXPIRE_DATE = "e_date";
	public static final String PARA_MATCH_ITEM_COUNT = "mic";
	public static final String PARA_REFRESH_TOKEN = "rto";
	public static final String PARA_QQ_OPEN_ID = "qqoid";
	
	public static final String PARA_CATEGORY_PRODUCTS_NUM = "cpn";

	public static final String PARA_ACTION_NAME = "an";
	public static final String PARA_ACTION_VALUE = "av";
	
    public static final String PARA_COMMENT_CONTENT = "comc";
    public static final String PARA_COMMENT_TYPE = "cmt";
    public static final String PARA_COMMENT_ID = "cmid";
    public static final String PARA_COMMENT_SUMMARY = "cmsm";
    public static final String PARA_COMMENT_USERID = "cmuid";
    public static final String PARA_COMMENT_NICKNAME = "cmnn";

    public static final String PARA_SUB_CATEGORY_ID = "sid";
    public static final String PARA_SUB_CATEGORY_NAME = "scn";
    public static final String PARA_SUB_CATEGORY_KEYS = "keys";
    public static final String PARA_ITEMID_ARRAY = "iia";
    public static final String PARA_REQUIRE_MATCH = "rm";
    
    public static final String PARA_REGISTER_TYPE = "rt";

    public static final String VERIFICATION = "1";
    public static final String NEED_REQURIE_MATCH = "1";


    public static final int REGISTER_TYPE_EMAIL     = 1;
    public static final int REGISTER_TYPE_SINA      = 2;
    public static final int REGISTER_TYPE_QQ        = 3;
    public static final int REGISTER_TYPE_RENREN    = 4;
    public static final int REGISTER_TYPE_FACEBOOK    = 5;
    public static final int REGISTER_TYPE_TWITTER    = 6;
    
    public static final int PARA_PASSWORD_MIN_LENGTH  = 6;

    // for download
    public static final String PARA_FILE_TYPE = "ft";
    public static final String PARA_FILE_NAME = "fn";
    public static final String PARA_FILE_URL  = "fu";
    public static final String PARA_FILE_SIZE = "fs";
    public static final String PARA_TYPE = "tp";
    public static final String PARA_DOWNLOAD_COUNT = "cnt";
    public static final Object PARA_DATA_COUNT = "count";
    
    public static final String PARA_PRODUCT_TYPE = "pt";

    public static final String PARA_AD_TYPE = "adt";
    public static final String PARA_FROM = "fr";
    
    public static final String PARA_AD_ID = "adi";
    public static final String PARA_AD_TEXT = "adt";
    public static final String PARA_AD_IMAGE = "adim";
    public static final String PARA_AD_LINK = "adl";
    public static final String METHOD_REPORT_SPAM_NUMBER = "rsn";
    
  //friend parameter
	public static final String PARA_TARGETUSERID   = "tid";
	public static final String PARA_SEARCHSTRING   = "ss";
	public static final String PARA_USERS	= "users";
	public static final String PARA_LAST_MODIFY_DATE = "lsmd";
	public static final String PARA_ONLINE_STATUS = "ols";
	public static final String PARA_FRIEND_TYPE = "ft";
	public static final String PARA_START_INDEX = "si";
	public static final String PARA_END_INDEX = "ei";
	public static final String PARA_RESULT_COUNT = "rc";
	
	public static final char USERID_SEPERATOR = '$';
	

//	public static final int FRIENDS_TYPE_FOLLOW =0;
//	public static final int FRIENDS_TYPE_FAN    =1;

    //const value for game
    public static final int CONST_PRICE_TYPE_COIN = 1;
    public static final int CONST_PRICE_TYPE_ITEM = 2;
	public static final int CONST_DEFAULT_PAGE_COUNT = 20;
    
    //response value for game service
    public static final String PARA_AMOUNT = "pa";
	public static final String PARA_ACCOUNT_BALANCE = "ab";
	public static final String PARA_ACCOUNT_INGOT_BALANCE = "aib";
	
    public static final String PARA_SAVE_PERCENT = "sp";
	public static final String PARA_APPLE_IAP_PRODUCT_ID = "ipi";
	public static final String PARA_SOURCE = "sr";
	public static final String PARA_TRANSACTION_ID = "tid";
	public static final String PARA_TRANSACTION_RECEIPT = "tre";
	public static final String PARA_ITEM_TYPE = "it";
	public static final String PARA_ITEM_AMOUNT = "ia";	
	public static final String PARA_ITEMS = "is";
	public static final String PARA_DEVIATION = "dv";
	

	public static final String METHOD_UPDATE_ITEM_AMOUNT ="uia";
	public static final String METHOD_UPDATE_ACCOUNT_BALANCE = "uab";
	public static final String METHOD_SYNC_USER_ACCOUNT_ITEM = "sai";
	public static final String METHOD_FEEDBACK = "fb";
	public static final String METHOD_NEW_JOIN_ROOM = "njr";

	
	public static final String METHOD_GET_OPUS_TIMES = "got";
	public static final String METHOD_GET_BOARD_LIST = "gbl";
	
	public static final String METHOD_GET_CONTEST_LIST = "gcl";
	public static final String METHOD_GET_TOP_PLAYERS = "gtpl";
	public static final String METHOD_GET_MY_COMMENT_LIST = "gmcl";
	public static final String METHOD_GET_CONTEST_OPUS_LIST = "gcol";

	public static final String METHOD_UPDATE_OPUS = "uop";
	public static final String METHOD_GET_TOP_OPUS_FOR_WEIBO = "gtow";
	public static final String METHOD_UPDATE_BOARD_STATISTIC = "ubs";

	public static final String METHOD_GET_FRIEND_LIST = "gfrl";
	public static final String METHOD_GET_RELATION_COUNT = "grc";
	
	public static final String METHOD_GET_MESSAGE_STAT_LIST = "gmsl";
	public static final String METHOD_GET_MESSAGE_LIST = "gml";
	
	public static final String METHOD_CLEAR_USER_GAME_STATUS = "cugs";
	public static final String METHOD_BLACK_USER = "blu";
	public static final String METHOD_BLACK_FRIEND = "blf";
	
	//for commit words
	public static final String PARA_NEW_WORDS = "nw";
	public static final char WORDS_SEPERATOR = '$';
	
	//for level
	public static final String PARA_EXP    = "exp";
	public static final String PARA_LEVEL  = "lvl";
	public static final String PARA_LEVEL_INFO	= "lif";
	public static final String PARA_SYNC_TYPE  =  "st";
	public static final int CONST_SYNC_TYPE_SYNC = 0;
	public static final int CONST_SYNC_TYPE_UPDATE = 1;
	public static final int CONST_SYNC_TYPE_AWARD = 2;
	
	
	//for offline game
	public static final String PARA_WORD = "word";
	public static final String PARA_WORD_SCORE = "wordScore";

//	public static final int CONST_MESSAGE_STATUS_NOT_READ = 0;
	public static final int CONST_MESSAGE_DIRECTION_SEND = 0;
	public static final int CONST_MESSAGE_DIRECTION_RECIEVE = 1;
//	public static final Object CONST_MESSAGE_STATUS_READ = 1;

	public static final String PARA_OPUS_ID = "opid";
	public static final String PARA_CORRECT = "cre";
	public static final String PARA_ACTION_TYPE = "act";
	public static final String PARA_WORD_LIST = "wl";
	public static final String PARA_SCORE = "sco";
	public static final String PARA_OPUS_CREATOR = "opc";
	public static final String PARA_FEED_ID = "fid";
	
	public static final Object PARA_RESCUE_DATA_TAG = "rdt";

	public static final String METHOD_GET_FEED_LIST = "gfl";
	public static final String METHOD_GET_OPUST_COUNT = "goc";
	public static final String METHOD_GET_FEED_COMMENT_LIST = "gfc";
	public static final String METHOD_GET_STATISTICS = "gss";
	
	public static final String METHOD_GET_TARGET_USER_INFO = "gtui";
	public static final String METHOD_GET_SYSTEM_STATISTIC = "gsys";
	public static final String METHOD_DELETE_FEED = "delf";
	public static final String METHOD_REPORT_STATUS = "rs";

	
	public static final String PARA_FEED_TIMESTAMP = "fts";
	public static final String PARA_FEED_COUNT = "fec";
	public static final String PARA_FAN_COUNT = "fac";
	public static final String PARA_MESSAGE_COUNT = "mc";
	public static final String PARA_ROOM_COUNT = "rc";
	public static final String PARA_TARGET_MESSAGE_ID = "tmd";
	public static final String PARA_AWARD_BALANCE = "gb";				// award balance
	public static final String PARA_AWARD_EXP = "ae";
	
	public static final String MESSAGEID_SEPERATOR = "$";

	//System statistic
	public static final String PARA_USER_NUMBER = "user_num";
	public static final String PARA_DRAW_NUMBER = "draw_num";
	public static final String PARA_DRAW_TO_USER_NUMBER = "draw_to_user_num";
	public static final String PARA_COMMENT_NUMBER = "comment_num";
	public static final String PARA_GUESS_NUMBER = "guess_num";
	public static final String PARA_MESSAGE_NUMBER = "message_num";
	public static final String PARA_GAME_ID = "gid";
	public static final String PARA_RETURN_ITEM = "ri";
	public static final String METHOD_CREATE_OPUS_IMAGE = "coi";
	public static final String PARA_DRAW_DATA = "dd";
	public static final String PARA_DRAW_IMAGE = "photo";
	
	public static final String PARA_WALL_DATA = "wall_data";
	public static final String PARA_WALL_IMAGE = "wall_bg_image";
	
	
	//board parameter
	public static final String PARA_INDEX = "idx";
	public static final String PARA_ADLIST = "adl";
	public static final String PARA_AD_NUMBER = "adn";
	public static final String PARA_AD_PLATFORM = "adp";
	public static final String PARA_AD_PUBLISH_ID = "adpid";
	public static final String PARA_WEB_TYPE = "wt";
	public static final String PARA_LOCAL_URL = "lu";
	public static final String PARA_REMOTE_URL = "ru";
	public static final String PARA_IMAGE_CLICK_URL = "icu";
	public static final String PARA_BOARDID = "bid";
	public static final String PARA_AD_IMAGE_URL = "aiu";
	public static final String PARA_CN_AD_IMAGE_URL = "caiu";
	public static final String PARA_CN_IMAGE_URL = "ciu";
	
	
	
	//feed times
	public static final String PARA_COMMENT_TIMES = "cmt";
	public static final String PARA_GUESS_TIMES = "gt";
	public static final String PARA_CORRECT_TIMES = "crt";
	public static final String PARA_FLOWER_TIMES = "ft";
	public static final String PARA_TOMATO_TIMES = "tt";
	public static final String PARA_SAVE_TIMES = "st";
	

	public static final String PARA_REQUEST_MESSAGE_ID = "rmid";
	public static final String PARA_REPLY_RESULT = "rre";
	
	public static final String PARA_CAN_SUMMIT_COUNT = "csc";
	
	public static final String PARA_CONTESTID = "cid";
	public static final String PARA_CONTEST_URL = "cu";
	public static final String PARA_STATEMENT_URL = "su";
	public static final String PARA_OPUS_COUNT = "oc";
	public static final String PARA_PARTICIPANT_COUNT = "pc";
	public static final String PARA_LIMIT = "lm";

	public static final String PARA_COMMENT_COUNT = "comc";
	public static final String PARA_DRAWTOME_COUNT = "dtc";
	public static final String PARA_CONTEST_IPAD_URL = "cpu";
	public static final String PARA_STATEMENT_IPAD_URL = "spu";
	public static final String PARA_RELATION = "rl";
	public static final String PARA_FORWARD = "fw";
	
	
	public static final String PARA_USER_COINS = "ucn";

	//bbs
	public static final String METHOD_GET_BBS_BOARD_LIST = "gbbl";
	public static final String METHOD_GET_BBS_POST_LIST = "gbpl";
	public static final String METHOD_GET_CONTEST_TOP_OPUS = "gcto";
	public static final String METHOD_GET_BBS_ACTION_LIST = "gbal";
	public static final String METHOD_CREATE_BBS_ACTION = "cba";

	public static final String METHOD_DELETE_BBS_POST = "dbp";
	public static final String METHOD_DELETE_BBS_ACTION = "dba";
	public static final String METHOD_GET_BBS_POST = "gbp";
	public static final String METHOD_GET_BBS_DRAWDATA = "gbd";
	public static final String METHOD_EDIT_BBS_POST = "edp";
	public static final String METHOD_PAY_BBS_REWARD = "pbr";
	public static final String METHOD_CHANGE_BBS_ROLE = "cbr";
	public static final String METHOD_GET_BBS_PRIVILEGE_LIST = "gbpr";
	public static final String METHOD_GET_USER_INFO_LIST = "gtuli";
	
	public static final String PARA_BONUS = "bn";
	public static final String PARA_RANGE_TYPE = "rt";
	public static final String PARA_ACTIONID = "aid";
	public static final String PARA_THUMB_IMAGE = "timg";
	public static final String PARA_DRAW_THUMB = "dti";
	public static final String PARA_ACTION_UID = "auid";
	public static final String PARA_POST_UID = "puid";
	public static final String PARA_BRIEF_TEXT = "btxt";
	public static final String PARA_SOURCE_ACTION_TYPE = "sat";
	public static final String PARA_BBS_ACTION_COUNT = "bac";
	public static final String PARA_ACTION_NICKNAME = "ann";


	
	//friend && relation
	public static final String PARA_RELATION_FAN_COUNT = "rfac";
	public static final String PARA_RELATION_FOLLOW_COUNT = "rflc";
	public static final String PARA_RELATION_BLACK_COUNT = "rfbc";
	

	// for bulletin
	public static final String PARA_LAST_BULLETIN_ID = "lbid";
	public static final String PARA_BULLETIN_ID = "bid";
	public static final String PARA_DATE = "date";
	public static final String PARA_FUNCTION = "function";
	public static final String PARA_BULLETIN_CONTENT = "content";
	
	// for hot word
	public static final String PARA_HOTWORD_GRAN = "gran";
	public static final String PARA_HOTWORD_NUM = "count";
	public static final String METHOD_GET_HOTWORDS = "hotwd";
	public static final String PARA_HOTWORD_CONTENT = "hotword";
	public static final String PARA_PERMISSION = "perm";

	// for wall
	public static final String PARA_WALL_ID = "wall_id";

	public static final int CONST_BLACK_FRIEND = 0;
	public static final int CONST_UNBLACK_FRIEND = 1;

	public static final String PARA_IS_DATA_ZIP = "idz";
	public static final String PARA_IS_DATA_COMPRESSED = "idc";
	public static final String PARA_RETURN_DATA_METHOD = "rdm";
	public static final String PARA_RETURN_COMPRESSED_DATA = "rcd";
	public static final String METHOD_REMOVE_LEARNDRAW = "rld";

	
	
	
	
	
}
