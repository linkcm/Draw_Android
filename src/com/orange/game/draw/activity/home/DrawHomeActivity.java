package com.orange.game.draw.activity.home;

import java.util.HashMap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import cn.sharesdk.framework.AbstractWeibo;

import com.orange.common.android.smart.model.SmartDataType;
import com.orange.common.android.smart.smartData.SmartData;
import com.orange.common.android.smart.smartData.SmartDataCompleteInterface;
import com.orange.common.android.smart.smartData.SmartDataUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.common.CommonDummyTabContent;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialog;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialogCompleteInterface;
import com.orange.game.draw.activity.friend.fragment.DrawHomeFriendFragment;
import com.orange.game.draw.activity.home.fragment.DrawHomeFragment;
import com.orange.game.draw.activity.message.fragment.DrawHomeMessageFragment;
import com.orange.game.draw.activity.more.DrawHomeMoreFragment;
import com.orange.game.draw.activity.opus.DrawHomeOpusFragment;
import com.orange.game.draw.activity.room.RoomActivity;
import com.orange.game.draw.mission.bbs.BBSMission;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.statistics.StatisticsMission;
import com.orange.game.draw.model.ConfigManager;
import com.orange.game.draw.model.activity.ActivityMange;
import com.orange.game.draw.model.shop.ChargeManager;
import com.orange.game.draw.model.shop.ShopManager;
import com.orange.network.game.protocol.constants.GameConstantsProtos.GameResultCode;
import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;
import com.readystatesoftware.viewbadger.BadgeView;

public class DrawHomeActivity extends CommonDrawActivity {
			
