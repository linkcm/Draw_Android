package com.orange.game.draw.model.message;


import com.orange.network.game.protocol.model.GameBasicProtos.PBMessage;

public class PPMessage {

	public static final int MessageTypeText = 0;
	public static final int MessageTypeLocationRequest = 1;
	public static final int MessageTypeLocationResponse = 2;
	public static final int MessageTypeDraw = 4;
	public static final int MessageTypeImage = 5;
	public static final int MessageTypeVoice = 6;

//	public static final int MessageStatusUnread = 0;
//	public static final int MessageStatusRread = 1;

	public static final int LocationResponseReject = 1;
	public static final int LocationResponseAccept = 0;
	public static final int FLAG_NORMAL = 0;
	public static final int FLAG_DELETE = 1;
	
	
	// message status
	public static final int MessageStatusRead = 0;
	public static final int MessageStatusUnread = 1;
	public static final int MessageStatusSending = 2;
	public static final int MessageStatusSent = 3;
	public static final int MessageStatusFail = 4;
	
	
	final PBMessage pbMessage;
	volatile int sentStatus;				// only for SEND MESSAGE
			
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pbMessage.getMessageId() == null) ? 0 : pbMessage.getMessageId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PPMessage other = (PPMessage) obj;
		if (pbMessage == null) {
			if (other.pbMessage != null)
				return false;
		} else if (!pbMessage.getMessageId().equals(other.pbMessage.getMessageId()))
			return false;
		return true;
	}

	public PPMessage(PBMessage pbMessage){
		this.pbMessage = pbMessage;
		sentStatus = pbMessage.getStatus();
	}

	public PBMessage getPbMessage() {
		return pbMessage;
	}

	public int getSentStatus() {
		return sentStatus;
	}

	public void setSentStatus(int status){
		sentStatus = status;
	}	
	
}
