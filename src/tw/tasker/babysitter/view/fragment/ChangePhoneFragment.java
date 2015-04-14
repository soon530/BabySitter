package tw.tasker.babysitter.view.fragment;

import tw.tasker.babysitter.Config;
import tw.tasker.babysitter.R;
import tw.tasker.babysitter.model.data.Babysitter;
import tw.tasker.babysitter.model.data.Sitter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class ChangePhoneFragment extends Fragment implements OnClickListener {

	private TextView mPhone;
	private TextView mMessageTop;
	private TextView mMessageBottom;
	private Button mWebSite;
	private Button mCall;
	private EditText mContact;
	private Button mSend;

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
		
		mContact = (EditText) rootView.findViewById(R.id.contact);
		mSend = (Button) rootView.findViewById(R.id.send);
		mSend.setOnClickListener(this);
		
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
		
		switch (id) {
		case R.id.send:
			
			// sendMail();
			String contact = mContact.getText().toString();
			if (contact.isEmpty()) {
				Toast.makeText(getActivity(), "請輸入聯絡方式!", Toast.LENGTH_LONG).show();
			} else {
				mSend.setEnabled(false);
				sendServer(contact);
			}

			break;
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
		default:
			break;
		}
		
	}
	
	private void sendServer(String contact) {
		Babysitter babysitter = Config.sitterInfo;
		Sitter sitter = new Sitter();
		sitter.setContact(contact);
		
		sitter.setName(babysitter.getName());
		sitter.setTel(babysitter.getTel());
		sitter.setAddress(babysitter.getAddress());
		sitter.setBabycareCount(babysitter.getBabycareCount());
		sitter.setBabycareTime(babysitter.getBabycareTime());
		sitter.setSkillNumber(babysitter.getSkillNumber());
		sitter.setEducation(babysitter.getEducation());
		sitter.setCommunityName(babysitter.getCommunityName());
		
		sitter.setBabysitterNumber(babysitter.getBabysitterNumber());
		sitter.setAge(babysitter.getAge());
		sitter.setIsVerify(false);
		
		sitter.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getActivity(), "寄送成功!", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity(), "寄送失敗!", Toast.LENGTH_LONG).show();
					mSend.setEnabled(true);
				}
			}
		});
	}

//	private void sendMail() {
//		BackgroundMail bm = new BackgroundMail(getActivity());
//		bm.setGmailUserName("soon530@gmail.com");
//        //"DoE/GTiYpX5sz5zmTFuoHg==" is crypted "password"
//		bm.setGmailPassword(Utils.decryptIt("pk6dEJB4trzLtCdnrKbvZQ==")); 
//		bm.setMailTo("service@babytone.cc");
//		bm.setFormSubject("聯絡方式：" + mContact.getText());
//		bm.setFormBody("聯絡方式：" + mContact.getText());
//		
//		// this is optional
//		//bm.setSendingMessage("寄送中...");
//		//bm.setSendingMessageSuccess("您的聯絡方式已寄出!");
//		// bm.setProcessVisibility(false);
//		// bm.setAttachment(Environment.getExternalStorageDirectory().getPath()+File.pathSeparator+"somefile.txt");
//		bm.send();
//		
//	}


	private void makePhoneCall(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		getActivity().startActivity(intent);
	}

}
