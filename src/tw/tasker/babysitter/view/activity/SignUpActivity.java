package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.fragment.ChangePhoneFragment;
import tw.tasker.babysitter.view.fragment.CreateAccountFragment;
import tw.tasker.babysitter.view.fragment.SignUpParentFragment;
import tw.tasker.babysitter.view.fragment.SyncDataFragment;
import tw.tasker.babysitter.view.fragment.VerifyCodeFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;

public class SignUpActivity extends BaseActivity {
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private Object inflater;
	private MenuItem mItem;
	private SubMenu mSubMenu;
	private MenuItem mLogoutItem;
	private MenuItem mProfileItem;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		
		inflater.inflate(R.menu.home, menu);

		mItem = menu.findItem(R.id.action_settings);
		mSubMenu = mItem.getSubMenu();
		mLogoutItem = mSubMenu.findItem(R.id.action_logout);
		mProfileItem = mSubMenu.findItem(R.id.action_profile);

		if (ParseUser.getCurrentUser() == null) {
			mLogoutItem.setTitle("登入");
			mProfileItem.setVisible(false);

		} else {
			mLogoutItem.setTitle("登出");
		}
		
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		String uri = "";
		Intent intent;

		switch (id) {
		case android.R.id.home:
			finish();
			break;

		case R.id.action_profile:
			intent = new Intent();
			intent.setClass(this, ProfileActivity.class);
			startActivity(intent);

			break;

		// case R.id.action_google_play:
		// uri = "market://details?id=tw.tasker.babysitter";
		// intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		// startActivity(intent);
		// break;
		case R.id.action_fb:
			uri = "fb://page/765766966779332";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			startActivity(intent);
			break;

		case R.id.action_gmail:
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "soon530@gmail.com" });
			intent.putExtra(Intent.EXTRA_SUBJECT, "Search保母意見回饋");
			// intent.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(intent, "Search保母意見回饋"));
			break;

		case R.id.action_logout:
			if (ParseUser.getCurrentUser() == null) { // 沒有登入
			} else { // 有登入
				// Call the Parse log out method
				ParseUser.logOut();
			}

			intent = new Intent();
			// Start and intent for the dispatch activity
			intent.setClass(this, DispatchActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

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
					if (type == -1)
						mFragmentAtPos1 = VerifyCodeFragment.newInstance(mListener);
					else if (type == 1) {
						mFragmentAtPos1 = ChangePhoneFragment.newInstance();
					}
					//mFragmentAtPos1 = CreateAccountFragment.newInstance();

				
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
					mFragmentAtPos1 = SyncDataFragment.newInstance(mListener);
					// mFragmentAtPos1 = VerifyCodeFragment.newInstance(mListener);
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
			
			if (object instanceof SyncDataFragment
					&& mFragmentAtPos1 instanceof ChangePhoneFragment) {
				return POSITION_NONE;
			}

			if (object instanceof SyncDataFragment
					&& mFragmentAtPos1 instanceof CreateAccountFragment) {
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
