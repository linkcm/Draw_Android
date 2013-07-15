package com.orange.game.draw.mission.common;

import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

public interface MissionCompleteGetFeedDetailInterface {

	void comlete(PBFeed feed, final int errorCode);

}
