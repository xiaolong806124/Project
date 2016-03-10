package com.project.util;

import java.net.HttpURLConnection;

import com.project.dao.DeviceDAO;
import com.project.dao.MusicDAO;
import com.project.entities.Device;
import com.project.entities.Music;
import com.project.http.DeviceHttpClient;

import android.content.Context;

public class MusicUtil {
	Context context;

	public MusicUtil(Context context) {
		super();
		this.context = context;
	}

	public void saveMusic(String name, String path, String artist) {
		Music m = new Music(name, path, artist);
		saveMusic(m);
	}

	public void saveMusic(Music music) {
		String sid = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("devicesid", null);
		// 1.
		int code = new DeviceHttpClient().update2Server(sid, "music", music);
		// 2.
		if (code == HttpURLConnection.HTTP_OK) {
			Device device = new DeviceDAO(context).findFromPhone(sid);
			new MusicDAO(context).save2Device(device, music);
		}
	}

	public void deleteMusic(Music music) {
		// 1. delete from server.
		// 2. delete the connection between device and music.
	}
}
