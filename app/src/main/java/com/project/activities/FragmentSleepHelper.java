package com.project.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.lidroid.xutils.util.LogUtils;
import com.project.R;
import com.project.dao.MusicDAO;
import com.project.entities.Music;
import com.project.others.SongListAdapterInSleepHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

enum MEDIA_OPTION {
	START, PAUSE, STOP, RESTART
};

public class FragmentSleepHelper extends Fragment {

	View lySleepHelper;
	ListView listViewSong;
	Button btnPlay, btnStop;
	List<Map<String, Object>> listSongs = new ArrayList<>();
	MediaPlayer player;
	String songPath = null, songName = null;
	static SongListAdapterInSleepHelper adapter;
	int curPostion;
	boolean ispaused = false;
	SeekBar processBar;
	Timer timer = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		lySleepHelper = inflater.inflate(R.layout.tab_sleephelper, container, false);

		// If adding menu to this fragment. you must write this sentence.
		setHasOptionsMenu(true);
		return lySleepHelper;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listViewSong = (ListView) lySleepHelper.findViewById(R.id.id_list_song);
		btnPlay = (Button) lySleepHelper.findViewById(R.id.id_btn_play_in_helper);
		btnStop = (Button) lySleepHelper.findViewById(R.id.id_btn_stop);
		processBar = (SeekBar) lySleepHelper.findViewById(R.id.id_seek_bar_of_song);
		// init this fragment.
		initView();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 1. check wether this view needs to update.
		boolean ischangeed = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getBoolean("change",
				false);

		if (ischangeed) {
			String sid = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("devicesid", null);

			// 2. update this view
			setSongDataWithDevice(sid);

			// 3. set "change" false. It is very important.
			Editor edit = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
			edit.putBoolean("change", false);
			edit.commit();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 1. cancl this timer.
		if (timer != null)
			timer.cancel();

		// 2. if player is playing, stop it.
		if (player.isPlaying()) {
			player.stop();
		}

		// 3. release all resources.
		player.release();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemAutoScan:
			// 1. turn to SongAddActivity.
			Intent intent = new Intent(getActivity(), ActivitySongAdd.class);
			startActivityForResult(intent, Activity.RESULT_FIRST_USER);
			// 2. add this fragment to the back stack.
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_song_add, menu);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Bundle bundle = data.getExtras();

			// 1. get songs from bundle.
			List<Map<String, Object>> songs = (List<Map<String, Object>>) bundle.getParcelableArrayList("songs").get(0);

			for (Map<String, ?> song : songs) {
				//
				String name = (String) song.get("name");
				String artist = (String) song.get("artist");
				String path = (String) song.get("path");

				Map<String, Object> map = new HashMap<>();
				map.put("name", name);
				map.put("artist", artist);
				map.put("path", path);

				Music music = new Music(name, path, artist);

				// 1. If this music have saved in device and phone, button
				// "upload" is
				// disable.
				if (isSavedInDeviceAndPhone(music)) {
					map.put("upload", false);
					continue;
				}

				// 2. save this song to phone. Here, we should discuss whether
				// it has saved in device.
				String resStr = new MusicDAO(getContext()).save2Phone(music);

				// "UPDATE" means this song existes in device, but not in phone.
				// So, button "upload" is disable.
				if (resStr.equals("UPDATE")) {
					map.put("upload", false);
				} else if (resStr.equals("CREATE")) {
					// "CREATE" means this song did not exists in device,
					// so, this button could be clicked.
					map.put("upload", true);
				}

				listSongs.add(map);
			}

