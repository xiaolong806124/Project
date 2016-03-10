package com.project.activities;

import com.project.R;
import com.project.entities.User;
import com.project.others.MyHandler;
import com.project.util.UserUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityRegister extends Activity {
	EditText editUsername, editPassword, editRepassword, editPhoneNumber, editAuthCode, editEmail;
	Button btnRegister, btnGetAuthCode;
	String userName, password, rePassword, phoneNum, authCode, email, sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// 1. Button
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnGetAuthCode = (Button) findViewById(R.id.btnGetAuthCode);

		// 2.EditText
		editUsername = (EditText) findViewById(R.id.id_edit_username_in_register);
		editPassword = (EditText) findViewById(R.id.id_edit_password_in_register);
		editRepassword = (EditText) findViewById(R.id.id_edit_repassword);
		editPhoneNumber = (EditText) findViewById(R.id.id_edit_phonenum);
		editAuthCode = (EditText) findViewById(R.id.id_edit_auth_code);
		editEmail = (EditText) findViewById(R.id.id_edit_email);

		// 4.add onclick event for Button Register.
		btnRegister.setOnClickListener(new RegisterButtonClick());

		// 5.add onclick event for Button GetAuthCode.
		btnGetAuthCode.setOnClickListener(new GetAuthCodeButtonClick());
	}

	class GetAuthCodeButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			authCode = editAuthCode.getText().toString();
			Toast.makeText(ActivityRegister.this, "please add this function", Toast.LENGTH_SHORT).show();
		}
	}

	class RegisterButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			userName = editUsername.getText().toString();
			password = editPassword.getText().toString();
			rePassword = editRepassword.getText().toString();

			// 1. name and password can not be null.
			if (userName.trim().isEmpty() || password.trim().isEmpty() || rePassword.trim().isEmpty())
				return;

			// 2.password and repassword should be equal.
			if (!password.equals(rePassword)) {
				Toast.makeText(ActivityRegister.this, getResources().getString(R.string.str_check_password),
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 3.save this new user.
			new Thread(new RegisterThread()).start();
		}
	}

	class RegisterThread implements Runnable {

		@Override
		public void run() {
			phoneNum = editPhoneNumber.getText().toString();
			email = editEmail.getText().toString();

			// 1. set this user.
			User user = new User(userName, password);
			user.setPhoneNum(phoneNum);
			user.seteMail(email);

			// 2.unable button, avoid being clicked twice.
			MyHandler.sendMyMessage(ActivityRegister.this, 3, btnRegister, false);

			// 3. call function register, and process the result.
			String res = new UserUtil(ActivityRegister.this).register(user);
			if (res.equals("SUCCESS")) {
				// 4. this user will login automatically next time.
				Editor edit = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
				edit.putBoolean("auto", true);
				edit.putString("username", userName);
				edit.commit();

				// 5. register successfully, turn to MainActivity.
				Intent intent = new Intent();
				intent.setClass(ActivityRegister.this, ActivityMain.class);
				ActivityRegister.this.startActivity(intent);
				finish();
			} else if (res.equals("EXISTED")) {
				// 6. this user's name has been used.
				MyHandler.sendMyMessage(ActivityRegister.this, 2, btnRegister, true);
			} else {
				// 7. If occurs some errors, enable button.
				MyHandler.sendMyMessage(ActivityRegister.this, 2, btnRegister, true);
			}
		}
	}
}
