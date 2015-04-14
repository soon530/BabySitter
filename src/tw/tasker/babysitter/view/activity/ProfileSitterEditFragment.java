package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Sitter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.SaveCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSitterEditFragment extends Fragment implements OnClickListener {

	private static SignUpListener mListner;

	public static Fragment newInstance(SignUpListener listener) {
		Fragment fragment = new ProfileSitterEditFragment();
		mListner = listener;
		return fragment;
	}

	private TextView mNumber;
	private TextView mSitterName;
	private TextView mEducation;
	private TextView mTel;
	private TextView mAddress;
	private RatingBar mBabycareCount;
	private TextView mBabycareTime;
	private TextView mSkillNumber;
	private TextView mCommunityName;
	private CircleImageView mAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Button mConfirm;
	private CheckBox mDayTime;
	private CheckBox mNightTime;
	private CheckBox mHalfDay;
	private CheckBox mFullDay;
	private CheckBox mPartTime;
	private CheckBox mInHouse;


	public ProfileSitterEditFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit_profile_sitter, container, false);
		
		mConfirm = (Button) rootView.findViewById(R.id.confirm);
		mConfirm.setOnClickListener(this);
		
		mAvatar = (CircleImageView) rootView.findViewById(R.id.avatar);

		
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

		mDayTime = (CheckBox) rootView.findViewById(R.id.day_time);
		mNightTime = (CheckBox) rootView.findViewById(R.id.night_time);
		mHalfDay = (CheckBox) rootView.findViewById(R.id.half_day);
		mFullDay = (CheckBox) rootView.findViewById(R.id.full_day);
		mPartTime = (CheckBox) rootView.findViewById(R.id.part_time);
		mInHouse = (CheckBox) rootView.findViewById(R.id.in_house);
		
		initData();
		return rootView;
	}

	private void initData() {
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		fillDataToUI(Config.tmpSiterInfo);
	
	}

	protected void fillDataToUI(Sitter sitter) {
		mSitterName.setText(sitter.getName());
		//mSex.setText(babysitter.getSex());
		//mAge.setText(babysitter.getAge());
		mTel.setText(sitter.getTel());
		mAddress.setText(sitter.getAddress());
		
		int babyCount = getBabyCount(sitter.getBabycareCount());
		mBabycareCount.setRating(babyCount);
		
		//mSkillNumber.setText("保母證號：" + sitter.getSkillNumber());
		mEducation.setText(sitter.getEducation());
		mCommunityName.setText(sitter.getCommunityName());
		
		//mBabycareTime.setText(babysitter.getBabycareTime());
		
		setBabyCareTime(sitter.getBabycareTime());

		
		String websiteUrl = "http://cwisweb.sfaa.gov.tw/";
		String parseUrl = sitter.getImageUrl();
		if (parseUrl.equals("../img/photo_mother_no.jpg")) {
			mAvatar.setImageResource(R.drawable.profile);
		} else {
			imageLoader.displayImage(websiteUrl + parseUrl, mAvatar, Config.OPTIONS, null);
		}

	}
	
	private void setBabyCareTime(String babycareTime) {
		if (babycareTime.indexOf("白天") > -1) {
			mDayTime.setChecked(true);
		}
		
		if (babycareTime.indexOf("夜間") > -1) {
			mNightTime.setChecked(true);
		}
		
		if (babycareTime.indexOf("全天") > -1) {
			mFullDay.setChecked(true);
		}
		
		if (babycareTime.indexOf("半天") > -1) {
			mHalfDay.setChecked(true);
		}
		
		if (babycareTime.indexOf("臨時托育(平日)") > -1 || babycareTime.indexOf("臨時托育(假日)") > -1) {
			mPartTime.setChecked(true);
		}
		
		if (babycareTime.indexOf("到宅服務") > -1) {
			mInHouse.setChecked(true);
		}

	}

	
	private int getBabyCount(String babycareCount) {
		String[] babies = babycareCount.split(" ");
		return babies.length;
	}

	@Override
	public void onClick(View v) {
		
		saveSitterInfo(Config.tmpSiterInfo);
		
	}

	private void saveSitterInfo(Sitter tmpSiterInfo) {
		String phone = mTel.getText().toString();
		String address = mAddress.getText().toString();
		
		String education = mEducation.getText().toString();
		String communityName = mCommunityName.getText().toString();

		String babycareTime = getBabycareTimeInfo();
		
		tmpSiterInfo.setTel(phone);
		tmpSiterInfo.setAddress(address);
		tmpSiterInfo.setEducation(education);
		tmpSiterInfo.setCommunityName(communityName);
		tmpSiterInfo.setBabycareTime(babycareTime);
		
		tmpSiterInfo.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getActivity(),
							"我的資料更新成功!" /* e.getMessage() */, Toast.LENGTH_LONG)
							.show();
					mListner.onSwitchToNextFragment(Config.SITTER_READ_PAGE);
				}
			}
		});
		
	}

	private String getBabycareTimeInfo() {
		String babycareTimeInfo = "";
		if (mDayTime.isChecked()) {
			babycareTimeInfo = babycareTimeInfo + "白天, ";
		}
		
		if (mNightTime.isChecked()) {
			babycareTimeInfo = babycareTimeInfo + "夜間, ";
		}
		
		if (mFullDay.isChecked()) {
			babycareTimeInfo = babycareTimeInfo + "全天, ";
		}
		
		if (mHalfDay.isChecked()) {
			babycareTimeInfo = babycareTimeInfo + "半天, ";
		}
		
		if (mPartTime.isChecked()) {
			babycareTimeInfo = babycareTimeInfo + "臨時托育(平日), 臨時托育(假日), ";
		}
		
		if (mInHouse.isChecked()) {
			babycareTimeInfo = babycareTimeInfo + "到宅服務, ";
		}
		return babycareTimeInfo;
	}


}
