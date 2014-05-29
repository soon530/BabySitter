package tw.tasker.babysitter.view.impl;

import tw.tasker.babysitter.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BabysitterDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_babysitter_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(
					BabysitterDetailFragment.BABYSITTER_OBJECT_ID,
					getIntent().getStringExtra(
							BabysitterDetailFragment.BABYSITTER_OBJECT_ID));

			BabysitterDetailFragment fragment = new BabysitterDetailFragment();

			fragment.setArguments(arguments);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.babysitter_detail_container, fragment).commit();
		}
	}
}
