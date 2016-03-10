package com.project.util;

import java.net.HttpURLConnection;

import com.project.dao.DeviceDAO;
import com.project.dao.UserDAO;
import com.project.entities.Device;
import com.project.entities.User;
import com.project.http.UserHttpClient;

import android.content.Context;

public class UserUtil {
	Context context;

	public UserUtil(Context context) {
		this.context = context;
	}

	public String register(User user) {
		// 1. save to server.
		int code = new UserHttpClient().save2Server(user);
		if (code == HttpURLConnection.HTTP_OK) {
			// 2. save to phone.
			String res = new UserDAO(context).save2Phone(user);
			return res;
		} else if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
			return "EXISTED";
		}
		return "ERROR";
	}

	/**
	 * login
	 * 
	 * @param name
	 *            user's name
	 * @param password
	 *            user's password
	 * @return
	 */
	public String login(String name, String password) {
		UserDAO userDAO = new UserDAO(context);

		// 1. check whether this user saved into the phone.
		User user = userDAO.findFromPhone(name);
		if (user != null) {
			// right, return true; wrong, delete user at phone.
			if (user.getName().equals(name)) {
				if (user.getPassword().equals(password))
					return "RIGHT";
			}
		}

		// 2. find user from server.
		user = new UserHttpClient().findFromServer(name);
		if (user == null) {
			return "NO USER";
		}

		if (user.getName() != null) {
			if (user.getName().equals(name) && user.getPassword().equals(password)) {
				userDAO.save2Phone(user);
				// 3. save devices in the phone.
				for (Device d : user.getDevices()) {
					// In ORMLite, it is way to save device.
					// save device, then update device.
					new DeviceDAO(context).save2Phone(d);

					d.setUser(user);
					new DeviceDAO(context).update2Phone(d);
				}
				return "RIGHT";
			}
		}
		return "ERROR";
	}

	public String update(String key, Object value) {
		String name = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE)
				.getString("username", null);

		int code = new UserHttpClient().update2Server(name, key, value);
		if (code == HttpURLConnection.HTTP_OK) {
			new UserDAO(context).updateToPhone(name, key, value);
			return "RIGHT";
		} else {
			return "WRONG";
		}
	}
}
