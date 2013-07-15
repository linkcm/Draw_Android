/**  
        * @title ShareItemData.java  
        * @package com.orange.game.draw.activity.drawDetail.share  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-20 上午10:14:34  
        * @version V1.0  
 */
package com.orange.game.draw.activity.share;

import android.view.View.OnClickListener;



public class ShareItemData
{

	final public int textId;
	final public int imageId;
	final public OnClickListener onClickListener;
	
	public ShareItemData(int textId, int imageId,
			OnClickListener onClickListener)
	{
		super();
		this.textId = textId;
		this.imageId = imageId;
		this.onClickListener = onClickListener;
	}
	
	
}
