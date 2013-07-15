package com.orange.game.draw.constants;

public enum LanguageType {
	
    ENGLISH(0),
    CHINESE(1);
    
    final int value;

    LanguageType(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
}
