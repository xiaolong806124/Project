package com.project.others;

import java.util.List;
import java.util.Map;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SongListAdapterInDeviceShow extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<Map<String, Object>> listItems;

	public SongListAdapterInDeviceShow(Context context, List<Map<String, Object>> listItems) {
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
		final TextView txtName;
		final Button btnPlay;
		final Map<String, Object> item = listItems.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_song_in_devie_show, null);
		}
		txtName = (TextView) convertView.findViewById(R.id.id_txt_song_name_of_device);
		btnPlay = (Button) convertView.findViewById(R.id.id_btn_play_in_device);
		txtName.setText((String) item.get("name"));
		btnPlay.setText((String) item.get("play"));

		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// new MusicUtil(context).saveMusic((String) item.get("name"),
				// (String) item.get("path"));
				System.out.println("play music in device.");
				Toast.makeText(context, "play in device", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	class SongItem {
		public String name;
		public String upload;
	}
}
