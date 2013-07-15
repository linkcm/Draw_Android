/**  
        * @title Charge.java  
        * @package com.orange.game.draw.model  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-23 下午12:10:38  
        * @version V1.0  
 */
package com.orange.game.draw.model.shop;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.orange.game.draw.model.user.UserManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItem;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItemList;
import com.orange.network.game.protocol.model.GameBasicProtos.PBIAPProductList;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-23 下午12:10:38  
 */

public class ChargeManager
{
	private static final String TAG = null;
		// thread-safe singleton implementation
		private static ChargeManager charge = new ChargeManager();
		private ChargeManager() {
			
		}	
		public static ChargeManager getInstance() {
			return charge;
		}
		
		//final List<PBSaleIngot>  list = new ArrayList<PBSaleIngot>();
		
		private PBIAPProductList iapProductList = null;
		
		public  void paresPBSaleIngotList(String filePath){
			
			File file = new File(filePath);
			try
			{
				FileInputStream fileInputStream = new FileInputStream(file);
				iapProductList = PBIAPProductList.parseFrom(fileInputStream);
				Log.d(TAG, "saleIngot list size = "+iapProductList.getProductsCount());
				fileInputStream.close();			
			} catch (Exception e)
			{
				e.printStackTrace();
				Log.e(TAG, "<PBGameItemList> but catch exception : "+e.toString());
			}
		}
		public PBIAPProductList getIapProductList()
		{
			return iapProductList;
		}
}
