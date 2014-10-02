package com.edu.pinochlescene;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import com.edu.pinochleclient.PlayActivity;

import android.content.Intent;
import android.widget.Toast;

public class MenuScene extends AbstractScene {
	
	private ButtonSprite startGame;
	private ITextureRegion startGameRegion;
	
	// Game Texture
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	
	@Override
	public void loadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	    
	    gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
	    		1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	    
	    startGameRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "startGame.png");
	    
	    try 
	    {
	        gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	        gameTextureAtlas.load();
	    } 
	    catch (final TextureAtlasBuilderException e)
	    {
	            Debug.e(e);
	    }
	}

	@Override
	public void create() {
		getBackground().setColor(Color.BLUE);
		camera.reset();
		
		final float centerX = camera.getCenterX();
		final float centerY = camera.getCenterY();
		
		startGame = new ButtonSprite(centerX, centerY, startGameRegion, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				activity.startActivity(new Intent(activity, PlayActivity.class));
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		this.registerTouchArea(startGame);
		this.attachChild(startGame);
	}


	@Override
	public void unloadResources() {
		startGame = null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
}
