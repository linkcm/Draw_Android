package com.orange.game.draw.model.message;

import com.orange.network.game.protocol.model.GameBasicProtos.PBMessageStat;

public class MessageStat {

	final PBMessageStat pbMessageStat;
	volatile int newMessageCount;
	
	public MessageStat(PBMessageStat pbMessageStat){
		this.pbMessageStat = pbMessageStat;
		this.newMessageCount = pbMessageStat.getNewMessageCount();
	}

	public void clearNewMessage(){
		this.newMessageCount = 0;
	}

	public PBMessageStat getPBMessageStat() {
		return pbMessageStat;
	}

	public int getNewMessageCount() {
		return newMessageCount;
	}
	
	
}
