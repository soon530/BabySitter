package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.fragment.BabysitterFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BabysitterActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);

		setContentView(R.layout.activity_container);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(
					Config.BABYSITTER_OBJECT_ID,
					getIntent().getStringExtra(
							Config.BABYSITTER_OBJECT_ID));

			BabysitterFragment fragment = new BabysitterFragment();

			fragment.setArguments(arguments);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}
}
