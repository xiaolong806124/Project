package com.project.activities;

import java.util.Calendar;
import java.util.Locale;

import org.achartengine.GraphicalView;

import com.project.R;
import com.project.chart.StatisticBarChartFactory;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentStatistic extends Fragment {
	View lyStatistic;
	LinearLayout lyStatisticChart;
	GraphicalView statisticChartView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		lyStatistic = (LinearLayout) inflater.inflate(R.layout.tab_statistic, container, false);

		return lyStatistic;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 1. intialize this fragment view.
		initView();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		//
		lyStatisticChart.removeAllViews();
	}

	public void initView() {
		// 1. get the linear layout that place graphical view in this fragment.
		lyStatisticChart = (LinearLayout) lyStatistic.findViewById(R.id.lyStatisticChart);

		// 2. build the graphical view that show statistical data.
		statisticChartView = new StatisticBarChartFactory().buildChart(getContext());

		// 3.place this graphical view.
		lyStatisticChart.addView(statisticChartView);

		// 4. find all the controllers in this pager.
		final TextView txtDateSelect = (TextView) lyStatistic.findViewById(R.id.id_txt_date_select);
		final Button btnPerDay = (Button) lyStatistic.findViewById(R.id.btnPerDay);
		final Button btnPerWeek = (Button) lyStatistic.findViewById(R.id.btnPerWeek);
		final Button btnPerMonth = (Button) lyStatistic.findViewById(R.id.btnPerMonth);
		final Button btnLast = (Button) lyStatistic.findViewById(R.id.id_btn_last_month);
		final Button btnNext = (Button) lyStatistic.findViewById(R.id.id_btn_next_month);

		// 5. add click listner to some controllers in this fragment.
		txtDateSelect.setOnClickListener(new DateSelectListener(txtDateSelect));
		btnLast.setOnClickListener(new DateSelectChangeListener("LAST", txtDateSelect));
		btnNext.setOnClickListener(new DateSelectChangeListener("NEXT", txtDateSelect));
	}

	/**
	 * click TextView "date select", pop a clalendar, you can select a date and
	 * save to this TextView.
	 * 
	 * @author zwl
	 *
	 */
	class DateSelectListener implements OnClickListener {
		TextView txtDateSelect;

		public DateSelectListener(TextView txtDateSelect) {
			this.txtDateSelect = txtDateSelect;
		}

		@Override
		public void onClick(View paramView) {
			final Calendar c = Calendar.getInstance();
			DatePickerDialog dialog = new DatePickerDialog(getContext(), new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// Calendar c = Calendar.getInstance();
					c.set(year, monthOfYear, dayOfMonth);
					txtDateSelect.setText(DateFormat.format("yyyy/MM", c));
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
	}

	/**
	 * listen whether this date must be changed.
	 * 
	 * @author zwl
	 *
	 */
	class DateSelectChangeListener implements OnClickListener {
		String orient;
		TextView txtDateSelect;

		public DateSelectChangeListener(String orient, TextView txtDateSelect) {
			this.orient = orient;
			this.txtDateSelect = txtDateSelect;
		}

		@Override
		public void onClick(View paramView) {
			String[] strs = txtDateSelect.getText().toString().split("/");

			int year = Integer.valueOf(strs[0]);
			int month = Integer.valueOf(strs[1]);

			// I think this is a bug in Java.
			if (orient.equals("LAST")) {
				month -= 2;
			} else if (orient.equals("NEXT")) {
				// month++;
			}

			Calendar c = Calendar.getInstance(Locale.CHINESE);
			c.set(year, month, 1);
			txtDateSelect.setText(DateFormat.format("yyyy/MM", c));
		}
	}
}
