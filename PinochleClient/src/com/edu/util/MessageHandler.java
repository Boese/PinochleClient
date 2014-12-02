package com.edu.util;

import org.andengine.util.debug.Debug;

import com.edu.message.ResponsePacket;
import com.edu.pinochleclient.LobbyManager;
import com.edu.pinochleclient.LoginManager;
import com.edu.pinochleclient.ResourceManager;
import com.edu.pinochleclient.SceneManager;
import com.edu.pinochlescene.LobbyScene;

public class MessageHandler {
	private MessageHandler(){
		this.msgTransformer = new MessageTransformer();
	}
	
	private static final MessageHandler INSTANCE = new MessageHandler();
	private MessageTransformer msgTransformer;
	
	public static MessageHandler getInstance() {
		return INSTANCE;
	}
	
	public void HandleMessage(String message) {
		Debug.i("Message received : " + message);
		ResponsePacket packet = (ResponsePacket) msgTransformer.getMessage(message, ResponsePacket.class);
		switch(packet.getResponse()) {
		case "salt": 
			LoginManager.getInstance().setSalt(packet.getMessage());
			break;
		case "stored_salt": 
			LoginManager.getInstance().setStored_salt(packet.getMessage());
			LoginManager.getInstance().loginActivity.updateMessage("account loaded, logging in now...");
			LoginManager.getInstance().login();
			break;
		case "stored_salt_failed": 
			LoginManager.getInstance().loginActivity.updateMessage(packet.getMessage());
			break;
		case "session_id": 
			LoginManager.getInstance().saveUser();
			LoginManager.getInstance().loginActivity.updateMessage("success");
			LoginManager.getInstance().setSession_id(packet.getMessage());
			LoginManager.getInstance().loginActivity.finish();
			SceneManager.getInstance().showScene(LobbyScene.class);
			break;
		case "create_success":
			LoginManager.getInstance().loginActivity.updateMessage(packet.getMessage());
			break;
		case "response": 
			LoginManager.getInstance().loginActivity.updateMessage(packet.getMessage());
			break;
		case "lobby":
			LobbyManager.getInstance().gameInfos = packet.getGames();
			LobbyManager.getInstance().gameTypes = packet.getGame_types();
			break;
		}
		
	}
}
