/**  
        * @title ChargeActivity.java  
        * @package com.orange.game.draw.activity.charge  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-23 上午11:13:21  
        * @version V1.0  
 */
package com.orange.game.draw.activity.charge;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.orange.game.R;
import com.orange.game.draw.activity.charge.adapter.ChargeAdapter;
import com.orange.game.draw.activity.common.CommonDrawActivity;
import com.orange.game.draw.model.shop.ChargeManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBIAPProductList;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-23 上午11:13:21  
 */

public class ChargeActivity extends CommonDrawActivity
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
		setContentView(R.layout.charge);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		ListView ingotListView = (ListView) findViewById(R.id.charge_list);
		PBIAPProductList iapProductList = ChargeManager.getInstance().getIapProductList();
		ChargeAdapter adapter = new ChargeAdapter(ChargeActivity.this, iapProductList);
		ingotListView.setAdapter(adapter);
	}
}
