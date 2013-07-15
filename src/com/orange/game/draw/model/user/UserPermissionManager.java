/**  
        * @title BBSUserPermissionManager.java  
        * @package com.orange.game.draw.model.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-8 下午4:08:09  
        * @version V1.0  
 */
package com.orange.game.draw.model.user;

import java.util.List;

import com.orange.network.game.protocol.model.BBSProtos.PBBBSPrivilege;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-8 下午4:08:09  
 */

public class UserPermissionManager
{
	private List<PBBBSPrivilege> privilegeList;

	/**  
	* Constructor Method   
	* @param privilegeList  
	*/
	
	
	public static final String PERMISSION_SITE = "PERMISSION_SITE";
	private boolean isPermissionSite = false;
	
	/**  
	* Constructor Method     
	*/
	public UserPermissionManager()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserPermissionManager(List<PBBBSPrivilege> privilegeList)
	{
		super();
		this.privilegeList = privilegeList;
		isPermissionSite = isPermissionSite(privilegeList);
	}
	
	
	public boolean canWriteOnBBBoard(String boardId){
		return judgeUserPermission(boardId,UserPermission.PermissionWrite);
	}
	
	public boolean canDeletePost(String boardId)
	{
		return judgeUserPermission(boardId, UserPermission.PermissionDelete);
	}
	
	public boolean canTransferPost(String boardId){
		return judgeUserPermission(boardId, UserPermission.PermissionTransfer);
	}
	
	public boolean canTopPost(String boardId){
		return judgeUserPermission(boardId, UserPermission.PermissionToTop);
	}
	
	public boolean canForbinUser(String boardId){
		return judgeUserPermission(boardId, UserPermission.PermissionForbidUser);
	}
	
	
	public boolean canCharge(){
		return isPermissionSite;
	}
	
	public boolean canPutDrawOnCell(){
		return isPermissionSite;
	}
	
	public boolean canForbidUserIntoBlackUserList(){
		return isPermissionSite;
	}
	
	private boolean isPermissionSite(List<PBBBSPrivilege> privileges){
		for (PBBBSPrivilege privilege:privileges)
		{
			if (privilege.getBoardId().equalsIgnoreCase(PERMISSION_SITE))
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	private boolean judgeUserPermission(String boardId,UserPermission bbsUserPermission){
		if (isPermissionSite)
			return true;

		for(PBBBSPrivilege privilege:privilegeList){
			if (privilege.getBoardId().equalsIgnoreCase(boardId))
			{
				return (privilege.getPermission()&bbsUserPermission.value) != 0;
			}
		}
		return false;
	}

	public List<PBBBSPrivilege> getPrivilegeList()
	{
		return privilegeList;
	}

	public void setPrivilegeList(List<PBBBSPrivilege> privilegeList)
	{
		this.privilegeList = privilegeList;
		isPermissionSite = isPermissionSite(privilegeList);
	}

	public boolean isPermissionSite()
	{
		return isPermissionSite;
	}
}
