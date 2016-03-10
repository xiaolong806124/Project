package com.project.chart;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Paint.Align;

public class TimeRenderer extends AbstractChartRenderer {

	final static int yMaxHeart = 120, yMinHeart = 20;
	final static int yMaxBreath = 60, yMinBreath = 0;

	@Override
	public XYMultipleSeriesRenderer buildRenderer(int scales, int[] colors, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = buildRenderer(scales);

		for (int i = 0; i < colors.length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			// width of line that shows data.
			r.setLineWidth(5);
			r.setPointStrokeWidth(8);
			renderer.addSeriesRenderer(r);
		}

		// 0- heart, 1 - breath;!!!!!!
		renderer.setYLabelsColor(0, colors[0]);
		renderer.setYLabelsColor(1, colors[1]);
		renderer.setYLabelsAlign(Align.RIGHT, 0);
		renderer.setYLabelsAlign(Align.LEFT, 1);
		renderer.setYAxisMin(yMinHeart, 0);
		renderer.setYAxisMax(yMaxHeart, 0);
		renderer.setYAxisMin(yMinBreath, 1);
		renderer.setYAxisMax(yMaxBreath, 1);
		renderer.setPointSize(5f);

		// set label number of x, y axis.
		renderer.setXLabels(12);
		renderer.setYLabels(12);

		// size
		renderer.setAxisTitleTextSize(18);
		renderer.setChartTitleTextSize(30);
		renderer.setLabelsTextSize(16);
		renderer.setLegendTextSize(15);

		// title
		renderer.setChartTitle("");
		renderer.setXTitle("");
		renderer.setYTitle("times/s");

		return renderer;
	}

}
