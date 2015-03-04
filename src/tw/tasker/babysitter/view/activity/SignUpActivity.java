package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.activity.BabysitterActivity.MyPagerAdapter;
import tw.tasker.babysitter.view.fragment.BabysitterCommentFragment;
import tw.tasker.babysitter.view.fragment.BabysitterFragment;
import tw.tasker.babysitter.view.fragment.ParentsSignUpFragment;
import tw.tasker.babysitter.view.fragment.SitterSignUpFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.EditText;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Activity which displays a login screen to the user.
 */
public class SignUpActivity extends ActionBarActivity {
	// UI references.
//	private EditText usernameView;
//	private EditText passwordView;
//	private EditText passwordAgainView;
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

		//getActionBar().setDisplayHomeAsUpEnabled(true);

//		setContentView(R.layout.activity_signup);
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//
//		// Set up the signup form.
//		usernameView = (EditText) findViewById(R.id.username);
//		passwordView = (EditText) findViewById(R.id.password);
//		passwordAgainView = (EditText) findViewById(R.id.passwordAgain);
//
//		// Set up the submit button click handler
//		findViewById(R.id.action_button).setOnClickListener(
//				new View.OnClickListener() {
//					public void onClick(View view) {
//
//						// Validate the sign up data
//						boolean validationError = false;
//						StringBuilder validationErrorMessage = new StringBuilder(
//								getResources().getString(R.string.error_intro));
//						if (isEmpty(usernameView)) {
//							validationError = true;
//							validationErrorMessage.append(getResources()
//									.getString(R.string.error_blank_username));
//						}
//						if (isEmpty(passwordView)) {
//							if (validationError) {
//								validationErrorMessage.append(getResources()
//										.getString(R.string.error_join));
//							}
//							validationError = true;
//							validationErrorMessage.append(getResources()
//									.getString(R.string.error_blank_password));
//						}
//						if (!isMatching(passwordView, passwordAgainView)) {
//							if (validationError) {
//								validationErrorMessage.append(getResources()
//										.getString(R.string.error_join));
//							}
//							validationError = true;
//							validationErrorMessage
//									.append(getResources()
//											.getString(
//													R.string.error_mismatched_passwords));
//						}
//						validationErrorMessage.append(getResources().getString(
//								R.string.error_end));
//
//						// If there is a validation error, display the error
//						if (validationError) {
//							Toast.makeText(SignUpActivity.this,
//									validationErrorMessage.toString(),
//									Toast.LENGTH_LONG).show();
//							return;
//						}
//
//						// Set up a progress dialog
//						final ProgressDialog dlg = new ProgressDialog(
//								SignUpActivity.this);
//						dlg.setTitle("註冊中");
//						dlg.setMessage("請稍候...");
//						dlg.show();
//
//						// Set up a new Parse user
//						ParseUser user = new ParseUser();
//						user.setUsername(usernameView.getText().toString());
//						user.setPassword(passwordView.getText().toString());
//						// Call the Parse signup method
//						user.signUpInBackground(new SignUpCallback() {
//
//							@Override
//							public void done(ParseException e) {
//								dlg.dismiss();
//								if (e != null) {
//									// Show the error message
//									Toast.makeText(SignUpActivity.this,
//											"註冊錯誤!" /* e.getMessage() */,
//											Toast.LENGTH_LONG).show();
//								} else {
//									// Start an intent for the dispatch activity
//									Intent intent = new Intent(
//											SignUpActivity.this,
//											DispatchActivity.class);
//									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//											| Intent.FLAG_ACTIVITY_NEW_TASK);
//									startActivity(intent);
//								}
//							}
//						});
//					}
//				});
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

	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isMatching(EditText etText1, EditText etText2) {
		if (etText1.getText().toString().equals(etText2.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Parents", "Sitter"};

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
				fragment = ParentsSignUpFragment.newInstance();
				//fragment.setArguments(arguments);
				break;

			case 1:
				fragment = SitterSignUpFragment.newInstance();				
				//fragment.setArguments(arguments);
				break;

			default:
				break;
			}
			
			return fragment;
		}
	}


}
