package com.project.activities;

import com.project.R;
import com.project.others.Internet;
import com.project.others.MyHandler;
import com.project.util.UserUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends Activity {
	EditText editUserName, editPassword;
	static Button btnLogin;
	static String userName, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// 1.if "auto" is true saved in shared preference, go to MainActivity
		// directly.
		boolean auto = getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean("auto", false);
		if (auto) {
			Intent intent = new Intent();
			intent.setClass(ActivityLogin.this, ActivityMain.class);
			startActivity(intent);
			finish();
		}

		// 2.controllers in this view.
		btnLogin = (Button) findViewById(R.id.id_btn_login);
		editUserName = (EditText) findViewById(R.id.id_edit_username_in_login);
		editPassword = (EditText) findViewById(R.id.id_edit_password_in_login);

		// 4.add onclick event to EditText txtRegister.
		((TextView) findViewById(R.id.id_txt_register)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityLogin.this, ActivityRegister.class);
				startActivity(intent);
				finish();
			}
		});

		// 5.add onclick event to Button btnLogin.
		btnLogin.setOnClickListener(new LoginButtonClick());

		// 6. add click event to TextView forget_password.
		((TextView) findViewById(R.id.id_txt_forget_password)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ActivityLogin.this, "please add processing to this controller", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		// enable Button btnLogin.
		MyHandler.sendMyMessage(ActivityLogin.this, 3, btnLogin, true);
	}

	private void login() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 1. call function login, and process the result.
				String result = new UserUtil(ActivityLogin.this).login(userName, password);

				if (result.equals("NO USER")) {
					MyHandler.sendMyMessage(ActivityLogin.this, 1, btnLogin, true);
				} else if (result.equals("ERROR")) {
					MyHandler.sendMyMessage(ActivityLogin.this, 1, btnLogin, true);
				} else {
					// 1. this user will login automatically next time.
					Editor edit = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
					edit.putBoolean("auto", true);
					edit.putString("username", userName);
					edit.commit();

					// 2. go to MainActivity.
					Intent intent = new Intent();
					intent.setClass(ActivityLogin.this, ActivityMain.class);
					startActivity(intent);
					finish();
				}
			}
		}).start();
	}

	/**
	 * 
	 * @author zwl
	 *
	 */
	class LoginButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get the value of name and password.
			userName = editUserName.getText().toString();
			password = editPassword.getText().toString();

			// 1. check whether network is working.
			if (!Internet.isConnectedToNetwork(ActivityLogin.this)) {
				MyHandler.sendMyMessage(ActivityLogin.this, 0, btnLogin, true);
			}

			// 2. disable Button btnlogin, and close the key board.
			MyHandler.sendMyMessage(ActivityLogin.this, 3, btnLogin, false);

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			// 3.name and password can not be empty.
			if (!userName.trim().isEmpty() && !password.trim().isEmpty()) {
				login();
			}
			return;
		}
	}
}
