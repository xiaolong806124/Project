package com.project.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.R;
import com.project.activities.FragmentDayData;
import com.project.activities.FragmentLastData;
import com.project.activities.FragmentSetting;
import com.project.activities.FragmentSleepHelper;
import com.project.activities.FragmentStatistic;

public class Pager implements OnClickListener {
    private Context context;
    private LinearLayout lyTabLastData, lyTabDayData, lyTabStatistic, lyTabSleepHelper, lyTabSetting;
    private ImageView imgLastData, imgDayData, imgStatistic, imgSleepHelper, imgSetting;
    private Fragment fragmentLastData, fragmentDayData, fragmentStatistic, fragmentSleepHelper, fragmentSetting;

    public Pager(Context context) {
        this.context = context;
    }

    /**
     * set page selector.
     */
    public void setPager() {
        initView();
        initEvent();
        setSelect(0);
    }

    private void initEvent() {
        lyTabDayData.setOnClickListener(this);
        lyTabLastData.setOnClickListener(this);
        lyTabStatistic.setOnClickListener(this);
        lyTabSleepHelper.setOnClickListener(this);
        lyTabSetting.setOnClickListener(this);
    }

    private void initView() {
        lyTabLastData = (LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_last_data);
        lyTabDayData = (LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_day_data);
        lyTabStatistic = (LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_statistic);
        lyTabSleepHelper = (LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_sleep_helper);
        lyTabSetting = (LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_setting);

        imgLastData = (ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_last_data);
        imgDayData = (ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_day_data);
        imgStatistic = (ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_statistic);
        imgSleepHelper = (ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_sleep_helper);
        imgSetting = (ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_setting);
    }

    @Override
    public void onClick(View v) {
        resetImages();
        switch (v.getId()) {
            case R.id.id_tab_last_data:
                setSelect(0);
                break;
            case R.id.id_tab_day_data:
                setSelect(1);
                break;
            case R.id.id_tab_statistic:
                setSelect(2);
                break;
            case R.id.id_tab_sleep_helper:
                setSelect(3);
                break;
            case R.id.id_tab_setting:
                setSelect(4);
                break;
            default:
                break;
        }
    }

    private void setSelect(int i) {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch (i) {
            case 0:
                if (fragmentLastData == null) {
                    fragmentLastData = new FragmentLastData();
                    transaction.add(R.id.id_content, fragmentLastData);
                } else {
                    transaction.show(fragmentLastData);
                }
                imgLastData.setImageResource(R.drawable.tab_lastdata_pressed);
                break;
            case 1:
                if (fragmentDayData == null) {
                    fragmentDayData = new FragmentDayData();
                    transaction.add(R.id.id_content, fragmentDayData);
                } else {
                    transaction.show(fragmentDayData);
                }
                imgDayData.setImageResource(R.drawable.tab_daydata_pressed);
                break;
            case 2:
                if (fragmentStatistic == null) {
                    fragmentStatistic = new FragmentStatistic();
                    transaction.add(R.id.id_content, fragmentStatistic);
                } else {
                    transaction.show(fragmentStatistic);
                }
                imgStatistic.setImageResource(R.drawable.tab_statistic_pressed);
                break;
            case 3:
                if (fragmentSleepHelper == null) {
                    fragmentSleepHelper = new FragmentSleepHelper();
                    transaction.add(R.id.id_content, fragmentSleepHelper);
                } else {
                    transaction.show(fragmentSleepHelper);
                }
                imgSleepHelper.setImageResource(R.drawable.tab_sleep_helper_pressed);
                break;
            case 4:
                if (fragmentSetting == null) {
                    fragmentSetting = new FragmentSetting();
                    transaction.add(R.id.id_content, fragmentSetting);
                } else {
                    transaction.show(fragmentSetting);
                }
                imgSetting.setImageResource(R.drawable.tab_setting_pressed);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentLastData != null) {
            transaction.hide(fragmentLastData);
        }
        if (fragmentDayData != null) {
            transaction.hide(fragmentDayData);
        }
        if (fragmentStatistic != null) {
            transaction.hide(fragmentStatistic);
        }
        if (fragmentSleepHelper != null) {
            transaction.hide(fragmentSleepHelper);
        }
        if (fragmentSetting != null) {
            transaction.hide(fragmentSetting);
        }
    }

    private void resetImages() {
        imgLastData.setImageResource(R.drawable.tab_lastdata_normal);
        imgDayData.setImageResource(R.drawable.tab_daydata_normal);
        imgStatistic.setImageResource(R.drawable.tab_statistic_normal);
        imgSleepHelper.setImageResource(R.drawable.tab_sleep_helper_normal);
        imgSetting.setImageResource(R.drawable.tab_setting_normal);
    }

}
