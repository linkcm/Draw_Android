/**  
        * @title OpusFragment.java  
        * @package com.orange.game.draw.activity.opus  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-29 上午10:11:14  
        * @version V1.0  
 */
package com.orange.game.draw.activity.opus;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.game.draw.activity.friend.fragment.DrawHomeFriendFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class DrawHomeOpusFragment extends Fragment
{

	private static final String TAG = "OpusFragment";

	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		DrawHomeOpusFragment.newInstance(fm, ft, tabId);
		ft.commit();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	
	
	public static DrawHomeOpusFragment newInstance(FragmentManager fm, FragmentTransaction ft,
			String tabId)
	{
		DrawHomeOpusFragment drawHomeOpusFragment = (DrawHomeOpusFragment) fm.findFragmentByTag(tabId);		
		if(drawHomeOpusFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			drawHomeOpusFragment = new DrawHomeOpusFragment();
			ft.add(R.id.real_tab_content, drawHomeOpusFragment, tabId);			
		}else{	
			ft.show(drawHomeOpusFragment);
		}
		return drawHomeOpusFragment;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (container == null)
		return null;
		
		View view = inflater.inflate(R.layout.draw_home_opus_fragment,container , false);
		return view;
	}

}
