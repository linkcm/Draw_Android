/**  
        * @title BbsAdapter.java  
        * @package com.orange.game.draw.activity.bbs.adapter  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-23 下午3:31:16  
        * @version V1.0  
 */
package com.orange.game.draw.activity.bbs.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.common.android.utils.DateUtil;
import com.orange.game.R;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-23 下午3:31:16  
 */

public class BBSBoardListAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private List<PBBBSBoard> boardList;
	private ImageLoader imageLoader;
	
	

	/**  
	* Constructor Method   
	* @param context
	* @param boardList  
	*/
	public BBSBoardListAdapter(Context context, List<PBBBSBoard> boardList)
	{
		super();
		this.context = context;
		this.boardList = boardList;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public Object getChild(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1)
	{
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4)
	{
		ViewHolder viewHolder ;
		if (arg3 == null)
		{
			arg3 = LayoutInflater.from(context).inflate(R.layout.bbs_list_child_view, null);
			viewHolder = new ViewHolder(arg3);
			arg3.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) arg3.getTag();
		}
		
		
		ViewGroup boardGroup = viewHolder.getBoardGroup();
		ImageView boardImage = viewHolder.getBoardImage();
		TextView boardName = viewHolder.getBoardNameTextView();
		TextView boardContent = viewHolder.getBoardContentTextView();
		TextView boardCount = viewHolder.getBoardCountTextView();
		TextView boardAuthor = viewHolder.getBoardAuthorTextView();
		TextView boardCreateTime = viewHolder.getBoardCreateTimeTextView();
		
		if (arg1== boardList.size()-2)
		{
			boardGroup.setBackgroundResource(R.drawable.bbs_board_last_bg);
		}else {
			boardGroup.setBackgroundResource(R.drawable.bbs_board_bg);
		}
		
		
		PBBBSBoard board = boardList.get(arg1+1);
		
		imageLoader.displayImage(board.getIcon(), boardImage);
		boardName.setText(board.getName());
		boardAuthor.setText(board.getLastPost().getCreateUser().getNickName());
		boardCreateTime.setText(DateUtil.dateFormatToString(board.getLastPost().getCreateDate(),context));
		boardContent.setText(board.getLastPost().getContent().getText());
		boardCount.setText(""+board.getPostCount());
		
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0)
	{
		if (boardList == null)
			return 0;
		return boardList.size()-1;
	}

	@Override
	public Object getGroup(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.bbs_list_group_view, null);
		}
		if (boardList!=null&&boardList.size()>0)
		{			
			PBBBSBoard board = boardList.get(groupPosition);
			ImageView boardImage = (ImageView) convertView.findViewById(R.id.board_image);
			TextView boardName = (TextView) convertView.findViewById(R.id.bbs_name);
			imageLoader.displayImage(board.getIcon(),boardImage);
			boardName.setText(board.getName());
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return true;
	}

	
	
	class ViewHolder{
		private View convertView;
		private ViewGroup boardGroup;
		private ImageView boardImage;
		private TextView boardNameTextView;
		private TextView boardAuthorTextView;
		private TextView boardCountTextView;
		private TextView boardCreateTimeTextView;
		private TextView boardContentTextView;
		
		
		public ViewHolder(View convertView)
		{
			super();
			this.convertView = convertView;
		}
		
		
		public ImageView getBoardImage()
		{
			if (convertView !=null)
			{
				boardImage = (ImageView) convertView.findViewById(R.id.bbs_board_image);
			}
			return boardImage;
		}
		public TextView getBoardNameTextView()
		{
			if (convertView != null)
			{
				boardNameTextView = (TextView) convertView.findViewById(R.id.bbs_board_name);
			}
			return boardNameTextView;
		}
		public TextView getBoardAuthorTextView()
		{
			if (convertView != null)
			{
				boardAuthorTextView = (TextView) convertView.findViewById(R.id.bbs_board_author);
			}
			return boardAuthorTextView;
		}
		public TextView getBoardCountTextView()
		{
			if (convertView != null)
			{
				boardCountTextView = (TextView) convertView.findViewById(R.id.bbs_board_count);
			}
			return boardCountTextView;
		}
		public TextView getBoardCreateTimeTextView()
		{
			if (convertView != null)
			{
				boardCreateTimeTextView = (TextView) convertView.findViewById(R.id.bbs_content_create_time);
			}
			return boardCreateTimeTextView;
		}
		public TextView getBoardContentTextView()
		{
			if (convertView != null)
			{
				boardContentTextView = (TextView) convertView.findViewById(R.id.bbs_board_content);
			}
			return boardContentTextView;
		}


		public ViewGroup getBoardGroup()
		{
			if (convertView != null)
			{
				boardGroup = (ViewGroup) convertView.findViewById(R.id.bbs_board_group);
			}
			return boardGroup;
		}
		
		
	
	}



	public List<PBBBSBoard> getBoardList()
	{
		return boardList;
	}

	public void setBoardList(List<PBBBSBoard> boardList)
	{
		this.boardList = boardList;
	}
}
