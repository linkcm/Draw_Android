package com.orange.game.constants;

import android.content.Context;

import com.orange.common.android.utils.FileUtil;
import com.orange.game.R;
import com.orange.game.application.DrawApplication;
import com.umeng.xp.controller.XpListenersCenter.STATUS;

public class FileNameConstants {
	static Context context = DrawApplication.getInstance();
	public static final String PB_FILE = ".pb";
	public static final String APP_NAME = context.getString(R.string.app_name);
	public static final String MESSAGE = "message";
	
	public static final String USER_BASIC_INFO = "user_basic_info.pb";
	
	public static final String IMAGE_CACHE_PATH = FileUtil.getSDCardCachePath(context,APP_NAME);
	
	public static final String MESSAGE_CACHE_PATH = FileUtil.getSDPath()+"/"+APP_NAME+"/"+MESSAGE+"/";

}
