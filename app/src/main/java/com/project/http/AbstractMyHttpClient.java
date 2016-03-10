package com.project.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.application.MyApplication;

public abstract class AbstractMyHttpClient {

	// static String basePath = "http://" + MyApplication.ipAddress +
	// ":8080/Server";
	byte[] buffer = new byte[1024 * 4];
	int readSize = 0;
	String objStr = "";
	Gson gson;

	public AbstractMyHttpClient() {
		gson = new GsonBuilder().setDateFormat("yyyy-mm-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();
//		gson = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").excludeFieldsWithoutExposeAnnotation().create();
	}

	protected String getPath(String path, Map<String, String> params) {
		String retPath = MyApplication.basePath + "/" + path + "?";
		if (params != null) {
			for (String s : params.keySet()) {
				String val = params.get(s);
				retPath += s + "=" + val + "&";
			}
		}
		// delete a "&";
		retPath = retPath.substring(0, retPath.length() - 1);
		return retPath;
	}

	protected HttpURLConnection getconnection(String path, Map<String, String> params) {

		try {
			URL url = new URL(getPath(path, params));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(1000 * 30);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("Accept", "text/plain");
			conn.setRequestProperty("Connection", "Keep-Alive");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
