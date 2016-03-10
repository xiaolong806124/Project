package com.project.entities;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table_data")
public class Data implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(columnName = "sid")
	private String sid;
	@DatabaseField(columnName = "breath_rate")
	private int breathRate;
	@DatabaseField(columnName = "heart_rate")
	private int heartRate;
	@DatabaseField(columnName = "roll_time")
	private Date rollTime;
	@DatabaseField(columnName = "time")
	private Date time;
	@DatabaseField(columnName = "device", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private Device device;

	public Data() {
	}

	public Data(String sid, Date time) {
		super();
		this.sid = sid;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getBreathRate() {
		return breathRate;
	}

	public void setBreathRate(int breathRate) {
		this.breathRate = breathRate;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public Date getRollTime() {
		return rollTime;
	}

	public void setRollTime(Date rollTime) {
		this.rollTime = rollTime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}