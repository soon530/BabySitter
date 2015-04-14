package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import android.content.Intent;
import android.net.Uri;
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
	private Button mWebSite;
	private Button mCall;

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
		
		//mWebSite = (Button) rootView.findViewById(R.id.website);
		//mWebSite.setOnClickListener(this);
		
		//mCall = (Button) rootView.findViewById(R.id.call);
		//mCall.setOnClickListener(this);
		
		String tel = Config.sitterInfo.getTel();
		
		if (tel.indexOf("09") > -1 ) {
			mMessageTop.setText("目前您在保母系統登錄的電話為：");
			mPhone.setText(tel);
			//mMessageBottom.setText("若是想要更改聯絡電話您可以選擇");
		} else {
			mMessageTop.setText("目前您在系統登錄僅有市話號碼，");
			mPhone.setText("認證程序需要手機號碼以完成驗證。");
			mPhone.setTextColor(getResources().getColor(R.color.primary));
			//mMessageBottom.setText("若是想要增加手機號碼可以選擇");
		}
		
		mMessageBottom.setText("若無法獲取驗證碼，\n請留下電話或E-Mail，將會有專人聯絡您。");
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
//		switch (id) {
//		case R.id.website:
//			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cwisweb.sfaa.gov.tw/index.jsp"));
//			startActivity(browserIntent);
//			getActivity().finish();
//			break;
//
//		case R.id.call:
//			String phoneNumber = Config.sitterInfo.getCommunityTel();
//			makePhoneCall(phoneNumber.replace("-", ""));
//			break;
//		default:
//			break;
//		}
		
	}
	
	private void makePhoneCall(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		getActivity().startActivity(intent);
	}

}
