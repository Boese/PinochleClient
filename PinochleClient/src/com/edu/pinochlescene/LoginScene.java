package com.edu.pinochlescene;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

public class LoginScene extends AbstractScene {
	
	private ITextureRegion LoginRegion;
	private ButtonSprite Login;
	
	// Game Texture
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	
	@Override
	public void loadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	    
	    gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
	    		256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	    
	    LoginRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity.getAssets(), "loginbutton.png");
	    
	    FontFactory.setAssetBasePath("fontfolder/");
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
		getBackground().setColor(Color.WHITE);
		camera.reset();
		
		final float centerX = camera.getCenterX();
		final float centerY = camera.getCenterY();
		Login = new ButtonSprite(centerX, centerY, LoginRegion, LoginRegion, LoginRegion, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				activity.toastOnUiThread("login clicked!");
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		Login.setScale(5);
		this.registerTouchArea(Login);
		this.attachChild(Login);
	}


	@Override
	public void unloadResources() {
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
