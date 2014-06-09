package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class BabysitterCommentActivity extends ActionBarActivity {

	// private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {

			Bundle arguments = new Bundle();
			arguments.putString(Config.BABYSITTER_OBJECT_ID, getIntent()
					.getStringExtra(Config.BABYSITTER_OBJECT_ID));

			arguments.putString(Config.TOTAL_RATING, getIntent()
					.getStringExtra(Config.TOTAL_RATING));

			arguments.putString(Config.TOTAL_COMMENT, getIntent()
					.getStringExtra(Config.TOTAL_COMMENT));

			BabysitterCommentFragment fragment = new BabysitterCommentFragment();

			fragment.setArguments(arguments);
			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
