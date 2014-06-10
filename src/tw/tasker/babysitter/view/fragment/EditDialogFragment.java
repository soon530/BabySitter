package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyRecord;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class EditDialogFragment extends DialogFragment {
	private ProgressDialog mRingProgressDialog;
	private EditText mTitle;
	private EditText mDescription;
	private BabyRecord mBabyRecord;

	public EditDialogFragment(BabyRecord babyRecord) {
		mBabyRecord = babyRecord;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View entryView = factory.inflate(R.layout.fragment_baby_record_edit, null);
		mTitle = (EditText) entryView.findViewById(R.id.title);
		mDescription = (EditText) entryView.findViewById(R.id.description);
		
		mTitle.setText(mBabyRecord.getTitle());
		mDescription.setText(mBabyRecord.getDescription());
        
        return new AlertDialog.Builder(getActivity())
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("編輯成長記錄")
            .setView(entryView)
            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
    				mRingProgressDialog = ProgressDialog.show(getActivity(),
					"請稍等 ...", "資料儲存中...", true);

                	updateBabyDiary();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked cancel so do some stuff */
                }
            })
            .create();
	}

	private void updateBabyDiary() {

		
		ParseQuery<BabyRecord> query = BabyRecord.getQuery();
		query.getInBackground(mBabyRecord.getObjectId(), new GetCallback<BabyRecord>() {
			
			@Override
			public void done(BabyRecord babyRecord, ParseException e) {
				babyRecord.setTitle(mTitle.getText().toString());
				babyRecord.setDescription(mDescription.getText().toString());
				
				try {
					babyRecord.save();
				} catch (ParseException exception) {
					exception.printStackTrace();
				}
				mRingProgressDialog.dismiss();
			}
		});
		
	}

}
