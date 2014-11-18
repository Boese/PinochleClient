package com.edu.util;

import org.andengine.util.debug.Debug;

import com.edu.message.ResponsePacket;
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
			ResourceManager.getInstance().setSalt(packet.getMessage());
			break;
		case "session_id": 
			ResourceManager.getInstance().setSession_id(packet.getMessage());
			ResourceManager.getInstance().loginActivity.finish();
			SceneManager.getInstance().showScene(LobbyScene.class);
			break;
		case "create_success":
			ResourceManager.getInstance().setMessage(packet.getMessage());
			ResourceManager.getInstance().saveUser();
			break;
		case "response": 
			ResourceManager.getInstance().setMessage(packet.getMessage());
			break;
		}
		
	}
}
