package tw.tasker.babysitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import static tw.tasker.babysitter.LogUtils.LOGD;

public class BabysitterCommentActivity extends ActionBarActivity {

	//private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_babysitter_comment);

		Bundle bundle = getIntent().getExtras();
		String objectId = bundle.getString("objectId");
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(objectId)).commit();
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private Button mButton;
		private EditText mTitle;
		private EditText mComment;
		private RatingBar mRating;
		private Object mObjectId;

		public PlaceholderFragment(String objectId) {
			mObjectId = objectId;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_babysitter_comment, container, false);
			
			
			mTitle = (EditText) rootView.findViewById(R.id.editText1);
			mComment = (EditText) rootView.findViewById(R.id.editText2);
			mRating = (RatingBar) rootView.findViewById(R.id.ratingBar1);
			
			mButton = (Button) rootView.findViewById(R.id.button1);
			mButton.setOnClickListener(new View.OnClickListener() {


				@Override
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "已送出..", Toast.LENGTH_LONG)
							.show();

					saveComment();

					Intent intent = new Intent();
					intent.setClass(getActivity().getApplicationContext(),
							BabysitterDetailActivity.class);
					//startActivity(intent);
				}

				private void saveComment() {
					// ParseObject post = new ParseObject("Comment");
					BabysitterComment post = new BabysitterComment();
					post.put("babysitterId", mObjectId);
					int r = (int) mRating.getRating();
					post.put("rating", r);
					post.put("title", mTitle.getText().toString());
					post.put("comment", mComment.getText().toString());

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
						}

					});
				}
			});

			return rootView;
		}
	}

}
