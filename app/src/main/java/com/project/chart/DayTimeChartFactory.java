package com.project.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.project.others.DateTransform;

import android.content.Context;
import android.graphics.Color;

public class DayTimeChartFactory implements IChartFactory {
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;

	@Override
	public GraphicalView buildChart(Context context) {

		// set the default color and style of line.
		int[] colors = new int[] { Color.GREEN, Color.BLUE, Color.RED };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT, PointStyle.CIRCLE };

		renderer = new TimeRenderer().buildRenderer(2, colors, styles);

		String[] titles = new String[] { "心跳速率", "呼吸速率", "翻身时刻" };
		// set value of x axis.
		List<Date[]> xValues = new ArrayList<>();
		DateTransform trans = new DateTransform();
		for (int i = 0; i < titles.length; i++) {
			Date[] dates = new Date[100];
			for (int j = 0; j < 100; j++) {
				// time zone. 12 hour should be added on the USA time.
				dates[j] = trans.getDate(12 + Calendar.HOUR, Calendar.MINUTE + j);
			}
			xValues.add(dates);
		}
		// set value of y axis.
		List<double[]> yValues = new ArrayList<double[]>();
		for (int i = 0; i < titles.length; i++) {
			double[] y = new double[100];
			for (int j = 0; j < 100; j++) {
				y[j] = Math.random() * 40 + 40;
			}
			yValues.add(y);
		}

		dataset = new TimeChartDataset().buildDataset(titles, xValues, yValues);

		GraphicalView view = ChartFactory.getTimeChartView(context, dataset, renderer, "H:mm ");

		view.setBackgroundColor(Color.BLACK);

		setChartBound(dataset, renderer);
		return view;
	}

	/**
	 * set showing style for chart, such as range, scale.
	 * 
	 * @param dataset
	 * @param renderer
	 */
	private void setChartBound(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		XYSeries xySeries = dataset.getSeries()[0];

		// in order to show the right of chart clearly.
		double xMax = xySeries.getX(xySeries.getItemCount() - 1);

		// only show the data of 30 minutes.
		double xMin = xMax - 30 * 60 * 1000;

		// the moving scale. 24hour
		renderer.setPanLimits(new double[] { xMax - 12 * 60 * 60 * 1000, xMax + 12 * 60 * 60 * 1000, 0, 180 });
		renderer.setZoomEnabled(false, false);
		renderer.setPanEnabled(true, false);
		renderer.setXAxisMin(xMin, 0);
		renderer.setXAxisMin(xMin, 1);
		renderer.setXAxisMax(xMax, 0);
		renderer.setXAxisMax(xMax, 1);
		return;
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
