package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.impl.BabysitterDetailCardFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BabysitterDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);

		setContentView(R.layout.activity_babysitter_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(
					Config.BABYSITTER_OBJECT_ID,
					getIntent().getStringExtra(
							Config.BABYSITTER_OBJECT_ID));

			BabysitterDetailCardFragment fragment = new BabysitterDetailCardFragment();

			fragment.setArguments(arguments);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.babysitter_detail_container, fragment).commit();
		}
	}
}
