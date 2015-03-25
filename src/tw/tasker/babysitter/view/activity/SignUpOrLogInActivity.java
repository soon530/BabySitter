package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.AccountChecker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Activity which displays a registration screen to the user.
 */
public class SignUpOrLogInActivity extends Activity {

	private EditText usernameView;
	private EditText passwordView;
	private Button mActionButton;
	private boolean mIsExist =true;
	private LinearLayout mAllScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_or_log_in);
		
		// Set up the login form.
		usernameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
		//usernameView.setOnFocusChangeListener(this);

		mAllScreen = (LinearLayout) findViewById(R.id.all_screen);
		mAllScreen.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
				return false;
			}
		});
		
		mActionButton = (Button) findViewById(R.id.logInButton);

		// Set up the submit button click handler
		mActionButton.setOnClickListener(
				new View.OnClickListener() {

					public void onClick(View view) {
						// Validate the log in data
						boolean validationError = false;
						StringBuilder validationErrorMessage = new StringBuilder(
								getResources().getString(R.string.error_intro));
						if (AccountChecker.isEmpty(usernameView)) {
							validationError = true;
							validationErrorMessage.append(getResources()
									.getString(R.string.error_blank_username));
						}
						if (AccountChecker.isEmpty(passwordView)) {
							if (validationError) {
								validationErrorMessage.append(getResources()
										.getString(R.string.error_join));
							}
							validationError = true;
							validationErrorMessage.append(getResources()
									.getString(R.string.error_blank_password));
						}
						validationErrorMessage.append(getResources().getString(
								R.string.error_end));

						// If there is a validation error, display the error
						if (validationError) {
							Toast.makeText(SignUpOrLogInActivity.this,
									validationErrorMessage.toString(),
									Toast.LENGTH_LONG).show();
							return;
						}

						
						if (mIsExist) {
							runLogin();
						} else { 
							runSignup();
						}
					}

					private void runLogin() {
						// Set up a progress dialog
						final ProgressDialog dlg = new ProgressDialog(
								SignUpOrLogInActivity.this);
						dlg.setTitle("登入中");
						dlg.setMessage("請稍候...");
						dlg.show();

						// Call the Parse login method
						ParseUser.logInInBackground(usernameView.getText()
								.toString(), passwordView.getText().toString(),
								new LogInCallback() {

									@Override
									public void done(ParseUser user,
											ParseException e) {
										dlg.dismiss();
										if (e != null) {
											// Show the error message
											Toast.makeText(
													SignUpOrLogInActivity.this,
													"登入錯誤!" /* e.getMessage() */,
													Toast.LENGTH_LONG).show();
										} else {
											// Start an intent for the dispatch
											// activity
											Intent intent = new Intent(
													SignUpOrLogInActivity.this,
													DispatchActivity.class);
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
													| Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(intent);
										}
									}
								});
					}

					private void runSignup() {
						// Set up a progress dialog
						final ProgressDialog dlg = new ProgressDialog(
								SignUpOrLogInActivity.this);
						dlg.setTitle("註冊中");
						dlg.setMessage("請稍候...");
						dlg.show();

						// Set up a new Parse user
						ParseUser user = new ParseUser();
						user.setUsername(usernameView.getText().toString());
						user.setPassword(passwordView.getText().toString());
						// Call the Parse signup method
						user.signUpInBackground(new SignUpCallback() {

							@Override
							public void done(ParseException e) {
								dlg.dismiss();
								if (e != null) {
									// Show the error message
									Toast.makeText(SignUpOrLogInActivity.this,
											"註冊錯誤!" /* e.getMessage() */,
											Toast.LENGTH_LONG).show();
								} else {
									// Start an intent for the dispatch activity
									Intent intent = new Intent(
											SignUpOrLogInActivity.this,
											DispatchActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
											| Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							}
						});

					}
				});

		
		// Log in button click handler
//		((Button) findViewById(R.id.logInButton))
//				.setOnClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						// Starts an intent of the log in activity
//						startActivity(new Intent(SignUpOrLogInActivity.this,
//								LoginActivity.class));
//					}
//				});

		// Sign up button click handler
		((Button) findViewById(R.id.signUpButton))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// Starts an intent for the sign up activity
						startActivity(new Intent(SignUpOrLogInActivity.this,
								SignUpActivity.class));
					}
				});

		// Sign up button click handler
		((Button) findViewById(R.id.go))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// Starts an intent for the sign up activity
						startActivity(new Intent(SignUpOrLogInActivity.this,
								HomeActivity.class));
					}
				});

//		MyPagerAdapter adapter = new MyPagerAdapter();
//		ViewPager myPager = (ViewPager) findViewById(R.id.pager);
//		myPager.setAdapter(adapter);
//		myPager.setCurrentItem(0);
		
//		CirclePageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        mIndicator.setViewPager(myPager);	
     }

	private class MyPagerAdapter extends PagerAdapter {

		public int getCount() {
			return 3;
		}

		public Object instantiateItem(View collection, int position) {

			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.first_page;
				break;
			case 1:
				resId = R.layout.second_page;
				break;
			case 2:
				resId = R.layout.third_page;
				break;
			}

			View view = inflater.inflate(resId, null);

			((ViewPager) collection).addView(view, 0);

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);

		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
