package tw.tasker.babysitter.view.activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import de.hdodenhof.circleimageview.CircleImageView;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.ProfileParentFragment;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.model.data.UserInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
		
		String websiteUrl = "http://cwisweb.sfaa.gov.tw/";
		String parseUrl = sitter.getImageUrl();
		if (parseUrl.equals("../img/photo_mother_no.jpg")) {
			mAvatar.setImageResource(R.drawable.profile);
		} else {
			imageLoader.displayImage(websiteUrl + parseUrl, mAvatar, Config.OPTIONS, null);
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
		
		tmpSiterInfo.setTel(phone);
		tmpSiterInfo.setAddress(address);
		tmpSiterInfo.setEducation(education);
		tmpSiterInfo.setCommunityName(communityName);
		
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

}
