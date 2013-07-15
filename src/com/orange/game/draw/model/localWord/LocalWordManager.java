/**  
        * @title LocalWordManager.java  
        * @package com.orange.game.draw.model.localWord  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-6 上午10:14:00  
        * @version V1.0  
 */
package com.orange.game.draw.model.localWord;

import com.orange.game.draw.model.db.DbManager;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-2-6 上午10:14:00  
 */

public class LocalWordManager
{
	// thread-safe singleton implementation
			private static LocalWordManager sharedInstance = new LocalWordManager();
			
			private static final String TAG = "DbManager";
			
			private LocalWordManager() {
			}
			
			public static LocalWordManager getInstance() {
				return sharedInstance;
			}
}
