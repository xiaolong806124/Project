package com.project.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.project.entities.Device;
import com.project.entities.Device_Music;
import com.project.entities.Music;

import android.content.Context;

public class MusicDAO implements IDao<Music> {
	Context context;
	DatabaseHelper helper;

	public MusicDAO(Context context) {
		super();
		this.context = context;
		helper = DatabaseHelper.getHelper(context);
	}

	public void save2Device(Device device, String name, String path, String artist) {
		Music music = new Music(name, path, artist);
		save2Device(device, music);
	}

	/**
	 * save this song to the device in phone.
	 * 
	 * @param music
	 */
	public void save2Device(Device device, Music music) {
		// care! this music has been saved in the table_music;
		// 1.find this music from table_music, and device from table_deivce
		// whose sid has been saved;
		Music m = new MusicDAO(context).findFromPhone(music.getName());

		if (m == null) {
			save2Phone(music);
		}

		// 2. just combine this music and device by the tble_device_music;
		Device_Music dm = new Device_Music();
		dm.setDevice(device);
		dm.setMusic(music);

		try {
			helper.getDao(Device_Music.class).create(dm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String save2Phone(Music o) {
		try {
			List<Music> list = helper.getDao(Music.class).queryBuilder().where().eq("name", o.getName()).query();

			if (list.size() == 1) {
				// 1. if music with the same name, we will update it.
				helper.getDao(Music.class).update(o);
				return "UPDATE";
			} else if (list.size() == 0) {
				// 2. if not, we will save this music.
				helper.getDao(Music.class).create(o);
				return "CREATE";
			}

			return "ERROR";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	/**
	 * find music saved in the phone, but not in device.
	 * 
	 * @param param
	 *            music name
	 * @return if this music is not existed in phone, return null
	 */
	@Override
	public Music findFromPhone(Object param) {
		Music m = null;
		try {
			m = helper.getDao(Music.class).queryBuilder().where().eq("name", param).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * find all songs saved in the phone, but not in device.
	 * 
	 * 
	 * @return return all songs saved in the phone.
	 */
	@Override
	public List<Music> findAllFromPhone(Object param) {

		if (param != null) {
			return findAllMusicInDevice(param);
		} else {
			List<Music> list = new ArrayList<>();
			try {
				list = helper.getDao(Music.class).queryForAll();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
		}
	}

	@Override
	public String update2Phone(Music o) {
		return null;
	}

	/**
	 * delete music saved in the phone with the same name.We distinguish whether
	 * this music is saved in device.
	 * 
	 * @param param
	 *            music's name
	 */
	@Override
	public String deleteInPhone(Object param) {
		try {
			Device_Music queryForFirst = helper.getDao(Device_Music.class).queryBuilder().where()
					.eq("music_name", param).queryForFirst();

			// 1. If this music has saved in the device, we could delete it
			// directly, just set "path" null.
			if (queryForFirst != null && queryForFirst.getMusic().getName().equals(param)) {
				Music query = helper.getDao(Music.class).queryBuilder().where().eq("name", param).queryForFirst();
				query.setPath(null);
				helper.getDao(Music.class).update(query);

				return "SUCCESS";
			} else {
				// 2. If this music is not saved int the device, we delete it
				// directly.
				DeleteBuilder<Music, ?> deleteBuilder = helper.getDao(Music.class).deleteBuilder();
				deleteBuilder.where().eq("name", param);
				deleteBuilder.delete();

				return "SUCCESS";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	/**
	 * find all the songs saved int the dvice.
	 * 
	 * @param sid
	 *            device's sid.
	 * @return
	 */
	private List<Music> findAllMusicInDevice(Object sid) {
		List<Music> list = new ArrayList<>();
		List<Device_Music> query = new ArrayList<>();
		try {
			// 1. find all the relation between this device and music, which
			// saved in table_device_music.
			query = helper.getDao(Device_Music.class).queryBuilder().where().eq("device_sid", sid).query();

			QueryBuilder<Music, ?> queryBuilder = helper.getDao(Music.class).queryBuilder();

			// 2. save all the songs
			for (Device_Music dm : query) {
				// 1. find
				Music queryForFirst = queryBuilder.where().eq("name", dm.getMusic().getName()).queryForFirst();

				// 2.save
				list.add(queryForFirst);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}
