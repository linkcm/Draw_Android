/**  
        * @title ChargeAdapter.java  
        * @package com.orange.game.draw.activity.charge.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-23 下午12:20:19  
        * @version V1.0  
 */
package com.orange.game.draw.activity.charge.adapter;

import com.orange.game.R;
import com.orange.network.game.protocol.model.GameBasicProtos.PBIAPProduct;
import com.orange.network.game.protocol.model.GameBasicProtos.PBIAPProductList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-23 下午12:20:19  
 */

public class ChargeAdapter extends BaseAdapter
{

	private Context context;
	private PBIAPProductList iapProductList;
	
	
	
	/**  
	* Constructor Method   
	* @param context
	* @param ingotList  
	*/
	public ChargeAdapter(Context context, PBIAPProductList iapProductList)
	{
		super();
		this.context = context;
		this.iapProductList = iapProductList;
	}

	@Override
	public int getCount()
	{
		if(iapProductList == null)
		return 0;
		return iapProductList.getProductsCount();
	}

	@Override
	public Object getItem(int arg0)
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
		ViewHolder viewHolder ;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.charge_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PBIAPProduct iapProduct = iapProductList.getProducts(position);
		TextView ingotNumber = viewHolder.getIngotNumber();
		TextView ingotPrice = viewHolder.getIngotPrice();
		TextView ingotPromo = viewHolder.getIngotPromo();
		ingotNumber.setText("X"+iapProduct.getCount());
		ingotPrice.setText(iapProduct.getCurrency()+iapProduct.getTotalPrice());
		if (iapProduct.hasSaving())
		{
			ingotPromo.setVisibility(View.VISIBLE);
			ingotPromo.setText(context.getString(R.string.promo)+iapProduct.getSaving());
		}else {
			ingotPromo.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}

	
	class ViewHolder{
		private View convertView;
		private TextView ingotNumber;
		private TextView ingotPrice;
		private TextView ingotPromo;
		/**  
		* Constructor Method   
		* @param convertView  
		*/
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}
		public TextView getIngotNumber()
		{
			if(convertView != null)
				ingotNumber = (TextView) convertView.findViewById(R.id.ingot_number);
			return ingotNumber;
		}
		public TextView getIngotPrice()
		{
			if (convertView != null)
				ingotPrice = (TextView) convertView.findViewById(R.id.ingot_price);
			return ingotPrice;
		}
		public TextView getIngotPromo()
		{
			if (convertView != null)
				ingotPromo = (TextView) convertView.findViewById(R.id.ingot_promot);
			return ingotPromo;
		}
		
		
	}
}
