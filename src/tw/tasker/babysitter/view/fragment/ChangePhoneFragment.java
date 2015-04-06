package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.view.activity.SignUpListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ChangePhoneFragment extends Fragment implements OnClickListener {


	public static Fragment newInstance() {
		Fragment fragment = new ChangePhoneFragment();
		return fragment;
	}

	public ChangePhoneFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_phone_change, container, false);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
	}

}
