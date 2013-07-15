/**  
        * @title DrawHomeMoreFragment.java  
        * @package com.orange.game.draw.activity.more.fragment  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-29 上午11:50:57  
        * @version V1.0  
 */
package com.orange.game.draw.activity.more;

import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat.ShareParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.sns.qqweibo.QQRequestListener;
import com.orange.common.android.sns.qqweibo.QQWeiboApi;
import com.orange.common.android.sns.sina.SinaWeiboApi;
import com.orange.common.android.utils.ActivityUtil;
import com.orange.common.android.utils.PopupWindowUtil;
import com.orange.common.android.utils.SystemUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.friend.fragment.DrawHomeFriendFragment;
import com.orange.game.draw.activity.more.adapter.MoreAdapter;
import com.orange.game.draw.model.ConfigManager;
import com.tencent.weibo.api.PrivateAPI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-29 上午11:50:57  
 */

public class DrawHomeMoreFragment extends Fragment implements WeiboActionListener ,Callback
{

	private static final String TAG = "DrawHomeMoreFragment";
	private Handler handler;
	private PopupWindow sharePopupWindow;
	
	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		DrawHomeMoreFragment.newInstance(fm, ft, tabId);
		ft.commit();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	
	
	public static DrawHomeMoreFragment newInstance(FragmentManager fm, FragmentTransaction ft,
			String tabId)
	{
		DrawHomeMoreFragment drawHomeMoreFragment = (DrawHomeMoreFragment) fm.findFragmentByTag(tabId);		
		if(drawHomeMoreFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			drawHomeMoreFragment = new DrawHomeMoreFragment();
			ft.add(R.id.real_tab_content, drawHomeMoreFragment, tabId);			
		}else{	
			ft.show(drawHomeMoreFragment);
		}
		return drawHomeMoreFragment;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		handler = new Handler(this);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (container == null)
		return null;
		View view = inflater.inflate(R.layout.draw_home_more_fragment, container, false);
		ListView moreList = (ListView) view.findViewById(R.id.draw_home_more_list);
		String[] data = getActivity().getResources().getStringArray(R.array.more);
		MoreAdapter adapter = new MoreAdapter(data,getActivity());
		moreList.setAdapter(adapter);
		moreList.setOnItemClickListener(onItemClickListener);
		return view;
	}

	
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			switch (arg2)
			{
			case 0:
				shareToFriend(arg1);
				break;
			case 1:
				followDrawlively();
				break;
			case 2:
				submitNewWord();
				break;
			case 3:
				submitBug();
				break;
			case 4:
				submitFeedBack();
				break;
			case 5:
				submitFeedBack();
				break;
			case 6:
				submitFeedBack();
				break;
			case 7:
				submitFeedBack();
				break;
			case 8:
				AboutUS();
				break;
			default:
				break;
			}
			
		}

		
	};
	
	private void followDrawlively(){
		AbstractWeibo sinaWeibo = AbstractWeibo.getWeibo(getActivity(), SinaWeibo.NAME);
		sinaWeibo.setWeiboActionListener(DrawHomeMoreFragment.this);
		sinaWeibo.followFriend(ConfigManager.getInstance().getDrawLivelySinaWeibo());
	}
	
	
	private void submitNewWord(){
		String title = getString(R.string.submit_new_word);
		String tips = getString(R.string.submit_new_word_tips);
		CommonFeedBackActivity.newInstance(getActivity(), title, tips, CommonFeedBackActivity.NEW_WORD);
	}
	
	
	private void submitBug(){
		String title = getString(R.string.submit_bug);
		String tips = getString(R.string.submit_tips);
		CommonFeedBackActivity.newInstance(getActivity(), title, tips, CommonFeedBackActivity.BUG);
		//initIntent(title, tips);
	}
	
	private void submitFeedBack(){
		String title = getString(R.string.submit_feedback);
		String tips = getString(R.string.submit_tips);
		CommonFeedBackActivity.newInstance(getActivity(), title, tips, CommonFeedBackActivity.ADVICE);
		//initIntent(title, tips);
	}
	

	private void AboutUS()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), AboutUSActivity.class);
		startActivity(intent);
		
	}
	
	private void shareToFriend(View parentView){
		if (sharePopupWindow == null)
		{
			View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.share_to_friend_popup_window, null);
			Button cancelButton = (Button) contentView.findViewById(R.id.cancel_button);
			Button shareByMessageButton = (Button) contentView.findViewById(R.id.share_to_friend_by_message_button);
			Button shareByEmailButton = (Button) contentView.findViewById(R.id.share_to_friend_by_email_button);
			Button shareBySinaWeiboButton = (Button) contentView.findViewById(R.id.share_to_friend_by_sina_weibo_button);
			Button shareByQQWeiboButton = (Button) contentView.findViewById(R.id.share_to_friend_by_qq_weibo_button);
			ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.half_transparent_black)); 
			sharePopupWindow = PopupWindowUtil.getMatcParentPopupWindow(contentView, background, getActivity());
			
			cancelButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (sharePopupWindow != null)
					{
						sharePopupWindow.dismiss();
					}
					
				}
			});
			
			shareByMessageButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (SystemUtil.isPhone(getActivity()))
					{
						String message = "";
						ActivityUtil.sendMessage(getActivity(), message);
					}else {
						Toast.makeText(getActivity(), R.string.is_not_phone, Toast.LENGTH_LONG).show();
					}
					
				}
			});
			
			
			shareByEmailButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					String content = "test";
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_TEXT, content);
					intent.setType("application/octet-stream");
					startActivity(intent);
				}
			});
			
			shareBySinaWeiboButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					String content = "test";
					AbstractWeibo sinaWeibo = AbstractWeibo.getWeibo(getActivity(), SinaWeibo.NAME);
					sinaWeibo.setWeiboActionListener(DrawHomeMoreFragment.this);
					ShareParams shareParams = new ShareParams();
					shareParams.text = content;
					sinaWeibo.share(shareParams);
					//sinaWeibo.share(content, "");
					
				}
			});
			
			
			
			
			
			
			
			shareByQQWeiboButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					
					String content = "test";
					AbstractWeibo qqWeibo = AbstractWeibo.getWeibo(getActivity(), TencentWeibo.NAME);
					qqWeibo.setWeiboActionListener(DrawHomeMoreFragment.this);
					
					ShareParams shareParams = new ShareParams();
					shareParams.text = content;
					
					qqWeibo.share(shareParams);
					
				}
			});
		}
		
		sharePopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);	
		
		
	}



	@Override
	public void onCancel(AbstractWeibo weibo, int action)
	{
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
		
	}



	@Override
	public void onComplete(AbstractWeibo weibo, int action,
			HashMap<String, Object> arg2)
	{
		
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
		
	}



	@Override
	public void onError(AbstractWeibo weibo, int action, Throwable arg2)
	{
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = weibo;
		handler.sendMessage(msg);
		
	}



	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.arg1) {
		case 1: { // 成功
			showNotification(2000,  "share_completed");
		}
		break;
		case 2: { // 失败
			showNotification(2000, "share_failed");
		}
		break;
		case 3: { // 取消
			showNotification(2000, "share_canceled");
		}
		break;
		
	}
		if (sharePopupWindow != null&&sharePopupWindow.isShowing())
		{
			sharePopupWindow.dismiss();
		}
	return false;
	}
	
	
	
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getActivity().getApplicationContext();
			final NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancelAll();
			
			int icon = R.drawable.draw_icon;
			String title = "share";
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, title, text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(1, notification);
			
			if (cancelTime > 0) {
				handler.postDelayed(new Runnable() {
					public void run() {
						if (nm != null) {
							nm.cancelAll();
						}
					}
				}, cancelTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
