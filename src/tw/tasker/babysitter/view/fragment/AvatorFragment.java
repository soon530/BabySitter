package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AvatorFragment extends Fragment {
	public static Fragment newInstance() {
		Fragment fragment = new AvatorFragment();

		return fragment;

	}

	public AvatorFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_avator, container, false);

		return rootView;
	}
}
