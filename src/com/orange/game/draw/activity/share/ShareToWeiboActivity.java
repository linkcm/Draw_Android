/**  
        * @title ShareToWeiboActivity.java  
        * @package com.orange.game.draw.activity.share  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-21 上午11:47:46  
        * @version V1.0  
 */
package com.orange.game.draw.activity.share;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat.ShareParams;

import com.google.protobuf.InvalidProtocolBufferException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.orange.common.android.sns.db.OauthTokenKeeper;
import com.orange.common.android.sns.qqweibo.QQOauth2AccessToken;
import com.orange.common.android.sns.qqweibo.QQRequestListener;
import com.orange.common.android.sns.qqweibo.QQWeiboApi;
import com.orange.common.android.sns.sina.SinaWeiboApi;
import com.orange.common.android.utils.FileUtil;
import com.orange.game.R;
import com.orange.game.R.string;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.model.ConfigManager;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;



public class ShareToWeiboActivity extends CommonDrawActivity
{

	public static final String IMAGE_URL = "image_url";
	public static final String IMAGE_CACHE_URL = "image_cache_url";
	public static final String SHARE_TO = "share_to";
	public static final String SHARE_CONTENT = "share_content";
	public static final int SINA_WEIBO = 1;
	public static final int QQ_WEIBO = 2;
	public static final int FACEBOOK = 3;
	public static final int WECHAT = 4;
	public static final int WECHAT_FRIEND = 5;
	public static final int EMAIL = 6;
	private String imageUrl;
	
	
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

	private String	imageCachePath;
	private EditText contentEditText;
	private int shareTo = SINA_WEIBO;
	/*private SinaWeiboApi sinaWeiboApi;
	private QQWeiboApi qqWeiboApi;*/
	private ImageLoader imageLoader;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_to_weibo);
		imageUrl = getIntent().getStringExtra(IMAGE_URL);
		shareTo = getIntent().getIntExtra(SHARE_TO, SINA_WEIBO);
		String shareContent = getIntent().getStringExtra(SHARE_CONTENT);
		contentEditText = (EditText) findViewById(R.id.share_content);
		Button sendButton = (Button) findViewById(R.id.send_button);
		ImageView opusImageView = (ImageView) findViewById(R.id.opus_image);
		contentEditText.setText(shareContent);
		imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(imageUrl, opusImageView);	
		imageCachePath = DiscCacheUtil.findInCache(imageUrl, imageLoader.getDiscCache()).getAbsolutePath();
		
		sendButton.setOnClickListener(sendOnClickListener);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		
	}

	
	private OnClickListener sendOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String content = contentEditText.getText().toString();
			switch (shareTo)
			{
			case SINA_WEIBO:
				sendToSinaWeibo(content, imageCachePath);
				break;
			case QQ_WEIBO:
				sendToQQWeibo(content, imageCachePath);
				break;
			default:
				break;
			}
			
		}
	};
	
	
	
	private void sendToSinaWeibo(String content,String imageUrl){		
		AbstractWeibo sinaWeibo = AbstractWeibo.getWeibo(ShareToWeiboActivity.this, SinaWeibo.NAME);
		sinaWeibo.setWeiboActionListener(new WeiboActionListener()
		{
			
			@Override
			public void onError(AbstractWeibo arg0, int arg1, Throwable arg2)
			{
				Log.e(TAG, "<sendToSinaWeibo> but catch weibo exception e = "+arg2.toString());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ShareToWeiboActivity.this, "send weibo fail", Toast.LENGTH_LONG)
								.show();
					}
				});
				
			}
			
			@Override
			public void onComplete(AbstractWeibo arg0, int arg1,
					HashMap<String, Object> arg2)
			{
				Log.d(TAG, "share opus to sina weibo success");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ShareToWeiboActivity.this, "send weibo success", Toast.LENGTH_LONG)
								.show();
					}
				});
				
			}
			
			@Override
			public void onCancel(AbstractWeibo arg0, int arg1)
			{
				Log.d(TAG, "share opus to sina weibo cancel");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ShareToWeiboActivity.this, "send weibo cancel", Toast.LENGTH_LONG)
								.show();
					}
				});
				
			}
		});
		SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();	
		shareParams.text = content;
		shareParams.imagePath = imageUrl;
		sinaWeibo.share(shareParams);
		finish();
	}
	
	
	
	private void sendToQQWeibo(String content, String imageUrl)
	{		
		
		AbstractWeibo qqWeibo = AbstractWeibo.getWeibo(ShareToWeiboActivity.this, TencentWeibo.NAME);
		qqWeibo.setWeiboActionListener(new WeiboActionListener()
		{
			
			@Override
			public void onError(AbstractWeibo arg0, int arg1, Throwable arg2)
			{
				Log.e(TAG, "<sendToQQWeibo> but catch weibo exception e = "+arg2.toString());
				runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						Toast.makeText(ShareToWeiboActivity.this, "share to qq weibo fail", Toast.LENGTH_LONG).show();
						
					}
				});
				
			}
			
			@Override
			public void onComplete(AbstractWeibo arg0, int arg1,
					HashMap<String, Object> arg2)
			{
				runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						Toast.makeText(ShareToWeiboActivity.this, "share to qq weibo success", Toast.LENGTH_LONG).show();
						
					}
				});
				
			}
			
			@Override
			public void onCancel(AbstractWeibo arg0, int arg1)
			{
				Log.e(TAG, "<sendToQQWeibo> cancel ");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(ShareToWeiboActivity.this, "send qq weibo cancel", Toast.LENGTH_LONG)
								.show();
					}
				});
				
			}
		});
		TencentWeibo.ShareParams shareParams = new TencentWeibo.ShareParams();
		shareParams.text = content;
		shareParams.imagePath = imageUrl;
		qqWeibo.share(shareParams);
		
		
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	
}
