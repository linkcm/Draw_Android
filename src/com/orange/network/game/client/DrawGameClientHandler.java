package com.orange.network.game.client;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.orange.game.draw.service.DrawGameNetworkService;
import com.orange.network.game.protocol.constants.GameConstantsProtos.GameCommandType;
import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameSession;

public class DrawGameClientHandler extends SimpleChannelUpstreamHandler {

	
	
	private static final String TAG = "DrawGameClientHandler";

	public DrawGameClientHandler() {
		super();
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		GameMessage message = (GameMessage) e.getMessage();		
		Log.d(TAG, "<messageReceived> message="+message.toString());
				
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putByteArray(DrawGameNetworkService.KEY_GAME_MESSAGE, message.toByteArray());		
		msg.setData(b);
		DrawGameNetworkService.getInstance().getHandler().sendMessage(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Log.e(TAG, "<exceptionCaught>", e.getCause());
		e.getChannel().close();
		
		DrawGameNetworkService.getInstance().handleException(e);		
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		Log.i(TAG, "<channelDisconnected> channel="+e.getChannel().toString());
		DrawGameNetworkService.getInstance().handleChannelDisconnected(e.getChannel());		
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		Log.i(TAG, "<channelClosed> channel="+e.getChannel().toString());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Log.i(TAG, "<channelConnected> channel="+e.getChannel().toString());
		DrawGameNetworkService.getInstance().handleChannelConnected(e.getChannel());		
	}
	
	
}
