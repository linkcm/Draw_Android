/**  
        * @title TimelineCommentFragment.java  
        * @package com.orange.game.draw.activity.timeline.fragment  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-11 上午11:32:38  
        * @version V1.0  
 */
package com.orange.game.draw.activity.timeline.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.orange.game.R;
import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonFragment;
import com.orange.game.draw.activity.drawDetail.DrawDetailActivity;
import com.orange.game.draw.activity.drawDetail.ReplyCommentActivity;
import com.orange.game.draw.activity.timeline.adapter.TimelineCommentListAdapter;
import com.orange.game.draw.activity.timeline.adapter.TimelineListAdapter;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.feed.FeedMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.feed.FeedListType;
import com.orange.game.draw.model.feed.FeedManager;
import com.orange.game.draw.network.DrawNetworkConstants;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-11 上午11:32:38  
 */

public class TimelineCommentFragment extends CommonFragment
{
	private static final String TAG = "TimelineCommentFragment";

	private static final String BUNDLE_KEY_TAB_ID = "tabId";

	public static TimelineCommentFragment newInstance(FragmentManager fm, FragmentTransaction ft, String tabId){

		TimelineCommentFragment timelineCommentFragment = (TimelineCommentFragment) fm.findFragmentByTag(tabId);		
		if(timelineCommentFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			Bundle bundle = TimelineCommentFragment.createBundle(tabId);
			timelineCommentFragment = new TimelineCommentFragment();
			timelineCommentFragment.setArguments(bundle);
			ft.add(R.id.real_tab_content, timelineCommentFragment, tabId);			
		}else{	
			ft.show(timelineCommentFragment);
		}
		return timelineCommentFragment;
	}
	
	
	
	
	public static Bundle createBundle(String tabId){
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_KEY_TAB_ID, tabId);
		return bundle;
	}
	
	
	
	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId ){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		TimelineCommentFragment.newInstance(fm, ft, tabId);
		ft.commit();
	}
	
	
	private String tabId;
	private FeedManager feedManager ;
	private PullToRefreshListView pullToRefreshListView;
	private TimelineCommentListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "<onCreate>");
		tabId = getArguments().getString(BUNDLE_KEY_TAB_ID);
		//feedManager = FeedMission.getInstance().getMyCommentManager();
		feedManager = new FeedManager(FeedListType.FeedListTypeComment);
		loadData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{	
		Log.d(TAG, "<onCreateView>");
		View fragmentView = null;
		fragmentView = initListFragment(inflater, container, getActivity());
		
		return fragmentView;
	}
	
	
	public View initListFragment(LayoutInflater inflater,ViewGroup container,Context context){
		View fragmetnView = inflater.inflate(R.layout.common_feed_list_fragment, container, false);
		pullToRefreshListView =  (PullToRefreshListView) fragmetnView.findViewById(R.id.common_fragment_list_view);
		ListView listView = pullToRefreshListView.getRefreshableView();
		pullToRefreshListView.setMode(Mode.BOTH);
		pullToRefreshListView.setOnRefreshListener(onRefreshListener2);
		adapter = new TimelineCommentListAdapter(feedManager.getFeedList(),getActivity() );
		listView.setAdapter(adapter);			
		//pullToRefreshListView.setOnScrollListener(listviewOnScrollListener);
		pullToRefreshListView.getRefreshableView().setOnItemClickListener(onItemClickListener);
		return fragmetnView;
	}
	
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			PBFeed feed = feedManager.getFeedList().get(arg2-1);
			//DrawDetailActivity.newInstance(getActivity(), feed.getFeedId());
			showAlertView(feed, getActivity(), arg1);
		}
	};
	
	
	
	private OnRefreshListener2 onRefreshListener2 = new OnRefreshListener2(){

		@Override
		public void onPullDownToRefresh(PullToRefreshBase refreshView)
		{
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			loadData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase refreshView)
		{
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getLastUpdateTime());
			loadMoreData();
			
		}
		
	};
	
	
	
	
	


	 
	

	private  AlertDialog alertDialog;
	
	
	private OnClickListener checkOpusOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			String feedId = (String) v.getTag();
			DrawDetailActivity.newInstance(getActivity(), feedId);
			alertDialog.dismiss();
		}
	};
	
	
	private void showAlertView(final PBFeed feed,final Activity activity,final View showLoationView){
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.show();
		alertDialog.setCancelable(true);
		Window window = alertDialog.getWindow();
		View view = LayoutInflater.from(activity).inflate(R.layout.time_line_comment_alert_dialog, null);
		TextView titleTextView = (TextView) view.findViewById(R.id.alert_dialog_title);
		titleTextView.setText(R.string.chose_action);
		Button checkOpus = (Button) view.findViewById(R.id.alert_dialog_button_1);
		checkOpus.setTag(feed.getOpusId());
		checkOpus.setText(R.string.check_opus);
		checkOpus.setOnClickListener(checkOpusOnClickListener );
		Button replyComment = (Button) view.findViewById(R.id.alert_dialog_button_2);
		replyComment.setText(R.string.reply_comment);
		replyComment.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.putExtra(ReplyCommentActivity.PB_FEED, feed.toByteArray());
				intent.setClass(activity, ReplyCommentActivity.class);
				activity.startActivity(intent);
				alertDialog.dismiss();
			}
		});
		window.setContentView(view);

	}
	
	
	private String getLastUpdateTime(){
		String lastUpdateTime = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		return lastUpdateTime;
	}




	@Override
	public void loadData()
	{
		if (((CommonDrawActivity)getActivity()).isProgressDialogShowing())
		    return;
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
        int offset = 0;
        int limit = ConfigManager.getInstance().getFeedListDisplayCount();
        FeedMission.getInstance().getMyCommentFeedList(feedManager,offset, limit, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				pullToRefreshListView.onRefreshComplete();
				((CommonDrawActivity)getActivity()).hideDialog();
				if (errorCode == ErrorCode.ERROR_SUCCESS){
					adapter.notifyDataSetChanged();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}
				
			}
		});
	}




	@Override
	public void loadMoreData()
	{
		if (((CommonDrawActivity)getActivity()).isProgressDialogShowing())
		    return;
		((CommonDrawActivity)getActivity()).showProgressDialog(R.string.loading);
        int offset = feedManager.getFeedListCount();
        int limit = ConfigManager.getInstance().getFeedListDisplayCount();
        FeedMission.getInstance().getMyCommentFeedList(feedManager,offset, limit, new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				((CommonDrawActivity)getActivity()).hideDialog();
				pullToRefreshListView.onRefreshComplete();
				if (errorCode == ErrorCode.ERROR_SUCCESS){
					adapter.notifyDataSetChanged();
				}else{
					DrawNetworkConstants.errorToast(getActivity(), errorCode);
				}
			}
		});
		
	}




	@Override
	public void clear()
	{
		feedManager.clear();
	}
}
