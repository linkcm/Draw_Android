package com.orange.network.game.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.orange.network.game.protocol.message.GameMessageProtos.GameMessage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CommonNetworkClient {
	
	protected static final String TAG = "CommonNetworkClient";
	ClientBootstrap bootstrap = null;
	ExecutorService executor = Executors.newSingleThreadExecutor();
	protected Channel channel = null;
	final Context context;
	
	public CommonNetworkClient(Context context){				
		this.context = context;
	}
	
	public void shutdown(){
		Runnable task = new Runnable() {					
			@Override
			public void run() {		
		        // Shut down thread pools to exit.
				Log.i(TAG, "<shutdown>");
				if (bootstrap != null){
					bootstrap.releaseExternalResources();
					bootstrap = null;
				}
			}
		};
		
		executor.execute(task);
	}
	
	public void disconnect(){
		if (channel != null){
			Log.i(TAG, "<disconnect> channel="+channel.toString());
			if (channel.isConnected()){
				channel.disconnect();
			}
			channel.close();
			channel = null;
		}
	} 
	
	public void connect(final String host, final int port){			
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {

				if (bootstrap == null){
					bootstrap = new ClientBootstrap(
			                new NioClientSocketChannelFactory(
			                        Executors.newSingleThreadExecutor(),
			                        Executors.newSingleThreadExecutor()));
			        
			        // Set up the event pipeline factory.
			        bootstrap.setPipelineFactory(new NetworkClientPipelineFactory());							
				}
								
				Log.i(TAG, "<connect> host="+host+", port="+port);				
				
				// Start the connection attempt.
		        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		        
		        // Wait until the connection is closed or the connection attempt fails.
		        channel = future.getChannel();
				Log.i(TAG, "<connect> done, channel="+channel.toString());				
		        
		        future.getChannel().getCloseFuture().awaitUninterruptibly();		        
			}
		};
		
		if (channel != null){
			Log.i(TAG, "<connect> but channel exist, channel="+channel.toString());
			disconnect();
		}
		
		executor.execute(task);				
	}

	public Channel getChannel() {
		return channel;
	}

}
