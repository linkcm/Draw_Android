/**  
        * @title AboutUSActivity.java  
        * @package com.orange.game.draw.activity.more  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-14 下午2:56:38  
        * @version V1.0  
 */
package com.orange.game.draw.activity.more;

import android.os.Bundle;
import android.widget.ImageButton;


import com.orange.game.R;
import com.orange.game.draw.activity.common.CommonDrawActivity;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-14 下午2:56:38  
 */

public class AboutUSActivity extends CommonDrawActivity
{

	@Override
	protected boolean isRegisterServerConnect()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void handleServiceConnected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
	}

	
	
	
	
}
