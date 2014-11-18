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
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.edu.message.LobbyPacket;
import com.edu.message.LoginPacket;
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
	private String session_id;
	private String salt;
	
	// Account Manager
	public AccountManager accountManager;
	private static final String accountType = "com.cards";
	private LoginPacket createPacket;
	private Account useraccount;
	
	// User Data
	private String username;
	private String hashed_password;
	private String stored_salt;
	private String email;

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
		this.accountManager = AccountManager.get(activity);
		loadUser();
	}
	
	public void init(LoginActivity activity) {
		this.loginActivity = activity;
	}
	
	public void connect() {
		this.socketThread = new Thread(new SocketManager());
		this.socketThread.start();
	}
	
	public void disconnect() {
		if((this.socket != null) && !this.socket.isOpen())
			this.socket.closeAfterWrite();
		this.session_id = null;
		this.salt = null;
		this.setMessage(null);
	}
	
	public void loadUser() {
		username = activity.getPreferences(Context.MODE_PRIVATE).getString("username", null);
		hashed_password = activity.getPreferences(Context.MODE_PRIVATE).getString("hash_password", null);
		email = activity.getPreferences(Context.MODE_PRIVATE).getString("email", null);
		stored_salt = activity.getPreferences(Context.MODE_PRIVATE).getString("salt", null);
	}
	
	public void saveUser() {
		Editor edit = activity.getPreferences(Context.MODE_PRIVATE).edit();
		edit.putString("username", createPacket.getUser_name());
		edit.putString("hash_password", createPacket.getHash_password());
		edit.putString("email", createPacket.getEmail());
		edit.putString("salt", salt);
		edit.apply();
	}
	
	public void login(LoginPacket loginPacket) {
		if(!socket.isOpen()) {
			this.connect();
			activity.Login();
			return;
		}
		socket.write(msgTransformer.writeMessage(loginPacket).getBytes());
	}
	
	public void lobby(LobbyPacket lobbyPacket) {
		if(!session_id.equals(null))
			socket.write(msgTransformer.writeMessage(lobbyPacket).getBytes());
		else {
			activity.Login();
		}
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return username;
	}

	public String getHashed_password() {
		return hashed_password;
	}

	public String getStored_salt() {
		return stored_salt;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		
		if(!loginActivity.equals(null))
			loginActivity.updateMessage(message);
	}
	
	public String getEmail() {
		return email;
	}

	public void setCreatePacket(LoginPacket createPacket) {
		this.createPacket = createPacket;
	}

	private class SocketManager implements Runnable {

		@Override
		public void run() {
			try
	        {
	                service = new NIOService();
	                //socket = service.openSocket("76.14.226.157", 5217);
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
	                		MessageHandler.getInstance().HandleMessage(new String(packet));
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
		}
		
	}
}
