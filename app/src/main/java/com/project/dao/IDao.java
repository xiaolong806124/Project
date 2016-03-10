package com.project.dao;

import java.util.List;

public interface IDao<T> {
	public String save2Phone(T o);

	public T findFromPhone(Object param);

	public List<T> findAllFromPhone(Object param);

	public String update2Phone(T o);

	public String deleteInPhone(Object param);

}
