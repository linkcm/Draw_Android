/**  
        * @title ShopFragment.java  
        * @package com.orange.game.draw.activity.shop.fragment  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-21 上午10:58:16  
        * @version V1.0  
 */
package com.orange.game.draw.activity.shop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.orange.game.R;
import com.orange.game.draw.activity.common.alertDialog.CommonAlertDialog;
import com.orange.game.draw.activity.shop.ShopActivity;
import com.orange.game.draw.activity.shop.adapter.ShopListAdapter;
import com.orange.game.draw.activity.top.fragment.TopNewestFragment;
import com.orange.game.draw.model.shop.ShopManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBDrawItemType;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItem;



public class ShopFragment extends Fragment
{
	
	
	private static final String TAG = "ShopFragment";
	private static final String BUNDLE_KEY_TAB_ID = "tabId";
	private ShopListAdapter adapter;


	public static ShopFragment newInstance(FragmentManager fm, FragmentTransaction ft, String tabId){

		ShopFragment shopFragment = (ShopFragment) fm.findFragmentByTag(tabId);		
		if(shopFragment==null){		
			Log.d(TAG, "add  draw fragment "+tabId);
			Bundle bundle = ShopFragment.createBundle(tabId);
			shopFragment = new ShopFragment();
			shopFragment.setArguments(bundle);
			ft.add(R.id.real_tab_content, shopFragment, tabId);			
		}else{	
			ft.show(shopFragment);
		}
		return shopFragment;
	}
	
	
	
	
	public static Bundle createBundle(String tabId){
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_KEY_TAB_ID, tabId);
		return bundle;
	}
	
	
	
	public static void createOrShowFragment(FragmentManager fm, String tabId,String currentTabId ){
		Fragment currentFragment = fm.findFragmentByTag(currentTabId);
		FragmentTransaction ft = fm.beginTransaction();
		if(currentFragment!=null)
			ft.hide(currentFragment);
		ShopFragment.newInstance(fm, ft, tabId);
		ft.commit();
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
		View view = inflater.inflate(R.layout.shop_fragment, container, false);
		ListView shopList = (ListView) view.findViewById(R.id.shop_list_view);
		adapter = new ShopListAdapter(getActivity(), null);
		shopList.setAdapter(adapter);
		String tabId = getArguments().getString(BUNDLE_KEY_TAB_ID);
		getPBGameItemList(tabId, adapter);
		shopList.setOnItemClickListener(itemClickListener);
		return view;
	}
	
	
	private void getPBGameItemList(String tabId,ShopListAdapter adapter)
	{
		if(tabId.equalsIgnoreCase(ShopActivity.PROPS)){
			adapter.setGameItemList(ShopManager.getInstance().getGameItemsByType(PBDrawItemType.DrawNomal_VALUE));
			adapter.setPromotion(false);
		}else if (tabId.equalsIgnoreCase(ShopActivity.TOOLS)) {
			adapter.setGameItemList(ShopManager.getInstance().getGameItemsByType(PBDrawItemType.DrawTool_VALUE));
			adapter.setPromotion(false);
		}else {
			adapter.setGameItemList(ShopManager.getInstance().getPromoGameItem());
			adapter.setPromotion(true);
		}
		adapter.notifyDataSetChanged();
	}
	
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			PBGameItem gameItem = adapter.getGameItemList().get(arg2);
			CommonAlertDialog.makeGameItemAlertDialog(getActivity(), gameItem);
			
		}
	};
	
}
