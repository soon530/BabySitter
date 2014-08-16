package tw.tasker.babysitter.view.activity;

import java.util.HashMap;
import java.util.Map;

import com.parse.ParseAnalytics;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.ProgressBarUtils;
import tw.tasker.babysitter.view.fragment.BabysittersFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class SearchActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBarUtils.init(this);
		setContentView(R.layout.activity_container);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Fragment fragment = BabysittersFragment.newInstance(2);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, fragment).commit();

		Map<String, String> dimensions = new HashMap<String, String>();
		dimensions.put("menu", "search");
		ParseAnalytics.trackEvent("home", dimensions);
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
