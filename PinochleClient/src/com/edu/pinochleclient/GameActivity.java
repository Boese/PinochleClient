package com.edu.pinochleclient;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity {
	
	public ZoomCamera camera = null;
	public static final int CW = 720;
	public static final int CH = 1200;
	public static final int FPS_LIMIT = 60;
	protected static final long SPLASH_DURATION = 2000;
	
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
			// TODO Auto-generated catch block
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
		SceneManager.getInstance().showSplash();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		      try {
		        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		      } catch (NullPointerException ne) {
		        // in highly unlikely situation when this there is no scene, do nothing
		        Debug.e("The current scene is null", ne);
		      }
		    }
		return false;
	}
	
}
