package com.project.others;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class Internet {
	/**
	 * check whether internet is working.
	 */
	public static boolean isConnectedToNetwork(Context context) {
		ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = conman.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		boolean net = conman.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		return (wifi | net) ? true : false;
	}

}
