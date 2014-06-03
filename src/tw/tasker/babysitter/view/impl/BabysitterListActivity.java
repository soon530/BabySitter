package tw.tasker.babysitter.view.impl;

import tw.tasker.babysitter.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

public class BabysitterListActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Enabling Progress bar for this activity */
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_babysitter_list);
	}
}
