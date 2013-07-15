/**  
        * @title CommonFeedBackActivity.java  
        * @package com.orange.game.draw.activity.more  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-1 下午2:45:02  
        * @version V1.0  
 */
package com.orange.game.draw.activity.more;

import com.orange.common.android.utils.KeyboardUtil;
import com.orange.common.android.utils.StringUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.support.SupportMission;
import com.orange.game.draw.model.support.FeedbackType;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-1 下午2:45:02  
 */

public class CommonFeedBackActivity extends CommonDrawActivity
{
	public static final String TITLE = "title";
	public static final String TIPS = "tips";
	public static final String TYPE = "type";
	public static final String TAG = "CommonFeedBackActivity";
	private EditText feedbackEditText;
	private EditText contactEditText;
	public static final int ADVICE = 0;
	public static final int BUG = 1;
	public static final int NEW_WORD = 2;
	
	private int type = 0;
	
	public static void newInstance(Context context,String title,String tips,int type){
		Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		bundle.putString(TIPS, tips);
		bundle.putInt(TYPE, type);
		Intent intent = new Intent();
		intent.putExtra(TAG, bundle);
		intent.setClass(context, CommonFeedBackActivity.class);
		context.startActivity(intent);
	}
	

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

	

	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_feedback);
		
		Bundle bundle = getIntent().getBundleExtra(TAG);
		String title = bundle.getString(TITLE);
		String tips = bundle.getString(TIPS);
		type = bundle.getInt(TYPE);
		TextView titleTextView = (TextView) findViewById(R.id.title_text);
		titleTextView.setText(title);
		TextView tipsTextView = (TextView) findViewById(R.id.tips);
		tipsTextView.setText(tips);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		feedbackEditText = (EditText) findViewById(R.id.feedback_content);
		contactEditText = (EditText) findViewById(R.id.feedback_contact);
		Button sendButton = (Button) findViewById(R.id.send_btn);
		sendButton.setOnClickListener(sendOnClickListener);
		if (type == NEW_WORD)
		{
			contactEditText.setVisibility(View.GONE);
		}
	}

	
	
	
	private OnClickListener sendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			
			String feedback = feedbackEditText.getText().toString();
			if (type == NEW_WORD)
			{
				if(StringUtil.isEmpty(feedback))
				{
					Toast.makeText(CommonFeedBackActivity.this, R.string.submit_new_word_null_toast, Toast.LENGTH_LONG).show();
					return;
				}
				submitNewWord(feedback);
			}else {
				
				String contact = contactEditText.getText().toString();
				if (StringUtil.isEmpty(feedback))
				{
					Toast.makeText(CommonFeedBackActivity.this, R.string.feedback_null_toast, Toast.LENGTH_LONG).show();
					return;
				}
				if (StringUtil.isEmpty(feedback))
				{
					Toast.makeText(CommonFeedBackActivity.this,R.string.feedback_bug_null_toast,Toast.LENGTH_LONG).show();
					return;
				}
				if (type ==  ADVICE)
				{
					feedback(feedback, contact, FeedbackType.ADVICE);
				}else {
					feedbackBug(feedback, contact, FeedbackType.BUGS);
				}
			}
			finish();
		}
	};
	
	
	
	private void feedback(String feedback,String contact,FeedbackType type){
		SupportMission.getInstance().feedBack(feedback, contact, FeedbackType.ADVICE, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					Toast.makeText(CommonFeedBackActivity.this, R.string.feedback_success, Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(CommonFeedBackActivity.this, R.string.feedback_fail, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void feedbackBug(String feedback,String contact,FeedbackType type){
		SupportMission.getInstance().feedBack(feedback, contact, FeedbackType.ADVICE, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					Toast.makeText(CommonFeedBackActivity.this, R.string.feedback_bug_success, Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(CommonFeedBackActivity.this, R.string.feedback_bug_fail, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	
	
	private void submitNewWord(String wordString){
		SupportMission.getInstance().submitNewWord(wordString, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					Toast.makeText(CommonFeedBackActivity.this,R.string.submit_new_word_success, Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(CommonFeedBackActivity.this, R.string.submit_new_word_fail, Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}
	
	
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		KeyboardUtil.showKeyboardForOnCreate(feedbackEditText, CommonFeedBackActivity.this);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		KeyboardUtil.hideKeyboard(feedbackEditText, CommonFeedBackActivity.this);
	}


	

}
