package com.project.util;

import java.net.HttpURLConnection;
import java.util.Collection;

import com.project.dao.DeviceDAO;
import com.project.dao.UserDAO;
import com.project.entities.Device;
import com.project.entities.User;
import com.project.http.UserHttpClient;

import android.content.Context;

public class DeviceUtil {

	Context context;

	public DeviceUtil(Context context) {
		super();
		this.context = context;
	}

	/**
	 * add device to phone
	 * 
	 * @param isSaved
	 *            whether this device has been saved in the phone.
	 * @param device
	 *            selected device
	 * @return
	 */
	public boolean addDevice(boolean isSaved, Device device) {
		if (isSaved) {
			return updateDevice(device);
		} else {
			return addDevice(device);
		}
	}

	private boolean addDevice(Device device) {
		String name = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE)
				.getString("username", null);
		// update user to the server.
		int code = new UserHttpClient().update2Server(name, "device", device);
		// add to the phone.
		if (code == HttpURLConnection.HTTP_OK) {
			User user = new UserDAO(context).findFromPhone(name);

			DeviceDAO dao = new DeviceDAO(context);

			dao.save2Phone(device);
			device.setUser(user);
			dao.update2Phone(device);
			return true;
		}
		return false;
	}

	private boolean updateDevice(Device device) {
		String name = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE)
				.getString("username", null);

		User user = new UserDAO(context).findFromPhone(name);
		Collection<Device> devices = user.getDevices();

		// 1. Check whether current user has saved this device.
		for (Device d : devices) {
			if (d.getName().equals(device.getName()))
				return true;
		}

		// 2. If current user didn't save this device,
		// then update user to the server.
		int code = new UserHttpClient().update2Server(name, "device", device);

		// 3. add this device to user, then update to the phone.
		if (code == HttpURLConnection.HTTP_OK) {
			device.setUser(user);
			new DeviceDAO(context).update2Phone(device);
			return true;
		}
		return false;
	}
}
