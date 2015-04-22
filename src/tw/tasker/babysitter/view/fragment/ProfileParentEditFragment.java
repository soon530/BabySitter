package tw.tasker.babysitter.view.fragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.drawable;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.model.data.UserInfo;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.PictureHelper;
import tw.tasker.babysitter.view.activity.SignUpListener;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileParentEditFragment extends Fragment implements OnClickListener {

	private static SignUpListener mListener;

	public static Fragment newInstance(SignUpListener listener) {
		Fragment fragment = new ProfileParentEditFragment();
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
	private Button mConfirm;
	private CircleImageView mAvatar;
	private PictureHelper mPictureHelper;
	private ProgressDialog mRingProgressDialog;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Spinner mKidsAgeYear;
	private Spinner mKidsAgeMonth;

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
		
		mAvatar = (CircleImageView) rootView.findViewById(R.id.avatar);
		mAvatar.setOnClickListener(this);
		
		mName = (TextView) rootView.findViewById(R.id.name);
		
		mAccount = (TextView) rootView.findViewById(R.id.account);
		mPassword = (TextView) rootView.findViewById(R.id.password);
		mPhone = (TextView) rootView.findViewById(R.id.phone);
		mAddress = (TextView) rootView.findViewById(R.id.address);
		
		//mKidsAge = (TextView) rootView.findViewById(R.id.kids_age);
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

		mKidsGender = (TextView) rootView.findViewById(R.id.kids_gender);

		mConfirm = (Button) rootView.findViewById(R.id.confirm);
		mConfirm.setOnClickListener(this);
		
		
		//initData();
		
		return  rootView;
	}
	
	private int getPositionFromYear() {
		
		String currentYear = Config.userInfo.getKidsAge();
		if (!currentYear.isEmpty()) {
		    currentYear = Config.userInfo.getKidsAge().substring(0, 3);
			
		} else {
			Calendar calendar=Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
		    currentYear = String.valueOf((calendar.get(Calendar.YEAR)-1911));
		}
		
		String[] months = getResources().getStringArray(R.array.kids_age_year);
		int position = Arrays.asList(months).indexOf(currentYear);
		
		LogUtils.LOGD("vic", "year: " + position);
		return position;
	}
	
	private int getPositionFromMonth() {
		
		String currentMonth = Config.userInfo.getKidsAge();
		if (!currentMonth.isEmpty()) {
			currentMonth = Config.userInfo.getKidsAge().substring(3, 5);
		} else {
			Calendar calendar=Calendar.getInstance(); 
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM"); 
			currentMonth = simpleDateFormat.format(calendar.getTime());
		}
		
		String[] months = getResources().getStringArray(R.array.kids_age_month);
		int position = Arrays.asList(months).indexOf(currentMonth);
		
		//LogUtils.LOGD("vic", "month: " + position);
		return position;
	}

	
	protected void initData() {
		mName.setText("");
		mAccount.setText("");
		mPassword.setText("");
		
		mPhone.setText("");
		mAddress.setText("");
		
		//mKidsAge.setText("");
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
		
		// mKidsAge.setText("小孩生日： 民國 " + year + " 年 " + month + " 月");

		mKidsGender.setText("小孩姓別：" + userInfo.getKidsGender());
		
		if (userInfo.getAvatorFile() != null) {
			String url = userInfo.getAvatorFile().getUrl();
			LogUtils.LOGD("vic", "url=" + url);

			imageLoader.displayImage(url, mAvatar, Config.OPTIONS, null);
		} else {
			mAvatar.setImageResource(R.drawable.photo_icon);
		}

	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		switch (id) {
		case R.id.avatar:
			saveAvatar();
			break;

		case R.id.confirm:
			saveUserInfo(Config.userInfo);
			
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
				saveComment(Config.userInfo);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Error saving: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}
	
	private void saveComment(UserInfo userInfo) {
		//ParseQuery<UserInfo> query = UserInfo.getQuery();
		//query.whereEqualTo("user", ParseUser.getCurrentUser());
		//query.getFirstInBackground(new GetCallback<UserInfo>() {
			
			//@Override
			//public void done(UserInfo userInfo, ParseException e) {
				userInfo.setAvatorFile(mPictureHelper.getFile());
				userInfo.saveInBackground();
			//}
		//});
		mRingProgressDialog.dismiss();
	}
	
	private void saveUserInfo(UserInfo userInfo) {
		String phone = mPhone.getText().toString();
		String address = mAddress.getText().toString();
		
		userInfo.setPhone(phone);
		userInfo.setAddress(address);
		userInfo.setKidsAge(mKidsAgeYear.getSelectedItem().toString() + mKidsAgeMonth.getSelectedItem().toString());
		userInfo.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getActivity(),
							"我的資料更新成功!" /* e.getMessage() */, Toast.LENGTH_LONG)
							.show();
					//getActivity().finish();
					mListener.onSwitchToNextFragment(Config.PARENT_READ_PAGE);
				} else {
					
				}
			}
		});
	
	}
}
