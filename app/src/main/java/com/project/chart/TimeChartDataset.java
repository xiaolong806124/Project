package com.project.chart;

import java.util.Date;
import java.util.List;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

public class TimeChartDataset implements IDataSet {

	/**
	 * XYSeries
	 * 
	 * @param titles
	 * @param xValues
	 * @param yValues
	 * @return
	 */
	public XYMultipleSeriesDataset buildDataset(String[] titles, List<Date[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < titles.length; i++) {
			// here is important. Becaust chart just have two scales but could
			// have more than two series, i > 1 ? 1 : i;
			MyTimeSeries series = new MyTimeSeries(titles[i], i > 1 ? 1 : i);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			for (int j = 0; j < xV.length; j++) {
				series.add(xV[j], yV[j]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 * this class is used to set two seriers in y axies.
	 * 
	 * @author zwl
	 *
	 */
	class MyTimeSeries extends XYSeries {
		private static final long serialVersionUID = 1L;

		public MyTimeSeries(String title, int scaleNumber) {
			super(title, scaleNumber);
		}

		public MyTimeSeries(String title) {
			super(title);
		}

		public synchronized void add(Date x, double y) {
			super.add(x.getTime(), y);
		}

		protected double getPadding() {
			return 1.0D;
		}
	}
}
