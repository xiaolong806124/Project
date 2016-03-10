package com.project.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class StatisticBarChartFactory implements IChartFactory {
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;

	@Override
	public GraphicalView buildChart(Context context) {
		int[] colors = new int[] { Color.BLUE, Color.CYAN };

		renderer = new BarRenderer().buildRenderer(1, colors, null);
		
		getDataSet();
		
		GraphicalView view = ChartFactory.getBarChartView(context, dataset, renderer, Type.DEFAULT);

		setBounds(renderer);

		return view;
	}

	private void getDataSet() {
		String[] titles = new String[] { "夜晚", "白天" };

		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 2, 3, 5, 4, 2, 1, 5, 6, 3, 5, 6, 3 });
		values.add(new double[] { 2 + 1, 3 + 1, 5 + 1, 4 + 1, 2 + 1, 1 + 1, 5 + 1, 6 + 1, 4, 5, 12, 4 });
		dataset = new BarDataSet().buildDataset(titles, null, values);
	}

	private void setBounds(XYMultipleSeriesRenderer renderer) {
		// xmin,xmax,ymin,ymax
		renderer.setPanLimits(new double[] { 0, 30, 0, 24 * 30 });
	}

	public XYMultipleSeriesRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(XYMultipleSeriesRenderer renderer) {
		this.renderer = renderer;
	}

	public XYMultipleSeriesDataset getDataset() {
		return dataset;
	}

	public void setDataset(XYMultipleSeriesDataset dataset) {
		this.dataset = dataset;
	}
}
