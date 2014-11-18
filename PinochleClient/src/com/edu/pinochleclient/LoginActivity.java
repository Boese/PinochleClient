package com.edu.pinochleclient;

import java.util.Timer;
import java.util.TimerTask;

import org.andengine.util.debug.Debug;
import org.mindrot.jbcrypt.BCrypt;

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

import com.edu.message.LoginPacket;

public class LoginActivity extends Activity{
	private EditText username;
	private EditText password;
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
	private Boolean passworddirty = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		email = (EditText)findViewById(R.id.emailC);
		message = (TextView)findViewById(R.id.textView2);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		spinner.setVisibility(View.GONE);
		
		username.setText(ResourceManager.getInstance().getUsername());
		password.setText(ResourceManager.getInstance().getHashed_password());
		email.setText(ResourceManager.getInstance().getEmail());
		
		password.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_UP == event.getAction()) {
					Debug.i("password touched");
					password = (EditText)findViewById(R.id.password);
					password.setText("");
	                passworddirty = true;
	            }
	            return false;
			}
	    });
		
		ResourceManager.getInstance().init(this);
		
		login = (Button)findViewById(R.id.loginbutton);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginPacket loginPacket = new LoginPacket("login",username.getText().toString());
				String encryptedpassword = encryptPassword(password.getText().toString());
				if(encryptedpassword != null) {
					loadSpinner(true);
					loginPacket.setHash_password(encryptedpassword);
	        		ResourceManager.getInstance().login(loginPacket);
				} else {
					setMessage("Password must be at least 8 characters");
				}
			}
		});
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
				email = (EditText)findViewById(R.id.emailC);
			}
		});
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
				username.setText(ResourceManager.getInstance().getUsername());
				email.setText(ResourceManager.getInstance().getEmail());
			}
		});
		createViewButton = (Button)findViewById(R.id.createbutton);
		createViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginPacket loginPacket = new LoginPacket("create_account",username.getText().toString());
				loginPacket.setEmail(email.getText().toString());
				if(password.getText().toString().length() < 8) {
					setMessage("Password must be at least 8 characters");
				} else {
					loadSpinner(true);
					loginPacket.setHash_password(BCrypt.hashpw(password.getText().toString(), ResourceManager.getInstance().getSalt()));
					ResourceManager.getInstance().setCreatePacket(loginPacket);
					ResourceManager.getInstance().login(loginPacket);
				}
			}
		});
		forgotViewButton = (Button)findViewById(R.id.forgotbutton);
		forgotViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadSpinner(true);
				LoginPacket loginPacket = new LoginPacket("forgot_password",username.getText().toString());
				loginPacket.setEmail(email.getText().toString());
				ResourceManager.getInstance().login(loginPacket);
			}
		});
		
	}
	
	private String encryptPassword(String password) {
		String encryptedpassword = null;
		if(!passworddirty && password.length() >= 8)
			encryptedpassword = BCrypt.hashpw(password, ResourceManager.getInstance().getSalt());
		else if(passworddirty && password.length() >= 8 && ResourceManager.getInstance().getStored_salt() != null) {
			encryptedpassword = BCrypt.hashpw(password, ResourceManager.getInstance().getStored_salt());
			Debug.i("Stored salt : " + ResourceManager.getInstance().getStored_salt());
			Debug.i("encrypted password : " + encryptedpassword);
			encryptedpassword = BCrypt.hashpw(encryptedpassword, ResourceManager.getInstance().getSalt());
		}
		else if(passworddirty && password.length() >= 8 && ResourceManager.getInstance().getStored_salt() == null) {
			encryptedpassword = BCrypt.hashpw(password, ResourceManager.getInstance().getSalt());
			encryptedpassword = BCrypt.hashpw(encryptedpassword, ResourceManager.getInstance().getSalt());
		}
		return encryptedpassword;	
	}
	
	public void updateMessage(final String message) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				loadSpinner(false);
				setMessage(message);
			}
		}, 1000);
	}
	
	public void setMessage(final String message) {
		final TextView t = this.message;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				t.setText(message);
			}
		});
		
	}
	
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			switch(state) {
			case "login": 
				finish();
				ResourceManager.getInstance().activity.finish();
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