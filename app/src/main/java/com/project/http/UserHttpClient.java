package com.project.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.entities.User;

public class UserHttpClient extends AbstractMyHttpClient implements IHttpClient<User> {

	@Override
	public int save2Server(User o) {
		return sendRequest("registerUser.do", o);
	}

	/**
	 * find user registed in server by user's name;
	 * 
	 * @param param
	 *            user's name
	 * @return user
	 */
	@Override
	public User findFromServer(String param) {
		Map<String, String> map = new HashMap<>();

		map.put("name", param);
		HttpURLConnection conn = getconnection("findUser.do", map);

		try {

			InputStream is = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int len = 0;
			byte[] buffer = new byte[4 * 1024];

			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			is.close();

			String str = new String(baos.toByteArray());
			User user = gson.fromJson(str, User.class);

			return user;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<User> findAllFromServer(String param) {
		return null;
	}

	@Override
	public int update2Server(User o) {
		return 0;
	}

	/**
	 * update a user's attribtue to the server whose name is o.
	 * 
	 * @param o
	 *            user's name
	 * @param key
	 *            attribute that will be update.
	 * @param value
	 *            new value of this attribute.
	 */
	@Override
	public int update2Server(String o, String key, Object value) {
		Map<String, String> map = new HashMap<>();

		map.put("name", o);
		try {
			if (key.equals("device")) {
				Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd HH:mm:ss")
						.excludeFieldsWithoutExposeAnnotation().create();

				String json = gson.toJson(value);

				HttpURLConnection conn = getconnection("updateUser.do", map);

				conn.setRequestProperty("device", json);
				int responseCode = conn.getResponseCode();

				return responseCode;
			} else if (key.equals("portrait")) {
				byte[] bytes = (byte[]) value;

				map.put("portrait", "YES");
				HttpURLConnection conn = getconnection("updateUser.do", map);

				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
				OutputStream os = conn.getOutputStream();

				while ((readSize = dis.read(buffer)) != -1) {
					os.write(buffer, 0, readSize);
				}
				os.flush();
				dis.close();

				return conn.getResponseCode();
			} else {
				HttpURLConnection conn = getconnection("updateUser.do", map);
				conn.setRequestProperty(key, (String) value);

				return conn.getResponseCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int deleteFromServer() {
		return 0;
	}

	private int sendRequest(String url, User user) {
		try {
			HttpURLConnection conn = getconnection(url, null);
			conn.addRequestProperty("user", gson.toJson(user));
			return conn.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}
