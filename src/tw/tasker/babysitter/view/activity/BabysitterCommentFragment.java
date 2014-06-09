package tw.tasker.babysitter.view.activity;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabysitterComment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
	 * A placeholder fragment containing a simple view.
	 */
	public class BabysitterCommentFragment extends Fragment implements
			OnClickListener {

		private Button mPostCommnet;
		private EditText mBabysitterTitle;
		private EditText mBabysitterComment;
		private RatingBar mBabysitterRating;

		private String mBabysitterObjectId;
		private int mTotalRating;
		private int mTotalComment;
		private ProgressDialog mRingProgressDialog;

		public BabysitterCommentFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(Config.BABYSITTER_OBJECT_ID)) {
				mBabysitterObjectId = getArguments().getString(
						Config.BABYSITTER_OBJECT_ID);
				mTotalRating = getArguments().getInt(Config.TOTAL_RATING);
				mTotalComment = getArguments().getInt(Config.BABYSITTER_OBJECT_ID);
			}

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_babysitter_comment, container, false);

			mBabysitterTitle = (EditText) rootView
					.findViewById(R.id.babysitter_comment_title);
			mBabysitterComment = (EditText) rootView
					.findViewById(R.id.babysitter_comment);
			mBabysitterRating = (RatingBar) rootView
					.findViewById(R.id.babysitter_rating);

			mPostCommnet = (Button) rootView.findViewById(R.id.post_comment);
			mPostCommnet.setOnClickListener(this);

			return rootView;
		}

		@Override
		public void onClick(View v) {
			mRingProgressDialog = ProgressDialog.show(getActivity(), "請稍等 ...",
					"資料儲存中...", true);

			Toast.makeText(v.getContext(), "已送出..", Toast.LENGTH_LONG).show();
			saveComment();
/*			Intent intent = new Intent();
			intent.setClass(getActivity().getApplicationContext(),
					BabysitterActivity.class);
*/
		}

		private void saveComment() {
			BabysitterComment post = new BabysitterComment();
			post.put("babysitterId", mBabysitterObjectId);
			int r = (int) mBabysitterRating.getRating();
			post.put("rating", r);
			post.put("title", mBabysitterTitle.getText().toString());
			post.put("comment", mBabysitterComment.getText().toString());

			post.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
						// setResult(RESULT_OK);
						// finish();
						Toast.makeText(getActivity().getApplicationContext(),
								"saving doen!", Toast.LENGTH_SHORT).show();
						updateBabysitter();
					} else {
						LOGD("vic", e.getMessage());
						Toast.makeText(getActivity().getApplicationContext(),
								"Error saving: " + e.getMessage(),
								Toast.LENGTH_SHORT).show();

					}
				}

			});
		}

		private void updateBabysitter() {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Babysitter");

			query.getInBackground(mBabysitterObjectId,
					new GetCallback<ParseObject>() {

						public void done(ParseObject babysitter,
								ParseException e) {
							if (e == null) {
								int r = (int) mBabysitterRating.getRating();
								babysitter.put("totalRating", mTotalRating + r);
								babysitter.put("totalComment",
										mTotalComment + 1);
								babysitter.saveInBackground(new SaveCallback() {

									@Override
									public void done(ParseException e) {
										if (e == null) {
											mRingProgressDialog.dismiss();
											getActivity().finish();

										}
									}
								});

							}
						}
					});
		}

	}