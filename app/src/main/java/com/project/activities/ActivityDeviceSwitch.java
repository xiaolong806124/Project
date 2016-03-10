package com.project.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.R;
import com.project.dao.DeviceDAO;
import com.project.dao.MusicDAO;
import com.project.dao.UserDAO;
import com.project.entities.Device;
import com.project.entities.Music;
import com.project.entities.User;
import com.project.http.MusicHttpClient;
import com.project.others.DeviceListAdapter;
import com.project.util.DeviceUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ActivityDeviceSwitch extends Activity {
	ListView listDevice;
	DeviceListAdapter adapter;
	List<Map<String, Object>> listItems = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switch_device);

		// 1.find controller
		listDevice = (ListView) findViewById(R.id.id_list_device);
		((Button) findViewById(R.id.id_btn_add_device_by_bluetooth)).setOnClickListener(new AddDeviceClick());

		// 2. initlize the ListView.
		initListView();
	}

	/**
	 * initialize the ListView that show all devices that have been added by
	 * user successfully.
	 */
	private void initListView() {
		// 1. find all the devices.
		String userName = getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null);

		Collection<Device> devices = new DeviceDAO(this).findAllFromPhone(userName);

		for (Device d : devices) {
			Map<String, Object> map = new HashMap<>();

			// these two properties are used to show in ListView.
			map.put("name", d.getName());
			map.put("sid", d.getSid());
			// it is used to distinguish whether device has been saved or is
			// added by blue tooth.
			map.put("saved", true);
			// this property is important.
			map.put("device", d);

			// 2. add wanted attributes' value to the dataset.
			listItems.add(map);
		}

		// 3. initialize the ListView.
		adapter = new DeviceListAdapter(ActivityDeviceSwitch.this, listItems);
		listDevice.setAdapter(adapter);

		// 4.
		listDevice.setOnItemClickListener(new DeviceItemClick());
	}

	class DeviceItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// 1. the index of clicked item in listview.
			final int arg = arg2;
			new Thread(new Runnable() {
				public void run() {
					Map<String, Object> map = listItems.get(arg);

					// 2. get property value of "device" saved in dataset of
					// ListView.
					Device d = (Device) map.get("device");

					// if clicking the same device, we wll return directly.
					String sid = getSharedPreferences("user", Context.MODE_PRIVATE).getString("sid", null);
					if (d.getSid().equals(sid)) {
						return;
					}

					// 3.call different function to process different device.
					boolean flag = new DeviceUtil(ActivityDeviceSwitch.this).addDevice((boolean) map.get("saved"), d);

					// 4. check this device's all kind of data,
					// i.e. music and device data
					if (flag) {
						// 1. find musics from phone,
						List<Music> musicList = new MusicDAO(ActivityDeviceSwitch.this).findAllFromPhone(d.getSid());

						// 2. if it is zero, get music data from server.
						if (musicList.size() == 0) {
							List<Music> musics = new MusicHttpClient().findAllFromServer(d.getSid());

							// 3. We will save in the phone.
							for (Music m : musics) {
								new MusicDAO(ActivityDeviceSwitch.this).save2Device(d, m);
							}
						}
					}

					// 4. save device's sid in shared preference. It is useful.
					Editor edit = getSharedPreferences("user", MODE_PRIVATE).edit();
					edit.putString("devicesid", d.getSid());
					edit.putBoolean("change", true);
					edit.commit();
					
					// 5. finish this activity.
					finish();
				}
			}).start();
		}
	}

	/**
	 * This is just for testing.
	 * 
	 * @author zwl
	 *
	 */
	class AddDeviceClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			String userName = ActivityDeviceSwitch.this.getSharedPreferences("user", Context.MODE_PRIVATE)
					.getString("username", null);

			User user = new UserDAO(ActivityDeviceSwitch.this).findFromPhone(userName);

			Device device = new Device();

			int j = user.getDevices().size() + 1;

			device.setLastTime(new Date());
			device.setMemory(0.97);
			device.setPower(0.98);
			device.setName("F D" + String.valueOf(j));
			device.setSid("d-d-d" + String.valueOf(j));
			device.setThreshold(0.76);
			device.setUser(user);

			Map<String, Object> map = new HashMap<>();

			map.put("name", "F D" + String.valueOf(j));
			map.put("sid", "d-d-d" + String.valueOf(j));
			map.put("saved", false);
			map.put("device", device);
			listItems.add(map);

			//
			adapter.notifyDataSetChanged();
		}
	}
}
