package tw.tasker.babysitter.view;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import tw.tasker.babysitter.R.menu;
import tw.tasker.babysitter.model.BabysitterComment;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class BabysitterCommentActivity extends ActionBarActivity {

	//private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_babysitter_comment);

		Bundle bundle = getIntent().getExtras();
		String objectId = bundle.getString("objectId");
		int totalRating = bundle.getInt("totalRating");
		int totalComment = bundle.getInt("totalComment");
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(objectId, totalRating, totalComment)).commit();
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
	public static class PlaceholderFragment extends Fragment {

		private Button mPostCommnet;
		private EditText mBabysitterTitle;
		private EditText mBabysitterComment;
		private RatingBar mBabysitterRating;
		
		private String mObjectId;
		private int mTotalRating;
		private int mTotalComment;


		public PlaceholderFragment(String objectId, int totalRating, int totalComment) {
			mObjectId = objectId;
			mTotalComment = totalRating;
			mTotalRating = totalComment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_babysitter_comment, container, false);
			
			
			mBabysitterTitle = (EditText) rootView.findViewById(R.id.babysitter_comment_title);
			mBabysitterComment = (EditText) rootView.findViewById(R.id.babysitter_comment);
			mBabysitterRating = (RatingBar) rootView.findViewById(R.id.babysitter_rating);
			
			mPostCommnet = (Button) rootView.findViewById(R.id.post_comment);
			mPostCommnet.setOnClickListener(new View.OnClickListener() {


				@Override
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "已送出..", Toast.LENGTH_LONG)
							.show();

					saveComment();
					//updateBabysitter();

					Intent intent = new Intent();
					intent.setClass(getActivity().getApplicationContext(),
							BabysitterDetailActivity.class);
					//startActivity(intent);
				}

				private void updateBabysitter() {
					ParseQuery<ParseObject> query = ParseQuery.getQuery("babysitter");
					 
					// Retrieve the object by id
					query.getInBackground(mObjectId, new GetCallback<ParseObject>() {

					public void done(ParseObject babysitter, ParseException e) {
					    if (e == null) {
					      // Now let's update it with some new data. In this case, only cheatMode and score
					      // will get sent to the Parse Cloud. playerName hasn't changed.
					    	int r = (int) mBabysitterRating.getRating();
					    	babysitter.put("totalRating", mTotalRating + r);
					    	babysitter.put("totalComment", mTotalComment + 1);
					    	babysitter.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e == null) {
								    	getActivity().finish();
										
									}
								}
							});
					    	
					    }
					  }
					});					
				}

				private void saveComment() {
					// ParseObject post = new ParseObject("Comment");
					BabysitterComment post = new BabysitterComment();
					post.put("babysitterId", mObjectId);
					int r = (int) mBabysitterRating.getRating();
					post.put("rating", r);
					post.put("title", mBabysitterTitle.getText().toString());
					post.put("comment", mBabysitterComment.getText().toString());

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
								updateBabysitter();
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
