package com.project.others;

import java.util.List;
import java.util.Map;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceListAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	List<Map<String, Object>> listItems;

	public DeviceListAdapter(Context context, List<Map<String, Object>> listItems) {
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
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		final TextView txtName;
		final TextView txtSID;

		Map<String, Object> map = listItems.get(arg0);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_device_in_device_switch, null);
		}
		txtName = (TextView) convertView.findViewById(R.id.id_txt_device_name);
		txtSID = (TextView) convertView.findViewById(R.id.id_txt_device_sid);

		final String name = (String) map.get("name");
		final String sid = (String) map.get("sid");
		txtName.setText(name);
		txtSID.setText(sid);
		
		return convertView;
	}
}
