package com.orange.game.draw.activity.home.menu;

import java.util.ArrayList;
import java.util.List;

import com.orange.game.R;
import com.orange.game.draw.activity.home.adapter.CommonViewPagerAdapter;

import android.R.integer;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MenuViewManager {

	
	
	private static final int MENU_ITEM_PER_PAGE = 6;
	private static final int MENU_ITEM_PER_ROW  = 3;
	private static final int[] MENU_TABLE_ROW_ID = {R.id.menu_view_row_1, R.id.menu_view_row_2};
	//static ArrayList<View> viewList = new ArrayList<View>();
	public static void createMenuView(Context context, ViewPager viewPager, LayoutInflater inflater, List<MenuData> menuDataList){

    	
		int menuItemCount = menuDataList.size();
		int pageCount = (menuItemCount % MENU_ITEM_PER_PAGE) == 0 ? (menuItemCount / MENU_ITEM_PER_PAGE) : (menuItemCount / MENU_ITEM_PER_PAGE) + 1;
		
        int menuItemIndex = 0;
		ArrayList<View> viewList = new ArrayList<View>();
		for (int page=0; page<pageCount; page++){			
			
			// create view by layout
	        View view = inflater.inflate(R.layout.home_menu_view, null);
	        
	        // set layout parameters
	        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.menu_view_table);
	        tableLayout.setStretchAllColumns(true);
	        
	        // get row and add menu item into rows
	        for (int tableRowIndex=0; tableRowIndex<MENU_TABLE_ROW_ID.length; tableRowIndex++){
	        	TableRow tableRow = (TableRow)view.findViewById(MENU_TABLE_ROW_ID[tableRowIndex]);	        
	        	for (int i=0; i<MENU_ITEM_PER_ROW; i++){

		        	// create menu item and set its information
		        	View menuItemView = (View)inflater.inflate(R.layout.home_menu_item, null);
		        	tableRow.addView(menuItemView);
		        	if (menuItemIndex >= menuItemCount){
		        		// this item is just a virtual item, set to INVISIBLE
		        		menuItemView.setVisibility(View.INVISIBLE);
		        	}
		        	else{
		        		MenuData data = menuDataList.get(menuItemIndex);
		        		
			        	// set button image and click listener
			        	ImageButton button = (ImageButton)menuItemView.findViewById(R.id.menu_button);
			        	button.setImageDrawable(context.getResources().getDrawable(data.imageId));
			        	button.setOnClickListener(data.onClickListener);
			        	
			        	//set badge number data and show badge number
			        	if (data.isShowBadgeNumber)
						{
			        		TextView badgeNumberTextView = (TextView) menuItemView.findViewById(R.id.badge_number);
			        		badgeNumberTextView.setTag(menuItemIndex);
			        		badgeNumberTextView.setVisibility(View.VISIBLE);
			        		if (data.badgeNumberData>10)
			        			badgeNumberTextView.setText("N");
							else 
								badgeNumberTextView.setText(Integer.toString(data.badgeNumberData));
						}
			        	// set button text
			        	TextView textView = (TextView)menuItemView.findViewById(R.id.menu_text);	        	
			        	textView.setText(data.textId);	        			        				        	
		        	}
		        	
		        	menuItemIndex ++;	        		        	
		        }
	        }
	        ImageView nextPageImage = (ImageView) view .findViewById(R.id.next_pager_image);
	        ImageView frontPageImage = (ImageView) view.findViewById(R.id.front_pager_image);
	        if (page%2 == 0)
			{
	        	nextPageImage.setVisibility(view.VISIBLE);
	        	nextPageImage.setImageResource(R.drawable.common_home_nextpage);
				Animation nextPageAnimation =AnimationUtils.loadAnimation(context, R.anim.next_page_anim);
				nextPageImage.setAnimation(nextPageAnimation);
			}else {
				frontPageImage.setVisibility(View.VISIBLE);
				frontPageImage.setImageResource(R.drawable.common_home_front_page);
				Animation frontpageAnimation =AnimationUtils.loadAnimation(context, R.anim.front_page_anim);
				frontPageImage.setAnimation(frontpageAnimation);			
			}
	        viewList.add(view);
		}
		
		// add viewList into viewPager 
  		CommonViewPagerAdapter adapter = new CommonViewPagerAdapter(viewList);
  		viewPager.setAdapter(adapter);
  		
		return;
	}
	
	/*public static void setBadge(int badgeNumber,int position){
		View view = null;
		if (position%6==0)
			view = viewList.get(0);
		else
			view = viewList.get(1);
		TextView textView = (TextView) view.findViewWithTag(position);
		textView.setText(""+badgeNumber);
	}*/
	
}
