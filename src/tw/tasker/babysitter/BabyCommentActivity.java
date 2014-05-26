package tw.tasker.babysitter;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;

import java.io.ByteArrayOutputStream;

import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.view.BabysitterDetailActivity;
import tw.tasker.babysitter.view.MainActivity;
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
import com.parse.SaveCallback;

public class BabyCommentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_comment);

		//Comment.babysitterId 先存 baby.objectId
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment("HXSAmYCUdG")).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baby_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
		private String mObjectId;
		private ImageView mUserAvator;
		private Bitmap mBmp;
		private ParseFile mFile;

		private ProgressDialog mRingProgressDialog;

		public PlaceholderFragment(String babyObjectId) {
			mObjectId = babyObjectId;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baby_comment,
					container, false);
			
			
			
			mBabysitterTitle = (EditText) rootView.findViewById(R.id.babysitter_comment_title);
			mBabysitterComment = (EditText) rootView.findViewById(R.id.babysitter_comment);
			
			mPostCommnet = (Button) rootView.findViewById(R.id.post_comment);
			mPostCommnet.setOnClickListener(new View.OnClickListener() {


				@Override
				public void onClick(View v) {
			        mRingProgressDialog = ProgressDialog.show(getActivity(), "請稍等 ...", "資料儲存中...", true); 

					Toast.makeText(v.getContext(), "已送出..", Toast.LENGTH_LONG)
							.show();

					savePicture();
					//saveComment();
					//updateBabysitter();

					//Intent intent = new Intent();
					//intent.setClass(getActivity().getApplicationContext(), BabysitterDetailActivity.class);
					//startActivity(intent);
				}
			});
			
			
			Button selectPhoto = (Button)rootView.findViewById(R.id.photo_button);
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
							// setResult(RESULT_OK);
							// finish();
							Toast.makeText(
									getActivity().getApplicationContext(),
									"upload doen!", Toast.LENGTH_SHORT)
									.show();
							saveComment();
						} else {
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Error saving: " + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
					}
				});

             
            // Create a New Class called "ImageUpload" in Parse
//            ParseObject imgupload = new ParseObject("ImageUpload");

            // Create a column named "ImageName" and set the string
//            imgupload.put("ImageName", "AndroidBegin Logo");

            // Create a column named "ImageFile" and insert the image
//            imgupload.put("ImageFile", file);

            // Create the class and the columns
//            imgupload.saveInBackground();

		}
		
		private void saveComment() {
			
			// ParseObject post = new ParseObject("Comment");
			BabysitterComment post = new BabysitterComment();
			post.put("babysitterId", mObjectId);
			post.put("title", mBabysitterTitle.getText().toString());
			post.put("comment", mBabysitterComment.getText().toString());
			post.setPhotoFile(mFile);
			

			// Save the post and return
			post.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
						// setResult(RESULT_OK);
						// finish();
						Toast.makeText(
								getActivity().getApplicationContext(),
								"saving doen!", Toast.LENGTH_SHORT)
								.show();
					} else {
						LOGD("vic", e.getMessage());
						Toast.makeText(
								getActivity().getApplicationContext(),
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
