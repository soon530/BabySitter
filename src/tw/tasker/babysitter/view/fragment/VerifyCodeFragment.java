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

public class VerifyCodeFragment extends Fragment implements OnClickListener {

	private static SignUpListener mListener;

	public static Fragment newInstance(SignUpListener listener) {
		Fragment fragment = new VerifyCodeFragment();
		mListener = listener;
		return fragment;
	}

	private Button mConfirm;

	public VerifyCodeFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_code_verify, container, false);
		mConfirm = (Button)rootView.findViewById(R.id.confirm);
		mConfirm.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		mListener.onSwitchToNextFragment();
	}

}
