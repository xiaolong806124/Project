package com.project.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.entities.Device;
import com.project.entities.User;

import android.content.Context;

public class DeviceDAO implements IDao<Device> {

	Context context;
	DatabaseHelper helper;

	public DeviceDAO(Context context) {
		super();
		this.context = context;
		helper = DatabaseHelper.getHelper(context);
	}

	@Override
	public String save2Phone(Device o) {
		try {
			helper.getDao(Device.class).create(o);
			return "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	/**
	 * find device from using device's sid.
	 * 
	 * @param param
	 *            device's sid
	 */
	@Override
	public Device findFromPhone(Object param) {
		try {
			Device device = helper.getDao(Device.class).queryBuilder().where().eq("sid", param).queryForFirst();
			return device;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Device> findAllFromPhone(Object param) {

		User user = new UserDAO(context).findFromPhone(param);
		if (user == null)
			return null;

		List<Device> list = new ArrayList<>();
		for (Device d : user.getDevices()) {
			list.add(d);
		}
		return list;
	}

	@Override
	public String update2Phone(Device o) {
		try {
			helper.getDao(Device.class).update(o);
			return "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	@Override
	public String deleteInPhone(Object param) {
		return null;
	}

}
