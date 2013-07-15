package com.orange.game.draw.activity.user;

import com.orange.common.android.utils.StringUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.home.DrawHomeActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.user.GenderUtils;
import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

import android.app.Activity;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SetUserSimpleInfoActivity extends Activity {
	private static Button submitBtn = null;
	private static Button skipBtn = null;
	private static EditText nickNameEt = null;
	private static LinearLayout maleLo = null;
	private static LinearLayout femaleLo = null;
	private static Button maleBtn = null;
	private static Button femaleBtn = null;
	private static PBGameUser user = null;
	private static Boolean gender = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_simple_info_setting);
		submitBtn = (Button)findViewById(R.id.submitBtn);
		skipBtn = (Button)findViewById(R.id.skipBtn);
		nickNameEt  = (EditText)findViewById(R.id.nickNameEt); 
		maleLo  = (LinearLayout)findViewById(R.id.maleLo);
		femaleLo  = (LinearLayout)findViewById(R.id.femaleLo);
		maleBtn  = (Button)findViewById(R.id.maleBtn);
		femaleBtn = (Button)findViewById(R.id.femaleBtn);
		
		maleBtn.setOnClickListener(maleBtnListener);
		femaleBtn.setOnClickListener(femaleBtnListener);
		submitBtn.setOnClickListener(submitBtnListener);
		skipBtn.setOnClickListener(skipBtnListener);
		nickNameEt.setText(UserManager.getInstance().getUser().getNickName());
		
		user = UserManager.getInstance().getUser();
		gender = user.getGender();

		if (gender == false) {
			femaleLo.setBackgroundResource(0);
			maleLo.setBackgroundResource(R.drawable.user_pic_bgselected);
		}else{
			femaleLo.setBackgroundResource(R.drawable.user_pic_bgselected);
			maleLo.setBackgroundResource(0);
		}
	}
	
	private final OnClickListener maleBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			femaleLo.setBackgroundResource(0);
			maleLo.setBackgroundResource(R.drawable.user_pic_bgselected);
			UserManager.getInstance().setGender(true);
			gender = true;
		}
	};
	
	private final OnClickListener femaleBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			femaleLo.setBackgroundResource(R.drawable.user_pic_bgselected);
			maleLo.setBackgroundResource(0);
			UserManager.getInstance().setGender(false);
			gender = false;
		}
	};
	
	private final OnClickListener submitBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			nickNameEt =(EditText)findViewById(R.id.nickNameEt);
			String nickName = nickNameEt.getText().toString();
			if (StringUtil.isEmpty(nickName)) {
		 		Toast toast=Toast.makeText(SetUserSimpleInfoActivity.this, R.string.nick_name_must_not_empty, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
			}else{
				UserMission.getInstance().updateUser(nickName, null,
						gender, null, null, new MissionCompleteInterface() {

							@Override
							public void onComplete(int errorCode) {
								Log.d("SetUserSimpleInfoActivity", "updateUser errorCode = " + errorCode);
								
								if (errorCode==ErrorCode.ERROR_SUCCESS) {
									Intent intent = new Intent();
									intent.setClass(SetUserSimpleInfoActivity.this, DrawHomeActivity.class);
									startActivity(intent); 
								}else {
							 		Toast toast=Toast.makeText(SetUserSimpleInfoActivity.this, R.string.register_user_failed, Toast.LENGTH_LONG);
									toast.setGravity(Gravity.TOP, 0, 0);
									toast.show();
							 	}
							}
						});
			}
		}
	};
	
	private final OnClickListener skipBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetUserSimpleInfoActivity.this, DrawHomeActivity.class);
			startActivity(intent); 
		}
	};
}
