package com.orange.game.draw.activity.user;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.orange.common.android.utils.JsonUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;

@SuppressLint({ "UseValueOf", "DefaultLocale" })
public class RegisterUserActivity extends CommonDrawActivity {

	private static Button btnStart = null;
	private static Button sinaBtn = null;
	private static Button tencentBtn = null;
		
	@Override
	protected boolean isRegisterServerConnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void handleServiceConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);
		AbstractWeibo.initSDK(this);
		
		btnStart=(Button) findViewById(R.id.btnStart);
		sinaBtn=(Button)findViewById(R.id.btnSinaBlog);
		tencentBtn=(Button)findViewById(R.id.btnTencentBlog);
		
		
		btnStart.setOnClickListener(startBtnListener);
		sinaBtn.setOnClickListener(sinaBtnListener);
		tencentBtn.setOnClickListener(tencentBtnListener);
	}
	
	private  final OnClickListener startBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText emailTv = (EditText)findViewById(R.id.inputEmailEt);
			String email = emailTv.getText().toString();
			UserMission.getInstance().registerUser(email, null, completeHandler);
		}
	};
	
	private  final OnClickListener sinaBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AbstractWeibo weibo = AbstractWeibo.getWeibo(RegisterUserActivity.this, SinaWeibo.NAME);
			weibo.setWeiboActionListener(waListener);
//			weibo.authorize();
			weibo.showUser(null);
		}
	};
	
	private  final OnClickListener tencentBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AbstractWeibo weibo = AbstractWeibo.getWeibo(RegisterUserActivity.this, TencentWeibo.NAME);
			weibo.setWeiboActionListener(waListener);
