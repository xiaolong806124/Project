package com.project.chart;

import org.achartengine.GraphicalView;

import android.content.Context;

public interface IChartFactory {
	public GraphicalView buildChart(Context context);
}
