package tw.tasker.babysitter;

import java.util.List;

import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.utils.LogUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link SitterSettingFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class SitterSettingFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private Button mSync;
	private EditText mNumber;
	private EditText mName;
	private EditText mSex;
	private EditText mAge;
	private EditText mEducation;
	private EditText mTel;
	private EditText mAddress;
	private EditText mBabycareCount;
	private EditText mBabycareTime;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SitterSettingFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static Fragment newInstance() {
		Fragment fragment = new SitterSettingFragment();
		//Bundle args = new Bundle();
		//args.putString(ARG_PARAM1, param1);
		//args.putString(ARG_PARAM2, param2);
		//fragment.setArguments(args);
		return fragment;
	}

	public SitterSettingFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sitter_setting, container, false);
		mSync = (Button) rootView.findViewById(R.id.sync);
		
		mNumber = (EditText) rootView.findViewById(R.id.number);
		mName = (EditText) rootView.findViewById(R.id.name);
		mSex = (EditText) rootView.findViewById(R.id.sex);
		mAge = (EditText) rootView.findViewById(R.id.age);
		mEducation = (EditText) rootView.findViewById(R.id.education);
		mTel = (EditText) rootView.findViewById(R.id.tel);
		mAddress = (EditText) rootView.findViewById(R.id.address);
		mBabycareCount = (EditText) rootView.findViewById(R.id.babycare_count);
		mBabycareTime = (EditText) rootView.findViewById(R.id.babycare_time);

		
		mSync.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				syncData();
			}

		});
		return rootView;
	}

	private void syncData() {
		ParseQuery<Babysitter> query = Babysitter.getQuery();
		query.whereEqualTo("babysitterNumber", mNumber.getText().toString());
		query.getFirstInBackground(new GetCallback<Babysitter>() {
			
			@Override
			public void done(Babysitter babysitter, ParseException e) {
				LogUtils.LOGD("vic", "syncData()" + babysitter);
				fillUI(babysitter);
			}

		});
	}

	private void fillUI(Babysitter babysitter) {
		mName.setText(babysitter.getName());
		mSex.setText(babysitter.getSex());
		mAge.setText(babysitter.getAge());
		mEducation.setText(babysitter.getEducation());
		mTel.setText(babysitter.getTel());
		mAddress.setText(babysitter.getAddress());
		mBabycareCount.setText(babysitter.getBabycareCount());
		mBabycareTime.setText(babysitter.getBabycareTime());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.favorite, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_favorite:
			
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	
}
