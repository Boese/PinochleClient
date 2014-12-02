package com.edu.pinochleclient;

import java.util.ArrayList;
import java.util.List;

import com.edu.message.GameInfo;
import com.edu.message.RequestPacket;
import com.edu.pinochlescene.LobbyScene;

public class LobbyManager {
	private static final LobbyManager INSTANCE = new LobbyManager();
	
	public LobbyScene lobbbyScene;
	
	public List<GameInfo> gameInfos = new ArrayList<GameInfo>();
	public List<String> gameTypes = new ArrayList<String>();
	public RequestPacket packet;
	
	private LobbyManager() {}
	
	public static LobbyManager getInstance() {
		return INSTANCE;
	}
	
	public void joinGame(String game_id) {
		packet = new RequestPacket().setRequest("join_game").setGame_id(game_id);
		ResourceManager.getInstance().sendRequest(packet);
	}
	
	public void newGame(String game_type) {
		packet = new RequestPacket().setRequest("new_game").setGame_type(game_type);
		ResourceManager.getInstance().sendRequest(packet);
	}
	
	public void randomGame(String game_type) {
		packet = new RequestPacket().setRequest("random_game").setGame_type(game_type);
		ResourceManager.getInstance().sendRequest(packet);
	}
	
	public void quitGame() {
		packet = new RequestPacket().setRequest("quit_game");
		ResourceManager.getInstance().sendRequest(packet);
	}
}
