package com.project.entities;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table_music")
public class Music implements Serializable {
	private static final long serialVersionUID = 1L;
	@Expose
	@DatabaseField(id = true, canBeNull = false)
	private String name;

	@Expose
	@DatabaseField(canBeNull = true)
	private String artist;

	@DatabaseField(canBeNull = true)
	private String path;

	public Music() {
	}

	public Music(String name, String path, String artist) {
		super();
		this.name = name;
		this.path = path;
		this.artist = artist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
