package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ParentsSignUpFragment extends Fragment {

	public static Fragment newInstance() {
		ParentsSignUpFragment fragment = new ParentsSignUpFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_signup_parents, container, false);

		
		return rootView;
	}
}
