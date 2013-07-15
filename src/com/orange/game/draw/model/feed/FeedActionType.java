package com.orange.game.draw.model.feed;

public enum FeedActionType {
    
    NoAction(0),
    Guess(2),
    Comment(3),
    Flower(6),
    Tomato(7);
    
    final int value;

    FeedActionType(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
	
	public static boolean isFeedAction(int actionType){
		return (actionType == 2 ||
				actionType == 3 ||
				actionType == 6 ||
				actionType == 7);
	}
}
