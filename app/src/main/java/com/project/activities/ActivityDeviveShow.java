package com.project.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.util.LogUtils;
import com.project.R;
import com.project.dao.DeviceDAO;
import com.project.dao.MusicDAO;
import com.project.entities.Device;
import com.project.entities.Music;
import com.project.others.SongListAdapterInDeviceShow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityDeviveShow extends Activity {
	String curDeviceSID;
	SongListAdapterInDeviceShow adapter;
	List<Map<String, Object>> listSongInDevice = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_show);

		// 1. check whether a device's sid has been add to shared preference.
		curDeviceSID = getSharedPreferences("user", MODE_PRIVATE).getString("devicesid", null);
		if (curDeviceSID == null) {
			return;
		}

		// 2. find device whose sid is curDeviceSID.
		Device device = new DeviceDAO(ActivityDeviveShow.this).findFromPhone(curDeviceSID);

		if (device == null) {
			new Exception(" an error happened when find the deivce with the sid saved in the shared preference.");
			return;
		}

		// 3. after finding this device, show device's attributes value.
		TextView txtCurDeviceName = (TextView) findViewById(R.id.id_txt_cur_dvice_name);
		((TextView) findViewById(R.id.id_txt_cur_device_sid)).setText(device.getSid());
		((TextView) findViewById(R.id.txtCurDeviceMemory)).setText(String.valueOf(device.getMemory()));
		((TextView) findViewById(R.id.txtCurDevicePower)).setText(String.valueOf(device.getPower() * 100) + "%");
		((TextView) findViewById(R.id.txtCurDeviceThreshold)).setText(String.valueOf(device.getThreshold()));
		((TextView) findViewById(R.id.txtCurDeviceLastTime)).setText(String.valueOf(device.getLastTime()));

		txtCurDeviceName.setText(device.getName());
		ListView listViewSongSInCurDevice = (ListView) findViewById(R.id.listSongsInCurDevice);

		// 4. show device's songs info which have been upload to the server.
		initSongListOfDevice();
		adapter = new SongListAdapterInDeviceShow(ActivityDeviveShow.this, listSongInDevice);
		listViewSongSInCurDevice.setAdapter(adapter);

		// 5. item long click event is used to delete the song.
		listViewSongSInCurDevice.setOnItemLongClickListener(new SongItemInDeviceLongClickListener());
	}

	/**
	 * initialize the listview which is used to show device'songs.
	 */
	private void initSongListOfDevice() {
		// 1. find all the songs.
		List<Music> list = new MusicDAO(ActivityDeviveShow.this).findAllFromPhone(curDeviceSID);

		// 2. add to the dataset that is used in the listview adapter.
		for (Music m : list) {
			Map<String, Object> map = new HashMap<>();

			// 3. watch! ======I do not add "artist" to the listview.=========
			map.put("name", m.getName());
			map.put("play", getResources().getString(R.string.str_play));

			listSongInDevice.add(map);
		}
	}

	class SongItemInDeviceLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			///////////// =======================///////////////
			LogUtils.d("song item long click in ActivityDeviceShow.");
			return false;
		}

	}
}
