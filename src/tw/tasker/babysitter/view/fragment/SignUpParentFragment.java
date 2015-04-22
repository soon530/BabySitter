package tw.tasker.babysitter.view.fragment;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import tw.tasker.babysitter.BuildConfig;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.DisplayUtils;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.activity.DispatchActivity;
import tw.tasker.babysitter.view.activity.SignUpActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpParentFragment extends Fragment implements OnClickListener {

	public static Fragment newInstance() {
		SignUpParentFragment fragment = new SignUpParentFragment();
		return fragment;
	}

	private EditText mName;
	private EditText mPassword;
	private EditText mPasswordAgain;
	private Button mSignUp;
	private EditText mParentsName;
	private EditText mParentsAddress;
	private EditText mParents_phone;
	//private EditText mKidsGender;
	private Spinner mKidsAgeYear;
	private Spinner mKidsAgeMonth;
	private CheckBox mKidsGenderBoy;
	private CheckBox mKidsGenderGirl;
	private ScrollView mAllScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_parent_signup,
				container, false);
		
		mAllScreen = (ScrollView) rootView.findViewById(R.id.all_screen);
		mAllScreen.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				DisplayUtils.hideKeypad(getActivity());
				return false;
			}
		});

		
		// Set up the signup form.
		mName = (EditText) rootView.findViewById(R.id.username);
		mPassword = (EditText) rootView.findViewById(R.id.password);
		mPasswordAgain = (EditText) rootView.findViewById(R.id.passwordAgain);
		
		// Parent info
		mParentsName = (EditText) rootView.findViewById(R.id.parents_name);
		mParentsAddress = (EditText) rootView.findViewById(R.id.parents_address);
		mParents_phone = (EditText) rootView.findViewById(R.id.parents_phone);
		mKidsAgeYear = (Spinner) rootView.findViewById(R.id.kids_age_year);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.kids_age_year, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mKidsAgeYear.setAdapter(adapter);
		mKidsAgeYear.setSelection(getPositionFromYear());
		
		mKidsAgeMonth = (Spinner) rootView.findViewById(R.id.kids_age_month);
		adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.kids_age_month, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mKidsAgeMonth.setAdapter(adapter);
		mKidsAgeMonth.setSelection(getPositionFromMonth());
		
		//mKidsGender = (EditText) rootView.findViewById(R.id.kids_gender);
		mKidsGenderBoy = (CheckBox) rootView.findViewById(R.id.kids_gender_boy);
		mKidsGenderGirl = (CheckBox) rootView.findViewById(R.id.kids_gender_girl);

		mKidsGenderBoy.setOnClickListener(this);
		mKidsGenderGirl.setOnClickListener(this);
		
		//
		mSignUp = (Button) rootView.findViewById(R.id.action_button);
		mSignUp.setOnClickListener(this);
		
		if (BuildConfig.DEBUG)
			loadTestData();

		return rootView;
	}

	
	
	private int getPositionFromYear() {
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
	    String currentYear = String.valueOf((calendar.get(Calendar.YEAR)-1911));
		
		String[] months = getResources().getStringArray(R.array.kids_age_year);
		int position = Arrays.asList(months).indexOf(currentYear);
		
		LogUtils.LOGD("vic", "year: " + position);
		return position;
	}
	
	private int getPositionFromMonth() {
		Calendar calendar=Calendar.getInstance(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM"); 
		String currentMonth = simpleDateFormat.format(calendar.getTime());
		
		String[] months = getResources().getStringArray(R.array.kids_age_month);
		int position = Arrays.asList(months).indexOf(currentMonth);
		
		//LogUtils.LOGD("vic", "month: " + position);
		return position;
	}

	private void loadTestData() {
		mName.setText("vic2");
		mPassword.setText("vic2");
		mPasswordAgain.setText("vic2");
		
		mParentsName.setText("張小誠");
		mParentsAddress.setText("高雄市鳳山區建國路一段31巷37號");
		mParents_phone.setText("0915552673");
		
		//mKidsAgeYear.setText("2015");
		//mKidsAgeMonth.setText("03");
		//mKidsGender.setText("男");
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		switch (id) {
		case R.id.action_button:
			if (isAccountOK()) {
				signUpParents();
			}
			
			break;

		case R.id.kids_gender_boy:
			mKidsGenderGirl.setChecked(false);
			mKidsGenderBoy.setChecked(true);
			break;

		case R.id.kids_gender_girl:
			mKidsGenderBoy.setChecked(false);
			mKidsGenderGirl.setChecked(true);

			break;
		default:
			break;
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
		user.put("userType", "parent");
		
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
		
		userInfo.setKidsAge(mKidsAgeYear.getSelectedItem().toString() + mKidsAgeMonth.getSelectedItem().toString());
		
		String kidsGender;
		if (mKidsGenderBoy.isChecked()) {
			kidsGender = "男";
		} else {
			kidsGender = "女";
		}
		userInfo.setKidsGender(kidsGender);
		
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
