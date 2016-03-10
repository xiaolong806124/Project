package com.project.chart;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Paint.Align;

public class BarRenderer extends AbstractChartRenderer {
	@Override
	public XYMultipleSeriesRenderer buildRenderer(int scales, int[] colors, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = buildRenderer(scales);

		renderer.setAxisTitleTextSize(18);
		renderer.setChartTitleTextSize(30);
		renderer.setLabelsTextSize(16);
		renderer.setLegendTextSize(15);

		for (int i = 0; i < colors.length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}

		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(true);

		renderer.setXLabels(10);
		renderer.setYLabels(12);

		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);

		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false, false);
		renderer.setZoomButtonsVisible(false);

		renderer.setYAxisMin(0);

		// renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		// title
		renderer.setChartTitle("");
		renderer.setXTitle("");
		renderer.setYTitle("times/s");
		return renderer;
	}

}
