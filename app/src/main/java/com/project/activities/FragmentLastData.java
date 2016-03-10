package com.project.activities;

import org.achartengine.GraphicalView;

import com.project.R;
import com.project.chart.LastTimeChartFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentLastData extends Fragment {
	View lyLastData;
	LinearLayout lyLastDataChart;
	GraphicalView lastChartView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// add SocketService to connect the device.


		// 1. build the graphical view that show data gathered real time.
		lastChartView = new LastTimeChartFactory().buildChart(getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		lyLastData = inflater.inflate(R.layout.tab_lastdata, container, false);
		return lyLastData;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 1. get the layout used to place the graphical view.
		lyLastDataChart = (LinearLayout) lyLastData.findViewById(R.id.id_ly_last_data_chart);

		// 2. place graphical view.
		lyLastDataChart.addView(lastChartView);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		lyLastDataChart.removeAllViews();
	}
}
