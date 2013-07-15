/**  
        * @title FeedbackType.java  
        * @package com.orange.game.draw.model.support  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-22 上午10:29:47  
        * @version V1.0  
 */
package com.orange.game.draw.model.support;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-22 上午10:29:47  
 */

public enum FeedbackType
{
	ADVICE(1),
	BUGS(0);

    final int value;

    FeedbackType(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
}
