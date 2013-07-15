/**  
        * @title BBSUserPermission.java  
        * @package com.orange.game.draw.model.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-8 下午3:20:49  
        * @version V1.0  
 */
package com.orange.game.draw.model.user;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-8 下午3:20:49  
 */

public enum UserPermission
{
	
		PermissionRead(0x1), // 读
	    PermissionWrite (0x1 << 1), // 写
	    PermissionDelete (0x1 << 2), // 删帖
	    PermissionTransfer (0x1 << 3), // 转移
	    PermissionToTop (0x1 << 4), // 置顶
	    PermissionForbidUser (0x1 << 5), // 封禁用户, Board
	    PermissionBlackUserList (0x1 << 6), // 封禁用户, System black user list
	    PermissionCharge (0x1 << 7), // 充值
	    PermissionPutDrawOnCell (0x1 << 8), //将画加入销售池
	    PermissionAll(0x1<<31-1);
		    
	 final int value;

	 UserPermission(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
}
