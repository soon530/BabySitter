package tw.tasker.babysitter.view.fragment;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.model.data.Babysitter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddDialogFragment extends DialogFragment {
	private Spinner mShareType;
	private boolean mIsPublic;
	private ProgressDialog mRingProgressDialog;
	private EditText mName;
	private String mBabysitterObjectId;

	public AddDialogFragment() {
	}

	public AddDialogFragment(String babysitterObjectId) {
		mBabysitterObjectId = babysitterObjectId;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View entryView = factory.inflate(R.layout.fragment_baby_add, null);
		mName = (EditText) entryView.findViewById(R.id.name);

        setSpinnerData(entryView);
        
        return new AlertDialog.Builder(getActivity())
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("新增寶寶日記")
            .setView(entryView)
            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
    				mRingProgressDialog = ProgressDialog.show(getActivity(),
					"請稍等 ...", "資料儲存中...", true);

                	saveComment();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked cancel so do some stuff */
                }
            })
            .create();
	}

	private void setSpinnerData(View rootView) {
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
	}
	
	private void saveComment() {

		// ParseObject post = new ParseObject("Comment");
		
		Babysitter babysitter = ParseObject
				.createWithoutData(Babysitter.class, mBabysitterObjectId);

		
		BabyDiary baby = new BabyDiary();
		
		baby.setBabysitter(babysitter);
		baby.setName(mName.getText().toString());
		baby.setNote("趕快點選進來，幫寶寶記錄成長的軌跡吧!");
		baby.setFavorite(0);
		//baby.setPhotoFile(mPictureHelper.getFile());
		baby.setUser(ParseUser.getCurrentUser());
		baby.setIsPublic(mIsPublic);

		// Save the post and return
		baby.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					// setResult(RESULT_OK);
					// finish();
/*					Toast.makeText(getActivity().getApplicationContext(),
							"saving doen!", Toast.LENGTH_SHORT).show();
*/				} else {
					LOGD("vic", e.getMessage());
/*					Toast.makeText(getActivity().getApplicationContext(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
*/				}
				mRingProgressDialog.dismiss();
				//getActivity().finish();
			}

		});
	}

}
