package com.project.dao;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.project.entities.Data;
import com.project.entities.Device;
import com.project.entities.Device_Music;
import com.project.entities.Music;
import com.project.entities.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	/**
	 * database name that save all tables needed in this application.
	 */
	final static String dbName = "project.db";

	private DatabaseHelper(Context context) {
		super(context, dbName, null, 1);
	}

	/**
	 * single instance.
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized DatabaseHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (DatabaseHelper.class) {
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			// 1. ceate all tables used in this application.
			TableUtils.createTable(arg1, User.class);
			TableUtils.createTable(arg1, Device.class);
			TableUtils.createTable(arg1, Music.class);
			TableUtils.createTable(arg1, Device_Music.class);
			TableUtils.createTable(arg1, Data.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Device.class, true);
			TableUtils.dropTable(connectionSource, Device_Music.class, true);
			TableUtils.dropTable(connectionSource, Data.class, true);
			TableUtils.dropTable(connectionSource, Music.class, true);
			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static DatabaseHelper instance;

}
