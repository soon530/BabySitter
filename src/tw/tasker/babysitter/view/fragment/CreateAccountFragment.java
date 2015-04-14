package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.BuildConfig;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.activity.DispatchActivity;
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

public class CreateAccountFragment extends Fragment implements OnClickListener {

	public static Fragment newInstance() {
		Fragment fragment = new CreateAccountFragment();
		return fragment;
	}

	private EditText mName;
	private EditText mPassword;
	private EditText mPasswordAgain;
	private Button mCreate;

	public CreateAccountFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_account_create,
				container, false);

		// Set up the signup form.
		mName = (EditText) rootView.findViewById(R.id.username);
		mPassword = (EditText) rootView.findViewById(R.id.password);
		mPasswordAgain = (EditText) rootView.findViewById(R.id.passwordAgain);

		mCreate = (Button) rootView.findViewById(R.id.create);
		mCreate.setOnClickListener(this);

		if (BuildConfig.DEBUG)
			loadTestData();

		return rootView;
	}

	private void loadTestData() {
		mName.setText("vic");
		mPassword.setText("vic");
		mPasswordAgain.setText("vic");

	}

	@Override
	public void onClick(View v) {
		if (isAccountOK()) {
			signUpSitter();
		}
	}

	private void signUpSitter() {
		// Set up a progress dialog
		final ProgressDialog dlg = new ProgressDialog(getActivity());
		dlg.setTitle("註冊中");
		dlg.setMessage("請稍候...");
		dlg.show();

		// Set up a new Parse user
		ParseUser user = new ParseUser();
		user.setUsername(mName.getText().toString());
		user.setPassword(mPassword.getText().toString());
		user.put("userType", "sitter");

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
					LogUtils.LOGD("vic", "user object id"
							+ ParseUser.getCurrentUser().getObjectId());

					addSitterInfo();
				}
			}
		});

	}

	private void addSitterInfo() {
		Babysitter sitterInfo = Config.sitterInfo;
		//Sitter sitter = new Sitter();
		sitterInfo.setUser(ParseUser.getCurrentUser());
		
		//sitter.setName(sitterInfo.getName());

		//sitter.setTel(sitterInfo.getTel());
		//sitter.setAddress(sitterInfo.getAddress());
		//sitter.setBabycareCount(sitterInfo.getBabycareCount());
		//sitter.setBabycareTime(sitterInfo.getBabycareTime());
		
		//sitter.setSkillNumber(sitterInfo.getSkillNumber());
		//sitter.setEducation(sitterInfo.getEducation());
		//sitter.setCommunityName(sitterInfo.getCommunityName());
		
		//sitter.setImageUrl(sitterInfo.getImageUrl());
		
		//sitterInfo.setIsVerify(false);

		sitterInfo.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					goToNextActivity();
				} else {
					LogUtils.LOGD("vic", e.getMessage());
					Toast.makeText(getActivity(),
							"註冊失敗" /* e.getMessage() */, Toast.LENGTH_LONG)
							.show();

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
