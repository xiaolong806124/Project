package com.project.chart;

import java.util.Date;
import java.util.List;

import org.achartengine.model.XYMultipleSeriesDataset;

public interface IDataSet {
	public XYMultipleSeriesDataset buildDataset(String[] titles, List<Date[]> xValues, List<double[]> yValues);
}
