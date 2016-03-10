package com.project.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ActivitySongAdd extends Activity {
	ListView listView;
	List<Map<String, Object>> songList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_add);

		// 1.find controllers in this view.
		((Button) findViewById(R.id.id_btn_scan_auto)).setOnClickListener(new AutoScanClick());
		((Button) findViewById(R.id.id_btn_add_song)).setOnClickListener(new AddClick());
		listView = (ListView) findViewById(R.id.id_list_add_song_result);

		// 2.set choice for listview
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	class AutoScanClick implements OnClickListener {

		@Override
		public void onClick(View paramView) {
			// 1. set columns we want, "title"=song name, "artist", and "path".
			String[] columns = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.DATA };

			// 2. get the cursor by which we could find all the songs saved in
			// the phone.
			Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null, null,
					null);

			if (cursor == null) {
				return;
			}

			// 3.traverse and save all the songs that cursor could find.
			while (cursor.moveToNext()) {
				String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

				Map<String, Object> map = new HashMap<>();

				// 4. add column value we want.
				map.put("name", title);
				map.put("artist", artist);
				map.put("path", path);
				// checkbox's default status is checked.
				map.put("add", true);

				songList.add(map);
			}

			// 5.close this cursor.
			cursor.close();

			// 6. after find all songs, we could add them to the adapter
			SimpleAdapter adapter = new SimpleAdapter(ActivitySongAdd.this, songList, R.layout.item_song_in_song_add,
					new String[] { "add", "name", "artist" },
					new int[] { R.id.id_chb_add, R.id.id_txt_song_name_in_scan, R.id.id_txt_artist_name_in_scan });

			// 7.initialize the listview.
			listView.setAdapter(adapter);
		}
	}

	class AddClick implements OnClickListener {
		@Override
		public void onClick(View paramView) {

			// 1. If there do not have a aong added to ListView,
			// we could not click this button.
			if (listView.getChildCount() == 0) {
				return;
			}

			Intent intent = getIntent();
			Bundle bundle = new Bundle();
			ArrayList list = new ArrayList();
			List<Map<String, Object>> result = new ArrayList<>();

			// 2. get checked items.
			for (int i = 0; i < listView.getChildCount(); i++) {
				View view = listView.getChildAt(i).findViewById(R.id.id_chb_add);

				// 1.find checked item
				if (((CheckBox) view).isChecked()) {
					// 2. add to a container that send to 'FragmentSleepHelper'.
					result.add(songList.get(i));
				}
			}

			// 3. If no item is checked.
			if (songList.size() == 0) {
				return;
			}

			// 4. send to "FragmentSleepHelper".
			list.add(result);
			bundle.putParcelableArrayList("songs", list);
			intent.putExtras(bundle);

			ActivitySongAdd.this.setResult(2, intent);
			finish();
		}
	}
}
