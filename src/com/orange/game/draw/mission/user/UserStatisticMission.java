package com.orange.game.draw.mission.user;

import com.orange.game.draw.mission.common.CommonMission;

public class UserStatisticMission extends CommonMission {

	
	protected static final String TAG = "UserStatisticMission";
	
	// thread-safe singleton implementation
	private static UserStatisticMission sharedInstance = new UserStatisticMission();
	private UserStatisticMission() {

	}
	public static UserStatisticMission getInstance() {
		return sharedInstance;
	}	
	
	
}
