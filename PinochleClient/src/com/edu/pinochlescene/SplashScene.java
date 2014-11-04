package com.edu.pinochlescene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import com.edu.pinochleclient.GameActivity;

public class SplashScene extends AbstractScene {

	private ITextureRegion backgroundRegion;
	private BitmapTextureAtlas gameTextureAtlas;
	private Sprite splash;
	private Text loadingText;
	
	@Override
	public void loadResources() {
		Font f = FontFactory.createFromAsset(engine.getFontManager(), engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR, activity.getAssets(), "fontfolder/Geeza Pro Bold.ttf", 100f, true, Color.WHITE_ARGB_PACKED_INT);
		f.load();
		loadingText = new Text(GameActivity.CW/2, GameActivity.CH-220, f, "CARDS",vbom);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		gameTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),860, 1078, TextureOptions.BILINEAR);
		backgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "logincardscopy.png", 0, 0);
		gameTextureAtlas.load();
	}

	@Override
	public void create() {
		splash = new Sprite(GameActivity.CW/2,GameActivity.CH-((backgroundRegion.getHeight()/3)*2),backgroundRegion,vbom);
		splash.setSize(GameActivity.CW, GameActivity.CH);
	    this.attachChild(splash);
	    this.attachChild(loadingText);
	}

	@Override
	public void unloadResources() {
		// TODO Auto-generated method stub
		
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
