/**  
        * @title SnsPopupView.java  
        * @package com.orange.game.draw.activity.common  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-7 下午4:02:37  
        * @version V1.0  
 */
package com.orange.game.draw.activity.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.friends.Wechat.ShareParams;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.orange.common.android.sns.qqweibo.QQWeiboHandler;
import com.orange.common.android.sns.sina.SinaWeiboHandler;
import com.orange.common.android.sns.wechat.WeChatApi;
import com.orange.common.android.utils.FileUtil;
import com.orange.common.android.utils.ImageUtil;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.activity.drawDetail.adapter.SNSSharePopupAdapter;
import com.orange.game.draw.activity.share.ShareItemData;
import com.orange.game.draw.activity.share.ShareToWeiboActivity;
import com.orange.game.draw.model.ConfigManager;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-7 下午4:02:37  
 */

public class SNSPopupView
{
	

	private static final String TAG = "SNSPopupView";
	private Context context;
	private String opusWord;
	private String imageUrl;
	

	private ImageLoader imageLoader;
	private PopupWindow sharePopupWindow;
	
	public SNSPopupView(Context context, String opusWord, String imageUrl)
	{
		super();
		this.context = context;
		this.opusWord = opusWord;
		this.imageUrl = imageUrl;
		imageLoader = ImageLoader.getInstance();
	}





	public PopupWindow getSharePopupWindow(View view)
	{
		if (sharePopupWindow == null)
		{
			View contentView = LayoutInflater.from(context).inflate(R.layout.share_popup_window, null);
			GridView shareGridView = (GridView) contentView.findViewById(R.id.share_popup_grid_view);
			
			SNSSharePopupAdapter	sharePopupAdapter = new SNSSharePopupAdapter(context, createShareItem());
			
			shareGridView.setAdapter(sharePopupAdapter);			
			sharePopupWindow = PopupWindowUtil.getPopupWindow(contentView, context.getResources().getDrawable(R.drawable.share), context, 400, LayoutParams.WRAP_CONTENT);
		}		
		sharePopupWindow.showAtLocation(view, Gravity.BOTTOM,0, view.getBottom());
		return sharePopupWindow;
	}
	
	
	
	
	
	
	private  List<ShareItemData> createShareItem()
	{			
		ShareItemData sinaWeibo = new ShareItemData(R.string.sina_weibo, R.drawable.sina, sinaWeiboOnClickListener);
		ShareItemData qqWeibo = new ShareItemData(R.string.qq_weibo, R.drawable.qq, qqWeiboOnClickListener);
		ShareItemData facebook = new ShareItemData(R.string.facebook, R.drawable.facebook, null);
		ShareItemData weiXin = new ShareItemData(R.string.wechat,R.drawable.wechat, weChatOnClickListener);
		ShareItemData weiXinFriend = new ShareItemData(R.string.wechat_friend, R.drawable.wechat_friend, weChatFriOnClickListener);
		ShareItemData email = new ShareItemData(R.string.email,R.drawable.email,emailOnClickListener);
		ShareItemData gallery = new ShareItemData(R.string.gallery, R.drawable.gallery, galleryOnClickListener);
		ShareItemData favorite = new ShareItemData(R.string.favorite, R.drawable.favorite, null);
		
		List<ShareItemData> shareItemDatas = new ArrayList<ShareItemData>();
		shareItemDatas.add(sinaWeibo);
		shareItemDatas.add(qqWeibo);
		shareItemDatas.add(facebook);
		shareItemDatas.add(weiXin);
		shareItemDatas.add(weiXinFriend);
		shareItemDatas.add(email);
		shareItemDatas.add(gallery);
		shareItemDatas.add(favorite);
		
		return shareItemDatas;
		}
		
		
	private OnClickListener sinaWeiboOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String shareContent = String.format(context.getString(R.string.share_other_to_weibo), opusWord);
			Intent intent = new Intent();
			intent.putExtra(ShareToWeiboActivity.IMAGE_URL,imageUrl);
			intent.putExtra(ShareToWeiboActivity.SHARE_TO, ShareToWeiboActivity.SINA_WEIBO);
			intent.putExtra(ShareToWeiboActivity.SHARE_CONTENT, shareContent);
			intent.setClass(context, ShareToWeiboActivity.class);
			context.startActivity(intent);
			
			
		}
	};
	
	
	private OnClickListener qqWeiboOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String shareContent = String.format(context.getString(R.string.share_other_to_weibo), opusWord);
			Intent intent = new Intent();
			intent.putExtra(ShareToWeiboActivity.IMAGE_URL,imageUrl);
			intent.putExtra(ShareToWeiboActivity.SHARE_TO, ShareToWeiboActivity.QQ_WEIBO);
			intent.putExtra(ShareToWeiboActivity.SHARE_CONTENT, shareContent);
			intent.setClass(context, ShareToWeiboActivity.class);
			context.startActivity(intent);
					
		}
	};
	
	
	private OnClickListener weChatOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			
			AbstractWeibo wechat = AbstractWeibo.getWeibo(context, Wechat.NAME);
			Wechat.ShareParams params = new Wechat.ShareParams();
			params.title = "test";
			params.imagePath = imageUrl;
			wechat.share(params);
		}
	};
	
	
	private OnClickListener weChatFriOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{				
			
			AbstractWeibo wechatFriend = AbstractWeibo.getWeibo(context, WechatMoments.NAME);
			wechatFriend.setWeiboActionListener(new WeiboActionListener()
			{
				
				@Override
				public void onError(AbstractWeibo arg0, int arg1, Throwable arg2)
				{
					Toast.makeText(context,  "share to wechat friend fail",Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onComplete(AbstractWeibo arg0, int arg1,
						HashMap<String, Object> arg2)
				{
					Toast.makeText(context,  "share to wechat friend success",Toast.LENGTH_SHORT).show();
					
				}
				
				@Override
				public void onCancel(AbstractWeibo arg0, int arg1)
				{
					Toast.makeText(context,  "share to wechat friend cancel",Toast.LENGTH_SHORT).show();
				}
			});
			
			Wechat.ShareParams params = new Wechat.ShareParams();
			params.title = "test";
			params.imagePath = imageUrl;
			wechatFriend.share(params);
		}	
	};
	
	private OnClickListener emailOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			File file = DiscCacheUtil.findInCache(imageUrl, imageLoader.getDiscCache());
			String content = "android test";
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, content);
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			intent.setType("application/octet-stream");
			context.startActivity(intent);	
			/*AbstractWeibo email = AbstractWeibo.getWeibo(activity, Email.NAME);
			(()email).send("","test", content,imageUrl);*/
		}
	};
	
	
	private OnClickListener galleryOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String imageCachePath = DiscCacheUtil.findInCache(imageUrl, imageLoader.getDiscCache()).getAbsolutePath();
			Bitmap bitmap = BitmapFactory.decodeFile(imageCachePath);
			if (bitmap != null)
			{
				boolean result = ImageUtil.saveImageToGallery(bitmap, context);
				if (result)
				{
					Toast.makeText(context, "save in gallery success ",Toast.LENGTH_SHORT ).show();
				}else {
					Toast.makeText(context, "save in gallery failed ",Toast.LENGTH_SHORT ).show();
				}			
			}else {
				Log.e(TAG, "<saveImageCache> but image bitmap cache is null");
			}
			closeSharePopupWindow();
		}
	};
	
	private void closeSharePopupWindow(){
		if (sharePopupWindow != null)
		{
			sharePopupWindow.dismiss();
		}
	}
	
	

	
	
}
