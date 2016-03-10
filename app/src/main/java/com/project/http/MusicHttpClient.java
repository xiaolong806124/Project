package com.project.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.project.entities.Music;

public class MusicHttpClient extends AbstractMyHttpClient implements IHttpClient<Music> {

	@Override
	public Music findFromServer(String param) {
		return null;
	}

	/**
	 * find all songs of a device.
	 * 
	 * @param param
	 *            device's sid
	 */
	List<Music> list = new ArrayList<>();
	// It is used for waiting server returning result.
	boolean flag = false;

	@Override
	public List<Music> findAllFromServer(String param) {

		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("sid", param);

		String url = getPath("findMusic.do", null);

		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				List<Music> musics = gson.fromJson(arg0.result, new TypeToken<List<Music>>() {
				}.getType());
				list = musics;
				flag = true;
			}
		});
		while (!flag)
			;
		return list;
	}

	@Override
	public int save2Server(Music o) {

		return 0;
	}

	@Override
	public int deleteFromServer() {
		return 0;
	}

	@Override
	public int update2Server(Music o) {
		return 0;
	}

	@Override
	public int update2Server(String o, String key, Object value) {
		return 0;
	}

}
