package tw.tasker.babysitter.view.activity;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.UserType;
import tw.tasker.babysitter.utils.AccountChecker;
import tw.tasker.babysitter.view.fragment.DataCheckParentEditFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

public class DataCheckActivity extends BaseActivity implements OnClickListener {
//	private SignUpListener mListener = new Listener();
//	private Fragment mProfileSitterFragment;
//	private Fragment mProfileParentFragment;
//	private Fragment mProfileParentEditFragment;
//	private Fragment mProfileSitterEditFragment;
	private FragmentTransaction mFragmentTransaction;
	private Fragment mDataCheckParentEditFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		
//		mProfileSitterFragment = ProfileSitterFragment.newInstance(mListener);
//		mProfileSitterEditFragment = ProfileSitterEditFragment.newInstance(mListener);
		
//		mProfileParentFragment = ProfileParentFragment.newInstance(mListener);
//		mProfileParentEditFragment = ProfileParentEditFragment.newInstance(mListener);
		
		mDataCheckParentEditFragment = DataCheckParentEditFragment.newInstance(null);
		
		mFragmentTransaction = getSupportFragmentManager().beginTransaction();
		
		if (savedInstanceState == null) {
			Fragment fragment = null;
			
			UserType userType = AccountChecker.getUserType();
			if (userType == UserType.PARENT) {
				fragment = mDataCheckParentEditFragment;
			} else if (userType == UserType.SITTER) {
				fragment = mDataCheckParentEditFragment;
			}
			
			mFragmentTransaction.add(R.id.container, fragment).commit();
		
		}
		
//		mEdit = (Button) findViewById(R.id.edit);
//		mEdit.setOnClickListener(this);
		

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
	
//	private final class Listener implements SignUpListener {
//
//		@Override
//		public void onSwitchToNextFragment(int type) {
//			
//			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//			
//			switch (type) {
//			case Config.PARENT_READ_PAGE:
//				ft.replace(R.id.container, mProfileParentFragment).commit();
//				break;
//
//			case Config.PARENT_EDIT_PAGE:
//				ft.replace(R.id.container, mProfileParentEditFragment).commit();
//				break;
//				
//			case Config.SITTER_READ_PAGE:
//				ft.replace(R.id.container, mProfileSitterFragment).commit();
//				break;
//				
//			case Config.SITTER_EDIT_PAGE:
//				ft.replace(R.id.container, mProfileSitterEditFragment).commit();
//				break;
//				
//			default:
//				break;
//			}
//			
//		}
//		
//	}

}
