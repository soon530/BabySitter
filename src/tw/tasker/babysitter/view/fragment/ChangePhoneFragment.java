package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.Config;
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

	private TextView mPhone;
	private TextView mMessageTop;
	private TextView mMessageBottom;

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
		
		mPhone = (TextView) rootView.findViewById(R.id.phone);
		mMessageTop = (TextView) rootView.findViewById(R.id.message_top);
		mMessageBottom = (TextView) rootView.findViewById(R.id.message_bottom);
		
		String tel = Config.sitterInfo.getTel();
		
		if (tel.indexOf("09") > -1 ) {
			mMessageBottom.setText("目前您在保母系統登錄的電話為：");
			mPhone.setText(tel);
			mMessageTop.setText("若是想要更改聯絡電話您可以選擇");
		} else {
			mMessageBottom.setText("目前您在系統登錄僅有市話號碼，");
			mPhone.setText("認證程序需要手機號碼以完成驗證。");
			mMessageTop.setText("若是想要增加手機號碼可以選擇");
		}		
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
	}

}
