package tw.tasker.babysitter;

import tw.tasker.babysitter.model.data.UserInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileParentEditFragment extends Fragment {

	public static Fragment newInstance() {
		Fragment fragment = new ProfileParentEditFragment();
		return fragment;
	}

	private TextView mName;
	private TextView mAccount;
	private TextView mPassword;
	private TextView mPhone;
	private TextView mAddress;
	private TextView mKidsAge;
	private TextView mKidsGender;

	public ProfileParentEditFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit_profile_parent, container, false);
		
		mName = (TextView) rootView.findViewById(R.id.name);
		
		mAccount = (TextView) rootView.findViewById(R.id.account);
		mPassword = (TextView) rootView.findViewById(R.id.password);
		mPhone = (TextView) rootView.findViewById(R.id.phone);
		mAddress = (TextView) rootView.findViewById(R.id.address);
		
		mKidsAge = (TextView) rootView.findViewById(R.id.kids_age);
		mKidsGender = (TextView) rootView.findViewById(R.id.kids_gender);

		//initData();
		
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
		
		fillDataToUI(Config.userInfo);
		
	}


	protected void fillDataToUI(UserInfo userInfo) {
		mName.setText(userInfo.getName());
		mAccount.setText("帳號：" + ParseUser.getCurrentUser().getUsername());
		mPassword.setText("密碼：*******");
		
		mPhone.setText(userInfo.getPhone());
		mAddress.setText(userInfo.getAddress());
		
		mKidsAge.setText("小孩歲數：" + userInfo.getKidsAge());
		mKidsGender.setText("小孩姓別：" + userInfo.getKidsGender());
	}
}
