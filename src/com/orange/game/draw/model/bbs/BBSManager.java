/**  
        * @title BBSManager.java  
        * @package com.orange.game.draw.model.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-24 上午10:14:58  
        * @version V1.0  
 */
package com.orange.game.draw.model.bbs;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.orange.network.game.protocol.model.BBSProtos;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSAction;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSBoard;
import com.orange.network.game.protocol.model.BBSProtos.PBBBSPost;
import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-24 上午10:14:58  
 */

public class BBSManager
{
	final List<PBBBSBoard> boardList = new ArrayList<PBBBSBoard>();
	/*final List<PBBBSPost> newPostList = new ArrayList<PBBBSPost>();
	final List<PBBBSPost> hotPostList = new ArrayList<PBBBSPost>();
	final List<PBBBSAction> postCommentActionList = new ArrayList<PBBBSAction>();
	final List<PBBBSAction> postSupportActionList = new ArrayList<PBBBSAction>();*/
	final List<PBBBSPost> postList = new ArrayList<PBBBSPost>();
	final List<PBBBSAction> actionList = new ArrayList<PBBBSAction>();
	
	//private PBBBSPost targetPost;
	
	
	public void addBoardList(List<PBBBSBoard> list){
		if (list == null){
			return;
		}

		boardList.clear();
		boardList.addAll(list);
	}
	
	
	
	public void addPostList(List<PBBBSPost> list,int offset,int limit)
	{
		if (list == null || offset < 0){
			return;
		}

		int newLen = list.size();
		int existDataLen = postList.size();
		for (int i=0; i<newLen; i++){
			PBBBSPost post = list.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				postList.set(i, post);
			}
			else{
				// insert new one
				postList.add(post);
			}
		}	
	}
	
	
	public void addActionList(List<PBBBSAction> list,int offset,int limit)
	{
		if (list == null || offset < 0){
			return;
		}

		int newLen = list.size();
		int existDataLen = actionList.size();
		for (int i=0; i<newLen; i++){
			PBBBSAction action = list.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				actionList.set(i, action);
			}
			else{
				// insert new one
				actionList.add(action);
			}
		}		
	}
	
	/*public void addNewPostList(List<PBBBSPost> list,int offset,int limit){
		if (list == null || offset < 0){
			return;
		}

		int newLen = list.size();
		int existDataLen = newPostList.size();
		for (int i=0; i<newLen; i++){
			PBBBSPost post = list.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				newPostList.set(i, post);
			}
			else{
				// insert new one
				newPostList.add(post);
			}
		}		
	}
	
	
	public void addHotPostList(List<PBBBSPost> list,int offset,int limit){
		if (list == null || offset < 0){
			return;
		}

		int newLen = list.size();
		int existDataLen = hotPostList.size();
		for (int i=0; i<newLen; i++){
			PBBBSPost post = list.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				hotPostList.set(i, post);
			}
			else{
				// insert new one
				hotPostList.add(post);
			}
		}		
	}
	
	
	public void addPostCommentList(List<PBBBSAction> list,int offset,int limit){
		if (list == null || offset < 0){
			return;
		}

		int newLen = list.size();
		int existDataLen = postCommentActionList.size();
		for (int i=0; i<newLen; i++){
			PBBBSAction action = list.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				postCommentActionList.set(i, action);
			}
			else{
				// insert new one
				postCommentActionList.add(action);
			}
		}		
	}
	
	
	public void addPostSupportList(List<PBBBSAction> list,int offset,int limit){
		if (list == null || offset < 0){
			return;
		}

		int newLen = list.size();
		int existDataLen = postSupportActionList.size();
		for (int i=0; i<newLen; i++){
			PBBBSAction action = list.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				postSupportActionList.set(i, action);
			}
			else{
				// insert new one
				postSupportActionList.add(action);
			}
		}
	}
	*/

	public List<PBBBSBoard> getBoardList()
	{
		return boardList;
	}

	/*public List<PBBBSPost> getNewPostList()
	{
		return newPostList;
	}*/
	
	public void boardClear(){
		boardList.clear();
	}
	
	public void postClear(){
		postList.clear();
	}
	
	public void actionClear(){
		actionList.clear();
	}



	public List<PBBBSPost> getPostList()
	{
		return postList;
	}



	public List<PBBBSAction> getActionList()
	{
		return actionList;
	}

	/*public void postClear(){
		newPostList.clear();
	}
	
	public void commentActionListClear(){
		postCommentActionList.clear();
	}
	
	public void supportActionListClear()
	{
		postSupportActionList.clear();
	}

	public List<PBBBSAction> getPostCommentActionList()
	{
		return postCommentActionList;
	}



	public List<PBBBSAction> getPostSupportActionList()
	{
		return postSupportActionList;
	}*/



	/*public PBBBSPost getTargetPost()
	{
		return targetPost;
	}



	public void setTargetPost(PBBBSPost targetPost)
	{
		this.targetPost = targetPost;
	}
*/

/*
	public List<PBBBSPost> getHotPostList()
	{
		return hotPostList;
	}*/



}
