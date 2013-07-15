/**  
 * @title DrawHomeFragment.java  
 * @package com.orange.game.draw.activity.home  
 * @description   
 * @author liuxiaokun  
 * @update 2012-12-29 上午11:00:44  
 * @version V1.0  
 */
package com.orange.game.draw.activity.home.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.ImageUtil;
import com.orange.game.constants.ErrorCode;
import com.orange.game.R;
import com.orange.game.draw.activity.bbs.BBSActivity;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.activity.home.DrawHomeActivity;
import com.orange.game.draw.activity.home.adapter.CommonViewPagerAdapter;
import com.orange.game.draw.activity.home.menu.MenuData;
import com.orange.game.draw.activity.home.menu.MenuViewManager;
import com.orange.game.draw.activity.shop.ShopActivity;
import com.orange.game.draw.activity.timeline.TimelineActivity;
import com.orange.game.draw.activity.top.TopDrawActivity;
import com.orange.game.draw.activity.user.RegisterUserActivity;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.mission.statistics.StatisticsMission;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.model.friend.Friend;
import com.orange.game.draw.model.user.UserManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;
import com.readystatesoftware.viewbadger.BadgeView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class DrawHomeFragment extends Fragment {

	private List<MenuData> menuList = new ArrayList<MenuData>();

	private static final String TAG = "DrawHomeFragment";
	private View drawHomeFragmentView = null;
	private FeedManager feedManager;
	private ImageLoader imageLoader;
	private CommonViewPagerAdapter adapter;
	private ViewPager topDrawPicPager;
	
	private final Timer timer = new Timer();
	private TimerTask timerTask ;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			int position= msg.arg1;
			topDrawPicPager.setCurrentItem(position);
		}
		
	};
	private boolean flag = true;
	
	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		DrawHomeFragment.newInstance(fm, ft, tabId);
		ft.commit();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	
	
	public static DrawHomeFragment newInstance(FragmentManager fm, FragmentTransaction ft,
			String tabId)
	{
		DrawHomeFragment drawHomeFragment = (DrawHomeFragment) fm.findFragmentByTag(tabId);		
		if(drawHomeFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			drawHomeFragment = new DrawHomeFragment();
			ft.add(R.id.real_tab_content, drawHomeFragment, tabId);			
		}else{	
			ft.show(drawHomeFragment);
		}
		return drawHomeFragment;
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "<onCreate>");
		feedManager = new FeedManager(FeedListType.FeedListTypeHot);
		imageLoader = ImageLoader.getInstance();
		
		 
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Log.d(TAG, "<onCreateView>");
		drawHomeFragmentView = inflater.inflate(R.layout.draw_home_fragment, container,false);
		initUserInfo(drawHomeFragmentView);
		initTopDrawPicture(drawHomeFragmentView, inflater);
		
		initControlView(drawHomeFragmentView, inflater);
		return drawHomeFragmentView;
	}

	private void initUserInfo(View parent) {
		ImageView userAvatar = (ImageView) parent.findViewById(R.id.user_avatar);
		ImageView reChargeImageView = (ImageView) parent.findViewById(R.id.user_recharge_imageview);
		TextView userNameTextView = (TextView) parent.findViewById(R.id.user_name);
		TextView userRichTextView = (TextView) parent.findViewById(R.id.user_rich);
		TextView userLevelTextView = (TextView) parent.findViewById(R.id.user_level);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.common_home_wood_bg);
		bitmap = ImageUtil.getRoundBitmap(bitmap);
		userAvatar.setImageBitmap(bitmap);
		userNameTextView.setText("Taoge");
		userRichTextView.setText("x " + "100000");
		userLevelTextView.setText("LV: " + 6);
		reChargeImageView.setOnClickListener(reChargeOnClickListener);
		userAvatar.setOnClickListener(userAvatarOnClickListener);
	}

	
	
	private OnClickListener userAvatarOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			UserDetailActivity.newInstance(getActivity(), UserManager.getInstance().getUser(),Friend.FRIEND_UNKNOW);
			
		}
	};
	
	private void initTopDrawPicture(View parent, LayoutInflater inflater) {
		
		topDrawPicPager = (ViewPager) parent.findViewById(R.id.top_draw_picture);
		adapter = new CommonViewPagerAdapter(null);
		topDrawPicPager.setAdapter(adapter);
		//topDrawPicPager.setOnPageChangeListener(onPageChangeListener);
	}

	
	
	
	
	private void loadFeedData(){
       ((DrawHomeActivity)getActivity()).showProgressDialog(R.string.loading);
        FeedMission.getInstance().getTopFeedList(feedManager, 0, 6, new MissionCompleteInterface()
		{			
			@Override
			public void onComplete(int errorCode)
			{
				if (getActivity() !=null)
					((DrawHomeActivity)getActivity()).hideDialog();
				if (errorCode == ErrorCode.ERROR_SUCCESS)
				{
					updateTopPicture();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}
			}
		}) ;                
	}
	
	private void updateTopPicture(){
		ArrayList<View> topPictures = new ArrayList<View>();
		List<PBFeed> feeds = feedManager.getFeedList();
		View view;
		ImageView imageView1;
		ImageView imageView2;
		ImageView imageView3;
		int startPosition = 0;
		for (int i = 0; i < 2; i++) {
			view = getActivity().getLayoutInflater().inflate(R.layout.draw_home_gallery, null);
			imageView1 = (ImageView) view.findViewById(R.id.top_picture_1);
			imageView2 = (ImageView) view.findViewById(R.id.top_picture_2);
			imageView3 = (ImageView) view.findViewById(R.id.top_picture_3);
			startPosition = i*3;
			setTopDrawPicture(startPosition, feeds, imageView1);
			setTopDrawPicture(startPosition+1, feeds, imageView2);
			setTopDrawPicture(startPosition+2, feeds, imageView3);
			topPictures.add(view);
		}
		adapter.setTopPictures(topPictures);
		adapter.notifyDataSetChanged();
		setTimerTask();
	}	
	
	private void setTopDrawPicture(int position,List<PBFeed> feeds,ImageView imageView){
		final PBFeed feed;
		if (feeds.size() > position)
		{
			feed = feeds.get(position);
			imageLoader.displayImage(feed.getOpusImage(), imageView);
			
			imageView.setOnClickListener(new OnClickListener()
			{		
				@Override
				public void onClick(View v)
				{
					DrawDetailActivity.newInstance(getActivity(), feed.getFeedId());			
				}
			});
		}
	}
	
	
	
	private void setTimerTask(){
		
		timerTask = new TimerTask()
		{
			
			@Override
			public void run()
			{
				Message message = new Message();
				message.what = 1;
				if (flag)
				{				
					message.arg1 = 1;
					flag = false;
				}else {
					message.what = 0;
					flag = true;
				}
				
				handler.sendMessage(message);
				
			}
		};
		timer.schedule(timerTask, 30000, 30000);
	}
	
	
	
	
	
	private void initMenuData() {
		
		int timelineCount = StatisticsMission.getInstance().getFeedCount();
		int bbsCount = StatisticsMission.getInstance().getBbsCommentCount();
		
		MenuData item1 = new MenuData(R.drawable.start_draw_btn, R.string.draw,
				startDrawOnClickListener,0,false);
		MenuData item2 = new MenuData(R.drawable.start_offline_btn,
				R.string.offline_guess, null,0,false);
		MenuData item3 = new MenuData(R.drawable.start_online_btn,
				R.string.online_guess, null,0,false);
		MenuData item4 = new MenuData(R.drawable.start_timeline_btn,R.string.timeline, 
				timelineOnClickListener,timelineCount,true);
		MenuData item5 = new MenuData(R.drawable.start_top_btn, R.string.top,
				topDrawOnClickListener,0,false);
		MenuData item6 = new MenuData(R.drawable.start_bbs_btn, R.string.bbs,
				bbsOnClickListener,bbsCount,true);
		MenuData item7 = new MenuData(R.drawable.start_shop_btn, R.string.shop,
				shopOnClickListener,0,false);
		
		MenuData item8 = new MenuData(R.drawable.start_contest_btn,
				R.string.draw_contest, null,0,false);

		menuList.add(item1);
		menuList.add(item2);
		menuList.add(item3);
		menuList.add(item4);
		menuList.add(item5);
		menuList.add(item6);
		menuList.add(item7);
		menuList.add(item8);
		
		
	}

	private void initControlView(View parent, LayoutInflater inflater) {

		initMenuData();
		ViewPager viewPager = (ViewPager) parent.findViewById(R.id.control_view_pager);
		MenuViewManager.createMenuView(this.getActivity(), viewPager, inflater,menuList);

		 
		
	}

	

	private OnClickListener startDrawOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			/*((DrawHomeActivity) getActivity()).showProgressDialog(R.string.message, R.string.connecting);
			((DrawHomeActivity) getActivity()).gameNetworkService.connect("192.168.0.19", 8080);
*/
		}
	};

	private OnClickListener topDrawOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (UserManager.getInstance().getUserId()==null) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), RegisterUserActivity.class);
				startActivity(intent); 
			}else{
				gotoTop();
			}
		}

		private void gotoTop() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getActivity(), TopDrawActivity.class);
			startActivity(intent);
		}
	};
	
	

	private OnClickListener timelineOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), TimelineActivity.class);
			startActivity(intent);		
		}
	};
	
	
	private OnClickListener shopOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), ShopActivity.class);
			startActivity(intent);
			
		}
	};
	
	
	private OnClickListener bbsOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), BBSActivity.class);
			startActivity(intent);
			
		}
	};
	
	
	private OnClickListener reChargeOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	@Override
	public void onDestroy() {
		Log.d(TAG, "<onDestroy>");
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "<onStart>");
		if (isHidden())
		{
			return;
		}
		loadFeedData();
		
	}



	@Override
	public void onPause()
	{
		super.onPause();
	}

	
	
	
}
