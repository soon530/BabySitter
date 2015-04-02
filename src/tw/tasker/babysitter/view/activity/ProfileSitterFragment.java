package tw.tasker.babysitter.view.activity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import tw.tasker.babysitter.ProfileParentFragment;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.model.data.UserInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileSitterFragment extends Fragment {

	public static Fragment newInstance() {
		Fragment fragment = new ProfileSitterFragment();
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

	public ProfileSitterFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile_sitter, container, false);
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
		
		loadProfileData();
	}

	private void loadProfileData() {
		ParseQuery<Sitter> query = Sitter.getQuery();
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.getFirstInBackground(new GetCallback<Sitter>() {
			
			@Override
			public void done(Sitter sitter, ParseException exception) {
				if (sitter == null) {
					Toast.makeText(getActivity(), "唉唷~產生一些錯誤了~", Toast.LENGTH_SHORT).show();

				} else {
					fillDataToUI(sitter);
				}
			}
		});

	}

	protected void fillDataToUI(Sitter sitter) {
		mSitterName.setText(sitter.getName());
		//mSex.setText(babysitter.getSex());
		//mAge.setText(babysitter.getAge());
		mTel.setText("聯絡電話：" + sitter.getTel());
		mAddress.setText("住家地址：" + sitter.getAddress());
		
		int babyCount = getBabyCount(sitter.getBabycareCount());
		mBabycareCount.setRating(babyCount);
		
		//mSkillNumber.setText("保母證號：" + sitter.getSkillNumber());
		mEducation.setText("教育程度：" + sitter.getEducation());
		//mCommunityName.setText(sitter.getCommunityName());
		
		//mBabycareTime.setText(babysitter.getBabycareTime());
		
	}
	
	private int getBabyCount(String babycareCount) {
		String[] babies = babycareCount.split(" ");
		return babies.length;
	}



}
