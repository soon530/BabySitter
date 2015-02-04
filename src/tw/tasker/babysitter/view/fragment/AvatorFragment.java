package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AvatorFragment extends Fragment implements OnClickListener {
	private ImageView mAvator;

	public static Fragment newInstance() {
		Fragment fragment = new AvatorFragment();

		return fragment;

	}

	public AvatorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_avator, container,
				false);
		mAvator = (ImageView) rootView.findViewById(R.id.avator);
		mAvator.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		final String[] imageMethods = {"照相", "相簿"}; 
		DialogFragment frag = new ListDialogFragment(imageMethods, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		frag.show(getFragmentManager(), "dialog");
	}
}
