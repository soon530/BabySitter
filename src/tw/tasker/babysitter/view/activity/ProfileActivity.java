package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.ProfileParentFragment;
import tw.tasker.babysitter.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ProfileActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {

			
			Fragment fragment = ProfileParentFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
