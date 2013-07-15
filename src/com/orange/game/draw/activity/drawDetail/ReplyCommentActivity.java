/**  
        * @title ReplyCommentActivity.java  
        * @package com.orange.game.draw.activity.drawDetail  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-2 上午9:51:41  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedActionMission;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;



public class ReplyCommentActivity extends CommonDrawActivity
{

	public static final String PB_FEED = "pbFeed";
	public static final int REQUEST_CODE = 999;
	public static final String RESULT_CODE = "resultCode";
	private PBFeed feed;
	
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
	
	private EditText commentContentEditText;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.common_reply_comment);
		byte[] pbFeedData = getIntent().getByteArrayExtra(PB_FEED); 
		try
		{
			feed = PBFeed.parseFrom(pbFeedData);
		} catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		TextView titleTextView = (TextView) findViewById(R.id.comment_title_text);
		titleTextView.setText(R.string.reply_comment);			
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		Button sendButton = (Button) findViewById(R.id.send_button);
		commentContentEditText = (EditText) findViewById(R.id.comment_text);
		sendButton.setOnClickListener(sendOnClickListener);
		KeyboardUtil.showKeyboardForOnCreate(commentContentEditText, ReplyCommentActivity.this);
	}

	
	private OnClickListener sendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String comment = commentContentEditText.getText().toString();
			FeedActionMission.getInstance().commentOnFeed(feed, comment, new MissionCompleteInterface()
			{
				
				@Override
				public void onComplete(int errorCode)
				{
					Intent data = new Intent();
					commentContentEditText.setText("");
					if (errorCode == ErrorCode.ERROR_SUCCESS)
					{
						data.putExtra(RESULT_CODE, true);
					}else {
						data.putExtra(RESULT_CODE, false);
					}
					setResult(REQUEST_CODE,data);
					KeyboardUtil.hideKeyboard(commentContentEditText, ReplyCommentActivity.this);
					finish();
				}
			});
			
		}
	};
	
	private OnClickListener backOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			KeyboardUtil.hideKeyboard(commentContentEditText, ReplyCommentActivity.this);
			finish();
			
		}
	};
	
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		KeyboardUtil.hideKeyboard(commentContentEditText, ReplyCommentActivity.this);
		feed = null;
		
	}


}
