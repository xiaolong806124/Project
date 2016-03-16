package com.project.others;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.R;
import com.project.activities.FragmentDayData;
import com.project.activities.FragmentLastData;
import com.project.activities.FragmentSetting;
import com.project.activities.FragmentSleepHelper;
import com.project.activities.FragmentStatistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("InflateParams")
public class Pager {
	private ViewPager mTabPager;
	private Map<Integer, Drawable> mapImages = new HashMap<>();
	private List<ImageView> listImg = new ArrayList<>();
	private List<LinearLayout> listTabs = new ArrayList<>();
	private Context context;

	public Pager(Context context) {
		this.context = context;
	}

	/**
	 * set page selector.
	 */
	@SuppressWarnings("deprecation")
	public void setPager() {

		mTabPager = (ViewPager) ((FragmentActivity) context).findViewById(R.id.id_tabpager);

		// avoid the fragment recreated during app working.
		mTabPager.setOffscreenPageLimit(5);

		// mTabPager.setPageTransformer(true, );
		listTabs.add((LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_last_data));
		listTabs.add((LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_day_data));
		listTabs.add((LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_statistic));
		listTabs.add((LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_sleep_helper));
		listTabs.add((LinearLayout) ((FragmentActivity) context).findViewById(R.id.id_tab_setting));

		// catch for the order added to list. if you change, please change
		// corresponding mapping in class Pager;
		listImg.add((ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_last_data));
		listImg.add((ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_day_data));
		listImg.add((ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_statistic));
		listImg.add((ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_sleep_helper));
		listImg.add((ImageView) ((FragmentActivity) context).findViewById(R.id.id_img_setting));

		// put all of the page views into an array.
		// save all the views of application.
		List<Fragment> fragments = new ArrayList<>();

		fragments.add(new FragmentLastData());
		fragments.add(new FragmentDayData());
		fragments.add(new FragmentStatistic());
		fragments.add(new FragmentSleepHelper());
		fragments.add(new FragmentSetting());

		// get all the pairs of image and tab;
		// 0-lastdata;
		mapImages.put(0, context.getResources().getDrawable(R.drawable.tab_lastdata_normal));
		mapImages.put(1, context.getResources().getDrawable(R.drawable.tab_lastdata_pressed));
		// 1-day data;
		mapImages.put(2, context.getResources().getDrawable(R.drawable.tab_daydata_normal));
		mapImages.put(3, context.getResources().getDrawable(R.drawable.tab_daydata_pressed));
		// 2-statistic
		mapImages.put(4, context.getResources().getDrawable(R.drawable.tab_statistic_normal));
		mapImages.put(5, context.getResources().getDrawable(R.drawable.tab_statistic_pressed));
		// 3-sleep helper
		mapImages.put(6, context.getResources().getDrawable(R.drawable.tab_sleep_helper_normal));
		mapImages.put(7, context.getResources().getDrawable(R.drawable.tab_sleep_helper_pressed));
		// 4-setting
		mapImages.put(8, context.getResources().getDrawable(R.drawable.tab_settings_normal));
		mapImages.put(9, context.getResources().getDrawable(R.drawable.tab_settings_pressed));

		mTabPager.addOnPageChangeListener(new MyOnPageChangeListener());

		for (int i = 0; i < listTabs.size(); i++) {
			listTabs.get(i).setOnClickListener(new MyOnClickListener(mTabPager, i));
		}
		// set the page adapter.
		MyFragmentPagerAdapter mPagerAdapter = new MyFragmentPagerAdapter(
				((FragmentActivity) context).getSupportFragmentManager(), fragments);

		mTabPager.setAdapter(mPagerAdapter);
	}

	public class MyOnClickListener implements OnClickListener {
		private int index = 0;
		private ViewPager mTabPager;

		public MyOnClickListener(ViewPager mTabPager, int i) {
			index = i;
			this.mTabPager = mTabPager;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	}

	/*
	 * monitor the page switching
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			// change the image of tabs when page is selected.
			listImg.get(arg0).setImageDrawable(mapImages.get(arg0 * 2 + 1));
			for (int i = 0; i < listImg.size(); i++) {
				if (arg0 == i)
					continue;
				listImg.get(i).setImageDrawable(mapImages.get(i * 2));
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
