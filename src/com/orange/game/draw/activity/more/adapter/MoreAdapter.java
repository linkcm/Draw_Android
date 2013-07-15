/**  
        * @title MoreAdapter.java  
        * @package com.orange.game.draw.activity.more.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-29 下午2:56:23  
        * @version V1.0  
 */
package com.orange.game.draw.activity.more.adapter;

import com.orange.game.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class MoreAdapter extends BaseAdapter
{
	private String[] data;
	private Context context;
	
	
	
	/**  
	* Constructor Method   
	* @param data
	* @param context  
	*/
	public MoreAdapter(String[] data, Context context)
	{
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		if (data == null)
		return 0;
		return data.length;
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
		ViewHolder viewHolder;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.draw_home_more_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TextView moreItemTextView = viewHolder.getMoreItemTextView();
		moreItemTextView.setText(data[position]);
		return convertView;
	}

	
	class ViewHolder{
		private View convertView;
		private TextView moreItemTextView;
		/**  
		* Constructor Method   
		* @param convertView  
		*/
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}
		
		public TextView getMoreItemTextView()
		{
			if (convertView != null)
			{
				moreItemTextView = (TextView) convertView.findViewById(R.id.more_item_text);
			}
			return moreItemTextView;
		}
		
		
	}
	
}