    protected static final String TAG = "DrawHomeActivity"; 
    private static final String TAB_DRAW = "draw";
    private static final String TAB_OPUS = "opus";
    private static final String TAB_FRIEND = "friend";
    private static final String TAB_MESSAGE = "message";
    private static final String TAB_MORE ="more";
    private String currentTabId = TAB_DRAW;
    private TabHost tabHost;
    private HashMap<String, BadgeView> badgeMap = new HashMap<String, BadgeView>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "<onCreate>");
        ActivityMange.getInstance().addActivity(this);
        setContentView(R.layout.main);
        initFooterTab();
        initSmartData();
        
        BBSMission.getInstance().getBBSUserPrivilegeList(new MissionCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
			}
		});
        
        
        
		setProgressDialogOnKey(onKeyListener);
    }
    
    
    private void initSmartData(){
    	SmartData smartData = new SmartData(getApplication());
		final String appName = getString(R.string.app_name);
		final String shopItem = ConfigManager.getInstance().getShopItemName();
		final String iapProduct = ConfigManager.getInstance().getIapProductName();
		smartData.initDataWithName(shopItem, SmartDataType.PB);
		smartData.checkUpdateAndDownload(shopItem,SmartDataType.PB,new SmartDataCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				String path = SmartDataUtil.getPathByName(appName, shopItem, SmartDataType.PB);
				ShopManager.getInstance().paresPBGameItemList(path);
				
			}
		});
		smartData.initDataWithName(iapProduct,SmartDataType.PB);
		smartData.checkUpdateAndDownload(iapProduct,SmartDataType.PB,new SmartDataCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				String path = SmartDataUtil.getPathByName(appName, iapProduct, SmartDataType.PB);
				ChargeManager.getInstance().paresPBSaleIngotList(path);
				
			}
		});
    }
    
	@Override  
    protected void onStart(){  
        super.onStart();                
    }
    
    @Override  
    protected void onStop(){  
        super.onStop(); 
    } 
    
    
    protected boolean isRegisterServerConnect(){
    	return true;
    }
    
    protected boolean isRegisterGameMessage(){
    	return true;    	
    }
    
    @Override
    protected void handleServerConnected(GameMessage message){
    	showProgressDialog(R.string.message, R.string.loading);
    	hideDialog();
	    gameNetworkService.joinGame();
    }    
    
    @Override
    protected void handleServerDisconnected(GameMessage message){
    	hideDialog();
    }    
    
    @Override
    protected void handleJoinGameResponse(GameMessage message){
    	hideDialog();
    	if (message.getResultCode() == GameResultCode.SUCCESS){
    		// goto room activity
    		gotoRoom();
    	}
    	else{
    		// TODO show error here
    	}
    }

    private void gotoRoom(){
    	Log.d(TAG, "<gotoRoom>");
    	Intent intent = new Intent(this, RoomActivity.class);
    	startActivity(intent);
    }
    
   
    
   
	
	 private void initFooterTab()
	   {
		  
		   tabHost = (TabHost) findViewById(android.R.id.tabhost);
		   tabHost.setup();
		   tabHost.setOnTabChangedListener(tabChangeListener);
		  
		  int friendCount = StatisticsMission.getInstance().getFanCount();
		  int messageCount = StatisticsMission.getInstance().getMessageCount();
		   
		 //  tabHost.getTabWidget().setStripEnabled(false);	       
	       createOneTab(TAB_DRAW, R.drawable.tab_home,0);
	       createOneTab(TAB_OPUS, R.drawable.tab_opus,0);
	       createOneTab(TAB_FRIEND, R.drawable.tab_friend,friendCount);
	       createOneTab(TAB_MESSAGE, R.drawable.tab_message,messageCount);
	       createOneTab(TAB_MORE, R.drawable.tab_more,0);
	              
	       tabHost.setCurrentTab(0);
	   }

	 
	 private void createOneTab(String tabId,int backgroundRes,int badgeNumber)
	{
		 View tabView = getLayoutInflater().inflate(R.layout.footer_tab_item, null);
	     tabView.findViewById(R.id.tab_item_button).setBackgroundResource(backgroundRes);
	     if(tabId.equalsIgnoreCase(TAB_MORE)){
	    	 tabView.findViewById(R.id.splie_line).setVisibility(View.GONE);
	     }	     
	     
	     if (badgeNumber>0)
			{
		    	 BadgeView badgeView = new BadgeView(this, tabView.findViewById(R.id.tab_item_button));
		    	 if (badgeNumber>10)
				{
					badgeView.setText("N");
				}else {
					badgeView.setText(""+badgeNumber);
				}
			     badgeView.show();
			     badgeMap.put(tabId, badgeView);
			}
	     
		 TabHost.TabSpec spec = tabHost.newTabSpec(tabId);
		 spec.setIndicator(tabView);         
		 spec.setContent(new CommonDummyTabContent(getBaseContext()));
	     tabHost.addTab(spec);
	     
	    
	     
	}
	 
	 
	 
	 

	   TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {	
				if (tabId.equalsIgnoreCase(TAB_DRAW))
				{
					DrawHomeFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTabId);
				}else if (tabId.equalsIgnoreCase(TAB_FRIEND)) {
					DrawHomeFriendFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTabId);
				}else if (tabId.equalsIgnoreCase(TAB_MESSAGE)) {
					DrawHomeMessageFragment.createOrShowFragment(getSupportFragmentManager(), tabId, currentTabId);
				}else if(tabId.equalsIgnoreCase(TAB_OPUS)){
					DrawHomeOpusFragment.createOrShowFragment(getSupportFragmentManager(), TAB_OPUS, currentTabId);
				}else if (tabId.equalsIgnoreCase(TAB_MORE)) {
					DrawHomeMoreFragment.createOrShowFragment(getSupportFragmentManager(), TAB_MORE, currentTabId);
				}			
				currentTabId = tabId;
				
				BadgeView badgeView = badgeMap.get(tabId);
				if (badgeView!= null)
				{
					Log.d(TAG, "badgeview clear text");
					badgeView.hide();
				}
			}				
			
		};
		
		
		
	
    @Override  
    public void onDestroy(){  
        super.onDestroy();    
        
    }

	@Override
	protected void handleServiceConnected() {
	}

	@Override
	public void onAttachFragment(Fragment fragment)
	{
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
	} 
	
	
	
	public  void Exit(final Context cont)
	{
		CommonAlertDialog.makeAlertDialog(DrawHomeActivity.this, "", getString(R.string.app_exit_alert), new CommonAlertDialogCompleteInterface()
		{
				
				@Override
				public void complete(String[] data)
				{
					ActivityMange.getInstance().AppExit(DrawHomeActivity.this);
				}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Exit(this);
		}
		return true;
	}
	
	
	private OnKeyListener onKeyListener =  new OnKeyListener()
	{
		
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				Exit(DrawHomeActivity.this);
			} 
			return false;
		}
	};
	
	

}