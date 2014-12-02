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
import org.andengine.util.debug.Debug;

import com.edu.pinochlescene.AbstractScene;
import com.edu.pinochlescene.GameScene;
import com.edu.pinochlescene.LoadingScene;
import com.edu.pinochlescene.LobbyScene;
import com.edu.pinochlescene.SelectGameScene;
import com.edu.pinochlescene.SplashScene;

import android.content.Intent;

public class GameActivity extends BaseGameActivity {
	
	public ZoomCamera camera = null;
	public static final int CW = 720;
	public static final int CH = 1200;
	public static final int FPS_LIMIT = 60;
	protected static final long SPLASH_DURATION = 1000;
	
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
		//camera = new SmoothCamera(0, 0, CW, CH, 0, 700, 0);
		camera = new ZoomCamera(0, 0, CW, CH);
		camera.setZClippingPlanes(-100, 100);
		IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
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
			ResourceManager.getInstance().connect();
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
				login();
			}
		}, SPLASH_DURATION);
	}
	
	@Override
	public void finish() {
		ResourceManager.getInstance().disconnect();
		super.finish();
	}
	
	@Override
    public void onBackPressed()
    {
		final Scene scene = SceneManager.getInstance().getCurrentScene();
        if(!scene.getClass().equals(LobbyScene.class)) {
        	SceneManager.getInstance().showScene(LobbyScene.class);
        }
        else{
            this.finish();
        }
    }
	
	public void login() {
		startActivity(new Intent(this,LoginActivity.class));
	}
	
}
