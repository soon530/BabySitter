package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import static tw.tasker.babysitter.utils.LogUtils.LOGE;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.PictureHelper;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class BabyAddFragment extends Fragment {

	public class BabyAddSaveCallback extends SaveCallback {

		@Override
		public void done(ParseException e) {
			if (e == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"upload doen!", Toast.LENGTH_SHORT).show();
				saveComment();
			} else {

				LOGD("vic", "Error saving: " + e.getMessage());
				Toast.makeText(getActivity().getApplicationContext(),
						"Error saving: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	
	private EditText mBabysitterTitle;
	private EditText mBabysitterComment;
	private Button mPostCommnet;
	private String mBabysitterObjectId;
	private ImageView mUserAvator;
	private Bitmap mBmp;
	private ParseFile mFile;
	private boolean mIsPublic;

	private ProgressDialog mRingProgressDialog;
	private Spinner mShareType;
	private PictureHelper mPictureHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
			mBabysitterObjectId = getArguments().getString(
					Config.BABYSITTER_OBJECT_ID);
		}
		
		mPictureHelper = new PictureHelper();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_baby_add,
				container, false);
		
		TextView user_name = (TextView) rootView.findViewById(R.id.user_name);
		user_name.setText("保母編號" + mBabysitterObjectId);
		

		mShareType = (Spinner) rootView.findViewById(R.id.share_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				new String[] { "私藏", "公開" });
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mShareType.setAdapter(adapter);
		mIsPublic = false;
		mShareType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					mIsPublic = false;
				} else {
					mIsPublic = true;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		mBabysitterTitle = (EditText) rootView
				.findViewById(R.id.babysitter_comment_title);
		mBabysitterComment = (EditText) rootView
				.findViewById(R.id.babysitter_comment);

		mPostCommnet = (Button) rootView.findViewById(R.id.post_comment);
		mPostCommnet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPictureHelper.noPicture()) {
					Toast.makeText(getActivity().getApplicationContext(),
							"拍張照吧，不會花你太多時間的!", Toast.LENGTH_SHORT).show();
					return;
				}

				mRingProgressDialog = ProgressDialog.show(getActivity(),
						"請稍等 ...", "資料儲存中...", true);

				Toast.makeText(v.getContext(), "已送出..", Toast.LENGTH_LONG)
						.show();

				mPictureHelper.setSaveCallback(new BabyAddSaveCallback());
				mPictureHelper.savePicture();
			}
		});

		Button selectPhoto = (Button) rootView
				.findViewById(R.id.photo_button);
		selectPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_camera = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 0);

			}
		});

		mUserAvator = (ImageView) rootView.findViewById(R.id.user_avator);

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (resultCode == BabyAddActivity.RESULT_OK) {
			// 取出拍照後回傳資料
			Bundle extras = data.getExtras();
			// 將資料轉換為圖像格式
			Bitmap bmp = (Bitmap) extras.get("data");

			mPictureHelper.setBitmap(bmp);

			// 載入ImageView
			mUserAvator.setImageBitmap(bmp);
		}

		// 覆蓋原來的Activity
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void saveComment() {

		// ParseObject post = new ParseObject("Comment");
		
		Babysitter babysitter = ParseObject
				.createWithoutData(Babysitter.class, mBabysitterObjectId);

		
		BabyDiary baby = new BabyDiary();
		
		baby.setBabysitter(babysitter);
		baby.setName(mBabysitterTitle.getText().toString());
		baby.setNote(mBabysitterComment.getText().toString());
		baby.setFavorite(0);
		baby.setPhotoFile(mPictureHelper.getFile());
		baby.setUser(ParseUser.getCurrentUser());
		baby.setIsPublic(mIsPublic);

		// Save the post and return
		baby.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					// setResult(RESULT_OK);
					// finish();
					Toast.makeText(getActivity().getApplicationContext(),
							"saving doen!", Toast.LENGTH_SHORT).show();
				} else {
					LOGD("vic", e.getMessage());
					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				mRingProgressDialog.dismiss();
				getActivity().finish();
			}

		});
	}
}