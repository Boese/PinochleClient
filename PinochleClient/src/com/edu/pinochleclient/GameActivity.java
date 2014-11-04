package com.edu.pinochleclient;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.edu.pinochlescene.LobbyScene;
import com.edu.pinochlescene.SplashScene;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity {
	
	public ZoomCamera camera = null;
	public static final int CW = 720;
	public static final int CH = 1200;
	public static final int FPS_LIMIT = 60;
	protected static final long SPLASH_DURATION = 4000;
	
	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
	}
	
	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new ZoomCamera(0,0,CW,CH);
		camera.setZClippingPlanes(-100, 100);
		IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, resolutionPolicy, camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new Engine(pEngineOptions);
		return engine;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		try {
			ResourceManager.getInstance().init(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		SceneManager.getInstance().initLoadingScreen();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				Login();
			}
		}, SPLASH_DURATION);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
	
	private void Login() {
		startActivityForResult(new Intent(this,LoginActivity.class), 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if (resultCode == RESULT_OK) {
	        	String Username= data.getStringExtra("Username").trim();
	        	String Password= data.getStringExtra("Password").trim();
	        	
	        	if(Username.equals("chris") && Password.equals("dogcat"))
	        		SceneManager.getInstance().showScene(LobbyScene.class);
	        	else {
	        		ResourceManager.getInstance().connect();
	        		//ResourceManager.getInstance().activity.toastOnUiThread("denied!");
	        		//Login();
	        		SceneManager.getInstance().showScene(LobbyScene.class);
	        	}
	        }
	        else if(resultCode == RESULT_CANCELED) {
	        	ResourceManager.getInstance().activity.toastOnUiThread("cancelled!");
	        	finish();
	        }
	        else {
	        	finish();
	        }
	    }
	}
	
}
