package tw.tasker.babysitter;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.UserInfo;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileParentFragment extends Fragment {

	public static Fragment newInstance() {
		Fragment fragment = new ProfileParentFragment();
		return fragment;
	}

	private TextView mName;
	private TextView mAccount;
	private TextView mPassword;
	private TextView mPhone;
	private TextView mAddress;
	private TextView mKidsAge;
	private TextView mKidsGender;

	public ProfileParentFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile_parent, container, false);
		
		mName = (TextView) rootView.findViewById(R.id.name);
		
		mAccount = (TextView) rootView.findViewById(R.id.account);
		mPassword = (TextView) rootView.findViewById(R.id.password);
		mPhone = (TextView) rootView.findViewById(R.id.phone);
		mAddress = (TextView) rootView.findViewById(R.id.address);
		
		mKidsAge = (TextView) rootView.findViewById(R.id.kids_age);
		mKidsGender = (TextView) rootView.findViewById(R.id.kids_gender);

		initData();
		
		return  rootView;
	}
	
	protected void initData() {
		mName.setText("");
		mAccount.setText("");
		mPassword.setText("");
		
		mPhone.setText("");
		mAddress.setText("");
		
		mKidsAge.setText("");
		mKidsGender.setText("");
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		loadProfileData();

	}

	private void loadProfileData() {
		ParseQuery<UserInfo> query = UserInfo.getQuery();
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.getFirstInBackground(new GetCallback<UserInfo>() {
			
			@Override
			public void done(UserInfo userInfo, ParseException exception) {
				if (userInfo == null) {
					Toast.makeText(getActivity(), "查不到你的資料!", Toast.LENGTH_SHORT).show();

				} else {
					fillDataToUI(userInfo);
				}
			}
		});
	}

	protected void fillDataToUI(UserInfo userInfo) {
		mName.setText(userInfo.getName());
		mAccount.setText("帳號：" + ParseUser.getCurrentUser().getUsername());
		mPassword.setText("密碼：*******");
		
		mPhone.setText("聯絡電話：" + userInfo.getPhone());
		mAddress.setText("聯絡地址：" + userInfo.getAddress());
		
		mKidsAge.setText("小孩歲數：" + userInfo.getKidsAge());
		mKidsGender.setText("小孩姓別：" + userInfo.getKidsGender());
	}
}
