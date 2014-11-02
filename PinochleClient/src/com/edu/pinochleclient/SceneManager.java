package com.edu.pinochleclient;

import org.andengine.util.debug.Debug;

import com.edu.pinochlescene.AbstractScene;
import com.edu.pinochlescene.LoadingScene;
import com.edu.pinochlescene.LoginScene;
import com.edu.pinochlescene.SplashScene;

import android.os.AsyncTask;

public class SceneManager {

	  private static final SceneManager INSTANCE = new SceneManager();
	  
	  private ResourceManager res = ResourceManager.getInstance();
	  private AbstractScene currentScene;
	  private LoadingScene loadingScene;
	  
	  private SceneManager() {}
	  
	  /**
	   * Shows splash screen and loads resources on background
	   */
	  public void showSplash() {
	    Debug.i("Scene: Splash");
	final SplashScene splash = new SplashScene();
	setCurrentScene(splash);
	splash.loadResources();
	splash.create();
	res.engine.setScene(splash);
	
	new AsyncTask<Void, Void, Void>() {
	
	  @Override
	  protected Void doInBackground(Void... params) {
	    long timestamp = System.currentTimeMillis();
	    // TODO later load common resources here
	    
	    loadingScene = new LoadingScene();
	    loadingScene.initialize(res);
	    loadingScene.loadResources();
	    loadingScene.create();
	    // we want to show the splash at least SPLASH_DURATION miliseconds
	    long elapsed = System.currentTimeMillis() - timestamp;
	    if (elapsed < GameActivity.SPLASH_DURATION) {
	      try {
	        Thread.sleep(GameActivity.SPLASH_DURATION - elapsed);
	      } catch (InterruptedException e) {
	        Debug.w("This should not happen");
	          }
	        }
	        splash.destroy();
	        splash.unloadResources();
	        return null;
	      }
	    }.execute();
	  }
	  
	  public void showScene(Class<? extends AbstractScene> sceneClazz) {
	    if (sceneClazz == LoadingScene.class) {
	      throw new IllegalArgumentException("You can't switch to Loading scene");
	}
	
	try {
	  final AbstractScene scene = sceneClazz.newInstance();
	  Debug.i("Showing scene " + scene.getClass().getName());
	  
	  final AbstractScene oldScene = getCurrentScene();
	  setCurrentScene(loadingScene);
	  res.engine.setScene(loadingScene);
	  
	  new AsyncTask<Void, Void, Void>() {
	
	    @Override
	    protected Void doInBackground(Void... params) {
	      if (oldScene != null) {
	    	  oldScene.unloadResources();
	        oldScene.destroy();
	      }
	      Debug.i("loading new scene");
	      scene.initialize(res);
	      scene.loadResources();
    	  scene.create();
	      setCurrentScene(scene);
	      res.engine.setScene(scene);
	      return null;
	    }
	  }.execute();
	} catch (Exception e) {
	  String message = "Error while changing scene";
	      Debug.e(message, e);
	      throw new RuntimeException(message, e);
	    }
	    
	  }
	 
	  public static SceneManager getInstance() {
	    return INSTANCE;
	  }
	  public AbstractScene getCurrentScene() {
	    return currentScene;
	  }
	  private void setCurrentScene(AbstractScene currentScene) {
		  this.currentScene = null;
	    this.currentScene = currentScene;
	  }

}
