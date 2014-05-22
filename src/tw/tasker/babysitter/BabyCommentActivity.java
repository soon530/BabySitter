package tw.tasker.babysitter;

import static tw.tasker.babysitter.utils.LogUtils.LOGD;
import tw.tasker.babysitter.model.BabysitterComment;
import tw.tasker.babysitter.view.BabysitterDetailActivity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.os.Build;

public class BabyCommentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_comment);

		//Comment.babysitterId 先存 baby.objectId
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment("HXSAmYCUdG")).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baby_comment, menu);
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

		private EditText mBabysitterTitle;
		private EditText mBabysitterComment;
		private Button mPostCommnet;
		private String mObjectId;

		public PlaceholderFragment(String babyObjectId) {
			mObjectId = babyObjectId;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baby_comment,
					container, false);
			
			
			
			mBabysitterTitle = (EditText) rootView.findViewById(R.id.babysitter_comment_title);
			mBabysitterComment = (EditText) rootView.findViewById(R.id.babysitter_comment);
			
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


				private void saveComment() {
					// ParseObject post = new ParseObject("Comment");
					BabysitterComment post = new BabysitterComment();
					post.put("babysitterId", mObjectId);
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
