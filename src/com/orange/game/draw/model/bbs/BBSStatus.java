/**  
        * @title BBSStatusType.java  
        * @package com.orange.game.draw.model.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-26 上午10:49:22  
        * @version V1.0  
 */
package com.orange.game.draw.model.bbs;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-26 上午10:49:22  
 */

public class BBSStatus
{
	public static final int ActionTypeComment = 1;
//	public static final int ActionTypeReply = 2;
	public static final int ActionTypeSupport = 2;
	public static final int ActionTypeNO = 0;
	public static final int StatusNormal = 0;
	public static final int StatusDelete = 1;
	public static final int BBSBoardTypeParent = 1;
	public static final int BBSBoardTypeSub = 2;
	public static final int ContentTypeNo = 0;
	public static final int ContentTypeText = 1;
	public static final int ContentTypeImage = 2;
	public static final int ContentTypeDraw = 4;
	/*public static final int StatusNormal = 0;
	public static final int StatusDelete = 1;*/
	public static final int StatusMark = 0x1 << 1;
	public static final int StatusTop = 0x1 << 2;
	public static final int RewardStatusNo = 0;
	public static final int RewardStatusOn = 1;
	public static final int RewardStatusOff = 2;

}
