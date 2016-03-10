package com.project.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table_device_music")
public class Device_Music {

	@DatabaseField(columnName = "device_sid", foreign = true)
	private Device device;
	@DatabaseField(columnName = "music_name", foreign = true)
	private Music music;

	public Device_Music() {
		super();
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

}
