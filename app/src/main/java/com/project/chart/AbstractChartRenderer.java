package com.project.chart;

import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;

public abstract class AbstractChartRenderer implements IRenderer {

	/**
	 * create renderer for last data chart. (line style), i is scales' number.
	 */
	public XYMultipleSeriesRenderer buildRenderer(int scales) {
		// set two scales in a chart.
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(scales);

		setRendererDefault(renderer);

		for (int i = 0; i < renderer.getSeriesRendererCount(); i++) {
			XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
			r.setFillPoints(true);
		}
		return renderer;
	}

	/**
	 * set some default setting for my charts.
	 * 
	 * @param renderer
	 */
	private void setRendererDefault(XYMultipleSeriesRenderer renderer) {
		renderer.setShowGrid(false);

		// size between chart and parent's top, left, bottom, right.
		renderer.setMargins(new int[] { 18, 45, 20, 10 });

		// the length between lengend and the bottom of its parent controller.
		renderer.setLegendHeight(50);

		renderer.setMarginsColor(Color.rgb(147, 60, 55));
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.WHITE);
	}
}