			// notify adapter that data is changed.
			adapter.notifyDataSetChanged();
		}
	}

	private boolean isSavedInDeviceAndPhone(Music m) {
		// 1. find all songs in the phone.
		List<Music> songInPhone = new MusicDAO(getContext()).findAllFromPhone(null);

		boolean flag = false;
		for (Music mu : songInPhone) {
			if (mu.getName().equals(m.getName()) && mu.getArtist().equals(m.getArtist())) {
				// 2. If this song is existed and song's path isn't null.
				// It means device had this song, but phone did not save.
				if (mu.getPath() != null) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	private void initPlayer() {
		ispaused = false;
		btnPlay.setTag("play");
		btnPlay.setText(R.string.str_play);
		btnStop.setEnabled(false);
		processBar.setProgress(0);
	}

	/**
	 * initialize this fragment.
	 */
	private void initView() {
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		initPlayer();

		String sid = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("devicesid", null);
		if (sid == null) {
			setSongDataWithoutDeivce();
		} else {
			setSongDataWithDevice(sid);
		}
		adapter = new SongListAdapterInSleepHelper(getContext(), listSongs);
		// set listview adapter.
		listViewSong.setAdapter(adapter);

		// item long click.
		listViewSong.setOnItemLongClickListener(new SongItemLongClickitem());

		// only one item could be selected.
		listViewSong.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// set item click event on items in listview.
		listViewSong.setOnItemClickListener(new ListItemSongClick());
		//
		btnPlay.setOnClickListener(new ButtonPlayClick());
		//
		btnStop.setOnClickListener(new ButtonStopClick());
	}

	public void setSongDataWithDevice(String sid) {
		// 1.clear ListView.
		listSongs.clear();

		MusicDAO musicDAO = new MusicDAO(getContext());
		List<Music> findFromDevice = musicDAO.findAllFromPhone(sid);
		List<Music> findAllFromPhone = musicDAO.findAllFromPhone(null);

		for (Music music : findAllFromPhone) {
			File file = new File(music.getPath());

			Map<String, Object> map = new HashMap<>();
			map.put("name", music.getName());
			map.put("path", music.getPath());
			map.put("artist", music.getArtist());

			// 2. we just add songs that are not delete by end-user.
			// if this song is not deleted.
			if (file.exists()) {
				boolean flag = false;
				for (Music music2 : findFromDevice) {
					if (music2.getName().equals(music.getName()) && music2.getArtist().equals(music.getArtist())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					// if device has this music, button "upload" is disable.
					map.put("upload", false);
				} else {
					map.put("upload", true);
				}

				listSongs.add(map);
			} else {
				// 3. delete this music, while discussing whether this
				// music has saved in device.
				new MusicDAO(getContext()).deleteInPhone(music.getName());
			}
		}

		// 4. notify adapter
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private void setSongDataWithoutDeivce() {
		// 1. clear ListView.
		listSongs.clear();

		List<Music> list = new MusicDAO(getContext()).findAllFromPhone(null);

		for (Music m : list) {
			File file = new File(m.getPath());

			// if this song is not deleted.
			if (file.exists()) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", m.getName());
				map.put("path", m.getPath());
				map.put("artist", m.getArtist());
				map.put("upload", true);

				listSongs.add(map);
			} else {
				// if user delete this song, remove it from user's songs list.
				new MusicDAO(getContext()).deleteInPhone(m.getName());
			}
		}
	}

	private void play(MEDIA_OPTION m) {
		try {
			switch (m) {
			case START:
				if (songPath != null) {
					player.reset();
					player.setDataSource(songPath);
					player.prepare();
					player.start();

					btnStop.setEnabled(true);
					timer = new Timer();
					timer.schedule(new MyTimerTask(), new Date(), 5);
				}
				break;
			case PAUSE:
				if (player.isPlaying())
					player.pause();
				break;
			case STOP:
				if (timer != null)
					timer.cancel();
				player.stop();
				break;
			case RESTART:
				player.seekTo(curPostion);
				player.start();
				break;
			}
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			if (player.isPlaying()) {
				curPostion = player.getCurrentPosition();
				int duration = player.getDuration();
				processBar.setProgress((int) (curPostion * 100.0 / duration));
			}
		}
	}

	class ButtonPlayClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (songPath == null)
				return;
			// 2. click "PLAY"
			if (btnPlay.getTag().equals("play")) {
				// 3.If player is paused, we call the "RESTART" operation,
				// otherwise, call "START", change some status.
				if (ispaused) {
					play(MEDIA_OPTION.RESTART);
					ispaused = false;
				} else {
					play(MEDIA_OPTION.START);
				}

				ispaused = false;
				btnPlay.setTag("pause");
				btnPlay.setText(R.string.str_pause);
			} else if (btnPlay.getTag().equals("pause")) {
				// if this player is playing, we could call "PAUSE".
				play(MEDIA_OPTION.PAUSE);

				ispaused = true;
				btnPlay.setTag("play");
				btnPlay.setText(R.string.str_play);
			}
		}
	}

	class ButtonStopClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			play(MEDIA_OPTION.STOP);
			// 4. stop, initialize the player.
			initPlayer();
		}
	}

	class ListItemSongClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			songPath = (String) listSongs.get(arg2).get("path");
			songName = (String) listSongs.get(arg2).get("name");

			// 1. start this music player, and change the responding status.
			play(MEDIA_OPTION.START);

			ispaused = false;
			btnPlay.setText(R.string.str_pause);
			btnPlay.setTag("pause");
		}
	}

	class SongItemLongClickitem implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			//////////////////// ========================////////////////////////
			LogUtils.d("song item long click in FragmentSleepHelper.");
			return false;
		}

	}
}
