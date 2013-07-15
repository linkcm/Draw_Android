package com.orange.game.draw.network;

import org.json.JSONObject;

public interface NetworkRequestJSONCallbackInterface {
	void handleSuccessResponse(JSONObject jsonData);
	void handleFailureResponse(int errorCode);
}
