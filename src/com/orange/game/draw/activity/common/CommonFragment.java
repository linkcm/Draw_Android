/**  
        * @title CommonFragment.java  
        * @package com.orange.game.draw.activity.common  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-11 下午3:15:35  
        * @version V1.0  
 */
package com.orange.game.draw.activity.common;

import android.support.v4.app.Fragment;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-11 下午3:15:35  
 */

public abstract class CommonFragment extends Fragment
{
	abstract public void loadData();
	abstract public void loadMoreData();
	abstract public void clear();
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		//clear();
	}
	@Override
	public void onDestroyView()
	{
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	
	
}
