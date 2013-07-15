/**  
        * @title BBSSendPostActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-1 上午10:46:47  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.orange.common.android.utils.FileUtil;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.common.android.utils.MemoryUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.common.android.utils.StringUtil;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-1 上午10:46:47  
 */

public class BBSSendPostActivity extends CommonDrawActivity
{

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

	private PBBBSBoard board;
	public static final String PBBBSBOARD = "pbbbsBoard";
	
	
	private EditText contentEditText;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int RESULT_CAMERA = 2;
	private ImageButton createDrawButton;
	private ImageButton createImageButton;
	private String imageURL = "";
	private PopupWindow rewardPopupWindow;
	private PopupWindow createImagePopupWindow;
	private int bonus = 0;
	int contentType = BBSStatus.ContentTypeText;
	
	public static void newInstance(Context context,PBBBSBoard board){
		Intent intent = new Intent();
		intent.setClass(context, BBSSendPostActivity.class);
		intent.putExtra(PBBBSBOARD, board.toByteArray());
		context.startActivity(intent);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_send_post);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);		
		contentEditText = (EditText) findViewById(R.id.bbs_post_content);		
		Button sendButton = (Button) findViewById(R.id.bbs_send_button);
		sendButton.setOnClickListener(sendOnClickListener);
		
		createDrawButton = (ImageButton) findViewById(R.id.bbs_post_create_draw_button);
		createImageButton = (ImageButton) findViewById(R.id.bbs_post_create_image_button);
		createImageButton.setOnClickListener(createImageOnClickListener);
		
		
		
		Button postRewardButton = (Button) findViewById(R.id.bbs_post_reward_button);
		postRewardButton.setOnClickListener(postRewardOnClickListener);
		KeyboardUtil.showKeyboardForOnCreate(contentEditText, BBSSendPostActivity.this);
		try
		{
			board = PBBBSBoard.parseFrom(getIntent().getByteArrayExtra(PBBBSBOARD));
		} catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		
		
	}

	
	
	private OnClickListener sendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String content = "";
			content = contentEditText.getText().toString();
			if (StringUtil.isEmpty(content)||content.trim().length()<5)
			{
				Toast.makeText(
						BBSSendPostActivity.this, 
						R.string.post_content_less_toast, 
						Toast.LENGTH_LONG).show();
				return;
			}
			sendPost(content, bonus, imageURL, null, "");
		}
	};
	
	
	private OnClickListener postRewardOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			showPostReward(v);
			hidekeyboard();
		}
	};
	
	
	
	private OnClickListener createImageOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			 showCreateImageMenu(v);
			 hidekeyboard();
		}
	};
	
	
	private void showCreateImageMenu(View parent){
		if (createImagePopupWindow == null)
		{
			View createImageMenuView = getLayoutInflater().inflate(R.layout.bbs_create_image_menu, null);
			Button galleryButton = (Button) createImageMenuView.findViewById(R.id.gallery_button);
			Button cameraButton = (Button) createImageMenuView.findViewById(R.id.camera_button);
			Button cancelButton = (Button) createImageMenuView.findViewById(R.id.cancel_button);
			galleryButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Intent i = new Intent(
		                     Intent.ACTION_PICK,
		                     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		             startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
			});
			
			cameraButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{

				    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    startActivityForResult(camera, RESULT_CAMERA);

					
				}
			});
			
			cancelButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					createImagePopupWindow.dismiss();
					
				}
			});
			ColorDrawable background = new ColorDrawable(getResources().getColor(android.R.color.transparent));
			createImagePopupWindow = PopupWindowUtil.getPopupWindow(
																	createImageMenuView, 
																	background, 
																	BBSSendPostActivity.this, 
																	LayoutParams.WRAP_CONTENT, 
																	LayoutParams.WRAP_CONTENT);
		}
		if (!createImagePopupWindow.isShowing())
		{
			createImagePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		}
		
	}
	
	
	private void showPostReward(final View parentView){
		if (rewardPopupWindow ==null)
		{
			View rewardOptionView = getLayoutInflater().inflate(R.layout.bbs_post_reward_option, null);
			Button noReward = (Button) rewardOptionView.findViewById(R.id.bbs_no_reward);
			Button reward100 = (Button) rewardOptionView.findViewById(R.id.bbs_reward_100);
			Button reward300 = (Button) rewardOptionView.findViewById(R.id.bbs_reward_300);
			Button reward500 = (Button) rewardOptionView.findViewById(R.id.bbs_reward_500);
			Button reward1000 = (Button) rewardOptionView.findViewById(R.id.bbs_reward_1000);
			
			
			noReward.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					bonus = 0;
					rewardPopupWindow.dismiss();
					
				}
			});
			
			reward100.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					bonus = 100;
					rewardPopupWindow.dismiss();
				}
			});
			
			
			reward300.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					bonus = 300;
					rewardPopupWindow.dismiss();
				}
			});
			
			reward500.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					bonus = 500;
					rewardPopupWindow.dismiss();
				}
			});
			
			reward1000.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					bonus = 1000;
					rewardPopupWindow.dismiss();					
				}
			});
			
			ColorDrawable background = new ColorDrawable(getResources().getColor(android.R.color.transparent)); 
			rewardPopupWindow = PopupWindowUtil.getPopupWindow(
																rewardOptionView, 
																background,
																BBSSendPostActivity.this, 
																LayoutParams.WRAP_CONTENT, 
																LayoutParams.WRAP_CONTENT);
		}
		if (!rewardPopupWindow.isShowing())
		{
			rewardPopupWindow.showAsDropDown(parentView, 
					 parentView.getScrollX(), 
					 parentView.getScrollY()-3*parentView.getHeight());
		}
		
		rewardPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
		{
			
			@Override
			public void onDismiss()
			{
				if (bonus>0)
				{
					((Button)parentView).setText("+"+bonus);
				}
				
			}
		});
		
	}
	
	
	
	
	private void sendPost(String content,int bonus,String imageURL,byte[] drawData,String drawImage){
		
		
		
		if(!StringUtil.isEmpty(imageURL)){
			contentType = BBSStatus.ContentTypeImage;
		}
		
		if (drawData != null)
		{
			contentType = BBSStatus.ContentTypeDraw;
		}
		
		if (!StringUtil.isEmpty(imageURL)&&drawData!=null)
		{
			contentType = BBSStatus.ContentTypeImage&BBSStatus.ContentTypeDraw;
		}
		
		BBSMission.getInstance().createBBSPost
									(board.getBoardId(), 
									 contentType, 
									 content, 
									 bonus, 
									 imageURL, 
									 drawData, 
									 drawImage, 
									 new MissionCompleteInterface()
									{
										
										@Override
										public void onComplete(int errorCode)
										{
											switch (errorCode)
											{
											case ErrorCode.ERROR_SUCCESS:
												Toast.makeText(BBSSendPostActivity.this, R.string.send_post_success, 
														Toast.LENGTH_LONG).show();
												break;
											case ErrorCode.ERROR_BLACK_USER:
												Toast.makeText(BBSSendPostActivity.this, R.string.black_user, 
														Toast.LENGTH_LONG).show();
												break;
											case ErrorCode.ERROR_BLACK_DEVICE:
												Toast.makeText(BBSSendPostActivity.this, R.string.black_device, 
														Toast.LENGTH_LONG).show();
												break;
											default:
												Toast.makeText(BBSSendPostActivity.this, R.string.send_post_fail, 
														Toast.LENGTH_LONG).show();
												break;
											}
											
										}
									});
		
		finish();
		hidekeyboard();
	}
	
	
	

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		hidekeyboard();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageURL = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(imageURL);
            createImageButton.setImageBitmap(bitmap);
 
        }
		
		if(requestCode == RESULT_CAMERA && resultCode == RESULT_OK && null != data){
			   if(!MemoryUtil.sdcardEnable()){
				    Log.e(TAG, "sd card disable");
				    return;
			   }
			   String fileName = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
			   String filePath = FileUtil.getSDCardCachePath(BBSSendPostActivity.this, getString(R.string.app_name));
			   Bundle bundle = data.getExtras();
			   //获取相机返回的数据，并转换为图片格式
			   Bitmap bitmap = (Bitmap)bundle.get("data");
			   FileUtil.saveBitmapInFile(filePath, fileName, bitmap);
			   imageURL = filePath+fileName;
			   createImageButton.setImageBitmap(bitmap);
			  }
	}
	
	private void hidekeyboard(){
		KeyboardUtil.hideKeyboard(contentEditText, BBSSendPostActivity.this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (createImagePopupWindow!=null&&createImagePopupWindow.isShowing())
		{
			createImagePopupWindow.dismiss();
		}
	}
	
	
}
