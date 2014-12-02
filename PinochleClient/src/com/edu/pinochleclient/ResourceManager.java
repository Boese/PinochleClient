package com.edu.pinochleclient;

import naga.NIOService;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.packetreader.AsciiLinePacketReader;
import naga.packetwriter.AsciiLinePacketWriter;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.edu.message.RequestPacket;
import com.edu.util.MessageHandler;
import com.edu.util.MessageTransformer;

public class ResourceManager {
	
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	// common objects
	public GameActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	public MessageTransformer msgTransformer;
	public LoginActivity loginActivity;
	
	// Socket
	private NIOService service;
	private NIOSocket socket;
	private Thread socketThread;
	private String message;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	    
	private ResourceManager() {}
	
	public static ResourceManager getInstance() {
		return INSTANCE;
	}
	
	public void init(GameActivity activity) throws Exception {
		this.activity = activity;
		this.engine = activity.getEngine();
		this.camera = engine.getCamera();
		this.vbom = engine.getVertexBufferObjectManager();
		this.msgTransformer = new MessageTransformer();
	}
	
	public void connect() {
		this.socketThread = new Thread(new SocketManager());
		this.socketThread.start();
	}
	
	public void disconnect() {
		if(this.socket != null)
			this.socket.closeAfterWrite();
		this.setMessage(null);
	}
	
	public void sendRequest(RequestPacket packet) {
		if(!socket.isOpen()) {
			this.connect();
		} else
			socket.write(msgTransformer.writeMessage(packet).getBytes());
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		loginActivity.updateMessage(message);
	}
	
	public LoginActivity getLoginActivity() {
		return loginActivity;
	}

	public void setLoginActivity(LoginActivity loginActivity) {
		this.loginActivity = loginActivity;
	}

	private class SocketManager implements Runnable {
		
		@Override
		public void run() {
			try
	        {
	                service = new NIOService();
	                socket = service.openSocket("76.14.226.157", 5217);
	                //socket = service.openSocket("10.0.2.2", 5217);
	                
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
	                		MessageHandler.getInstance().HandleMessage(new String(packet));
	                    }
	                	@Override
	                    public void connectionBroken(NIOSocket nioSocket, Exception exception)
	                    {
	                		setMessage("connection lost");
	                    }
	                });
	                
	            while(socket.isOpen()) {
	            	service.selectNonBlocking();
	            }
	            
	        }
	        catch (Exception e) {}
		}
		
	}
}
