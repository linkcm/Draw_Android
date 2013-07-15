package com.orange.game.draw.mission.common;

import android.text.Html.TagHandler;

import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.user.UserManager;

public class CommonMission {

	public static final UserManager userManager = UserManager.getInstance();
	public static final ConfigManager configManager = ConfigManager.getInstance();

	protected void callCompleteHandler(final MissionCompleteInterface completeHandler, int errorCode){
		if (completeHandler != null){
			completeHandler.onComplete(errorCode);
		}
	}
	
}
