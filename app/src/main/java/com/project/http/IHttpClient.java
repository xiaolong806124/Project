package com.project.http;

import java.util.List;

public interface IHttpClient<T> {
	public T findFromServer(String param);

	public List<T> findAllFromServer(String param);

	public int save2Server(T o);

	public int deleteFromServer();

	public int update2Server(T o);

	public int update2Server(String o, String key, Object value);
}
