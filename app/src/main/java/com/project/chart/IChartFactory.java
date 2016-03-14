package com.project.chart;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;

public interface IChartFactory {
	GraphicalView buildChart(Context context);
	GraphicalView buildChart(Context context, XYMultipleSeriesDataset dataSet);
	GraphicalView buildChart(Context context, XYMultipleSeriesDataset dataSet, XYMultipleSeriesRenderer renderer);
}
