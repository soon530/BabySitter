package tw.tasker.babysitter.view.fragment;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import tw.tasker.babysitter.view.activity.SignUpActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class ParentsSignUpFragment extends Fragment implements OnClickListener {

	public static Fragment newInstance() {
		ParentsSignUpFragment fragment = new ParentsSignUpFragment();
		return fragment;
	}

	private EditText mName;
	private EditText mPassword;
	private EditText mPasswordAgain;
	private Button mSignUp;
	private EditText mParentsName;
	private EditText mParentsAddress;
	private EditText mParents_phone;
	private EditText mKidsAge;
	private EditText mKidsGender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_signup_parents,
				container, false);
		// Set up the signup form.
		mName = (EditText) rootView.findViewById(R.id.username);
		mPassword = (EditText) rootView.findViewById(R.id.password);
		mPasswordAgain = (EditText) rootView.findViewById(R.id.passwordAgain);
		
		// Parent info
		mParentsName = (EditText) rootView.findViewById(R.id.parents_name);
		mParentsAddress = (EditText) rootView.findViewById(R.id.parents_address);
		mParents_phone = (EditText) rootView.findViewById(R.id.parents_phone);
		mKidsAge = (EditText) rootView.findViewById(R.id.kids_age);
		mKidsGender = (EditText) rootView.findViewById(R.id.kids_gender);
		
		//
		mSignUp = (Button) rootView.findViewById(R.id.action_button);
		mSignUp.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {

		if (isAccountOK()) {
			signUpParents();
		}

	}

	private void signUpParents() {
		// Set up a progress dialog
		final ProgressDialog dlg = new ProgressDialog(getActivity());
		dlg.setTitle("註冊中");
		dlg.setMessage("請稍候...");
		dlg.show();

		// Set up a new Parse user
		ParseUser user = new ParseUser();
		user.setUsername(mName.getText().toString());
		user.setPassword(mPassword.getText().toString());
		// Call the Parse signup method
		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				dlg.dismiss();
				if (e != null) {
					// Show the error message
					Toast.makeText(getActivity(), "註冊錯誤!" /* e.getMessage() */,
							Toast.LENGTH_LONG).show();
				} else {
					// Start an intent for the dispatch activity
					LogUtils.LOGD("vic", "user object id" + ParseUser.getCurrentUser().getObjectId());
				
					addUserInfo();
				}
			}
		});
	}

	private void addUserInfo() {
		LogUtils.LOGD("vic", "addUserInfo");

		UserInfo userInfo = new UserInfo();
		//userInfo.setLocation(Config.MY_LOCATION);
		userInfo.setUser(ParseUser.getCurrentUser());
		userInfo.setName(mParentsName.getText().toString());
		userInfo.setAddress(mParentsAddress.getText().toString());
		userInfo.setPhone(mParents_phone.getText().toString());
		userInfo.setKidsAge(mKidsAge.getText().toString());
		userInfo.setKidsGender(mKidsGender.getText().toString());
		
		userInfo.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					goToNextActivity();
				} else {
					LOGD("vic", e.getMessage());
				}
			}
		});
	}

	private void goToNextActivity() {
		Intent intent = new Intent(getActivity(), DispatchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private boolean isAccountOK() {
		// Validate the sign up data
		boolean validationError = false;
		StringBuilder validationErrorMessage = new StringBuilder(getResources()
				.getString(R.string.error_intro));
		if (AccountChecker.isEmpty(mName)) {
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_blank_username));
		}
		if (AccountChecker.isEmpty(mPassword)) {
			if (validationError) {
				validationErrorMessage.append(getResources().getString(
						R.string.error_join));
			}
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_blank_password));
		}
		if (!AccountChecker.isMatching(mPassword, mPasswordAgain)) {
			if (validationError) {
				validationErrorMessage.append(getResources().getString(
						R.string.error_join));
			}
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_mismatched_passwords));
		}
		validationErrorMessage.append(getResources().getString(
				R.string.error_end));

		// If there is a validation error, display the error
		if (validationError) {
			Toast.makeText(getActivity(), validationErrorMessage.toString(),
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}
}
