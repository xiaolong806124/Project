package com.project.activities;

import org.achartengine.GraphicalView;

import com.project.R;
import com.project.chart.DayTimeChartFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentDayData extends Fragment {
	View lyDaydata;
	LinearLayout lyDayDataChart;
	GraphicalView dayChartView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		lyDaydata = (LinearLayout) inflater.inflate(R.layout.tab_daydata, container, false);

		return lyDaydata;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 1. get the layout used to place the graphical view.
		lyDayDataChart = (LinearLayout) lyDaydata.findViewById(R.id.id_ly_day_data_chart);

		// 2. build the graphical view that show data gathered one day.
		dayChartView = new DayTimeChartFactory().buildChart(getContext());

		// 3. place the graphical view.
		lyDayDataChart.addView(dayChartView);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// this is important.
		lyDayDataChart.removeAllViews();
	}
}