//			weibo.authorize();
			weibo.showUser(null);
		}
	};
	
	
	private final WeiboActionListener waListener =new WeiboActionListener() {
		
		@Override
		public void onError(AbstractWeibo arg0, int arg1, Throwable arg2) {
			arg2.printStackTrace();
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//			 		Toast toast=Toast.makeText(RegisterUserActivity.this, R.string.authorize_failed, Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.TOP, 0, 0);
//					toast.show();
//				}
//			});
		}
		
		@Override
		public void onComplete(AbstractWeibo arg0, int arg1,
				HashMap<String, Object> arg2) {
			
			AbstractWeibo weibo = arg0;
			int action = arg1;
			HashMap<String, Object> hashMap = arg2;

			
			if (arg0 == null) {
				return;
			}
			
			if (action == AbstractWeibo.ACTION_USER_INFOR && weibo.getName().equals(SinaWeibo.NAME)) {
				registerWithSinaWeibo(weibo, hashMap);
			}
			
			if (action == AbstractWeibo.ACTION_USER_INFOR && weibo.getName().equals(TencentWeibo.NAME)) {
				registerWithTencentWeibo(weibo, hashMap);
			}
			
		}
		
		@Override
		public void onCancel(AbstractWeibo arg0, int arg1) {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
			 		Toast toast=Toast.makeText(RegisterUserActivity.this, R.string.authorize_cancel, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();	
				}
			});
		}
	};
	
	private void registerWithSinaWeibo(AbstractWeibo weibo, HashMap<String, Object> hashMap) {
		if (hashMap == null) {
			return;
		}
		
		Log.d(TAG, hashMap.toString());
		
		Integer snsIdInt = (Integer)hashMap.get("id");
		String snsId = String.valueOf(snsIdInt.intValue());
		String nickName = (String)hashMap.get("screen_name");
		String domain = (String)hashMap.get("domain");
		String gender = (String)hashMap.get("gender").toString();
		String province = (String)hashMap.get("province");
		String city = (String)hashMap.get("city");
		String location = (String)hashMap.get("location");
		String avatarUrl = (String)hashMap.get("profile_image_url");
		String accessToken = weibo.getDb().getToken();
		Integer date = new Integer((int)weibo.getDb().getExpiresTime());

		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put(SNSConstants.SNS_NETWORK, SNSConstants.SNS_SINA_WEIBO);
		userInfo.put(SNSConstants.SNS_USER_ID, snsId);
		userInfo.put(SNSConstants.SNS_NICK_NAME, nickName);
		userInfo.put(SNSConstants.SNS_DOMAIN, domain);
		userInfo.put(SNSConstants.SNS_GENDER, gender);
		userInfo.put(SNSConstants.SNS_PROVINCE, province);
		userInfo.put(SNSConstants.SNS_CITY, city);
		userInfo.put(SNSConstants.SNS_LOCATION, location);
		userInfo.put(SNSConstants.SNS_USER_IMAGE_URL, avatarUrl);
		
		userInfo.put(SNSConstants.SNS_OAUTH_TOKEN, accessToken);
		userInfo.put(SNSConstants.SNS_EXPIRATION_DATE, date);
		
		UserMission.getInstance().registerUserWithUserInfo(userInfo, completeHandler);
	}
	
	private void registerWithTencentWeibo(AbstractWeibo weibo, HashMap<String, Object> hashMap) {
		if (hashMap == null) {
			return;
		}
		
		Log.d(TAG, hashMap.toString());
		
		String snsId = (String)hashMap.get("name");
		String nickName = (String)hashMap.get("nick");
		String domain = (String)hashMap.get("name");
		
		String gender = (String)hashMap.get("sex").toString();
		if(gender == null){
			gender = "f";
		}else{
			if (gender.equals("1") == true) {
				gender = "m";
			}else{
				gender = "f";
			}
		}
		
		String province = (String)hashMap.get("province_code");
		String city = (String)hashMap.get("city_code");
		String location = (String)hashMap.get("location");
		
		Integer day = (Integer)hashMap.get("birth_day");
		Integer month = (Integer)hashMap.get("birth_month");
		Integer year = (Integer)hashMap.get("birth_year");
		String birthDay = null;
		if (day != null && month != null && year != null) {
			birthDay = String.format("%d-%d-%d", year.intValue(), month.intValue(), day.intValue());
		}
		
		String avatarUrl = (String)hashMap.get("head");
		if (avatarUrl != null && avatarUrl.length() > 0) {
			avatarUrl = String.format("%s/%d", avatarUrl, 100);
		}
		
		String refreshToken = null;
		String accessToken = weibo.getDb().getToken();
		Integer date = new Integer((int)weibo.getDb().getExpiresTime());
		String qqOpenId = null;
		
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		
		userInfo.put(SNSConstants.SNS_NETWORK, SNSConstants.SNS_QQ_WEIBO);
		userInfo.put(SNSConstants.SNS_USER_ID, snsId);
		userInfo.put(SNSConstants.SNS_NICK_NAME, nickName);
		userInfo.put(SNSConstants.SNS_DOMAIN, domain);
		userInfo.put(SNSConstants.SNS_GENDER, gender);
		userInfo.put(SNSConstants.SNS_PROVINCE, province);
		userInfo.put(SNSConstants.SNS_CITY, city);
		userInfo.put(SNSConstants.SNS_LOCATION, location);
		userInfo.put(SNSConstants.SNS_BIRTHDAY, birthDay);
		userInfo.put(SNSConstants.SNS_USER_IMAGE_URL, avatarUrl);
		
		userInfo.put(SNSConstants.SNS_REFRESH_TOKEN, refreshToken);
		userInfo.put(SNSConstants.SNS_OAUTH_TOKEN, accessToken);
		userInfo.put(SNSConstants.SNS_EXPIRATION_DATE, date);
		userInfo.put(SNSConstants.SNS_QQ_OPEN_ID, qqOpenId);
			
		UserMission.getInstance().registerUserWithUserInfo(userInfo, completeHandler);
	}
	
	private MissionCompleteInterface completeHandler=new MissionCompleteInterface() {
		
		@Override
		public void onComplete(int errorCode) {
			if (errorCode==ErrorCode.ERROR_SUCCESS) {
				Intent intent = new Intent();
				intent.setClass(RegisterUserActivity.this, SetUserSimpleInfoActivity.class);
				startActivity(intent); 
			}else if (errorCode==ErrorCode.ERROR_EMAIL_EXIST) {
				Toast toast=Toast.makeText(RegisterUserActivity.this, R.string.user_exist, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
			}else if (errorCode==ErrorCode.ERROR_EMAIL_NOT_VALID) {
				Toast toast=Toast.makeText(RegisterUserActivity.this, R.string.email_invalid, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
		 	}else if (errorCode==ErrorCode.ERROR_EMAIL_VERIFIED) {
		 		Toast toast=Toast.makeText(RegisterUserActivity.this, R.string.email_verified_failed, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
		 	}else {
		 		Toast toast=Toast.makeText(RegisterUserActivity.this, R.string.register_user_failed, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
		 	}
		}
	};

}
