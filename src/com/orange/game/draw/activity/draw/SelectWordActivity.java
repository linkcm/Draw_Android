package com.orange.game.draw.activity.draw;

import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.model.draw.DrawWord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectWordActivity extends CommonDrawActivity {

	Button word1Button;
	Button word2Button;
	Button word3Button;
 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_word);
                
        initButtons();
        
        showProgressDialog(R.string.loading,R.string.loading);
    }

    private OnClickListener selectWordListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String wordText = "";
			if ( v == word1Button){		
				wordText = word1Button.getText().toString();
			}
			else if (v == word2Button){
				wordText = word2Button.getText().toString();				
			}
			else{
				wordText = word3Button.getText().toString();				
			}
			
			// finishi this intent, and start a new intent
			DrawWord word = new DrawWord(wordText, 1, 1);		// TODO this is just test code			
			gotoPlay(word);
		}

	};
    
	private void initButtons() {
		
		word1Button = (Button)findViewById(R.id.word1_button);
		word2Button = (Button)findViewById(R.id.word2_button);
		word3Button = (Button)findViewById(R.id.word3_button);
					
		word1Button.setOnClickListener(selectWordListener);
		word2Button.setOnClickListener(selectWordListener);
		word3Button.setOnClickListener(selectWordListener);		
	}

	@Override
	protected boolean isRegisterGameMessage() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean isRegisterServerConnect() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void handleServiceConnected() {
		hideDialog();
	}
	
	private void gotoPlay(DrawWord word) {			
		
		gameNetworkService.startDraw(word);
		
		Intent intent = new Intent(this, DrawActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);	
	}
}
