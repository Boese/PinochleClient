package com.edu.pinochlescene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.adt.color.Color;

import android.view.KeyEvent;

import com.edu.pinochleclient.GameActivity;
import com.edu.pinochleclient.SceneManager;

public class SelectGameScene extends AbstractScene {
	
	private float mTouchOffsetY = 0;
	private float mTouchY = 0;
	private float mMaxY = GameActivity.CH/2;
	private float mMinY = GameActivity.CH-200;
	
	
	private Text loadingText;
	private Font f;
	
	@Override
	public void loadResources() {
		f = FontFactory.createFromAsset(engine.getFontManager(), engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR, activity.getAssets(), "fontfolder/Geeza Pro Bold.ttf", 100f, true, Color.WHITE_ABGR_PACKED_INT);
		f.load();
	}

	@Override
	public void create() {
		this.camera.setCenter(activity.CW/2, activity.CH/2);
		this.setBackground(new Background(Color.BLUE));
		
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent event) {
				if(event.isActionDown())
					mTouchY = event.getY();
				else if(event.isActionMove()) {
					float newY = event.getY();
					mTouchOffsetY = newY - mTouchY;
					float newScrollY = SelectGameScene.this.camera.getCenterY() - mTouchOffsetY;
					if(newScrollY < mMaxY && newScrollY > (mMinY+activity.CH/2)) {
						SelectGameScene.this.camera.setCenter(activity.CW/2, newScrollY);
						mTouchY = newY;
					}
				}
				return true;
			}
		});
		
		initRooms();
	}
	
	public void initRooms() {
		for(int i = 0; i < 8; i++) {
			loadingText = new Text(GameActivity.CW/2, mMinY, f, "Room : " + (i+1),vbom);
			this.attachChild(loadingText);
			mMinY-=200;
		}
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
