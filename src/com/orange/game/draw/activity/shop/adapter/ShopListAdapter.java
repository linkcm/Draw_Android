/**  
        * @title ShopListAdapter.java  
        * @package com.orange.game.draw.activity.shop.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-21 上午11:25:10  
        * @version V1.0  
 */
package com.orange.game.draw.activity.shop.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameCurrency;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItem;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItemList;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-21 上午11:25:10  
 */

public class ShopListAdapter extends BaseAdapter
{

	private Context context;
	private List<PBGameItem> gameItemList;
	private ImageLoader imageLoader;
	private boolean isPromotion = false;
	
	public ShopListAdapter(Context context, List<PBGameItem> gameItemList)
	{
		super();
		this.context = context;
		this.gameItemList = gameItemList;
		this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount()
	{
		if (gameItemList == null)
			return 0;
		return gameItemList.size();
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
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageView shopItemImage = viewHolder.getShopItemImage();
		ImageView shopItemProImage = viewHolder.getShopItemProImageView();
		TextView shopItemName = viewHolder.getShopItemName();
		TextView shopItemDesc = viewHolder.getShopItemDesc();
		TextView shopItemPrice = viewHolder.getShopItemPrice();
		ImageView shopItemPriceIcon = viewHolder.getShopItemPriceIcon();
		
		PBGameItem gameItem = gameItemList.get(position);
		imageLoader.displayImage(gameItem.getImage(), shopItemImage);
		shopItemName.setText(gameItem.getName());
		shopItemDesc.setText(gameItem.getDesc());
		
		if (gameItem.hasPriceInfo())
		{
			shopItemPrice.setText(""+gameItem.getPriceInfo().getPrice());
			if (gameItem.getPriceInfo().hasCurrency())
			{
				if (gameItem.getPriceInfo().getCurrency() == PBGameCurrency.Coin)
				{
					shopItemPriceIcon.setImageResource(R.drawable.coin);
				}else {
					shopItemPriceIcon.setImageResource(R.drawable.ingot);
				}
			}
		}
		if (isPromotion)
		{
			shopItemProImage.setVisibility(View.VISIBLE);
		}else {
			shopItemProImage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	class ViewHolder{
		private View convertView;
		private ImageView shopItemImage;
		private ImageView shopItemProImageView;
		private ImageView shopItemPriceIcon;
		private TextView shopItemName;
		private TextView shopItemDesc;
		private TextView shopItemPrice;
		
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}

		public ImageView getShopItemImage()
		{
			if (convertView != null)
			{
				shopItemImage = (ImageView) convertView.findViewById(R.id.shop_item_image);
			}
			return shopItemImage;
		}

		public TextView getShopItemName()
		{
			if (convertView != null)
			{
				shopItemName = (TextView) convertView.findViewById(R.id.shop_item_name);
			}
			return shopItemName;
		}

		public TextView getShopItemDesc()
		{
			if (convertView != null)
			{
				shopItemDesc = (TextView) convertView.findViewById(R.id.shop_item_desc);
				
			}
			return shopItemDesc;
		}

		public TextView getShopItemPrice()
		{
			if (convertView != null)
			{
				shopItemPrice = (TextView) convertView.findViewById(R.id.shop_item_price);
			}
			return shopItemPrice;
		}

		public ImageView getShopItemPriceIcon()
		{
			if (convertView != null)
			{
				shopItemPriceIcon = (ImageView) convertView.findViewById(R.id.shop_item_price_icon);
			}
			return shopItemPriceIcon;
		}

		public ImageView getShopItemProImageView()
		{
			if (convertView != null)
			{
				shopItemProImageView = (ImageView) convertView.findViewById(R.id.shop_item_pro_bg);
			}
			return shopItemProImageView;
		}

		
		
		
		
		
	}

	public void setGameItemList(List<PBGameItem> gameItemList)
	{
		this.gameItemList = gameItemList;
	}

	public List<PBGameItem> getGameItemList()
	{
		return gameItemList;
	}

	public void setPromotion(boolean isPromotion)
	{
		this.isPromotion = isPromotion;
	}

}
