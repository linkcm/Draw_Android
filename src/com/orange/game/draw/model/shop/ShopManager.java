/**  
        * @title ShopManager.java  
        * @package com.orange.game.draw.model.shop  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-22 上午10:51:43  
        * @version V1.0  
 */
package com.orange.game.draw.model.shop;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.orange.game.draw.model.localWord.LocalWordManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBDrawItemType;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItem;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItemConsumeType;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItemList;
import com.orange.network.game.protocol.model.GameBasicProtos.PBPromotionInfo;



public class ShopManager
{
	private static final String TAG = "ShopManager";
	// thread-safe singleton implementation
	private static ShopManager sharedInstance = new ShopManager();
	
	
	private ShopManager() {
	}
	
	public static ShopManager getInstance() {
		return sharedInstance;
	}
	
	
	
	final List<PBGameItem> gameItems = new ArrayList<PBGameItem>();

	public  PBGameItemList paresPBGameItemList(String filePath){
		PBGameItemList gameItemList = null;
		File file = new File(filePath);
		try
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			gameItemList = PBGameItemList.parseFrom(fileInputStream);
			addGameItems(gameItemList.getItemsList());
			fileInputStream.close();			
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, "<paresPBGameItemList> but catch exception : "+e.toString());
		}
		return gameItemList;
	}
	
	
	public List<PBGameItem> getPromoGameItem(){
		List<PBGameItem> list = new ArrayList<PBGameItem>();
		for(PBGameItem gameItem:gameItems){
			if (gameItem.hasPromotionInfo())
			{
				PBPromotionInfo promotionInfo = gameItem.getPromotionInfo();
				long currentTime = System.currentTimeMillis()/1000;
				if (promotionInfo.getStartDate()<currentTime&&currentTime<promotionInfo.getExpireDate())
				{
					list.add(gameItem);
				}
				
			}
		}
		return list;
	}
	
	
	
	public List<PBGameItem> getGameItems()
	{
		return gameItems;
	}
	
	public void addGameItems(List<PBGameItem> list)
	{
		gameItems.clear();
		if (list !=null)
			gameItems.addAll(list);
	}
	
	
	public List<PBGameItem> getGameItemsByType(int PBDrawItemType){
		List<PBGameItem> list = new ArrayList<PBGameItem>();
		for (PBGameItem gameItem:gameItems)
		{
			if (gameItem.getType() == PBDrawItemType)
				list.add(gameItem);
		}
		return list;
	}
	
}
