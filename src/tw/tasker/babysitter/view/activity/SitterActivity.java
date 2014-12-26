package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.SitterSettingFragment;
import tw.tasker.babysitter.view.fragment.SitterFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;

public class SitterActivity extends ActionBarActivity {
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private int currentColor = Color.parseColor("#FF4343"); //0xFF666666;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		tabs.setIndicatorColor(currentColor);

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "保母資料", "保母設定"};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = SitterFragment.newInstance(position);
				//fragment.setArguments(arguments);
				break;

			case 1:
				fragment = SitterSettingFragment.newInstance();				
				//fragment.setArguments(arguments);
				break;

			default:
				break;
			}
			
			return fragment;
		}
	}

}
