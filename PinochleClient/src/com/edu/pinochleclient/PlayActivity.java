package com.edu.pinochleclient;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.adt.color.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PlayActivity extends SimpleLayoutGameActivity{
	
		private EditText username;
		private EditText password;

		private Button login;
		
		public PlayActivity() {}
		
		@Override
		protected int getLayoutID() {
			return R.layout.login;
		}

		@Override
		protected int getRenderSurfaceViewID() {
			return R.id.login_rendersurfaceview;
		}

		@Override
		protected void onSetContentView() {
			super.onSetContentView();
			    
			username = (EditText)findViewById(R.id.username);
			password = (EditText)findViewById(R.id.password);
			login = (Button)findViewById(R.id.loginbutton);
			login.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ResourceManager.getInstance().activity.toastOnUiThread(username.getText() + " " + password.getText());
					finish();
				}
			});
		}

		@Override
		public EngineOptions onCreateEngineOptions() {
			return ResourceManager.getInstance().engine.getEngineOptions();
		}

		@Override
		public void onCreateResources() {
		}

		@Override
		public Scene onCreateScene() {
			return SceneManager.getInstance().getCurrentScene();
		}
	}