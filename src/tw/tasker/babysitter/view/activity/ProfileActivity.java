package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.ProfileParentEditFragment;
import tw.tasker.babysitter.ProfileParentFragment;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.utils.LogUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseUser;

public class ProfileActivity extends BaseActivity implements OnClickListener {
	private Button mEdit;
	private SignUpListener mListener = new Listener();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		if (savedInstanceState == null) {
			Fragment fragment = null;
			if (isSitter()) {
				fragment = ProfileSitterFragment.newInstance();
			} else {
				fragment = ProfileParentFragment.newInstance(mListener);
			}
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		
		}
		
//		mEdit = (Button) findViewById(R.id.edit);
//		mEdit.setOnClickListener(this);
		

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

	@Override
	public void onClick(View v) {
//		LogUtils.LOGD("vic", "編輯");
//		Fragment fragment = ProfileParentEditFragment.newInstance();
//		getSupportFragmentManager().beginTransaction()
//		.replace(R.id.container, fragment).addToBackStack(null).commit();
	}
	
	private final class Listener implements SignUpListener {

		@Override
		public void onSwitchToNextFragment(int type) {
			LogUtils.LOGD("vic", "編輯");
			Fragment fragment = ProfileParentEditFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, fragment).addToBackStack(null).commit();
		}
		
	}

}
