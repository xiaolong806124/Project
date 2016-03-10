package com.project.others;

import com.project.R;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class MyHandler extends Handler {
	private static Context context;

	public MyHandler(Looper arg0) {
		super(arg0);
	}

	/***
	 * send a messsge.
	 * 
	 * @param cont
	 *            the Activity
	 * @param arg1
	 *            the message index. (0-check web, 1- login error, 2-register
	 *            error,3- enable or disable a view).
	 * @param view
	 *            the view (or controller) in activity.
	 * @param arg2
	 *            set this view (controller) is enable or disable.
	 */
	public static void sendMyMessage(Context cont, int arg1, Object view, boolean arg2) {
		context = cont;
		Message msg = new Message();
		msg.arg1 = arg1;
		msg.arg2 = arg2 == true ? 1 : 0;
		msg.obj = view;
		context.getMainLooper();
		new MyHandler(context.getMainLooper()).sendMessage(msg);
	}

	@Override
	public void handleMessage(Message msg) {

		if (msg.arg1 == 0) {// check web
			Toast.makeText(context, context.getResources().getString(R.string.str_check_net), Toast.LENGTH_LONG).show();
			((View) msg.obj).setEnabled(true);
		} else if (msg.arg1 == 1) {
			Toast.makeText(context, context.getResources().getString(R.string.str_login_error), Toast.LENGTH_LONG).show();
			// login result.
			((View) msg.obj).setEnabled(true);
		} else if (msg.arg1 == 2) {
			Toast.makeText(context, context.getResources().getString(R.string.str_register_error), Toast.LENGTH_LONG).show();
			// register result.
			((View) msg.obj).setEnabled(true);
		} else if (msg.arg1 == 3) {
			// set a view.
			((View) msg.obj).setEnabled(msg.arg2 == 0 ? false : true);
		}
	}
}
