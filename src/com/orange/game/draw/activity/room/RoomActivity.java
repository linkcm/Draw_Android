package com.orange.game.draw.activity.room;

import java.util.List;

import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.activity.draw.SelectWordActivity;
import com.orange.game.draw.model.session.DrawGameSession;
import com.orange.network.game.protocol.constants.GameConstantsProtos.GameResultCode;
import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RoomActivity extends CommonDrawActivity {

	// UI elements
	Button startButton;
	
	@Override
	protected boolean isRegisterGameMessage() {
		return true;
	}

	@Override
	protected boolean isRegisterServerConnect() {
		return true;
	}
	
	@Override
    protected void handleNewUserJoin(GameMessage message) {
    	updateUsers();
	}
	
	@Override
    protected void handleStartGameResponse(GameMessage message) {
		hideDialog();
		if (message.getResultCode() != GameResultCode.SUCCESS)
			return;
		
		if (gameNetworkService.isMyTurn()){
			// goto select word
			gotoPickWord();
		}
		else{
			// goto guess view
		}
	}

	private void gotoPickWord() {
		Intent intent = new Intent(this, SelectWordActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
		startActivity(intent);
	}

	@Override  
    protected void handleServiceConnected(){
        updateUsers();	    
    }	

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_room);         
        
        initButtons();
    }
        
    @Override  
    protected void onStart(){  
        super.onStart();
    }
    
    @Override  
    protected void onStop(){  
        super.onStop(); 
    } 
    
    
	private void updateUsers() {
		Log.d(TAG, "<updateUsers>");
		DrawGameSession session = gameNetworkService.getSession();
		List<PBGameUser> userList = session.getUserList();
		
		int[] buttonIds = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6};
		for (int i=0; i<buttonIds.length && i<userList.size(); i++){
			PBGameUser user = userList.get(i);
			Button button = (Button)findViewById(buttonIds[i]);
			button.setText(user.getNickName());
		}
	}

	private void initButtons() {
		startButton = (Button)findViewById(R.id.start_game_button);
		startButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading,R.string.loading);
				gameNetworkService.startGame();
			}
		});
	}

}
