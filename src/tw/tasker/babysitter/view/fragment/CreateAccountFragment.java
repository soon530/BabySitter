package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.activity.SignUpListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateAccountFragment extends Fragment {

	public static Fragment newInstance() {
		Fragment fragment = new CreateAccountFragment();
		return fragment;
	}
	
	public CreateAccountFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_account_create, container, false);
		return rootView;
	}

	


}
