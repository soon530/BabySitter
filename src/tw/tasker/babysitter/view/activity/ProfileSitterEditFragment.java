package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.ProfileParentEditFragment.BabyRecordSaveCallback;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.PictureHelper;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
	private ProgressDialog mRingProgressDialog;
	private PictureHelper mPictureHelper;


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
		mAvatar.setOnClickListener(this);

		
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
		
		if (sitter.getAvatorFile()==null) {
			getOldAvator(sitter);
		} else {
			getNewAvator(sitter);
		}
	}
	
	private void getOldAvator(Sitter sitter) {
		String websiteUrl = "http://cwisweb.sfaa.gov.tw/";
		String parseUrl = sitter.getImageUrl();
		if (parseUrl.equals("../img/photo_mother_no.jpg")) {
			mAvatar.setImageResource(R.drawable.photo_icon);
		} else {
			imageLoader.displayImage(websiteUrl + parseUrl, mAvatar, Config.OPTIONS, null);
		}
	}
	
	private void getNewAvator(Sitter sitter) {
		if (sitter.getAvatorFile() != null) {
			String url = sitter.getAvatorFile().getUrl();
			imageLoader.displayImage(url, mAvatar, Config.OPTIONS, null);
		} else {
			mAvatar.setImageResource(R.drawable.photo_icon);
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
		int id = v.getId();
		
		switch (id) {
		case R.id.avatar:
			saveAvatar();
			break;

		case R.id.confirm:
			saveSitterInfo(Config.tmpSiterInfo);
			
		default:
			break;
		}
		
	}
	
	private void saveAvatar() {
		mPictureHelper = new PictureHelper();	
		openCamera();
	}

	private void openCamera() {
		Intent intent_camera = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent_camera, 0);
	}

	private void openGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1); 		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		LogUtils.LOGD("vic", "requestCode="+ requestCode + "resultCode=" + resultCode);
		
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}

		switch (requestCode) {
		case 0:
			getFromCamera(data);
			break;
			
		case 1:
			getFromGallery(data);
			break;
		default:
			break;
		}
	}
	
	private void getFromCamera(Intent data) {
		mRingProgressDialog = ProgressDialog.show(getActivity(),
				"請稍等 ...", "資料儲存中...", true);

		// 取出拍照後回傳資料
		Bundle extras = data.getExtras();
		// 將資料轉換為圖像格式
		Bitmap bmp = (Bitmap) extras.get("data");
        mAvatar.setImageBitmap(bmp);

		mPictureHelper.setBitmap(bmp);
		mPictureHelper.setSaveCallback(new BabyRecordSaveCallback());
		mPictureHelper.savePicture();
	}
	
	private void getFromGallery(Intent data) {
		mRingProgressDialog = ProgressDialog.show(getActivity(),
				"請稍等 ...", "資料儲存中...", true);

		Uri selectedImage = data.getData();

        String filePath = getFilePath(selectedImage);
        
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        mAvatar.setImageBitmap(bmp);
        
		mPictureHelper.setBitmap(bmp);
		mPictureHelper.setSaveCallback(new BabyRecordSaveCallback());
		mPictureHelper.savePicture();
	}
	
	private String getFilePath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
		return filePath;
	}
	
	public class BabyRecordSaveCallback extends SaveCallback {

		@Override
		public void done(ParseException e) {
			if (e == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"大頭照已上傳..", Toast.LENGTH_SHORT).show();
				saveComment(Config.tmpSiterInfo);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Error saving: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}
	
	private void saveComment(Sitter tmpSiterInfo) {
		//ParseQuery<UserInfo> query = UserInfo.getQuery();
		//query.whereEqualTo("user", ParseUser.getCurrentUser());
		//query.getFirstInBackground(new GetCallback<UserInfo>() {
			
			//@Override
			//public void done(UserInfo userInfo, ParseException e) {
				tmpSiterInfo.setAvatorFile(mPictureHelper.getFile());
				tmpSiterInfo.saveInBackground();
			//}
		//});
		mRingProgressDialog.dismiss();
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
