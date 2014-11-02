package com.edu.pinochleclient;

import org.andengine.util.debug.Debug;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import naga.NIOService;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.packetreader.AsciiLinePacketReader;
import naga.packetwriter.AsciiLinePacketWriter;

public class SocketManager extends AsyncTask<Void,Void,Void> {

	private static final SocketManager INSTANCE = new SocketManager();
	
	private NIOService service;
	private NIOSocket socket;
	private static JSONObject message;
	
	private SocketManager() {
	}
	
	public static SocketManager getInstance() {
		return INSTANCE;
	}
	
	public static JSONObject getMessage() {
		return message;
	}

	@Override
	protected Void doInBackground(Void... params) {
				try
		        {
		                service = new NIOService();
		                socket = service.openSocket("10.0.2.2", 5217);
		                
		                socket.setPacketReader(new AsciiLinePacketReader());
		                socket.setPacketWriter(new AsciiLinePacketWriter());
		                
		                socket.listen(new SocketObserver()
		                {
		                	@Override
		                    public void connectionOpened(NIOSocket nioSocket)
		                    {
		                		Debug.i("connection opened");
		                    }
		                	@Override
				            public void packetSent(NIOSocket socket, Object tag)
				            {
		                		Debug.i("message sent");
				            }
		                	@Override
			            	public void packetReceived(NIOSocket socket, byte[] packet)
		                    {
		                		Debug.i("message received");
		                		String temp = new String(packet).trim();
		                		try {
									message = new JSONObject(temp);
								} catch (JSONException e) {
									e.printStackTrace();
								}
		                    }
		                	@Override
		                    public void connectionBroken(NIOSocket nioSocket, Exception exception)
		                    {
		                		Debug.i("connection broken");
		                    }
		                });
		                
		            while(true) {
		            	service.selectNonBlocking();
		            }
		            
		        }
		        catch (Exception e)
		        {
		        }
			return null;
	}
}
