package tw.tasker.babysitter.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

public class PictureHelper {

	private Bitmap mBitmap;
	private ParseFile mFile;
	private SaveCallback mSaveCallback;

	public void savePicture() {
		// Locate the image in res > drawable-hdpi
		Bitmap bitmap = mBitmap;
		// Convert it to byte
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Compress image to lower quality scale 1 - 100
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] image = stream.toByteArray();

		// Create the ParseFile
		mFile = new ParseFile("androidbegin.png", image);

		// Upload the image into Parse Cloud
		mFile.saveInBackground(mSaveCallback);
	}

	public void setBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
	}

	public void setSaveCallback(SaveCallback saveCallback) {
		mSaveCallback = saveCallback;
	}

	public boolean noPicture() {
		if (mBitmap == null) {
			return true;
		} else {
			return false;
		}
	}

	public ParseFile getFile() {
		return mFile;
	}
}
