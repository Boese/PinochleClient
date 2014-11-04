package com.edu.pinochlescene;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import com.edu.pinochleclient.GameActivity;

public class LobbyScene extends AbstractScene{

	private TiledTextureRegion LobbyRegion;
	private TiledTextureRegion RoomsRegion;
	private TiledTextureRegion JoinRegion;
	private TiledTextureRegion OptionsRegion;
	private TextureRegion backgroundRegion;
	
	List<TiledTextureRegion> buttonRegions = new ArrayList<TiledTextureRegion>();
	List<String> buttonNames = new ArrayList<String>();
	
	@Override
	public void loadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas LobbyTexture = new BitmapTextureAtlas(activity.getTextureManager(), 448, 157, TextureOptions.BILINEAR);
		LobbyRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(LobbyTexture, activity, "lobby.png", 0, 0, 1, 2);
		LobbyTexture.load();
		buttonRegions.add(LobbyRegion);
		buttonNames.add("Lobby");
		
		BitmapTextureAtlas RoomsTexture = new BitmapTextureAtlas(activity.getTextureManager(), 448, 157, TextureOptions.BILINEAR);
		RoomsRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(RoomsTexture, activity, "rooms.png", 0, 0, 1, 2);
		RoomsTexture.load();
		buttonRegions.add(RoomsRegion);
		buttonNames.add("Rooms");
		
		BitmapTextureAtlas JoinTexture = new BitmapTextureAtlas(activity.getTextureManager(), 448, 157, TextureOptions.BILINEAR);
		JoinRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(JoinTexture, activity, "join.png", 0, 0, 1, 2);
		JoinTexture.load();
		buttonRegions.add(JoinRegion);
		buttonNames.add("Join");
		
		BitmapTextureAtlas OptionsTexture = new BitmapTextureAtlas(activity.getTextureManager(), 448, 157, TextureOptions.BILINEAR);
		OptionsRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(OptionsTexture, activity, "options.png", 0, 0, 1, 2);
		OptionsTexture.load();
		buttonRegions.add(OptionsRegion);
		buttonNames.add("Options");
		
		BitmapTextureAtlas backgroundTexture = new BitmapTextureAtlas(activity.getTextureManager(), 860, 1078, TextureOptions.BILINEAR);
		backgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTexture, activity, "logincardscopy.png",0,0);
		backgroundTexture.load();
	}

	@Override
	public void create() {
		initBackground();
		initButtons();
	}
	
	@Override
	public void onUpdate() {
		
	}
	
	public void initBackground() {
		AutoParallaxBackground background = new AutoParallaxBackground(0, 0, 0, 5);
		Sprite s = new Sprite(GameActivity.CW/2, GameActivity.CH-((934/3)*2), backgroundRegion, vbom);
		ParallaxEntity b = new ParallaxEntity(-5,s);
		background.attachParallaxEntity(b);
		this.setBackground(background);
	}
	
	public void initButtons() {
		int i = 0;
		for(TiledTextureRegion region:buttonRegions) {
			final int ii = i;
			TiledSprite button = new TiledSprite(GameActivity.CW/2, GameActivity.CH-
					(((GameActivity.CH/buttonRegions.size())/2)+((GameActivity.CH/buttonRegions.size())*i)), region, vbom){
	            @Override
			    public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
			            switch(pAreaTouchEvent.getAction()){
			            case TouchEvent.ACTION_DOWN:
			                    this.setCurrentTileIndex(1);
			                    activity.toastOnUiThread(buttonNames.get(ii) + " clicked");
			                    break;
	                    case TouchEvent.ACTION_UP:
	                            this.setCurrentTileIndex(0);
	                            break;
			            }
			            return true;
			    }
			};
			button.setWidth(GameActivity.CW - 100);
			this.registerTouchArea(button);
			this.attachChild(button);
			i++;
		}
	}

	@Override
	public void unloadResources() {
		buttonRegions = null;
		buttonNames = null;
		backgroundRegion = null;
	}

	@Override
	public void destroy() {
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
