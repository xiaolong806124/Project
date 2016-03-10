package com.project.others;

import java.util.List;
import java.util.Map;

import com.project.R;
import com.project.util.MusicUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SongListAdapterInSleepHelper extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<Map<String, Object>> listItems;

	public SongListAdapterInSleepHelper(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Button btnUpload;
		final Map<String, Object> item = listItems.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_song_in_sleep_helper, null);
		}

		btnUpload = (Button) convertView.findViewById(R.id.id_btn_upload_to_device);

		final String name = (String) item.get("name");
		final String artist = (String) item.get("artist");
		final String path = (String) item.get("path");

		((TextView) convertView.findViewById(R.id.id_txt_song_name)).setText(name);
		((TextView) convertView.findViewById(R.id.id_txt_artist_name)).setText(artist);

		btnUpload.setEnabled((boolean) item.get("upload"));

		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String sid = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("devicesid", null);

				// 1. If there did not have added a device, could not upload
				// music to server.
				if (sid == null) {
					Toast.makeText(context, "please select a device", Toast.LENGTH_SHORT).show();
					return;
				}

				// 2. save this music to server and create relationship between
				// music and device.
				new MusicUtil(context).saveMusic(name, path, artist);

				// 3. set this button disable.
				btnUpload.setEnabled(false);
			}
		});
		return convertView;
	}

	class SongItem {
		public String name;
		public String upload;
	}
}
