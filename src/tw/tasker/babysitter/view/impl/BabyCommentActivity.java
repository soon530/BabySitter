package tw.tasker.babysitter.view.impl;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.io.ByteArrayOutputStream;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Baby;
import tw.tasker.babysitter.model.data.BabyRecord;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BabyCommentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_comment);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(Config.BABY_OBJECT_ID, getIntent()
					.getStringExtra(Config.BABY_OBJECT_ID));
			PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.baby_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private EditText mBabysitterTitle;
		private EditText mBabysitterComment;
		private Button mPostCommnet;
		private String mBabyObjectId;
		private ImageView mUserAvator;
		private Bitmap mBmp;
		private ParseFile mFile;

		private ProgressDialog mRingProgressDialog;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(Config.BABY_OBJECT_ID)) {
				mBabyObjectId = getArguments().getString(Config.BABY_OBJECT_ID);
			}

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baby_comment,
					container, false);

			mBabysitterTitle = (EditText) rootView
					.findViewById(R.id.babysitter_comment_title);
			mBabysitterComment = (EditText) rootView
					.findViewById(R.id.babysitter_comment);

			mPostCommnet = (Button) rootView.findViewById(R.id.post_comment);
			mPostCommnet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mBmp == null) {
						Toast.makeText(getActivity().getApplicationContext(),
								"拍張照吧，不會花你太多時間的!", Toast.LENGTH_SHORT).show();
						return;
					}

					mRingProgressDialog = ProgressDialog.show(getActivity(),
							"請稍等 ...", "資料儲存中...", true);

					Toast.makeText(v.getContext(), "已送出..", Toast.LENGTH_LONG)
							.show();

					savePicture();
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
			if (resultCode == RESULT_OK) {
				// 取出拍照後回傳資料
				Bundle extras = data.getExtras();
				// 將資料轉換為圖像格式
				Bitmap bmp = (Bitmap) extras.get("data");

				mBmp = bmp;
				// 載入ImageView
				mUserAvator.setImageBitmap(bmp);
			}

			// 覆蓋原來的Activity
			super.onActivityResult(requestCode, resultCode, data);
		}

		private void savePicture() {
			// Locate the image in res > drawable-hdpi
			Bitmap bitmap = mBmp;
			// Convert it to byte
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// Compress image to lower quality scale 1 - 100
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] image = stream.toByteArray();

			// Create the ParseFile
			mFile = new ParseFile("androidbegin.png", image);

			// Upload the image into Parse Cloud
			mFile.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
						Toast.makeText(getActivity().getApplicationContext(),
								"upload doen!", Toast.LENGTH_SHORT).show();
						saveComment();
					} else {
						Toast.makeText(getActivity().getApplicationContext(),
								"Error saving: " + e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

				}
			});
		}

		private void saveComment() {
			Baby baby = ParseObject.createWithoutData(Baby.class, mBabyObjectId);

			BabyRecord babyRecord = new BabyRecord();
			babyRecord.setBaby(baby);
			babyRecord.setTitle(mBabysitterTitle.getText().toString());
			babyRecord.setDescription(mBabysitterComment.getText().toString());
			babyRecord.setPhotoFile(mFile);			
			babyRecord.setUser(ParseUser.getCurrentUser());
			
			babyRecord.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
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
}
