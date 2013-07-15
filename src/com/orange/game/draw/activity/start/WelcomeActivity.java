package com.orange.game.draw.activity.start;

import cn.sharesdk.framework.AbstractWeibo;

import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.home.DrawHomeActivity;
import com.orange.game.draw.activity.user.RegisterUserActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.statistics.StatisticsMission;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.activity.ActivityMange;
import com.orange.game.draw.model.db.DbManager;
import com.orange.game.draw.model.user.UserManager;
import com.tencent.mm.sdk.openapi.BaseResp.ErrCode;

import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends CommonDrawActivity {

	@Override
	protected boolean isRegisterServerConnect() {
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage() {
		return false;
	}

	@Override
	protected void handleServiceConnected() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityMange.getInstance().addActivity(this);
		setContentView(R.layout.welcome);
		AbstractWeibo.initSDK(this);
       
	}
	
	

	private void start() {
		
		StatisticsMission.getInstance().getStatistics(new MissionCompleteInterface()
		{
			@Override
			public void onComplete(int errorCode)
			{
				try
				{
					Thread.sleep(2000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Intent intent = new Intent();
		        intent.setClass(WelcomeActivity.this, DrawHomeActivity.class);
		        startActivity(intent);
		        ActivityMange.getInstance().finishCurrentActivity();
			}
		});        	
		
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		start();
	}

	
	
}
