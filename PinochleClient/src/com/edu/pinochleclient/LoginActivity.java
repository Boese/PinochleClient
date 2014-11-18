package com.edu.pinochleclient;

import org.mindrot.jbcrypt.BCrypt;

import com.edu.message.LoginPacket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		email = (EditText)findViewById(R.id.emailC);
		message = (TextView)findViewById(R.id.textView2);
		
		username.setText("");
		password.setText("");
		email.setText("");
		
		ResourceManager.getInstance().init(this);
		
		login = (Button)findViewById(R.id.loginbutton);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginPacket loginPacket = new LoginPacket("login",username.getText().toString());
				loginPacket.setHash_password(BCrypt.hashpw(password.getText().toString(), ResourceManager.getInstance().getSalt()));
        		ResourceManager.getInstance().login(loginPacket);
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
			}
		});
		createViewButton = (Button)findViewById(R.id.createbutton);
		createViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginPacket loginPacket = new LoginPacket("create_account",username.getText().toString());
				loginPacket.setEmail(email.getText().toString());
				loginPacket.setHash_password(BCrypt.hashpw(password.getText().toString(), ResourceManager.getInstance().getSalt()));
				ResourceManager.getInstance().login(loginPacket);
			}
		});
		forgotViewButton = (Button)findViewById(R.id.forgotbutton);
		forgotViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginPacket loginPacket = new LoginPacket("forgot_password",username.getText().toString());
				loginPacket.setEmail(email.getText().toString());
				ResourceManager.getInstance().login(loginPacket);
			}
		});
		
	}
	
	public void updateMessage(final String message) {
		final TextView t = this.message;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				t.setText(message);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			switch(state) {
			case "login": 
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
	    ResourceManager.getInstance().activity.finish();
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