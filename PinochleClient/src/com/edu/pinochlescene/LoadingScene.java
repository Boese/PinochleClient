package com.edu.pinochlescene;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.adt.color.Color;
import com.edu.pinochleclient.GameActivity;

public class LoadingScene extends AbstractScene {
	private Text loadingText;
	
	@Override
	public void loadResources() {
		Font f = FontFactory.createFromAsset(engine.getFontManager(), engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR, activity.getAssets(), "fontfolder/Geeza Pro Bold.ttf", 150f, true, Color.WHITE_ABGR_PACKED_INT);
		f.load();
		loadingText = new Text(GameActivity.CW/2, GameActivity.CH/2, f, "Loading...",vbom);
	}

	@Override
	public void create() {
		this.getBackground().setColor(Color.GREEN);
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
