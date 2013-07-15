package com.orange.game.draw.activity.draw;

import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;

import android.app.Activity;
import android.os.Bundle;

public class DrawActivity extends CommonDrawActivity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw);                
    }

	
	@Override
	protected void handleServiceConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isRegisterGameMessage() {
		return true;
	}

	@Override
	protected boolean isRegisterServerConnect() {
		return true;
	}

}
