package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabyRecord;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
import tw.tasker.babysitter.presenter.adapter.BabysitterCommentParseQueryAdapter;
import tw.tasker.babysitter.utils.LogUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class BabysitterCommentEditDialogFragment extends DialogFragment {
	private ProgressDialog mRingProgressDialog;
	private EditText mTitle;
	private EditText mDescription;
	private BabysitterComment mBabysitterComment;
	private BabysitterCommentParseQueryAdapter mAdapter;
	private RatingBar mRating;

	public BabysitterCommentEditDialogFragment(BabysitterComment babysitterComment, BabysitterCommentParseQueryAdapter adapter) {
		mBabysitterComment = babysitterComment;
		mAdapter = adapter;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View entryView = factory.inflate(R.layout.fragment_babysitter_comment, null);
        mRating = (RatingBar) entryView.findViewById(R.id.rating);
		mTitle = (EditText) entryView.findViewById(R.id.title);
		mDescription = (EditText) entryView.findViewById(R.id.description);
		
		mRating.setRating(mBabysitterComment.getRating());
		mTitle.setText(mBabysitterComment.getTitle());
		mDescription.setText(mBabysitterComment.getDescription());
        
        return new AlertDialog.Builder(getActivity())
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("編輯保母評論")
            .setView(entryView)
            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
    				mRingProgressDialog = ProgressDialog.show(getActivity(),
					"請稍等 ...", "資料儲存中...", true);

                	updateBabysitterComment();
                	updateBabysitter();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked cancel so do some stuff */
                }
            })
            .create();
	}

	private void updateBabysitterComment() {

		
		ParseQuery<BabysitterComment> query = BabysitterComment.getQuery();
		query.getInBackground(mBabysitterComment.getObjectId(), new GetCallback<BabysitterComment>() {
			
			@Override
			public void done(BabysitterComment babysitterComment, ParseException e) {
				babysitterComment.setRating(mRating.getRating());
				
				babysitterComment.setTitle(mTitle.getText().toString());
				
				//String name = ParseUser.getCurrentUser().getUsername();
				
				babysitterComment.setDescription(mDescription.getText().toString());
				
				try {
					babysitterComment.save();
				} catch (ParseException exception) {
					exception.printStackTrace();
				}
				mAdapter.loadObjects();
				mRingProgressDialog.dismiss();
			}
		});
		
	}
	
	private void updateBabysitter() {
		float oldRating = mBabysitterComment.getRating();
		float newRating = mRating.getRating();
		final float nowRating = newRating - oldRating;
		LogUtils.LOGD("vic", "oldRating="+oldRating+", newRating="+newRating+", nowRating="+nowRating);
		
		ParseQuery<Babysitter> query = Babysitter.getQuery();
		String babysitterObjectId = mBabysitterComment.getBabysitter().getObjectId(); 
		query.getInBackground(babysitterObjectId, new GetCallback<Babysitter>() {
			
			@Override
			public void done(Babysitter babysitter, ParseException e) {
				babysitter.increment("totalRating", nowRating);
				babysitter.saveInBackground();
			}
		});
	}


}
