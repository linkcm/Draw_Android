package com.orange.game.draw.model.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.R.integer;

import com.orange.network.game.protocol.model.DrawProtos.PBFeed;

public class FeedManager {
	
	final List<PBFeed> feedList = new ArrayList<PBFeed>();
	final FeedListType feedListType;
	final FeedActionType feedActionType;
		
	public FeedManager(FeedListType feedListType){		
		this.feedListType = feedListType;
		this.feedActionType = FeedActionType.NoAction;
	}

	public FeedManager(FeedActionType feedActionType){		
		this.feedListType = FeedListType.FeedListTypeUnknow;
		this.feedActionType = feedActionType;
	}	
	
	public List<PBFeed> getFeedList() {
		return feedList;
	}


	
	public void addFeedList(List<PBFeed> newFeedList, int offset, int limit) {
		if (feedList == null || offset < 0){
			return;
		}

		int newLen = newFeedList.size();
		int existDataLen = feedList.size();
		for (int i=0; i<newLen; i++){
			PBFeed feed = newFeedList.get(i);
			if (offset+i < existDataLen){
				// replace exist feed
				feedList.set(i, feed);
			}
			else{
				// insert new one
				feedList.add(feed);
			}
		}		
	}
	
	
	public List<PBFeed> getFeedList(int offset,int limit)
	{
		if (feedList == null || offset < 0){
			return Collections.emptyList();
		}
		return feedList.subList(offset, offset+limit);
		
	}
	
	public void clear(){
		feedList.clear();
	}

	public FeedListType getFeedListType()
	{
		return feedListType;
	}

	public int getFeedListCount()
	{
		return feedList.size();
	}

	public FeedActionType getFeedActionType() {
		return feedActionType;
	}
	
}
