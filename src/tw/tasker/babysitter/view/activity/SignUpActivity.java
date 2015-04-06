package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.fragment.ChangePhoneFragment;
import tw.tasker.babysitter.view.fragment.CreateAccountFragment;
import tw.tasker.babysitter.view.fragment.SignUpParentFragment;
import tw.tasker.babysitter.view.fragment.SyncDataFragment;
import tw.tasker.babysitter.view.fragment.VerifyCodeFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

public class SignUpActivity extends BaseActivity {
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_page);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		// getActionBar().setDisplayShowHomeEnabled(false);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final class Listener implements SignUpListener {
			public void onSwitchToNextFragment(int type) {
				mFragmentManager.beginTransaction().remove(mFragmentAtPos1)
						.commit();

				if (mFragmentAtPos1 instanceof SyncDataFragment) { // Page2
					mFragmentAtPos1 = VerifyCodeFragment.newInstance(mListener);
				} else if (mFragmentAtPos1 instanceof VerifyCodeFragment) { // Page3
					if (type == 0) // confirm
					{
						mFragmentAtPos1 = CreateAccountFragment.newInstance();
					} else if (type == 1) { // change_phone
						mFragmentAtPos1 = ChangePhoneFragment.newInstance();
					}

				} else {
					mFragmentAtPos1 = SyncDataFragment.newInstance(mListener); // Page1
				}

				notifyDataSetChanged();
			}
		}

		private final String[] TITLES = { "父母註冊", "保母註冊" };
		private final FragmentManager mFragmentManager;
		private Fragment mFragmentAtPos1;
		SignUpListener mListener = new Listener();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			mFragmentManager = fm;
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

			switch (position) {
			case 0:
				return SignUpParentFragment.newInstance();

			case 1:
				if (mFragmentAtPos1 == null) {
					// mFragmentAtPos1 = SyncDataFragment.newInstance(mListener);
					mFragmentAtPos1 = VerifyCodeFragment.newInstance(mListener);
					// mFragmentAtPos1 = CreateAccountFragment.newInstance();

				}
				return mFragmentAtPos1;
			}

			return null;
		}

		@Override
		public int getItemPosition(Object object) {
			if (object instanceof SyncDataFragment
					&& mFragmentAtPos1 instanceof VerifyCodeFragment) {
				return POSITION_NONE;
			}
			if (object instanceof VerifyCodeFragment
					&& mFragmentAtPos1 instanceof CreateAccountFragment) {
				return POSITION_NONE;
			}

			if (object instanceof VerifyCodeFragment
					&& mFragmentAtPos1 instanceof ChangePhoneFragment) {
				return POSITION_NONE;
			}

			return POSITION_UNCHANGED;
		}
	}
}
