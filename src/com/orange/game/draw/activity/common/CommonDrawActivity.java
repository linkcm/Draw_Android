package com.orange.game.draw.activity.common;

import com.google.protobuf.InvalidProtocolBufferException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.activity.PPActivity;
import com.orange.game.draw.model.draw.DrawWord;
import com.orange.game.draw.network.DrawNetworkRequest;
import com.orange.game.draw.service.DrawGameNetworkService;
import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;
import com.orange.network.game.protocol.message.GameMessageProtos.GeneralNotification;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class CommonDrawActivity extends PPActivity {

    protected static final String TAG = "CommonDrawActivity";

    GameNetworkReceiver receiver;
    
    public void registerGameNetworkReceiver(){
    	receiver = new GameNetworkReceiver();  
        IntentFilter filter = new IntentFilter();  
        filter.addAction(DrawGameNetworkService.ACTION_SERVER_CONNECTED);  
        filter.addAction(DrawGameNetworkService.ACTION_SERVER_DISCONNECTED);  
        filter.addAction(DrawGameNetworkService.ACTION_RECV_GAME_MESSAGE);  
        this.registerReceiver(receiver, filter);      	      
    }
        
	public class GameNetworkReceiver extends BroadcastReceiver {      // receive Broadcast  
        
		@Override  
        public void onReceive(Context context, Intent intent) {  
              
            if(intent != null){  

            	Log.d(TAG, "<onReceive> action="+intent.getAction());
            	
            	if (intent.getAction().equals(DrawGameNetworkService.ACTION_SERVER_CONNECTED)){
            		gameNetworkService.joinGame();
            		return;
            	}
            	
                Bundle bundle = intent.getExtras();
                if (bundle == null)
                	return;
                
                byte[] messageByte = bundle.getByteArray(DrawGameNetworkService.GAME_MESSAGE);
                if (messageByte == null)
                	return;
                
                try {
					GameMessage message = GameMessage.parseFrom(messageByte);
					Log.d(TAG, "<onReceive> message="+message.getCommand().toString());
					handleGameMessage(message);
				} catch (InvalidProtocolBufferException e) {
					Log.e(TAG, "<onReceive> parse message exception", e);
				}
                
            }  
        }  
    }  	
	
	public DrawGameNetworkService gameNetworkService;
    
    private void bindNetworkService() {  
        Intent intent = new Intent(this, DrawGameNetworkService.class);
        bindService(intent, networkServiceConnection, Context.BIND_AUTO_CREATE);          // bindService  
    }  
    
    private ServiceConnection networkServiceConnection = new ServiceConnection() {  
    	  
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {       //connect Service  
        	Log.d(TAG, "<onServiceConnected>");
        	gameNetworkService = ((DrawGameNetworkService.MyBinder)(service)).getService();  
        	handleServiceConnected();
        }  
          
        @Override  
        public void onServiceDisconnected(ComponentName name) {                 //disconnect Service  
        	Log.d(TAG, "<onServiceDisconnected>");
        	gameNetworkService = null;  
        }  
    };  
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);    
    	setProgressDialogOnKey(new DialogInterface.OnKeyListener()
		{
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				DrawNetworkRequest.cancelRequest();
				finish();
				return false;
			}
		});
        bindNetworkService();
    }	
	
    @Override  
    protected void onStart(){  
        super.onStart();
        registerGameNetworkReceiver();
    }
    
    @Override  
    protected void onStop(){  
        unregisterReceiver(receiver);            
        super.onStop();


    } 
    
    @Override  
    public void onDestroy(){  
        super.onDestroy();            
        if(networkServiceConnection != null){  
            unbindService(networkServiceConnection);  
        }  
    } 	    
    
    protected abstract boolean isRegisterServerConnect();
    protected abstract boolean isRegisterGameMessage();

    abstract protected void handleServiceConnected();
    
    protected void handleGameMessage(GameMessage message){
    	switch (message.getCommand()){
    	case JOIN_GAME_RESPONSE:
    		handleJoinGameResponse(message);
    		break;
    	
    	case USER_JOIN_NOTIFICATION_REQUEST:
    		handleNewUserJoin(message);
    		break;
    		
		case USER_QUIT_NOTIFICATION_REQUEST:
			handleUserQuit(message);
			break;
			
		case START_GAME_RESPONSE:
			handleStartGameResponse(message);
			break;
			
		case GAME_START_NOTIFICATION_REQUEST:
			hanldeGameStart(message);
			break;
			
		case NEW_DRAW_DATA_NOTIFICATION_REQUEST:
			handleDrawData(message);
			break;
			
		case CLEAN_DRAW_NOTIFICATION_REQUEST:
			handleCleanDraw(message);
			break;
			
		case GAME_TURN_COMPLETE_NOTIFICATION_REQUEST:
			handleGameTurnComplete(message);
			break;
			
		case CHAT_NOTIFICATION_REQUEST:
			handleChatNotification(message);
			break;    	}    	
    }
    
    protected void handleServerConnected(GameMessage message){
    	Log.d(TAG, "<handleServerConnected> default implementation");    	    	    	    	    
    }
    
    protected void handleServerDisconnected(GameMessage message){
    	Log.d(TAG, "<handleServerDisconnected> default implementation");    	    	    	
    }
    
    protected void handleGameStart(GameMessage message){
    	Log.d(TAG, "<handleGameStart> default implementation");    	    	
    }
    
    protected void handleJoinGameResponse(GameMessage message){
    	Log.d(TAG, "<handleJoinGameResponse> default implementation");    	
    }
    
    protected void handleNewUserJoin(GameMessage message) {
    	Log.d(TAG, "<handleNewUserJoin> default implementation");
	}

    protected void handleStartGameResponse(GameMessage message) {
    	Log.d(TAG, "<handleStartGameResponse> default implementation");
	}
    
    
    
	protected void handleChatNotification(GameMessage message) {
    	Log.d(TAG, "<handleChatNotification> default implementation");    	    	
	}

	protected void handleGameTurnComplete(GameMessage message) {
    	Log.d(TAG, "<handleGameTurnComplete> default implementation");    	    	
	}

	protected void handleCleanDraw(GameMessage message) {
    	Log.d(TAG, "<handleCleanDraw> default implementation");    	    	
	}

	private void handleDrawData(GameMessage message) {
		// TODO Auto-generated method stub
		if (DrawGameNetworkService.hasWord(message)){
			handleGameTurnGuessStart(message);
			
			GeneralNotification notification = message.getNotification();
			DrawWord word = new DrawWord(notification.getWord(), 
					notification.getLanguage(), notification.getLevel());
			handleGameTurnReceiveWord(word);		
			
		}
		else if (DrawGameNetworkService.hasGuessWord(message)){
			// TODO refer to iOS
//			handleGuessWord(message);
		}
	}

	protected void handleGameTurnReceiveWord(DrawWord word) {
    	Log.d(TAG, "<handleGameTurnReceiveWord> default implementation");    	    	
	}

	protected void handleGameTurnGuessStart(GameMessage message) {
    	Log.d(TAG, "<handleGameTurnGuessStart> default implementation");    	    	
	}

	protected void hanldeGameStart(GameMessage message) {
    	Log.d(TAG, "<hanldeGameStart> default implementation");    	    	
	}

	protected void handleUserQuit(GameMessage message) {
    	Log.d(TAG, "<handleUserQuit> default implementation");    	    	
	}

	public OnClickListener backOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			finish();
			ImageLoader.getInstance().clearMemoryCache();
		}
	};
	
	
	public String  getLastUpdateTime()
	{
		String lastUpdateTime = DateUtils.formatDateTime(CommonDrawActivity.this, System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		return lastUpdateTime;
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(CommonDrawActivity.this);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(CommonDrawActivity.this);
	}

	

	
	
    
}
