package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.AccountChecker;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class LogInActivity extends BaseActivity implements OnTouchListener,
		OnClickListener {

	private LinearLayout mAllScreen;

	private EditText mAccoutn;
	private EditText mPassword;

	private Button mLogIn;
	private Button mLater;
	private Button mSignUp;

	private StringBuilder mValidationErrorMessage;

	private boolean mIsExist = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);

		// Set up the login form.
		mAccoutn = (EditText) findViewById(R.id.account);
		mPassword = (EditText) findViewById(R.id.password);
		// mAccoutn.setOnFocusChangeListener(this);

		mAllScreen = (LinearLayout) findViewById(R.id.all_screen);
		mAllScreen.setOnTouchListener(this);

		mLogIn = (Button) findViewById(R.id.log_in);

		// Set up the submit button click handler
		mLogIn.setOnClickListener(this);
		mSignUp = (Button) findViewById(R.id.sign_up);
		mSignUp.setOnClickListener(this);

		mLater = (Button) findViewById(R.id.later);
		mLater.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		case R.id.sign_up:
			startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
			break;

		case R.id.later:
			startActivity(new Intent(LogInActivity.this, HomeActivity.class));
			break;

		case R.id.log_in:
			// If there is a validation error, display the error
			if (isValidationError()) {
				Toast.makeText(LogInActivity.this,
						mValidationErrorMessage.toString(), Toast.LENGTH_LONG)
						.show();
				return;
			}

			if (mIsExist) {
				runLogin();
			} else {
				runSignup();
			}
			break;

		default:
			break;
		}
	}

	protected boolean isValidationError() {
		// Validate the log in data
		boolean validationError = false;
		mValidationErrorMessage = new StringBuilder(getResources().getString(
				R.string.error_intro));
		if (AccountChecker.isEmpty(mAccoutn)) {
			validationError = true;
			mValidationErrorMessage.append(getResources().getString(
					R.string.error_blank_username));
		}
		if (AccountChecker.isEmpty(mPassword)) {
			if (validationError) {
				mValidationErrorMessage.append(getResources().getString(
						R.string.error_join));
			}
			validationError = true;
			mValidationErrorMessage.append(getResources().getString(
					R.string.error_blank_password));
		}
		mValidationErrorMessage.append(getResources().getString(
				R.string.error_end));
		return validationError;
	}

	private void runLogin() {
		// Set up a progress dialog
		final ProgressDialog dlg = new ProgressDialog(LogInActivity.this);
		dlg.setTitle("登入中");
		dlg.setMessage("請稍候...");
		dlg.show();

		String userName = mAccoutn.getText().toString();
		String password = mPassword.getText().toString();

		// Call the Parse login method
		ParseUser.logInInBackground(userName, password, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException e) {
				dlg.dismiss();
				if (isSuccess(e)) {
					logInSuccess();
				} else {
					logInFail();
				}
			}
		});
	}

	private boolean isSuccess(ParseException e) {
		if (e == null) {
			return true;
		} else {
			return false;
		}
	}

	private void logInSuccess() {
		// Start an intent for the dispatch
		// activity
		Intent intent = new Intent(LogInActivity.this, DispatchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	private void logInFail() {
		// Show the error message
		Toast.makeText(LogInActivity.this, "登入錯誤!" /*
													 * e. getMessage ()
													 */, Toast.LENGTH_LONG)
				.show();
	}

	private void runSignup() {
		// Set up a progress dialog
		final ProgressDialog dlg = new ProgressDialog(LogInActivity.this);
		dlg.setTitle("註冊中");
		dlg.setMessage("請稍候...");
		dlg.show();

		// Set up a new Parse user
		ParseUser user = new ParseUser();
		user.setUsername(mAccoutn.getText().toString());
		user.setPassword(mPassword.getText().toString());
		// Call the Parse signup method
		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				dlg.dismiss();
				if (e != null) {
					// Show the error message
					Toast.makeText(LogInActivity.this, "註冊錯誤!" /*
																 * e. getMessage
																 * ()
																 */,
							Toast.LENGTH_LONG).show();
				} else {
					// Start an intent for the dispatch activity
					Intent intent = new Intent(LogInActivity.this,
							DispatchActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		return false;
	}

}