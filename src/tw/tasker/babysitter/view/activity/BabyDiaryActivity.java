package tw.tasker.babysitter.view.activity;

import com.astuetz.PagerSlidingTabStrip;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.activity.MyFavoriteActivity.MyPagerAdapter;
import tw.tasker.babysitter.view.fragment.BabyDiaryFragment;
import tw.tasker.babysitter.view.fragment.BabyFavoriteFragment;
import tw.tasker.babysitter.view.fragment.BabysitterFavoriteFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;

public class BabyDiaryActivity extends ActionBarActivity {

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private int currentColor = Color.parseColor("#33B4E4"); //0xFF666666;
	private Bundle arguments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);
		
		setContentView(R.layout.activity_page);
		if (savedInstanceState == null) {
			
			arguments = new Bundle();
			arguments.putString(
					Config.BABYSITTER_OBJECT_ID,
					getIntent().getStringExtra(
							Config.BABYSITTER_OBJECT_ID));
		}

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		tabs.setIndicatorColor(currentColor);

	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "所有寶寶", "我的寶寶"};

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
				fragment = BabyDiaryFragment.newInstance(position);
				fragment.setArguments(arguments);
				break;

			case 1:
				fragment = BabyDiaryFragment.newInstance(position);				
				fragment.setArguments(arguments);
				break;

			default:
				break;
			}
			
			
/*			BabyDiaryFragment fragment = new BabyDiaryFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
*/
			
			return fragment;
			//return null;
		}

	}

}
