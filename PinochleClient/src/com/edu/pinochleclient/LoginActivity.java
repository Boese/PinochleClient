package com.edu.pinochleclient;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LoginActivity extends Activity{
	private EditText username;
	private EditText password;
	private EditText password2;
	private EditText email;
	private TextView message;
	
	private String state = "login";
	
	private ViewFlipper viewFlipper;

	private Button login;
	private Button createAccount;
	private Button forgotPassword;
	
	private Button createViewButton;
	private Button forgotViewButton;
	private Button createBackButton;
	private Button forgotBackButton;
	
	private ProgressBar spinner;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// Initialize components
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		
		message = (TextView)findViewById(R.id.textView2);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		spinner.setVisibility(View.GONE);
		
		// Load activity into LoginManager & ResourceManager
		LoginManager.getInstance().setLoginActivity(this);
		ResourceManager.getInstance().setLoginActivity(this);
		LoginManager.getInstance().init();
		
		// Set password field to clean
		LoginManager.getInstance().setPassword_dirty(false);
		
		// If user clicks on password field, set dirty to true, erase fake password, set salt to null
		password.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_UP == event.getAction()) {
					password = (EditText)findViewById(R.id.password);
					password.setText("");
	                LoginManager.getInstance().setPassword_dirty(true);
	                LoginManager.getInstance().setStored_salt(null);
	            }
	            return false;
			}
	    });
		
		// Login
		login = (Button)findViewById(R.id.loginbutton);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					LoginManager.getInstance().setUsername(username.getText().toString());
					LoginManager.getInstance().setPassword(password.getText().toString());
					LoginManager.getInstance().login();
			}
		});
		
		// Create Account
		createViewButton = (Button)findViewById(R.id.createbutton);
		createViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(password.getText().toString().equalsIgnoreCase(password2.getText().toString())) {
					LoginManager.getInstance().setUsername(username.getText().toString());
					LoginManager.getInstance().setPassword(password.getText().toString());
					LoginManager.getInstance().setEmail(email.getText().toString());
					LoginManager.getInstance().createAccount();
				}
				else
					setMessage("Password's don't match");
			}
		});
		
		// Forgot Password
		forgotViewButton = (Button)findViewById(R.id.forgotbutton);
		forgotViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginManager.getInstance().setUsername(username.getText().toString());
				LoginManager.getInstance().setEmail(email.getText().toString());
				LoginManager.getInstance().forgotPassword();
			}
		});
		
		// Switch to create account screen
		createAccount = (Button)findViewById(R.id.createaccountbutton);
		createAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewFlipper.showNext();
				state = "create";
				createBackButton = (Button)findViewById(R.id.createbackbutton);
				createBackButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewFlipper.showPrevious();
						state = "login";
					}
				});
				username = (EditText)findViewById(R.id.usernameC);
				password = (EditText)findViewById(R.id.passwordC);
				password2 = (EditText)findViewById(R.id.passwordC2);
				email = (EditText)findViewById(R.id.emailC);
			}
		});
		
		// switch to forgot password screen
		forgotPassword = (Button)findViewById(R.id.forgotpasswordbutton);
		forgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewFlipper.showNext();
				viewFlipper.showNext();
				state = "forgot";
				forgotBackButton = (Button)findViewById(R.id.forgotbackbutton);
				forgotBackButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						viewFlipper.showPrevious();
						viewFlipper.showPrevious();
						state = "login";
					}
				});
				username = (EditText)findViewById(R.id.usernameF);
				email = (EditText)findViewById(R.id.emailF);
				username.setText(LoginManager.getInstance().getUsername());
				email.setText(LoginManager.getInstance().getEmail());
			}
		});
	}
	// load credentials
	public void updateCredentials() {
		if(LoginManager.getInstance().getUsername() != null) {
			if(LoginManager.getInstance().getUsername().length() > 1) {
				password.setText("password");
				username.setText(LoginManager.getInstance().getUsername());
			}
		}
	}
	
	// update message and stop spinner
	public void updateMessage(final String message) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				loadSpinner(false);
				setMessage(message);
			}
		}, 800);
	}
	
	// update message
	public void setMessage(final String message) {
		final TextView t = this.message;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				t.setText(message);
			}
		});
		
	}
	
	// set spinner to spin or stop
	public void loadSpinner(final Boolean on) {
		final ProgressBar s = this.spinner;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(on)
					s.setVisibility(View.VISIBLE);
				else
					s.setVisibility(View.GONE);
			}
		});
	}
	
	// handle back button
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			switch(state) {
			case "login": 
			    ResourceManager.getInstance().activity.finish();
				finish();
				break;
			case "create":
				createBackButton.performClick();
				break;
			case "forgot":
				forgotBackButton.performClick();
				break;
			}
		}
		return true;
	}
	
	@Override
	public void finish() {
	    super.finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}