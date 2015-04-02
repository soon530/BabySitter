package tw.tasker.babysitter.view.activity;

import com.parse.ParseUser;

import tw.tasker.babysitter.ProfileParentFragment;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.LogUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ProfileActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {
			Fragment fragment = null;
			if (isSitter()) {
				fragment = ProfileSitterFragment.newInstance();
			} else {
				fragment = ProfileParentFragment.newInstance();
			}
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		
		}

	}

	private boolean isSitter() {
		String userType = ParseUser.getCurrentUser().getString("userType"); 
		LogUtils.LOGD("userType", userType);
		if (userType.equals("sitter")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
