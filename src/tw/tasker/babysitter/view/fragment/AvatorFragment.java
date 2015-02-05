package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Sitter;
import tw.tasker.babysitter.utils.LogUtils;
import tw.tasker.babysitter.utils.PictureHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AvatorFragment extends Fragment implements OnClickListener {
	private ImageView mAvator;
	private PictureHelper mPictureHelper;
	private ProgressDialog mRingProgressDialog;

	public static Fragment newInstance() {
		Fragment fragment = new AvatorFragment();

		return fragment;

	}

	public AvatorFragment() {
		mPictureHelper = new PictureHelper();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_avator, container,
				false);
		mAvator = (ImageView) rootView.findViewById(R.id.avator);
		mAvator.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		final String[] imageMethods = {"照相", "相簿"}; 
		DialogFragment frag = new ListDialogFragment(imageMethods, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intent_camera = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent_camera, 0);
					
					break;

				case 1:
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, 1); 
					break;

				default:
					break;
				}
			}
		});
		frag.show(getFragmentManager(), "dialog");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		LogUtils.LOGE("vic", "requestCode="+ requestCode + "resultCode=" + resultCode);
		
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
        mAvator.setImageBitmap(bmp);

		mPictureHelper.setBitmap(bmp);
		mPictureHelper.setSaveCallback(new BabyRecordSaveCallback());
		mPictureHelper.savePicture();
	}
	
	public class BabyRecordSaveCallback extends SaveCallback {

		@Override
		public void done(ParseException e) {
			if (e == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"照片已上傳，成長記錄寫入中...", Toast.LENGTH_SHORT).show();
				saveComment();
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Error saving: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private void saveComment() {
		ParseQuery<Sitter> query = Sitter.getQuery();
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.getFirstInBackground(new GetCallback<Sitter>() {
			
			@Override
			public void done(Sitter sitter, ParseException e) {
				sitter.setAvatorFile(mPictureHelper.getFile());
				sitter.saveInBackground();
			}
		});
		mRingProgressDialog.dismiss();
	}

	private void getFromGallery(Intent data) {
		mRingProgressDialog = ProgressDialog.show(getActivity(),
				"請稍等 ...", "資料儲存中...", true);

		Uri selectedImage = data.getData();

        String filePath = getFilePath(selectedImage);
        
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        mAvator.setImageBitmap(bmp);
        
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
}
