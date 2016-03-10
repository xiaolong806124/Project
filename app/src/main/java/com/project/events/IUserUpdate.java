package com.project.events;

public interface IUserUpdate {
	public void addListener(IUserListener listener);
	public void notifytion();
}
