package com.orange.game.draw.model.session;

import com.orange.game.draw.model.draw.DrawWord;

public class DrawGameTurn {

	String		currentPlayUserId;
	DrawWord	word;
	
	public void setWord(DrawWord word) {
		this.word = word;
	}
	
}
