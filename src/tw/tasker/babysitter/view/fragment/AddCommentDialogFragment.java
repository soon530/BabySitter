package tw.tasker.babysitter.view.fragment;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.BabysitterComment;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddCommentDialogFragment extends DialogFragment {
	private ProgressDialog mRingProgressDialog;
	private EditText mTitle;
	private String mBabysitterObjectId;
	private EditText mDescription;
	private RatingBar mRating;

	public AddCommentDialogFragment(String babysitterObjectId) {
		mBabysitterObjectId = babysitterObjectId;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// This example shows how to add a custom layout to an AlertDialog
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View entryView = factory.inflate(
				R.layout.fragment_babysitter_comment, null);
		mTitle = (EditText) entryView.findViewById(R.id.title);
		mDescription = (EditText) entryView.findViewById(R.id.description);
		mRating = (RatingBar) entryView.findViewById(R.id.rating);

		return new AlertDialog.Builder(getActivity())
				.setIconAttribute(android.R.attr.alertDialogIcon)
				.setTitle("新增保母評論")
				.setView(entryView)
				.setPositiveButton("確定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mRingProgressDialog = ProgressDialog.show(
								getActivity(), "請稍等 ...", "資料儲存中...", true);

						saveComment();
						updateBabysitter();
						mRingProgressDialog.dismiss();

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						/* User clicked cancel so do some stuff */
					}
				}).create();
	}

	private void saveComment() {
		Babysitter babysitter = ParseObject.createWithoutData(Babysitter.class,
				mBabysitterObjectId);

		BabysitterComment babysitterComment = new BabysitterComment();
		babysitterComment.setBabysitter(babysitter);
		babysitterComment.setRating(mRating.getRating());
		babysitterComment.setTitle(mTitle.getText().toString());
		
		String name = ParseUser.getCurrentUser().getUsername() + "說: ";
		babysitterComment.setDescription(name + mDescription.getText().toString());

		babysitterComment.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					// setResult(RESULT_OK);
					// finish();
					/*
					 * Toast.makeText(getActivity().getApplicationContext(),
					 * "saving doen!", Toast.LENGTH_SHORT).show();
					 */
					//updateBabysitter();
				} else {
					LOGD("vic", e.getMessage());
					/*
					 * Toast.makeText(getActivity().getApplicationContext(),
					 * "Error saving: " + e.getMessage(),
					 * Toast.LENGTH_SHORT).show();
					 */
				}
			}

		});
	}

	private void updateBabysitter() {

		ParseQuery<Babysitter> query = Babysitter.getQuery();

		query.getInBackground(mBabysitterObjectId,
				new GetCallback<Babysitter>() {
					public void done(Babysitter babysitter, ParseException e) {

						float totalRating = babysitter.getTotalRating()
								+ mRating.getRating();
						int totalComment = babysitter.getTotalComment() + 1;

						babysitter.put("totalRating", totalRating);
						babysitter.put("totalComment", totalComment);
						babysitter.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								// getActivity().finish();
							}
						});

					}
				});
	}

}
