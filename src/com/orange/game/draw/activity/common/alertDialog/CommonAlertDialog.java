/**  
        * @title CommonAlertDialog.java  
        * @package com.orange.game.draw.activity.common  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-4 上午10:50:23  
        * @version V1.0  
 */
package com.orange.game.draw.activity.common.alertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.KeyboardUtil;
import com.orange.game.R;
import com.orange.game.draw.activity.drawDetail.ReplyCommentActivity;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameCurrency;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItem;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameItemConsumeType;



public class CommonAlertDialog
{
	public static AlertDialog makeAlertDialog(Context context,String title,String content,final CommonAlertDialogCompleteInterface dialogComplete)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setCancelable(true);
		Window window = alertDialog.getWindow();
		View view = LayoutInflater.from(context).inflate(R.layout.common_alert_dialog, null);
		TextView alertTitle = (TextView) view.findViewById(R.id.alert_dialog_title);
		TextView alertContent = (TextView) view.findViewById(R.id.alert_dialog_content);
		Button cancelButton = (Button) view.findViewById(R.id.alert_dialog_button_cancel);
		Button sureButton = (Button) view.findViewById(R.id.alert_dialog_button_sure);
		ImageView closeButton = (ImageView) view.findViewById(R.id.alert_dialog_close_btn);
		if (title != null&&!title.equalsIgnoreCase(""))
		{
			alertTitle.setText(title);
		}
		if (content !=null&&!content.equalsIgnoreCase(""))
		{
			alertContent.setText(content);
		}
		cancelButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (alertDialog != null)
				{
					alertDialog.dismiss();
				}
				
			}
		});
		sureButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				dialogComplete.complete(null);
				alertDialog.dismiss();
			}
		});
		closeButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				alertDialog.dismiss();
				
			}
		});
		window.setContentView(view);
		return alertDialog;
	}
	
	
	public static void makeInputAlertDialog(final Context context,int titleId,final String[] editTextName,final String[] editTextHint,final CommonAlertDialogCompleteInterface dialogCompleteInterface){
		
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setView(LayoutInflater.from(context).inflate(R.layout.common_edit_text_alert_dialog, null)); 
		alertDialog.setCancelable(true);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		View view = LayoutInflater.from(context).inflate(R.layout.common_edit_text_alert_dialog, null);
		TextView titleTextView = (TextView) view.findViewById(R.id.alert_dialog_title);
		titleTextView.setText(titleId);
		ViewGroup editGroup1 = (ViewGroup) view.findViewById(R.id.alert_edit_group_1);
		ViewGroup editGroup2 = (ViewGroup) view.findViewById(R.id.alert_edit_group_2);
		TextView editTextName1 = (TextView) view.findViewById(R.id.alert_edit_text_name_1);
		TextView editTextName2 = (TextView) view.findViewById(R.id.alert_edit_text_name_2);
		final EditText editText1 = (EditText) view.findViewById(R.id.alert_edit_text_1);
		final EditText editText2 = (EditText) view.findViewById(R.id.alert_edit_text_2);
		Button cancelButton = (Button) view.findViewById(R.id.alert_dialog_button_cancel);
		Button sureButton = (Button) view.findViewById(R.id.alert_dialog_button_sure);
		
		KeyboardUtil.showSoftKeyboard(editText1, context);
		
		if (editTextHint != null)
		{
			switch (editTextHint.length)
			{
			case 1:
				editGroup1.setVisibility(View.VISIBLE);
				editText1.setHint(editTextHint[0]);
				break;
			case 2:
				editGroup1.setVisibility(View.VISIBLE);
				editGroup2.setVisibility(View.VISIBLE);
				editText1.setHint(editTextHint[0]);
				editText2.setHint(editTextHint[1]);
				break;
			default:
				break;
			}
		}
		
		if (editTextName != null)
		{
			switch (editTextName.length)
			{
			case 1:
				editTextName1.setText(editTextName[0]);
				break;
			case 2:
				editTextName1.setText(editTextName[0]);
				editTextName2.setText(editTextName[1]);
				break;
			default:
				break;
			}
		}
		
		cancelButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				
				alertDialog.dismiss();
				
			}
		});
		
		sureButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				KeyboardUtil.hideKeyboard(editText1, context);
				String[] data = new String[editTextHint.length];
				switch (data.length)
				{
				case 1:
					data[0] = editText1.getText().toString();
					break;
				case 2:
					data[0] = editText1.getText().toString();
					data[1] = editText2.getText().toString();
					break;
				default:
					break;
				}
				dialogCompleteInterface.complete(data);
				alertDialog.dismiss();
			}
		});
		window.setContentView(view);
		alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{
			
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				KeyboardUtil.hideSoftKeyboard(editText1, context);
			}
		});
	}
	
	
	static int ItemCount = 0;
	public static void makeGameItemAlertDialog(Context context,final PBGameItem gameItem)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCancelable(true);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		View view = LayoutInflater.from(context).inflate(R.layout.common_shop_item_alert_dialog, null);
		ImageView shopItemImage = (ImageView) view.findViewById(R.id.shop_item_image);
		TextView shopItemPromo = (TextView) view.findViewById(R.id.shop_item_promo);
		TextView shopItemPrice = (TextView) view.findViewById(R.id.shop_item_price);
		TextView shopItemDesc = (TextView) view.findViewById(R.id.shop_item_desc);
		ImageView closeButton = (ImageView) view.findViewById(R.id.alert_dialog_close_btn);
		Button presenButton = (Button) view.findViewById(R.id.alert_dialog_button_presen);
		Button buyButton = (Button) view.findViewById(R.id.alert_dialog_button_buy);
		
		ViewGroup shopItemManagerGroup = (ViewGroup) view.findViewById(R.id.shop_item_manger_group);
		ImageButton shopItemDelButton = (ImageButton) view.findViewById(R.id.shop_item_delete);
		ImageButton shopItemAddButton  = (ImageButton) view.findViewById(R.id.shop_item_add);
		final TextView shopItemCount = (TextView) view.findViewById(R.id.shop_item_count);
		final TextView shopItemTotalPrice = (TextView) view.findViewById(R.id.shop_item_total_price);
		ImageView shopItemIcon = (ImageView) view.findViewById(R.id.shop_item_icon);
		
		
		if (gameItem.getConsumeType() == PBGameItemConsumeType.AmountConsumable)
		{
			shopItemManagerGroup.setVisibility(View.VISIBLE);
			if (gameItem.getPriceInfo().getCurrency() == PBGameCurrency.Coin)
			{
				shopItemIcon.setImageResource(R.drawable.coin);
			}else {
				shopItemIcon.setImageResource(R.drawable.ingot);
			}
			
			
			shopItemCount.setText(""+ItemCount);
			shopItemTotalPrice.setText("X"+gameItem.getPriceInfo().getPrice()*ItemCount);
		}
		
		
		shopItemDelButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (ItemCount>0)
				{
					ItemCount--;
					shopItemCount.setText(""+ItemCount);
					shopItemTotalPrice.setText("X"+gameItem.getPriceInfo().getPrice()*ItemCount);
				}
				
			}
		});
		
		
		shopItemAddButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				ItemCount++;
				shopItemCount.setText(""+ItemCount);
				shopItemTotalPrice.setText("X"+gameItem.getPriceInfo().getPrice()*ItemCount);			
			}
		});
		
		closeButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				alertDialog.dismiss();
			}
		});
		
		
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(gameItem.getImage(), shopItemImage);
		if (gameItem.hasPromotionInfo())
		{
			long currentTime = System.currentTimeMillis()/1000;
			if (gameItem.getPromotionInfo().getStartDate()<currentTime
				&&currentTime<gameItem.getPromotionInfo().getExpireDate())
			{
				String promo = (1-gameItem.getPromotionInfo().getPrice()/gameItem.getPriceInfo().getPrice())*100+"%";
				shopItemPromo.setText(context.getString(R.string.promo)+promo);
			}else {
				shopItemPromo.setText(context.getString(R.string.no_promo));
			}
			
		}else{
			shopItemPromo.setText(context.getString(R.string.no_promo));
		}	
		shopItemPrice.setText(""+gameItem.getPriceInfo().getPrice());
		shopItemDesc.setText(gameItem.getDesc());
		window.setContentView(view);
	}
	
	
	
}
