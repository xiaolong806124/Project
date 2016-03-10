package com.project.chart;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public interface IRenderer {
	public XYMultipleSeriesRenderer buildRenderer(int scales, int[] colors, PointStyle[] styles);
}
