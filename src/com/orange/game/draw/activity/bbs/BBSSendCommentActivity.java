/**  
        * @title BBSSendCommentActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-1 下午3:38:14  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.orange.game.draw.model.bbs.BBSStatus;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSAction;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-1 下午3:38:14  
 */

public class BBSSendCommentActivity extends CommonDrawActivity
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

	public static final String PBBBSPOST = "pbbbsPost";
	public static final String PBBBSACTION = "pbbbsAction";
	
	private PBBBSPost post = null;
	private PBBBSAction action = null;
	private EditText commentEditText;
	private static int RESULT_LOAD_IMAGE = 1;
	private ImageButton createDrawButton;
	private ImageButton createImageButton;
	private String imageURL = "";
	private static int RESULT_CAMERA = 2;
	private PopupWindow createImagePopupWindow;
	
	public static void newInstance(Context context,PBBBSPost post,PBBBSAction action){
		if (post == null)
			return;
		Intent intent = new Intent();
		intent.setClass(context, BBSSendCommentActivity.class);
		intent.putExtra(PBBBSPOST, post.toByteArray());
		if (action != null)
		{
			intent.putExtra(PBBBSACTION, post.toByteArray());
		}
		
		context.startActivity(intent);
	}
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_send_coomment);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
		
		Button sendButton = (Button) findViewById(R.id.bbs_send_button);
		commentEditText = (EditText) findViewById(R.id.bbs_post_comment_content);
		sendButton.setOnClickListener(sendOnClickListener);
		
		createDrawButton = (ImageButton) findViewById(R.id.bbs_comment_create_draw_button);
		createImageButton = (ImageButton) findViewById(R.id.bbs_comment_create_image_button);
		
		createImageButton.setOnClickListener(createImageOnClickListener);
		
		try
		{
			post = PBBBSPost.parseFrom(getIntent().getByteArrayExtra(PBBBSPOST));
			if (getIntent().getByteArrayExtra(PBBBSACTION)!= null)
			{
				action = PBBBSAction.parseFrom(getIntent().getByteArrayExtra(PBBBSACTION));
			}
		} catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		
		KeyboardUtil.showKeyboardForOnCreate(commentEditText, BBSSendCommentActivity.this);
	}

	private OnClickListener sendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String content = "";
			content = commentEditText.getText().toString();
			if (StringUtil.isEmpty(content)||content.trim().length()<5)
			{
				Toast.makeText(
								BBSSendCommentActivity.this, 
								R.string.comment_content_less_toast, 
								Toast.LENGTH_SHORT).show();
				return;
			}
			
			sendComment(content,imageURL,null);
		}
	};
	
	
	private void showCreateImageMenu(View parent){
		hideKeyboard();
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
																	BBSSendCommentActivity.this, 
																	LayoutParams.WRAP_CONTENT, 
																	LayoutParams.WRAP_CONTENT);
		}
		if (!createImagePopupWindow.isShowing())
		{
			createImagePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		}
		
	}
	
	private OnClickListener createImageOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			 showCreateImageMenu(v);
			
		}
	};
	
	
	
	private void sendComment(String content,String imageURL,byte[] drawData){
		
		Log.d(TAG, "<sendComment> imageURL = "+imageURL);
		
		
		String postId = post.getPostId();
		String postUid = post.getCreateUser().getUserId();
		String actionId = "";
		String actionUid = "";
		String actionNickName = "";
		int sourceActionType =  BBSStatus.ActionTypeNO;
		int contentType = BBSStatus.ContentTypeText;
		int actionType = BBSStatus.ActionTypeComment;
		String drawImage = "";
		
		String briefText ;
		if (post.getContent().getText().length()>15)		
			briefText = post.getContent().getText().substring(0, 14);
		else
			briefText = post.getContent().getText();
		
		
		
		
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
		
		if (action != null)
		{
			actionId = action.getActionId();
			actionUid = action.getCreateUser().getUserId();
			actionNickName = action.getCreateUser().getNickName();
			sourceActionType = action.getType();
		}
		
		BBSMission.getInstance().createBBSPostComment(
				postId, 
				actionId, 
				postUid, 
				actionUid, 
				actionNickName, 
				briefText, 
				sourceActionType, 
				contentType, 
				actionType, 
				content, 
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
							Toast.makeText(BBSSendCommentActivity.this, R.string.send_comment_success, 
									Toast.LENGTH_LONG).show();
							break;
						case ErrorCode.ERROR_BLACK_USER:
							Toast.makeText(BBSSendCommentActivity.this, R.string.black_user, 
									Toast.LENGTH_LONG).show();
							break;
						case ErrorCode.ERROR_BLACK_DEVICE:
							Toast.makeText(BBSSendCommentActivity.this, R.string.black_device, 
									Toast.LENGTH_LONG).show();
							break;
						default:
							Toast.makeText(BBSSendCommentActivity.this, R.string.send_comment_fail, 
									Toast.LENGTH_LONG).show();
							break;
						}
						
											
					}
				});
		hideKeyboard();
		finish();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		hideKeyboard();
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
			   String filePath = FileUtil.getSDCardCachePath(BBSSendCommentActivity.this, getString(R.string.app_name));
			   Bundle bundle = data.getExtras();
			   //获取相机返回的数据，并转换为图片格式
			   Bitmap bitmap = (Bitmap)bundle.get("data");
			   FileUtil.saveBitmapInFile(filePath, fileName, bitmap);
			   imageURL = filePath+fileName;
			   createImageButton.setImageBitmap(bitmap);
			  }
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
	

	private void hideKeyboard(){
		KeyboardUtil.hideKeyboard(commentEditText, BBSSendCommentActivity.this);
	}
	
}
