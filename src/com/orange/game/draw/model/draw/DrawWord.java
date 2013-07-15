package com.orange.game.draw.model.draw;

public class DrawWord {

	final String text;
	final int language;
	final int level;
	
	public DrawWord(String text, int lang, int level){
		this.text = text;
		this.language = lang;
		this.level = level;
	}

	@Override
	public String toString() {
		return "DrawWord [language=" + language + ", level=" + level
				+ ", text=" + text + "]";
	}

	public String getText() {
		return text;
	}

	public int getLanguage() {
		return language;
	}

	public int getLevel() {
		return level;
	}
	
	
}
