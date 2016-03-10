package com.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.project.entities.User;

import android.content.Context;

public class UserDAO implements IDao<User> {
	DatabaseHelper helper;
	Context context;

	public UserDAO(Context context) {
		this.context = context;
		helper = DatabaseHelper.getHelper(context);
	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public int updateToPhone(String name, String key, Object value) {
		try {
			UpdateBuilder<User, ?> updateBuilder = helper.getDao(User.class).updateBuilder();
			//
			updateBuilder.updateColumnValue(key, value).where().eq("name", name);

			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Save this user to the phone.
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String save2Phone(User o) {

		try {
			helper.getDao(User.class).create(o);
			return "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	@Override
	public User findFromPhone(Object param) {
		try {
			List<User> users = helper.getDao(User.class).queryBuilder().where().eq("name", param).query();
			if (users.size() == 1) {
				return users.get(0);
			} else if (users.size() > 1) {
				throw new Exception("user number is greater than one, please check database.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<User> findAllFromPhone(Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update2Phone(User o) {
		try {
			helper.getDao(User.class).update(o);
			return "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	/**
	 * delete this user from phone
	 * 
	 * @param param
	 *            user's name
	 */
	@Override
	public String deleteInPhone(Object param) {
		try {
			DeleteBuilder<User, ?> deleteBuilder = helper.getDao(User.class).deleteBuilder();
			deleteBuilder.where().eq("name", param);
			deleteBuilder.delete();
			return "SUCCESS";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

}
