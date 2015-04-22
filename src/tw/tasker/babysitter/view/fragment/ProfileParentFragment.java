package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.drawable;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.view.activity.SignUpListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileParentFragment extends Fragment implements OnClickListener {

	private static SignUpListener mListener;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public static Fragment newInstance(SignUpListener listener) {
		Fragment fragment = new ProfileParentFragment();
		mListener = listener;
		return fragment;
	}

	private TextView mName;
	private TextView mAccount;
	private TextView mPassword;
	private TextView mPhone;
	private TextView mAddress;
	private TextView mKidsAge;
	private TextView mKidsGender;
	private Button mEdit;
	private CircleImageView mAvatar;

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
		
		mAvatar = (CircleImageView) rootView.findViewById(R.id.avatar);

		mEdit = (Button) rootView.findViewById(R.id.edit); 
		
		mName = (TextView) rootView.findViewById(R.id.name);
		
		mAccount = (TextView) rootView.findViewById(R.id.account);
		mPassword = (TextView) rootView.findViewById(R.id.password);
		mPhone = (TextView) rootView.findViewById(R.id.phone);
		mAddress = (TextView) rootView.findViewById(R.id.address);
		
		mKidsAge = (TextView) rootView.findViewById(R.id.kids_age);
		mKidsGender = (TextView) rootView.findViewById(R.id.kids_gender);

		mEdit.setOnClickListener(this);
				
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
		
		if (Config.userInfo == null) {
			loadProfileData();
		} else {
			fillDataToUI(Config.userInfo);
		}

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
					Config.userInfo = userInfo;
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

		
		String year = userInfo.getKidsAge();
		if (!year.isEmpty()) {
			year = "民國 " + userInfo.getKidsAge().substring(0, 3) + " 年 ";
		} else {
			year = "民國     年";
		}
		
		String month = userInfo.getKidsAge();
		if (!month.isEmpty()) {
			month = userInfo.getKidsAge().substring(3,5) + " 月";
		} else {
			month = "   月";
		}
		
		mKidsAge.setText("小孩生日：" + year + month);
		
		mKidsGender.setText("小孩姓別：" + userInfo.getKidsGender());
		
		if (userInfo.getAvatorFile() != null) {
			String url = userInfo.getAvatorFile().getUrl();
			imageLoader.displayImage(url, mAvatar, Config.OPTIONS, null);
		} else {
			mAvatar.setImageResource(R.drawable.profile);
		}
	}

	@Override
	public void onClick(View v) {
		mListener.onSwitchToNextFragment(Config.PARENT_EDIT_PAGE);
	}
}
