package com.project.activities;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.util.MathHelper;

import com.project.R;
import com.project.application.MyApplication;
import com.project.chart.LastTimeChartFactory;
import com.project.chart.TimeChartDataset;
import com.project.chart.TimeRenderer;
import com.project.data_process.Command;
import com.project.others.DateTransform;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentLastData extends Fragment {
    String TAG = "FragmentLastData";
    View lyLastData;
    private LastDataHandler handler = new LastDataHandler();
    private Timer timer = new Timer();
    LinearLayout lyLastDataChart;
    LastTimeChartFactory lastTimeChartFactory = new LastTimeChartFactory();
    XYMultipleSeriesDataset lastDataSet;
    XYMultipleSeriesRenderer lastRender;
    GraphicalView lastChartView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeRenderer timeRenderer = new TimeRenderer();

        lastRender = timeRenderer.getDefaultRenderer();

        lastDataSet = new TimeChartDataset().buildDataSet(timeRenderer.titles);
        // 1. build the graphical view that show data gathered real time.
        lastChartView = lastTimeChartFactory.buildChart(getContext(), lastDataSet, lastRender);

        sendCommand(0);
        // start a timer which send a command every 10 second.
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                sendCommand(0);
            }
        }, new Date(), 3 * 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lyLastData = inflater.inflate(R.layout.tab_lastdata, container, false);
        return lyLastData;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 1. get the layout used to place the graphical view.
        lyLastDataChart = (LinearLayout) lyLastData.findViewById(R.id.id_ly_last_data_chart);

        // 2. place graphical view.
        lyLastDataChart.addView(lastChartView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lyLastDataChart.removeAllViews();
        timer.cancel();
    }


    /**
     * send command to server for requesting monitor data.
     *
     * @param index the index of data packages.
     */
    private void sendCommand(int index) {
        // the service should not be a null, otherwise, throw a 'null pointer'
        // error.
        if (MyApplication.socketService != null) {
            // set handler for the service.
            MyApplication.socketService.getInputThread().setHandler(handler);
            // get the command package.
            byte[] info = Command.sendLastDataRequest((byte) index);
            MyApplication.socketService.getOutThread().setInfo(info);
        }
    }

    /**
     * LastData Handler
     *
     * @author zwl
     */
    class LastDataHandler extends Handler {
        DateTransform trans = new DateTransform();

        @Override
        public void handleMessage(Message msg) {

            byte[] info = (byte[]) msg.obj;
            int[] data = new int[3];
            int hour = info[4];
            int minute = info[5];
            data[0] = info[6];
            data[1] = info[7];
            data[2] = info[8];
            double x = Calendar.getInstance().getTimeInMillis();
            lastDataSet.getSeries()[0].add(x, data[0]);
            lastDataSet.getSeries()[1].add(x, data[1]);
            Log.i(TAG, String.valueOf(data[2]));
            lastDataSet.getSeries()[2].add(x, data[2] == 0 ? MathHelper.NULL_VALUE : (data[2] - 0.5));
            // add data to the chart.
            lastTimeChartFactory.addDataPoint(lastChartView, lastDataSet, lastRender);
        }
    }
}
