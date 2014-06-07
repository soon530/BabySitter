package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BabyDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(Config.BABY_OBJECT_ID, getIntent()
					.getStringExtra(Config.BABY_OBJECT_ID));
			BabyDetailFragment fragment = new BabyDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}
}