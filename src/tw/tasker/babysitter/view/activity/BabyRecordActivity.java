package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class BabyRecordActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);
		setContentView(R.layout.activity_container);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(Config.BABY_OBJECT_ID, getIntent()
					.getStringExtra(Config.BABY_OBJECT_ID));
			
			arguments.putInt(Config.TOTAL_RECORD,
					getIntent().getIntExtra(Config.TOTAL_RECORD, -1));
			BabyRecordFragment fragment = new BabyRecordFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
