package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyDiary;
import tw.tasker.babysitter.presenter.adapter.BabyDiaryParseQueryAdapter;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class BabyDiaryEditDialogFragment extends DialogFragment {
	private ProgressDialog mRingProgressDialog;
	private EditText mName;
	private BabyDiary mBabyDiary;
	private BabyDiaryParseQueryAdapter mAdapter;

	private Spinner mShareType;
	private boolean mIsPublic;

	public BabyDiaryEditDialogFragment(BabyDiary babyDiary, BabyDiaryParseQueryAdapter adapter) {
		mBabyDiary = babyDiary;
		mAdapter = adapter;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View entryView = factory.inflate(R.layout.fragment_baby_add, null);
		mName = (EditText) entryView.findViewById(R.id.name);	
		mName.setText(mBabyDiary.getName());
		setSpinnerData(entryView);
		
        return new AlertDialog.Builder(getActivity())
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("編輯寶寶日誌")
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
		
		// 前的值
		if (mBabyDiary.getIsPublic()) {
			mShareType.setSelection(1);
		}else {
			mShareType.setSelection(0);			
		}

	}

	private void updateBabyDiary() {
		
		ParseQuery<BabyDiary> query = BabyDiary.getQuery();
		query.getInBackground(mBabyDiary.getObjectId(), new GetCallback<BabyDiary>() {
			
			@Override
			public void done(BabyDiary babyDiary, ParseException e) {
				babyDiary.setName(mName.getText().toString());
				babyDiary.setIsPublic(mIsPublic);
				
				try {
					babyDiary.save();
				} catch (ParseException exception) {
					exception.printStackTrace();
				}
				mAdapter.loadObjects();
				mRingProgressDialog.dismiss();
			}
		});
		
	}

}
