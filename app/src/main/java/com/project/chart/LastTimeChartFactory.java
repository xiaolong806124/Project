package com.project.chart;

import android.content.Context;
import android.graphics.Color;

import com.project.others.DateTransform;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LastTimeChartFactory implements IChartFactory {

    TimeRenderer timeRenderer = new TimeRenderer();

    /**
     * This is a test method because the values of data set is generated in this method.
     *
     * @param context
     * @return
     */
    @Override
    public GraphicalView buildChart(Context context) {
        XYMultipleSeriesRenderer renderer0 = new TimeRenderer().getDefaultRenderer();
        // set value of x axis.
        List<Date[]> xValues = new ArrayList<>();

        DateTransform trans = new DateTransform();

        for (int i = 0; i < timeRenderer.titles.length; i++) {
            Date[] dates = new Date[100];
            for (int j = 0; j < 100; j++) {
                // time zone. 12 hour should be added on the USA time.
                dates[j] = trans.getDate(12 + Calendar.HOUR, Calendar.MINUTE + j);
            }
            xValues.add(dates);
        }
        // set value of y axis.
        List<double[]> yValues = new ArrayList<double[]>();
        for (int i = 0; i < timeRenderer.titles.length; i++) {
            double[] y = new double[100];
            for (int j = 0; j < 100; j++) {
                y[j] = Math.random() * 40 + 40;
            }
            yValues.add(y);
        }

        XYMultipleSeriesDataset dataset = new TimeChartDataset().buildDataSet(timeRenderer.titles, xValues, yValues);

        GraphicalView view = ChartFactory.getTimeChartView(context, dataset, renderer0, "H:mm ");

        view.setBackgroundColor(Color.BLACK);

        setChartBound(dataset, renderer0);
        return view;
    }

    @Override
    public GraphicalView buildChart(Context context, XYMultipleSeriesDataset dataSet) {
        XYMultipleSeriesRenderer renderer1 = timeRenderer.getDefaultRenderer();

        GraphicalView view = ChartFactory.getTimeChartView(context, dataSet, renderer1, "H:mm ");

        view.setBackgroundColor(Color.BLACK);

        setChartBound(dataSet, renderer1);
        return view;
    }

    @Override
    public GraphicalView buildChart(Context context, XYMultipleSeriesDataset dataSet, XYMultipleSeriesRenderer renderer) {
        GraphicalView view = ChartFactory.getTimeChartView(context, dataSet, renderer, "H:mm ");

        view.setBackgroundColor(Color.BLACK);

        setChartBound(dataSet, renderer);
        return view;
    }

    /**
     * repaint the chart when adding new data.
     *
     * @param view
     * @param dataset
     */
    public void addDataPoint(GraphicalView view, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        setChartBound(dataset, renderer);
        view.repaint();
        return;
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

        // only show the data of 10 minutes.
        double xMin = xMax - 10 * 60 * 1000;

        // the moving scale. 24hour
        renderer.setPanLimits(new double[]{xMax - 12 * 60 * 60 * 1000, xMax + 12 * 60 * 60 * 1000, 0, 180});
        renderer.setZoomEnabled(false, false);
        renderer.setPanEnabled(true, false);
        renderer.setXAxisMin(xMin, 0);
        renderer.setXAxisMin(xMin, 1);
        renderer.setXAxisMax(xMax, 0);
        renderer.setXAxisMax(xMax, 1);
        return;
    }
}
