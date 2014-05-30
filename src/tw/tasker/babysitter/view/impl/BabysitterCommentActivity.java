package tw.tasker.babysitter.view.impl;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.BabysitterComment;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class BabysitterCommentActivity extends ActionBarActivity {

	// private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_babysitter_comment);

		if (savedInstanceState == null) {

			Bundle arguments = new Bundle();
			arguments.putString(Config.BABYSITTER_OBJECT_ID, getIntent()
					.getStringExtra(Config.BABYSITTER_OBJECT_ID));

			arguments.putString(Config.TOTAL_RATING, getIntent()
					.getStringExtra(Config.TOTAL_RATING));

			arguments.putString(Config.TOTAL_COMMENT, getIntent()
					.getStringExtra(Config.TOTAL_COMMENT));

			PlaceholderFragment fragment = new PlaceholderFragment();

			fragment.setArguments(arguments);
			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.babysitter_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_send) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnClickListener {

		private Button mPostCommnet;
		private EditText mBabysitterTitle;
		private EditText mBabysitterComment;
		private RatingBar mBabysitterRating;

		private String mBabysitterObjectId;
		private int mTotalRating;
		private int mTotalComment;
		private ProgressDialog mRingProgressDialog;

		public PlaceholderFragment() {
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
					BabysitterDetailActivity.class);
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
			ParseQuery<ParseObject> query = ParseQuery.getQuery("babysitter");

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
}
