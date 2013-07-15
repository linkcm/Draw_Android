/**  
        * @title DrawHomeChatDialog.java  
        * @package com.orange.game.draw.activity.chat  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-31 下午3:43:29  
        * @version V1.0  
 */
package com.orange.game.draw.activity.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orange.common.android.utils.ToastUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.friend.SearchFriendActivity;
import com.orange.game.draw.activity.message.adapter.DrawHomeMessageDialogAdapter;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.message.MessageMission;
import com.orange.game.draw.mission.message.MessageMission.ReadDirection;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;



public class DrawHomeMessageDialogActivity extends CommonDrawActivity
{
	public static final String MESSAGE_DATA = "messageData";
	public static final String MESSAGE_TARGET_USER_ID = "targetUserID";
	public static final String MESSAGE_TARGET_USER_NICK_NAME = "targetUserNickName";
	public static final String MESSAGE_TARGET_USER_AVATAR = "userAvatar";
	
	private DrawHomeMessageDialogAdapter adapter;
	private String targetUserId = "";
	private String messageId = "";
	private EditText messageContent;
	private PullToRefreshListView listView;
	
	@Override
	protected boolean isRegisterServerConnect()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void handleServiceConnected()
	{
		// TODO Auto-generated method stub

	}
	
	public static void newInstance(Context context,PBGameUser gameUser){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(MESSAGE_TARGET_USER_ID,gameUser.getUserId());
		bundle.putString(MESSAGE_TARGET_USER_NICK_NAME,gameUser.getNickName());
		bundle.putString(MESSAGE_TARGET_USER_AVATAR,gameUser.getAvatar());
		intent.putExtra(MESSAGE_DATA, bundle);
		intent.setClass(context,DrawHomeMessageDialogActivity.class);
		context.startActivity(intent);
	}
	
	
	public static void newInstance(Context context,String userId,String userNickName,String userAvatar){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(MESSAGE_TARGET_USER_ID,userId);
		bundle.putString(MESSAGE_TARGET_USER_NICK_NAME,userNickName);
		bundle.putString(MESSAGE_TARGET_USER_AVATAR,userAvatar);
		intent.putExtra(DrawHomeMessageDialogActivity.MESSAGE_DATA, bundle);
		intent.setClass(context,DrawHomeMessageDialogActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_home_message_dialog);
		Bundle bundle = getIntent().getBundleExtra(MESSAGE_DATA);
		targetUserId = bundle.getString(MESSAGE_TARGET_USER_ID);
		String targetUserNickName = bundle.getString(MESSAGE_TARGET_USER_NICK_NAME);
		String targetUserAvatar = bundle.getString(MESSAGE_TARGET_USER_AVATAR);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		Button sendButton = (Button) findViewById(R.id.message_send_button);
		ImageButton refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		messageContent = (EditText) findViewById(R.id.message_content);
		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(targetUserNickName);
		listView = (PullToRefreshListView) findViewById(R.id.draw_home_message_dialog_list_view);
		adapter = new DrawHomeMessageDialogAdapter(null, targetUserId,targetUserAvatar,DrawHomeMessageDialogActivity.this);
		listView.getRefreshableView().setAdapter(adapter);
		backButton.setOnClickListener(backOnClickListener);
		sendButton.setOnClickListener(sendOnClickListener);
		refreshButton.setOnClickListener(refreshOnClickListener);
		listView.setOnRefreshListener(onRefreshListener);
		messageId = MessageMission.getInstance().getLastMessageIdFromFile(targetUserId);
	}

	@Override
	protected void onStart()
	{
		loadAllMessage();	
		super.onStart();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		hideSoftKeyboard();
	}

	private OnClickListener sendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			hideSoftKeyboard();
			String msgContent = messageContent.getText().toString();
			if (msgContent == null||msgContent.equalsIgnoreCase(""))
			{
				ToastUtil.showToastMessage(DrawHomeMessageDialogActivity.this, getString(R.string.empty_message_toast));
				return;
			}
			sendMessage(targetUserId, msgContent);
			messageContent.setText("");
		}
	};
	
	
	private OnClickListener refreshOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			listView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			loadAllMessage();
			
		}
	};
	

	private OnRefreshListener onRefreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh(PullToRefreshBase refreshView)
		{
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			refreshView.onRefreshComplete();
			loadAllMessage();
		}

		
	};
	
	private  void loadAllMessage(){
		int count = ConfigManager.getInstance().getFeedListDisplayCount();
		showProgressDialog(R.string.loading);
		MessageMission.getInstance().getMessageList(targetUserId, messageId, ReadDirection.FORWARD, count, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				hideDialog();
				if(errorCode == ErrorCode.ERROR_SUCCESS){
					adapter.setMessageList(MessageMission.getInstance().getMyMessageManager().getMessageList(targetUserId));
					adapter.notifyDataSetChanged();
					if (adapter.getCount()>0)
					{
						listView.getRefreshableView().setSelection(adapter.getCount()-1);
					}
					
				}else {
					DrawNetworkConstants.errorToast(DrawHomeMessageDialogActivity.this, errorCode);
				}
				
			}
		});
	}
	
	
	
	
	
	private void hideSoftKeyboard(){
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
	}
	
	
	
	private void sendMessage(String targetUserId,String msg){
		MessageMission.getInstance().sendTextMessage(targetUserId, msg, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					loadAllMessage();
				}else {
					DrawNetworkConstants.errorToast(DrawHomeMessageDialogActivity.this, errorCode);
				}	
			}
		});
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MessageMission.getInstance().saveMessageInFile(targetUserId);
	}
	
	
	
}
