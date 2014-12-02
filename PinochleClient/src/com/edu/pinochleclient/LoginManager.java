package com.edu.pinochleclient;

import org.andengine.util.debug.Debug;
import org.mindrot.jbcrypt.BCrypt;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.edu.message.RequestPacket;

public class LoginManager {
	private static final LoginManager INSTANCE = new LoginManager();
	
	public LoginActivity loginActivity;
	
	// User Data
	private String session_id;
	private String username;
	private String email;
	private String password;
	
	private String salt;
	private String stored_salt;
	private Boolean password_dirty;
	
	
	// Request Packet
	private RequestPacket packet;
	
	private LoginManager(){}
	
	public void init() {
		loadUser();
	}
	
	public static LoginManager getInstance() {
		return INSTANCE;
	}
	
	// Save user's credentials, hash password
	public void saveUser() {
		ResourceManager.getInstance();
		Editor edit = ResourceManager.getInstance().activity.getPreferences(Context.MODE_PRIVATE).edit();
		edit.putString("username", username);
		edit.putString("password", password);
		edit.apply();
	}
	
	// Try to load user's credentials
	public void loadUser() {
		ResourceManager.getInstance();
		username = ResourceManager.getInstance().activity.getPreferences(Context.MODE_PRIVATE).getString("username", null);
		loginActivity.updateCredentials();
	}
	
	// Login
	public void login() {
		packet = new RequestPacket();
		
		// check to make sure password length is at least 8 characters
		if(password.length() < 8) {
			loginActivity.setMessage("Password must be at least 8 characters");
			return;
		}
				
		// check if stored_salt is null && password is dirty to reload stored_salt
		if(stored_salt == null) {
			ResourceManager.getInstance().sendRequest(packet
					.setRequest("send_salt")
					.setUser_name(username));
			loginActivity.updateMessage("loading account... please wait");
			return;
		}
		
		// Create Request Packet
		loginActivity.loadSpinner(true);
		packet.setRequest("login").setUser_name(username);
		
		// If loaded password has been overridden, re-hash with salt
		if(password_dirty) {
			password = BCrypt.hashpw(password, stored_salt);
			packet.setHash_password(BCrypt.hashpw(password, salt));
		}
		else {
			ResourceManager.getInstance();
			password = ResourceManager.getInstance().activity.getPreferences(Context.MODE_PRIVATE).getString("password", null);
			packet.setHash_password(BCrypt.hashpw(password, salt));
		}
		
		// Send login packet
		ResourceManager.getInstance().sendRequest(packet);
	}
	
	// Forgot password
	public void forgotPassword() {
		loginActivity.loadSpinner(true);
		packet = new RequestPacket();
		ResourceManager.getInstance().sendRequest(packet
				.setRequest("forgot_password")
				.setUser_name(username)
				.setEmail(email));
	}
	
	// Create Account
	public void createAccount() {
		if(password.length() < 8) {
			loginActivity.setMessage("Password must be at least 8 characters");
			return;
		}
		
		loginActivity.loadSpinner(true);
		
		// Create packet and hash password, set stored_salt
		packet = new RequestPacket();
		packet.setRequest("create_account").setUser_name(username).setEmail(email);
		password = BCrypt.hashpw(password, salt);
		packet.setHash_password(password);
		stored_salt = salt;
		
		// Send request
		ResourceManager.getInstance().sendRequest(packet);
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStored_salt() {
		return stored_salt;
	}

	public void setStored_salt(String stored_salt) {
		this.stored_salt = stored_salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public RequestPacket getPacket() {
		return packet;
	}

	public void setPacket(RequestPacket packet) {
		this.packet = packet;
	}

	public Boolean getPassword_dirty() {
		return password_dirty;
	}

	public void setPassword_dirty(Boolean password_dirty) {
		this.password_dirty = password_dirty;
	}

	public LoginActivity getLoginActivity() {
		return loginActivity;
	}

	public void setLoginActivity(LoginActivity loginActivity) {
		this.loginActivity = loginActivity;
	}
}
