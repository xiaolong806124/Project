package com.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table_device")
public class Device implements Serializable {
	private static final long serialVersionUID = 1L;

	@DatabaseField(columnName = "name", unique = true)
	@Expose(serialize = true)
	private String name;

	@DatabaseField(columnName = "sid", unique = true, id = true)
	@Expose(serialize = true)
	private String sid;

	@DatabaseField(columnName = "memory")
	@Expose(serialize = true)
	private double memory;

	@DatabaseField(columnName = "power")
	@Expose(serialize = true)
	private double power;

	@DatabaseField(columnName = "threshold")
	@Expose(serialize = true)
	private double threshold;

	@DatabaseField(columnName = "last_time")
	@Expose(serialize = true)
	private Date lastTime;

	@DatabaseField(columnName = "user", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private User user;

	@ForeignCollectionField(columnName = "data", foreignFieldName = "device", orderColumnName = "sid")
	private Collection<Data> datas = new ArrayList<>();

	public Device() {
	}

	public Device(String sid, Date lastTime) {
		super();
		this.sid = sid;
		this.lastTime = lastTime;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public Collection<Data> getDatas() {
		return datas;
	}

	public void setDatas(Collection<Data> datas) {
		this.datas = datas;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
