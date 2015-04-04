package tw.tasker.babysitter.view.fragment;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.activity.SignUpListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link SyncDataFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class SyncDataFragment extends Fragment implements OnClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static SignUpListener mListener;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private Button mSync;
	
	private TextView mNumber;
	private TextView mSitterName;
	//private TextView mSex;
	private TextView mAge;
	private TextView mEducation;
	private TextView mTel;
	private TextView mAddress;
	private RatingBar mBabycareCount;
	private TextView mBabycareTime;
	private LinearLayout mSyncLayout;
	private LinearLayout mDataLayout;
	private EditText mName;
	private EditText mPassword;
	private EditText mPasswordAgain;
	private TextView mSkillNumber;
	private TextView mCommunityName;
	private ScrollView mAllScreen;
	private Button mConfirm;
	private CircleImageView mAvator;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SyncDataFragment.
	 */
	// TODO: Rename and change types and number of parameters
	
	public static Fragment newInstance(SignUpListener listener) {
		Fragment fragment = new SyncDataFragment();
		mListener = listener;
		
		//Bundle args = new Bundle();
		//args.putString(ARG_PARAM1, param1);
		//args.putString(ARG_PARAM2, param2);
		//fragment.setArguments(args);
		return fragment;
	}

	public SyncDataFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_data_sync, container, false);
		
		mAvator = (CircleImageView) rootView.findViewById(R.id.avator);
		
		mAllScreen = (ScrollView) rootView.findViewById(R.id.signup_form);
		mAllScreen.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
				return false;
			}
		});

		
		mSync = (Button) rootView.findViewById(R.id.sync);
		
		mNumber = (TextView) rootView.findViewById(R.id.number);
		mSitterName = (TextView) rootView.findViewById(R.id.name);
		//mSex = (TextView) rootView.findViewById(R.id.sex);
		//mAge = (TextView) rootView.findViewById(R.id.age);
		mEducation = (TextView) rootView.findViewById(R.id.education);
		mTel = (TextView) rootView.findViewById(R.id.tel);
		mAddress = (TextView) rootView.findViewById(R.id.address);
		mBabycareCount = (RatingBar) rootView.findViewById(R.id.babycareCount);
		mBabycareTime = (TextView) rootView.findViewById(R.id.babycare_time);

		mSkillNumber = (TextView) rootView.findViewById(R.id.skillNumber);
		mCommunityName = (TextView) rootView.findViewById(R.id.communityName);
		

		
		// layout
		mSyncLayout = (LinearLayout) rootView.findViewById(R.id.sync_layout);
		mDataLayout = (LinearLayout) rootView.findViewById(R.id.data_layout);
		
		mDataLayout.setVisibility(View.GONE);
		
		// set default data...
		mNumber.setText("031080");
		
		mSync.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				syncData();
				Toast.makeText(
						getActivity(),
						"資料同步..." /* e.getMessage() */,
						Toast.LENGTH_LONG).show();

				hideKeyPad();
			}

		});
		
		// Set up the signup form.
		mName = (EditText) rootView.findViewById(R.id.username);
		mPassword = (EditText) rootView.findViewById(R.id.password);
		mPasswordAgain = (EditText) rootView.findViewById(R.id.passwordAgain);
		
		mConfirm = (Button) rootView.findViewById(R.id.confirm);
		mConfirm.setOnClickListener(this);

		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.confirm:
			mListener.onSwitchToNextFragment();
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}
	
	protected void hideKeyPad() {
        InputMethodManager imm = (InputMethodManager) mSync.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSync.getWindowToken(),0);
	}

	private void syncData() {
		ParseQuery<Babysitter> query = Babysitter.getQuery();
		query.whereEqualTo("skillNumber", mNumber.getText().toString());
		query.getFirstInBackground(new GetCallback<Babysitter>() {
			
			@Override
			public void done(Babysitter babysitter, ParseException e) {
				LogUtils.LOGD("vic", "syncData()" + babysitter);
				
				if (babysitter == null) {
					Toast.makeText(getActivity(), "查不到此證號!", Toast.LENGTH_LONG).show();
					mDataLayout.setVisibility(View.INVISIBLE);

				} else {
					fillUI(babysitter);
					Config.sitterInfo = babysitter;
					//mSyncLayout.setVisibility(View.GONE);
					mDataLayout.setVisibility(View.VISIBLE);
				}
				
			}

		});
	}


	private void fillUI(Babysitter babysitter) {
		mSitterName.setText(babysitter.getName());
		//mSex.setText(babysitter.getSex());
		//mAge.setText(babysitter.getAge());
		mTel.setText("聯絡電話：" + babysitter.getTel());
		mAddress.setText("住家地址：" + babysitter.getAddress());
		
		int babyCount = getBabyCount(babysitter.getBabycareCount());
		mBabycareCount.setRating(babyCount);
		
		mSkillNumber.setText("保母證號：" + babysitter.getSkillNumber());
		mEducation.setText("教育程度：" + babysitter.getEducation());
		mCommunityName.setText(babysitter.getCommunityName());
		
		String websiteUrl = "http://cwisweb.sfaa.gov.tw/";
		String parseUrl = babysitter.getImageUrl();
		if (parseUrl.equals("../img/photo_mother_no.jpg")) {
			mAvator.setImageResource(R.drawable.profile);
		} else {
			imageLoader.displayImage(websiteUrl + parseUrl, mAvator, Config.OPTIONS, null);
		}
		
		//mBabycareTime.setText(babysitter.getBabycareTime());
	}
	
	private int getBabyCount(String babycareCount) {
		String[] babies = babycareCount.split(" ");
		return babies.length;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//inflater.inflate(R.menu.add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
//		case R.id.action_add:
//			if (ParseUser.getCurrentUser() != null) {
//				hasSitter();
//			}
//			addSitter(); 
			
//			if (isAccountOK()) {
//				signUpSitter();
//			}

			
//			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
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
					LogUtils.LOGD("vic", "user object id" + ParseUser.getCurrentUser().getObjectId());
				
					addSitter();
				}
			}
		});
	}

