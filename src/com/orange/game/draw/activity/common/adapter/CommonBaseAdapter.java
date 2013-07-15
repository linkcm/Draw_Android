/**  
        * @title CommonBaseAdapter.java  
        * @package com.orange.game.draw.activity.common.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-19 下午2:25:33  
        * @version V1.0  
 */
package com.orange.game.draw.activity.common.adapter;

import com.orange.game.constants.ErrorCode;
import com.orange.game.draw.activity.user.UserDetailActivity;
import com.orange.game.draw.mission.common.MissionCompleteInterface;
import com.orange.game.draw.mission.user.UserMission;
import com.orange.game.draw.model.user.UserManager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-19 下午2:25:33  
 */

public class CommonBaseAdapter extends BaseAdapter
{

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setUserAvatarOnClick(final Context context,View userAvatar,final String targetUserId)
	{
		if (targetUserId == null||targetUserId.equalsIgnoreCase("")||userAvatar == null||context ==null)
			return;
		
		
		userAvatar.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				UserMission.getInstance().getUserByUserId(targetUserId, new MissionCompleteInterface()
				{
					
					@Override
					public void onComplete(int errorCode)
					{
						if (errorCode == ErrorCode.ERROR_SUCCESS)
						{
							UserDetailActivity.newInstance(context, 
									UserManager.getInstance().getTempGameUser(), 
									UserManager.getInstance().getTempUserRelationShip());
						}
					}
				});
				
			}
		});
	}
	
}
