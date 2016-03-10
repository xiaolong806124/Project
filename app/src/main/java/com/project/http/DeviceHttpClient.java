package com.project.http;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.project.entities.Device;
import com.project.entities.Music;

public class DeviceHttpClient extends AbstractMyHttpClient implements IHttpClient<Device> {

	@Override
	public Device findFromServer(String param) {
		return null;
	}

	/**
	 * find all devices which belong to the same user.
	 * 
	 * @param param
	 *            user's name
	 * @return
	 */
	@Override
	public List<Device> findAllFromServer(String param) {
		List<Device> retList = new ArrayList<>();
		Map<String, String> map = new HashMap<>();

		map.put("name", param);
		HttpURLConnection conn = getconnection("findDevice.do", map);

		try {
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				DataInputStream dis = new DataInputStream(conn.getInputStream());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while ((readSize = dis.read(buffer)) != -1) {
					baos.write(buffer, 0, readSize);
				}
				objStr = new String(baos.toByteArray());

				retList = gson.fromJson(objStr, new TypeToken<List<Device>>() {
				}.getType());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}

	@Override
	public int save2Server(Device o) {
		return sendBytes("addDevice.do", o);
	}

	@Override
	public int deleteFromServer() {
		return 0;
	}

	@Override
	public int update2Server(Device o) {
		return 0;
	}

	/**
	 * update a device's attribtue to the server whose sid is o.
	 * 
	 * @param o
	 *            device's sid
	 * @param key
	 *            attribute that will be update.
	 * @param value
	 *            new value of this attribute.
	 */
	@Override
	public int update2Server(String o, String key, Object value) {
		Map<String, String> map = new HashMap<>();
		map.put("sid", o);

		if (key.equals("music")) {
			Music music = (Music) value;

			String path = music.getPath();
			String type = path.substring(path.indexOf(".") + 1);
			String name = music.getName();
			String artist = music.getArtist();

			File file = new File(music.getPath());
			map.put("name", name);
			map.put("artist", artist);
			map.put("type", type);

			String urlPath = getPath("uploadMusic", null);
			upload(urlPath, file, map);

			return HttpURLConnection.HTTP_OK;
		} else {
			map.put(key, (String) value);
			try {
				HttpURLConnection conn = getconnection("updateDevice", map);
				int code = conn.getResponseCode();
				return code;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	private void upload(String url, File file, Map<String, String> head) {
		// Complete file uploading by XUtils. It's so easy.
		// 1. set some query parameters to the URL.
		RequestParams params = new RequestParams("UTF-8");
		for (String s : head.keySet()) {
			params.addQueryStringParameter(s, head.get(s));
		}

		// 2. set file.
		params.addBodyParameter("file", file);
		HttpUtils httpUtils = new HttpUtils();

		// 3. call function send.
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
			}
		});
	}

	private int sendBytes(String url, Device device) {
		try {
			HttpURLConnection conn = getconnection(url, null);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			byte[] bytes2 = gson.toJson(device).getBytes();

			for (int i = 0; i < (bytes2.length / (1024 * 4) + 1); i++) {
				buffer = Arrays.copyOfRange(bytes2, i * 1024 * 4, (i + 1) * 1024 * 4);
				dos.write(buffer, 0, buffer.length);
			}
			return conn.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
