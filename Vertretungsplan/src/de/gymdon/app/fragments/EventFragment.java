package de.gymdon.app.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.gymdon.app.R;
import de.gymdon.app.activities.MainActivity;
import de.gymdon.app.api.API;
import de.gymdon.app.api.Event;

public class EventFragment extends Fragment implements ActionBar.TabListener {

	View rootView;
	MainActivity parentActivity;
	PlanPagerAdapter planPagerAdapter;
	ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentActivity = (MainActivity) getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = parentActivity.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_plan, container, false);

		planPagerAdapter = new PlanPagerAdapter(getChildFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) rootView.findViewById(R.id.plan_pager);
		viewPager.setAdapter(planPagerAdapter);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						parentActivity.getSupportActionBar()
								.setSelectedNavigationItem(position);
					}
				});

		ActionBar actionBar = parentActivity.getSupportActionBar();
		actionBar.removeAllTabs();
		for (int i = 0; i < planPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(planPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		return rootView;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding
		// page in
		// the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class PlanPagerAdapter extends FragmentPagerAdapter {

		private List<Long> startsOfWeek;

		public PlanPagerAdapter(FragmentManager fm) {
			super(fm);
			Collections.sort(API.DATA.events);
			startsOfWeek = new ArrayList<Long>();
			long weekTimestamp = -604800L;
			for (Event event : API.DATA.events) {
				if (event.timestamp >= weekTimestamp + 604800L) {
					weekTimestamp = getStartOfWeek(event.timestamp);
					startsOfWeek.add(weekTimestamp);
				}
			}
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new EventPageFragment();
			Bundle args = new Bundle();
			args.putLong(EventPageFragment.TIMESTAMP_WEEK_BEGIN,
					startsOfWeek.get(position));
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return startsOfWeek.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.",
					Locale.getDefault());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startsOfWeek.get(position) * 1000L);
			String part1 = sdf.format(calendar.getTime());
			calendar.setTimeInMillis((startsOfWeek.get(position) + 604700L) * 1000L);
			String part2 = sdf.format(calendar.getTime());
			return part1 + " - " + part2;
		}
	}

	public static long getStartOfWeek(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp * 1000L);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() / 1000L;
	}

}
