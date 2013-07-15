/**  
        * @title FeedConstants.java  
        * @package com.orange.game.draw.model.feed  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-12 上午11:33:24  
        * @version V1.0  
 */
package com.orange.game.draw.model.feed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-12 上午11:33:24  
 */

public enum FeedConstants
{
			FeedTimesTypeMatch(1),
		    FeedTimesTypeGuess (2),
		    FeedTimesTypeCorrect(3),
		    FeedTimesTypeComment(4),
		    FeedTimesTypeFlower(5),
		    FeedTimesTypeTomato(6),
		    FeedTimesTypeSave(7),
		    
			
			FeedTypeUnknow (0),
			FeedTypeDraw(1),
			FeedTypeGuess(2),
			FeedTypeComment(3), 
			FeedTypeRepost(4),
			FeedTypeDrawToUser(5),	    
			FeedTypeFlower(6),
			FeedTypeTomato(7),
			FeedTypeOnlyComment(8),
			FeedTypeDrawToContest(9);
		    
		    final int value;

			FeedConstants(int value) {
				this.value = value;
			}

			public int intValue() {
				return value;
			}
}
