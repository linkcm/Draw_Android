/**  
        * @title DrawDetailSharePopupAdapter.java  
        * @package com.orange.game.draw.activity.drawDetail.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-29 下午4:43:39  
        * @version V1.0  
 */
package com.orange.game.draw.activity.drawDetail.adapter;

import java.sql.Date;
import java.util.List;

import com.orange.game.R;
import com.orange.game.draw.activity.share.ShareItemData;

import android.content.Context;
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
 * @update 2013-1-29 下午4:43:39  
 */

public class SNSSharePopupAdapter extends BaseAdapter
{
	private Context context;
	private List<ShareItemData> shareItemDatas;
	
	/**  
	* Constructor Method   
	* @param context
	* @param data  
	*/
	public SNSSharePopupAdapter(Context context, List<ShareItemData> shareItemDatas)
	{
		super();
		this.context = context;
		this.shareItemDatas = shareItemDatas;
	}

	@Override
	public int getCount()
	{
		if (shareItemDatas == null)
		{
			return 0;
		}
		return shareItemDatas.size();
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
		ViewHolder viewHolder ;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.share_popup_window_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageView shareIcon = viewHolder.getShareIcon();
		TextView shareText = viewHolder.getShareText();
		ShareItemData shareItemData = shareItemDatas.get(position);
		shareIcon.setImageResource(shareItemData.imageId);
		shareText.setText(shareItemData.textId);
		convertView.setOnClickListener(shareItemData.onClickListener);
		return convertView;
	}

	
	class ViewHolder{
		private View convertView;
		private ImageView shareIcon;
		private TextView shareText;
	
		
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}


		public ImageView getShareIcon()
		{
			if (convertView != null)
			{
				shareIcon = (ImageView) convertView.findViewById(R.id.share_icon);
			}
			return shareIcon;
		}


		public TextView getShareText()
		{
			if (convertView != null)
			{
				shareText = (TextView) convertView.findViewById(R.id.share_text);
			}
			return shareText;
		}
		
		
	}
}
