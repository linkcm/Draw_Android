package com.orange.game.draw.model.feed;

public enum FeedListType {
	    
	    FeedListTypeUnknow(0),
	    FeedListTypeMy(1),
	    FeedListTypeAll(2),
	    FeedListTypeHot(3),
	    FeedListTypeUserFeed(4),
	    FeedListTypeUserOpus(5),
	    FeedListTypeLatest(6),	    
	    FeedListTypeDrawToMe(7),
	    FeedListTypeComment(8),
	    FeedListTypeHistoryRank(9),
	    FeedListTypeTopPlayer(10),
	    FeedListTypeUserFavorite(100);
	    
	    final int value;

	    FeedListType(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
		
		
}
