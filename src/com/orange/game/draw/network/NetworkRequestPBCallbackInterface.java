package com.orange.game.draw.network;

import org.json.JSONObject;

import com.orange.network.game.protocol.message.GameMessageProtos.DataQueryResponse;

public interface NetworkRequestPBCallbackInterface {

	void handleSuccessResponse(DataQueryResponse response);
	void handleFailureResponse(int errorCode);
}