//	private void addUserInfo() {
//		LogUtils.LOGD("vic", "addUserInfo");
//
//		UserInfo userInfo = new UserInfo();
//		//userInfo.setLocation(Config.MY_LOCATION);
//		userInfo.setUser(ParseUser.getCurrentUser());
//		userInfo.setName(mParentsName.getText().toString());
//		userInfo.setAddress(mParentsAddress.getText().toString());
//		userInfo.setPhone(mParents_phone.getText().toString());
//		userInfo.setKidsAge(mKidsAge.getText().toString());
//		userInfo.setKidsGender(mKidsGender.getText().toString());
//		
//		userInfo.saveInBackground(new SaveCallback() {
//
//			@Override
//			public void done(ParseException e) {
//				if (e == null) {
//					goToNextActivity();
//				} else {
//					LOGD("vic", e.getMessage());
//				}
//			}
//		});
//	}

	private void hasSitter() {
		ParseQuery<Sitter> query = Sitter.getQuery();
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.getFirstInBackground(new GetCallback<Sitter>() {
			
			@Override
			public void done(Sitter sitter, ParseException e) {
				if (sitter == null) {
					addSitter();
					LOGD("vic", "addSitter()");

				} else {
					updateSitter(sitter);
					LOGD("vic", "updateSitter()");

				}
				
			}
		});
	}

	private void addSitter() {
		Sitter sitter = new Sitter();
		sitter.setUser(ParseUser.getCurrentUser());
		sitter.setBabysitterNumber(mNumber.getText().toString());
		sitter.setName(mSitterName.getText().toString());
		//sitter.setSex(mSex.getText().toString());
		sitter.setAge(mAge.getText().toString());
		sitter.setEducation(mEducation.getText().toString());
		sitter.setTel(mTel.getText().toString());
		sitter.setAddress(mAddress.getText().toString());
		//sitter.setBabycareCount(mBabycareCount.getText().toString());
		sitter.setBabycareTime(mBabycareTime.getText().toString());
		sitter.setIsVerify(false);
		
		sitter.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					//LOGD("vic", "sitter 新增成功!");
					Toast.makeText(
							getActivity(),
							"資料新增成功..." /* e.getMessage() */,
							Toast.LENGTH_LONG).show();

				} else {
					LOGD("vic", e.getMessage());
				}
			}
		});
		
	}

	private void updateSitter(Sitter sitter) {
		
		sitter.setBabysitterNumber(mNumber.getText().toString());
		sitter.setName(mSitterName.getText().toString());
		//sitter.setSex(mSex.getText().toString());
		sitter.setAge(mAge.getText().toString());
		sitter.setEducation(mEducation.getText().toString());
		sitter.setTel(mTel.getText().toString());
		sitter.setAddress(mAddress.getText().toString());
		//sitter.setBabycareCount(mBabycareCount.getText().toString());
		sitter.setBabycareTime(mBabycareTime.getText().toString());
		sitter.setIsVerify(false);
		
		sitter.saveInBackground();
	
		Toast.makeText(
				getActivity(),
				"資料更新成功..." /* e.getMessage() */,
				Toast.LENGTH_LONG).show();

	}

}
