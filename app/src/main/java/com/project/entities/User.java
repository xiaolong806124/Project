package com.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@DatabaseField(unique = true, id = true)
	@Expose(serialize = true)
	private String name;

	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	@Expose(serialize = true)
	private byte[] portrait;

	@DatabaseField
	@Expose(serialize = true)
	private String password;

	@DatabaseField(columnName = "phonenum")
	@Expose(serialize = true)
	private String phoneNum;

	@DatabaseField(columnName = "email")
	@Expose(serialize = true)
	private String eMail;

	@ForeignCollectionField(foreignFieldName = "user", eager = true)
	@Expose(serialize = true)
	Collection<Device> devices = new ArrayList<>();

	public User() {
	}

	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public byte[] getPortrait() {
		return portrait;
	}

	public void setPortrait(byte[] portrait) {
		this.portrait = portrait;
	}

	public Collection<Device> getDevices() {
		return devices;
	}

	public void setDevices(Collection<Device> devices) {
		this.devices = devices;
	}

}
